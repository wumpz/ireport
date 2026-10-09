"""Verify migration rejects unsafe input before moving files and preserves others."""
from pathlib import Path
import importlib.util
import tempfile
import unittest
import zipfile

spec = importlib.util.spec_from_file_location('migration', Path(__file__).with_name('Migrate-LabDesigner.py'))
migration = importlib.util.module_from_spec(spec)
spec.loader.exec_module(migration)


class MigrationTest(unittest.TestCase):
    def setUp(self):
        lab = Path(__file__).resolve().parents[2] / 'laboratorio'
        self.temp = tempfile.TemporaryDirectory(prefix='migration-check-', dir=lab)
        self.lab = Path(self.temp.name)
        self.profile = self.lab / 'profile'
        tracking = self.profile / 'update_tracking'
        tracking.mkdir(parents=True)
        (tracking / 'ireport.xml').write_text('<module codename="com.jaspersoft.ireport/1"><module_version><file name="modules/old.jar"/><file name="modules/shared.jar"/></module_version></module>')
        (tracking / 'other.xml').write_text('<module codename="other.module"><module_version><file name="modules/shared.jar"/></module_version></module>')
        (self.profile / 'modules').mkdir()
        (self.profile / 'modules/old.jar').write_bytes(b'old')
        (self.profile / 'modules/shared.jar').write_bytes(b'shared')
        (self.profile / 'preferences').write_bytes(b'user settings')
        self.nbm = self.lab / 'candidate.nbm'
        self.quarantine = self.lab / 'quarantine'

    def tearDown(self):
        self.temp.cleanup()

    def archive(self, name):
        with zipfile.ZipFile(self.nbm, 'w') as archive:
            archive.writestr('netbeans/' + name, b'new')

    def run_migration(self):
        return migration.migrate(self.profile, self.nbm, self.quarantine, self.lab)

    def test_preserves_shared_module_and_preferences(self):
        self.archive('modules/new.jar')
        result = self.run_migration()
        self.assertTrue(result['preserved_files_unchanged'])
        self.assertEqual(b'shared', (self.profile / 'modules/shared.jar').read_bytes())
        self.assertEqual(b'user settings', (self.profile / 'preferences').read_bytes())
        self.assertEqual(b'old', (self.quarantine / 'modules/old.jar').read_bytes())

    def test_rejects_shared_overwrite_before_mutation(self):
        self.archive('modules/shared.jar')
        with self.assertRaises(ValueError):
            self.run_migration()
        self.assertFalse(self.quarantine.exists())
        self.assertEqual(b'old', (self.profile / 'modules/old.jar').read_bytes())

    def test_rejects_zip_path_escape_before_mutation(self):
        self.archive('../escaped.jar')
        with self.assertRaises(ValueError):
            self.run_migration()
        self.assertFalse(self.quarantine.exists())
        self.assertFalse((self.lab / 'escaped.jar').exists())

    def test_rejects_profile_outside_laboratory(self):
        self.archive('modules/new.jar')
        with self.assertRaises(ValueError):
            migration.migrate(self.profile, self.nbm, self.quarantine, self.lab / 'different')
        self.assertFalse(self.quarantine.exists())


if __name__ == '__main__':
    unittest.main(verbosity=2)

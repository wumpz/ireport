"""Prepare disposable paths/profile without copying private connection settings."""
from pathlib import Path
import hashlib, json, shutil, zipfile

fork = Path(__file__).resolve().parents[1]
lab = fork.parent / 'laboratorio'
run = lab / 'autonomous-2026-10-08'
run.mkdir(exist_ok=False)
reports = run / 'Relatórios com espaços e ação'
shutil.copytree(lab / 'erp-build-01/target/classes/Reports', reports)
shutil.copy(fork / 'test-fixtures/reports/barcode-components.jrxml', reports)
(reports / 'invalid.jrxml').write_text('<jasperReport><broken></jasperReport>', encoding='utf-8')
profile = lab / 'userdir-autonomous-upgrade-nb31-jdk21'
profile.mkdir(exist_ok=False)
old = lab / 'backup-plugin-5.5.0'
for folder in ('modules', 'config/Modules', 'update_tracking'):
    if (old / folder).exists():
        shutil.copytree(old / folder, profile / folder)
pref = profile / 'config/Preferences/com/jaspersoft/ireport.properties'
pref.parent.mkdir(parents=True, exist_ok=True)
pref.write_text('DefaultLanguage=groovy\nUnit=cm\neditorFontSize=13\ncompile_subreports=true\nuseReportDirectoryToCompile=true\n', encoding='utf-8')
before = hashlib.sha256(pref.read_bytes()).hexdigest()
nbm = lab / 'artifacts-production-candidate/ireport-designer-6.0-SNAPSHOT.nbm'
with zipfile.ZipFile(nbm) as archive:
    for entry in archive.infolist():
        if not entry.filename.startswith('netbeans/') or entry.is_dir():
            continue
        target = (profile / entry.filename[9:]).resolve()
        if not target.is_relative_to(profile.resolve()):
            raise ValueError('Unsafe NBM path')
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(archive.read(entry))
assert hashlib.sha256(pref.read_bytes()).hexdigest() == before
(profile / 'var').mkdir(exist_ok=True)
(profile / 'var/imported').touch()
(run / 'preparation.json').write_text(json.dumps({'method':'offline NBM overlay on copied legacy module; synthetic legacy preferences', 'preferences_sha256_before_launch':before, 'profile':str(profile), 'reports':str(reports)}, indent=2), encoding='utf-8')
print('PASS isolated overlay preserves synthetic legacy preferences')
print(run)

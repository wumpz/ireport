"""Replace tracked legacy iReport files only, in a disposable lab profile.

Old components/JasperServer are quarantined, not asserted API-compatible.
Other modules and preferences stay in place. Never accepts a daily userdir.
"""
from pathlib import Path
import argparse
import hashlib
import json
import shutil
import xml.etree.ElementTree as ET
import zipfile


def safe_path(root, name):
    target = (root / name).resolve()
    if not target.is_relative_to(root.resolve()) or target == root.resolve():
        raise ValueError(f'Unsafe module path: {name}')
    return target


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def migrate(profile, nbm, quarantine, lab):
    for path in (profile, quarantine):
        if not path.resolve().is_relative_to(lab.resolve()) or path.resolve() == lab.resolve():
            raise ValueError('Profile and quarantine must be inside the laboratory')
    if not profile.is_dir() or quarantine.exists():
        raise ValueError('Requires an existing disposable profile and a fresh quarantine')
    if quarantine.resolve().is_relative_to(profile.resolve()) or profile.resolve().is_relative_to(quarantine.resolve()):
        raise ValueError('Profile and quarantine must be separate directories')
    selected = {'com.jaspersoft.ireport', 'com.jaspersoft.ireport.components',
                'com.jaspersoft.ireport.jasperserver'}
    owned, shared, tracking = set(), set(), []
    for path in (profile / 'update_tracking').glob('*.xml'):
        root = ET.parse(path).getroot()
        names = {node.attrib['name'] for node in root.findall('.//file')}
        for name in names:
            safe_path(profile, name)
        if root.attrib['codename'].split('/')[0] in selected:
            owned.update(names)
            tracking.append(path)
        else:
            shared.update(names)
    if not tracking:
        raise ValueError('No tracked legacy iReport installation found')
    # Validate everything before moving any file.
    with zipfile.ZipFile(nbm) as archive:
        entries = [(item.filename[9:], archive.read(item)) for item in archive.infolist()
                   if item.filename.startswith('netbeans/') and not item.is_dir()]
    for name, _ in entries:
        safe_path(profile, name)
        if name in shared:
            raise ValueError(f'NBM would overwrite another module file: {name}')
    moving = {safe_path(profile, name) for name in owned - shared}
    moving.update(tracking)
    for name, _ in entries:
        path = safe_path(profile, name)
        if path.exists() and path not in moving:
            raise ValueError(f'NBM would overwrite an unowned file: {name}')
    preserved = {str(path.relative_to(profile)): digest(path)
                 for path in profile.rglob('*') if path.is_file() and path not in moving}
    quarantine.mkdir(parents=True)
    moved = []
    for path in sorted(moving):
        if path.is_file():
            relative = path.relative_to(profile)
            target = safe_path(quarantine, relative)
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.move(str(path), str(target))
            moved.append(str(relative))
    for name, data in entries:
        path = safe_path(profile, name)
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(data)
    changed = [name for name, expected in preserved.items()
               if not safe_path(profile, name).is_file() or digest(safe_path(profile, name)) != expected]
    if changed:
        raise ValueError(f'Unexpected changes to preserved files: {changed}')
    result = {'quarantined_files': moved, 'preserved_files': len(preserved),
              'installed_files': len(entries), 'preserved_files_unchanged': True,
              'legacy_addons': 'quarantined; compatibility not validated'}
    (quarantine / 'migration.json').write_text(json.dumps(result, indent=2), encoding='utf-8')
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--profile', required=True, type=Path)
    parser.add_argument('--nbm', required=True, type=Path)
    parser.add_argument('--quarantine', required=True, type=Path)
    args = parser.parse_args()
    lab = Path(__file__).resolve().parents[2] / 'laboratorio'
    print(json.dumps(migrate(args.profile, args.nbm, args.quarantine, lab), indent=2))

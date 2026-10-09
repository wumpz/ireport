"""Package an audited NBM without fetching or flattening its dependency JARs."""
import argparse
import hashlib
import io
import json
from pathlib import Path
import shutil
import zipfile

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('--nbm', type=Path, required=True)
parser.add_argument('--output', type=Path, required=True)
args = parser.parse_args()
fork = Path(__file__).resolve().parents[1]
out = args.output.resolve()
if not out.is_relative_to((fork.parent / 'laboratorio').resolve()):
    raise SystemExit('Output must remain inside laboratory')
out.mkdir(exist_ok=False)
shutil.copy2(args.nbm, out / 'ireport-designer-jr6.21.4.nbm')
inventory = []
with zipfile.ZipFile(args.nbm) as nbm:
    assert len(nbm.namelist()) == len(set(nbm.namelist()))
    assert nbm.testzip() is None
    for name in nbm.namelist():
        if '/ext/' not in name or not name.endswith('.jar'):
            continue
        data = nbm.read(name)
        notices = []
        coordinates = []
        with zipfile.ZipFile(io.BytesIO(data)) as jar:
            assert jar.testzip() is None
            for member in jar.namelist():
                leaf = member.rsplit('/', 1)[-1].lower()
                if any(leaf.startswith(word) for word in ('license', 'notice', 'copying')) and not member.endswith(('/', '.class')):
                    # Preserve complete upstream notices, never reconstruct legal text.
                    unique_name = hashlib.sha256(member.encode()).hexdigest()[:12] + '-' + member.rsplit('/', 1)[-1]
                    dest = out / 'third-party-notices' / (str(len(inventory) + 1) + '-' + Path(name).name) / unique_name
                    dest.parent.mkdir(parents=True, exist_ok=True)
                    dest.write_bytes(jar.read(member))
                    notices.append(member)
                if member.startswith('META-INF/maven/') and member.endswith('/pom.properties'):
                    coordinates.append(jar.read(member).decode('utf-8', errors='replace'))
        inventory.append({'path': name, 'sha256': hashlib.sha256(data).hexdigest(),
                          'bytes': len(data), 'maven_metadata': coordinates,
                          'embedded_notices': notices})
assert len(inventory) == 91, 'Unexpected dependency inventory; audit the candidate again'
(out / 'dependencies.json').write_text(json.dumps(inventory, indent=2), encoding='utf-8')
shutil.copy2(fork / 'docs/BINARY-DISTRIBUTION.md', out / 'INSTALL.md')
shutil.copy2(fork / 'COPYING', out / 'COPYING')
shutil.copy2(fork.parent / 'laboratorio/artifacts-production-candidate/release-audit.json', out / 'release-audit.json')
(out / 'NOTICE.md').write_text('''The project POM declares AGPL-3.0. Corresponding modified source is supplied in the release source archive and tagged Git repository.

Dependency JARs retain their own licenses and metadata. Their embedded license/notice files are copied verbatim under third-party-notices; dependencies.json records hashes and available metadata. Some historical upstream binaries do not carry embedded license notices. Their redistribution history and source provenance are inherited from the upstream libraries.zip; this inventory does not claim that absent notices mean unrestricted licensing or certify a complete legal audit.

This package contains the designer module only, not the ERP, proprietary report classes, credentials, Java, NetBeans, or unsupported legacy companion modules.
''', encoding='utf-8')
with zipfile.ZipFile(out / 'ireport-designer-jr6.21.4-offline.zip', 'w', zipfile.ZIP_DEFLATED) as bundle:
    for file in sorted(out.rglob('*')):
        if file.is_file() and file.suffix != '.zip':
            bundle.write(file, file.relative_to(out).as_posix())
checksums = []
for file in sorted(out.iterdir()):
    if file.is_file():
        checksums.append(f'{hashlib.sha256(file.read_bytes()).hexdigest()}  {file.name}')
(out / 'SHA256SUMS.txt').write_text('\n'.join(checksums) + '\n', encoding='utf-8')
print(json.dumps({'output': str(out), 'dependencies': len(inventory), 'nbm_sha256': hashlib.sha256(args.nbm.read_bytes()).hexdigest()}))

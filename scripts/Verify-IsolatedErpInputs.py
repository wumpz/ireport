"""Compare the read-only ERP inputs to the isolated build snapshot."""
import hashlib, json
from pathlib import Path

base = Path(__file__).resolve().parents[2]
lab = base / 'laboratorio/erp-build-01'
erp = base.parent / 'AtheneSistema'
baseline = json.loads((lab / 'original-input-hashes.json').read_text())
changed = []
for name, expected in baseline.items():
    source = erp / name
    if not source.is_file() or hashlib.sha256(source.read_bytes()).hexdigest() != expected:
        changed.append(name)
result = {'checked': len(baseline), 'changed': changed,
          'scope': 'ERP inputs read only; all build changes confined to the laboratory'}
(lab / 'original-integrity.json').write_text(json.dumps(result, indent=2))
print(json.dumps(result, indent=2))
if changed:
    raise SystemExit(1)

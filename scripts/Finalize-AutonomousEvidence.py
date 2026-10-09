"""Check concrete outputs and synthetic migrated preferences, without touching ERP."""
from pathlib import Path
import hashlib, json, zipfile
fork = Path(__file__).resolve().parents[1]
lab = fork.parent / 'laboratorio'
run = lab / 'autonomous-2026-10-08'
profile = lab / 'userdir-autonomous-upgrade-nb31-jdk21'
required = {
    'failure-recovery.log': ['PASS rejected malformed XML', 'PASS rejected missing expression dependency', 'PASS rejected missing subreport', 'PASS valid compile/fill/PDF after three failures'],
    'unicode-paths.log': ['PASS Historico_Caixa_Sintetico', 'PASS Produto_Ficha', 'PASS Pedido_Venda_SemObs8Cm', 'PASS sale layout rows=80 pages=1', 'PASS explicit sale pagination pages=8'],
    'native-recovery-runtime.log': ['PASS decoded CODE_128 LAB0001', 'PASS decoded EAN_13 5901234123457', 'PASS decoded QR_CODE TESTE SEM VALIDADE'],
}
for filename, markers in required.items():
    text = (run / filename).read_text(encoding='utf-8', errors='replace')
    assert all(marker in text for marker in markers), filename
expected = {'DefaultLanguage':'groovy', 'Unit':'cm', 'editorFontSize':'13', 'compile_subreports':'true', 'useReportDirectoryToCompile':'true'}
preferences = dict(line.split('=', 1) for line in (profile / 'config/Preferences/com/jaspersoft/ireport.properties').read_text().splitlines() if '=' in line and not line.startswith('#'))
assert all(preferences.get(key) == value for key, value in expected.items())
nbm = lab / 'artifacts-production-candidate/ireport-designer-6.0-SNAPSHOT.nbm'
with zipfile.ZipFile(nbm) as archive:
    jars = [entry for entry in archive.namelist() if entry.startswith('netbeans/modules/') and entry.endswith('.jar')]
    assert all(archive.read(entry) == (profile / entry[9:]).read_bytes() for entry in jars)
result = {'automated_logs':'PASS', 'migrated_synthetic_preferences':expected,
          'nbm_sha256':hashlib.sha256(nbm.read_bytes()).hexdigest(), 'profile_jars_identical_to_nbm':len(jars),
          'migration_status':'controlled clean designer migration only; legacy addons/direct overlay failed',
          'scope':'isolated laboratory snapshot; no certification of later ERP changes'}
proguard = (run / 'proguard-experiment/experiment.log').read_text(encoding='utf-8', errors='replace')
assert 'BUILD FAILURE' in proguard and 'antlr.LLkParser' in proguard
result['optimization_obfuscation'] = 'FAIL: incomplete OLAP hierarchy (antlr.LLkParser); no runtime artifact validated'
(run / 'results.json').write_text(json.dumps(result, indent=2), encoding='utf-8')
print(json.dumps(result, indent=2))

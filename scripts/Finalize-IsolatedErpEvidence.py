"""Require concrete passing evidence before recording laboratory completion."""
from pathlib import Path
import hashlib, json, zipfile

lab=Path(__file__).resolve().parents[2]/'laboratorio/erp-build-01'
checks={}
def require(name, tokens):
    content=(lab/name).read_text(encoding='utf-8-sig',errors='replace')
    assert all(token in content for token in tokens), 'Incomplete evidence: '+name
    checks[name]='PASS'

require('build-corrected-tests.log',['Compiled 215 report design files.',
    'Tests run: 30, Failures: 0, Errors: 0, Skipped: 0','BUILD SUCCESS'])
require('build-svg-runtime.log',['BUILD SUCCESS'])
require('build-clean-artifacts.log',['BUILD SUCCESS'])
require('maven-proguard.log',['ProGuard, version 7.6.0','BUILD SUCCESS'])
for variant in ('full','full-signed','small','small-signed'):
    require(variant+'-packaged-reports.log',['PASS all 215 packaged reports'])
    for name in ('runtime','fixed-layout'):
        require(variant+'-'+name+'.log', ['PASS Historico_Caixa_Sintetico',
            'PASS Produto_Ficha','PASS Pedido_Venda_SemObs8Cm',
            'PASS sale layout rows=0','PASS sale layout rows=1',
            'PASS sale layout rows=80','PASS explicit sale pagination'])
    require(variant+'-boleto.log',['PASS offline Sicoob rendering',
        'PASS packaged boletoA4 proposal'])
    require(variant+'-barcodes.log',['PASS decoded CODE_128 LAB0001',
        'PASS decoded EAN_13 5901234123457','PASS decoded QR_CODE TESTE SEM VALIDADE'])
for name in ('full-signing-verify.log','small-signing-verify.log'):
    require(name,['jar verified.'])
require('native-barcode-small-signed.log',['PASS decoded CODE_128 LAB0001',
    'PASS decoded EAN_13 5901234123457','PASS decoded QR_CODE TESTE SEM VALIDADE'])
integrity=json.loads((lab/'original-integrity.json').read_text())
assert not integrity['changed'], 'Original ERP inputs changed'
artifacts={}
for name in ('AtheneSistema.jar','AtheneSistema-signed-test.jar',
             'AtheneSistema-small.jar','AtheneSistema-small-signed-test.jar'):
    path=lab/'target'/name
    with zipfile.ZipFile(path) as jar:
        names=jar.namelist()
        assert len(names)==len(set(names)), 'Duplicate ZIP entries: '+name
        assert 'Reports/compilation-failures.tsv' not in names, 'Diagnostic evidence included: '+name
        assert b'Main-Class: pcs_systemas.Main' in jar.read('META-INF/MANIFEST.MF')
    artifacts[name]={'bytes':path.stat().st_size,'sha256':hashlib.sha256(path.read_bytes()).hexdigest()}
result={'status':'PASS laboratory compatibility checks', 'checks':checks,
        'artifacts':artifacts,'original_integrity':integrity,
        'production_limits':['proposals not applied to original ERP',
          'real data provider and application acceptance not performed',
          'physical printer unavailable','production certificate/TSA not used']}
(lab/'final-results.json').write_text(json.dumps(result,indent=2),encoding='utf-8')
print(json.dumps({'status':result['status'],'checks':len(checks),
                  'original_files_unchanged':integrity['checked'],
                  'artifact_bytes':{k:v['bytes'] for k,v in artifacts.items()}},indent=2))

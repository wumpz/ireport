"""Audit optional report libraries and compare module classes to the native test profile."""
from pathlib import Path
import argparse, hashlib, io, json, zipfile

fork=Path(__file__).resolve().parents[1]
lab=fork.parent/'laboratorio'
parser=argparse.ArgumentParser(description=__doc__)
parser.add_argument('--nbm',type=Path,default=fork/'ireport-designer/target/ireport-designer-6.0-SNAPSHOT.nbm')
parser.add_argument('--profile',type=Path,default=lab/'userdir-svg-validation-nb31-jdk21')
parser.add_argument('--output',type=Path,default=lab/'designer-svg-native-01/nbm-audit.json')
args=parser.parse_args()
path=args.nbm
profile=args.profile
with zipfile.ZipFile(path) as nbm:
    names=nbm.namelist()
    required=['batik-bridge.jar','batik-svggen.jar','core.jar','javase.jar']
    assert all(any(n.endswith('/'+r) for n in names) for r in required)
    assert not any('pdfbox' in n.lower() for n in names), 'Test library packaged'
    modules=[n for n in names if n.startswith('netbeans/modules/')
             and n.endswith('.jar') and '/ext/' not in n]
    assert len(modules)==1
    with zipfile.ZipFile(io.BytesIO(nbm.read(modules[0]))) as final, \
         zipfile.ZipFile(profile/modules[0].removeprefix('netbeans/')) as native:
        classes=[n for n in final.namelist() if n.endswith('.class')]
        assert set(classes)=={n for n in native.namelist() if n.endswith('.class')}
        changed=[n for n in classes if final.read(n)!=native.read(n)]
        assert not changed, 'Module classes differ from native-tested build: '+str(changed[:5])
        assert all(int.from_bytes(final.read(n)[6:8],'big')<=61 for n in classes)
    ext=[n for n in names if n.startswith('netbeans/modules/ext/') and n.endswith('.jar')]
    assert all(nbm.read(n)==(profile/n.removeprefix('netbeans/')).read_bytes() for n in ext)
result={'sha256':hashlib.sha256(path.read_bytes()).hexdigest(),
        'required_libraries':required,'pdfbox_test_dependency_not_packaged':True,
        'native_tested_module_classes_identical':len(classes),
        'native_tested_extension_jars_identical':len(ext),
        'module_bytecode_maximum':'Java 17'}
args.output.write_text(json.dumps(result,indent=2))
print(json.dumps(result,indent=2))

"""Read-only class provenance and application manifest checks for laboratory jars."""
import argparse, hashlib, json, zipfile
from pathlib import Path

parser=argparse.ArgumentParser()
parser.add_argument('candidate',type=Path)
parser.add_argument('source',type=Path)
args=parser.parse_args()
m2=Path.home()/'.m2/repository'
expected={
 'net/sf/jasperreports/engine/design/JRAbstractCompiler.class':'net/sf/jasperreports/jasperreports/6.21.4/jasperreports-6.21.4.jar',
 'com/lowagie/text/Phrase.class':'com/github/librepdf/openpdf/1.3.30.jaspersoft.3/openpdf-1.3.30.jaspersoft.3.jar',
 'org/codehaus/groovy/reflection/ReflectionUtils.class':'org/codehaus/groovy/groovy/3.0.20/groovy-3.0.20.jar',
}
def manifest(data):
    lines=[]
    for line in data.decode('utf-8').replace('\r\n','\n').split('\n\n')[0].splitlines():
        if line.startswith(' ') and lines:lines[-1]+=line[1:]
        else:lines.append(line)
    return dict(line.split(': ',1) for line in lines if ': ' in line)
result={}
with zipfile.ZipFile(args.candidate) as candidate,zipfile.ZipFile(args.source) as source:
    names=candidate.namelist()
    assert len(names)==len(set(names)), 'Duplicate ZIP entries'
    main=manifest(candidate.read('META-INF/MANIFEST.MF'))
    original=manifest(source.read('META-INF/MANIFEST.MF'))
    assert main.get('Main-Class')==original.get('Main-Class'), 'Application entry point changed'
    assert main.get('Main-Class'), 'Missing application entry point'
    result['application_entry_point_preserved']=True
    for name,dependency in expected.items():
        with zipfile.ZipFile(m2/dependency) as official:
            assert candidate.read(name)==official.read(name), 'Wrong class provenance: '+name
        result[name]='matches official dependency'
result['sha256']=hashlib.sha256(args.candidate.read_bytes()).hexdigest()
print(json.dumps(result,indent=2))

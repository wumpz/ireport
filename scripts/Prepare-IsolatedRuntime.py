"""Build an unsigned lab candidate from a delivered jar; never writes to ERP."""
from pathlib import Path
import zipfile, hashlib, json, re, argparse

parser = argparse.ArgumentParser()
parser.add_argument('--name', default='runtime-candidate-02')
args = parser.parse_args()
if not re.fullmatch(r'runtime-candidate-[0-9]+', args.name):
    raise SystemExit('Invalid laboratory candidate name')
lab = Path(__file__).resolve().parents[2] / 'laboratorio' / args.name
erp = Path(__file__).resolve().parents[3] / 'AtheneSistema'
m2 = Path.home() / '.m2/repository'
if lab.exists():
    raise SystemExit('Refusing to overwrite an existing candidate')
lab.mkdir()
official = [m2 / p for p in [
    'net/sf/jasperreports/jasperreports/6.21.4/jasperreports-6.21.4.jar',
    'net/sf/jasperreports/jasperreports-fonts/6.21.4/jasperreports-fonts-6.21.4.jar',
    'org/codehaus/groovy/groovy/3.0.20/groovy-3.0.20.jar',
    'com/github/librepdf/openpdf/1.3.30.jaspersoft.3/openpdf-1.3.30.jaspersoft.3.jar']]
for p in official:
    if not p.is_file(): raise FileNotFoundError(p)
prefixes = ('net/sf/jasperreports/', 'com/lowagie/', 'groovy/', 'org/codehaus/groovy/', 'groovyjarjar')
def replaced(n):
    return n.startswith(prefixes) or (n.startswith('META-INF/versions/') and any('/'+p in n for p in prefixes))
def signature(n):
    return n.startswith('META-INF/') and n.upper().endswith(('.SF', '.RSA', '.DSA', '/INDEX.LIST'))
source = erp / 'target/AtheneSistema.jar'
entries, services, extensions = {}, {}, {}
manifests = []
def add(z, primary=False):
    for info in z.infolist():
        n = info.filename
        if info.is_dir() or signature(n) or (primary and replaced(n)): continue
        data=z.read(n)
        if n.upper() == 'META-INF/MANIFEST.MF':
            manifests.append((primary, data))
            continue
        if n.startswith('META-INF/services/'):
            services.setdefault(n,set()).update(s.split('#')[0].strip() for s in data.decode('utf-8-sig').splitlines() if s.split('#')[0].strip())
        elif n == 'jasperreports_extension.properties':
            for line in data.decode('latin1').splitlines():
                if line.strip() and not line.lstrip().startswith(('#','!')) and '=' in line:
                    k,v=line.split('=',1);extensions[k.strip()]=v.strip()
        else: entries[n]=data
with zipfile.ZipFile(source) as z: add(z,True)
for p in official:
    with zipfile.ZipFile(p) as z: add(z)
# Preserve the application entry point, removing obsolete signing digests.
# Dependency manifests must never replace the application manifest.
main = next(data for primary, data in manifests if primary)
main = main.replace(b'\r\n', b'\n').split(b'\n\n', 1)[0]
logical = []
for line in main.split(b'\n'):
    if line.startswith(b' ') and logical:
        logical[-1] += line[1:]
    elif line:
        logical.append(line)
logical = [line for line in logical if b'digest' not in line.split(b':',1)[0].lower()
           and not line.lower().startswith(b'multi-release:')]
if any(b'multi-release: true' in data.lower() for _, data in manifests):
    logical.append(b'Multi-Release: true')
wrapped = []
for line in logical:
    wrapped.append(line[:70])
    line = line[70:]
    while line:
        wrapped.append(b' ' + line[:69])
        line = line[69:]
entries['META-INF/MANIFEST.MF'] = b'\r\n'.join(wrapped) + b'\r\n\r\n'
for n, providers in services.items(): entries[n]=('\n'.join(sorted(providers))+'\n').encode()
entries['jasperreports_extension.properties']=('\n'.join(k+'='+v for k,v in sorted(extensions.items()))+'\n').encode('latin1')
target=lab/'candidate-full.jar'
with zipfile.ZipFile(target,'w',zipfile.ZIP_DEFLATED,compresslevel=1) as z:
    for n,data in entries.items(): z.writestr(n,data)
rules=(erp/'proguard-rules.pro').read_text(encoding='utf-8-sig')
rules+='\n# Isolated report runtime proposal: preserve dynamic expression access.\n'
rules+='-keep class com.lowagie.** { *; }\n'
for package in ['groovy.**','org.codehaus.groovy.**','groovyjarjar**','Bean.**','Util.MetodosUteis','Util.MetodosUteis$*','org.apache.pdfbox.**','org.apache.fontbox.**','br.com.java_brasil.**','org.hibernate.validator.**','javax.validation.**','org.jboss.logging.**','javax.el.**','com.sun.el.**']:
    rules+='-keep class '+package+' { *; }\n'
for provider in sorted(set().union(*services.values())):
    if re.fullmatch(r'[\w.$]+',provider): rules+='-keep class '+provider+' { *; }\n'
(lab/'runtime-rules.pro').write_text(rules,encoding='utf-8')
(lab/'proguard.pro').write_text("-injars 'candidate-full.jar'\n-outjars 'candidate-small.jar'\n-libraryjars 'C:/Program Files/Java/jdk-17/jmods'(!**.jar;!module-info.class)\n-include runtime-rules.pro\n-dontnote\n-dontwarn **\n-ignorewarnings\n-printusage removed.txt\n",encoding='utf-8')
(lab/'pom.xml').write_text('''<project xmlns="http://maven.apache.org/POM/4.0.0"><modelVersion>4.0.0</modelVersion><groupId>lab</groupId><artifactId>runtime-validation</artifactId><version>1</version><dependencies><dependency><groupId>com.guardsquare</groupId><artifactId>proguard-base</artifactId><version>7.6.0</version></dependency></dependencies></project>''',encoding='utf-8')
(lab/'provenance.json').write_text(json.dumps({'source':str(source),'source_sha256':hashlib.sha256(source.read_bytes()).hexdigest(),'official_dependencies':[str(p) for p in official],'candidate_sha256':hashlib.sha256(target.read_bytes()).hexdigest(),'scope':'unsigned isolated experiment; not for distribution'},indent=2),encoding='utf-8')
print(lab)

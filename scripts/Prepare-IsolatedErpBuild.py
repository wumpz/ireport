"""Prepare a sanitized source build experiment; ERP inputs are read only."""
from pathlib import Path
import argparse, hashlib, json, shutil, xml.etree.ElementTree as ET

base = Path(__file__).resolve().parents[2]
erp = base.parent / 'AtheneSistema'
parser = argparse.ArgumentParser()
parser.add_argument('--destination', default=str(base / 'laboratorio/erp-build-01'))
args = parser.parse_args()
lab = Path(args.destination).resolve()
if not lab.is_relative_to((base / 'laboratorio').resolve()):
    raise SystemExit('Destination must stay within the laboratory')
if lab.exists():
    raise SystemExit('Refusing to overwrite existing laboratory build')
lab.mkdir()
baseline = {}
for directory in ('src', 'Reports', 'lib', 'openapi'):
    for source in (erp / directory).rglob('*'):
        if source.is_file():
            relative = source.relative_to(erp)
            target = lab / relative
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, target)
            baseline[str(relative)] = hashlib.sha256(source.read_bytes()).hexdigest()
for name in ('pom.xml', 'proguard-rules.pro'):
    baseline[name] = hashlib.sha256((erp / name).read_bytes()).hexdigest()
(lab / 'original-input-hashes.json').write_text(json.dumps(baseline, indent=2))

ns = {'m': 'http://maven.apache.org/POM/4.0.0'}
ET.register_namespace('', ns['m'])
tree = ET.parse(erp / 'pom.xml')
root = tree.getroot()
dependencies = root.find('m:dependencies', ns)
for group, name, version in [('net.sf.barcode4j', 'barcode4j', '2.1'),
                             ('net.sourceforge.barbecue', 'barbecue', '1.5-beta1'),
                             ('org.apache.xmlgraphics', 'batik-bridge', '1.17'),
                             ('org.apache.xmlgraphics', 'batik-svggen', '1.17')]:
    if not any(d.findtext('m:artifactId', namespaces=ns) == name for d in dependencies):
        dependency = ET.SubElement(dependencies, '{'+ns['m']+'}dependency')
        for key, value in [('groupId', group), ('artifactId', name), ('version', version)]:
            ET.SubElement(dependency, '{'+ns['m']+'}'+key).text = value
for resource in root.findall('m:build/m:resources/m:resource', ns):
    if resource.findtext('m:directory', namespaces=ns) == 'Reports':
        includes = resource.find('m:includes', ns)
        if includes is not None:
            for include in list(includes):
                if include.text == '**/*.jasper':
                    includes.remove(include)
profiles = root.find('m:profiles', ns)
if profiles is not None:
    root.remove(profiles)
for container in root.findall('.//m:plugins', ns):
    for plugin in list(container):
        artifact = plugin.findtext('m:artifactId', namespaces=ns)
        if artifact == 'maven-antrun-plugin':
            container.remove(plugin)
        elif artifact == 'jasperreports-maven-plugin':
            for phase in plugin.findall('m:executions/m:execution/m:phase', ns):
                phase.text = 'process-classes'
            dependencies = plugin.find('m:dependencies', ns)
            for group, name, version in [('net.sf.barcode4j', 'barcode4j', '2.1'),
                                         ('net.sourceforge.barbecue', 'barbecue', '1.5-beta1')]:
                dependency = ET.SubElement(dependencies, '{'+ns['m']+'}dependency')
                for key, value in [('groupId', group), ('artifactId', name), ('version', version)]:
                    ET.SubElement(dependency, '{'+ns['m']+'}'+key).text = value
properties = root.find('m:properties', ns)
if properties is not None:
    for prop in list(properties):
        key = prop.tag.split('}')[-1].lower()
        if any(word in key for word in ('password', 'keystore', 'signing')):
            properties.remove(prop)
    for prop in properties:
        if prop.tag.split('}')[-1] == 'skip.jarsigner':
            prop.text = 'true'
ET.indent(tree, space='  ')
tree.write(lab / 'pom.xml', encoding='utf-8', xml_declaration=True)

assembly = lab / 'src/assembly/athene-jar.xml'
ans = {'a': 'http://maven.apache.org/ASSEMBLY/2.1.1'}
ET.register_namespace('', ans['a'])
atree = ET.parse(assembly)
aroot = atree.getroot()
excludes = aroot.find('a:dependencySets/a:dependencySet/a:unpackOptions/a:excludes', ans)
for pattern in ('net/sf/jasperreports/**', 'com/lowagie/**', 'groovy/**', 'org/codehaus/groovy/**', 'groovyjarjar*/**'):
    ET.SubElement(excludes, '{'+ans['a']+'}exclude').text = pattern
handlers = ET.SubElement(aroot, '{'+ans['a']+'}containerDescriptorHandlers')
handler = ET.SubElement(handlers, '{'+ans['a']+'}containerDescriptorHandler')
ET.SubElement(handler, '{'+ans['a']+'}handlerName').text = 'metaInf-services'
ET.indent(atree, space='  ')
atree.write(assembly, encoding='utf-8', xml_declaration=True)
shutil.copy2(base / 'laboratorio/runtime-candidate-02/runtime-rules.pro', lab / 'proguard-rules.pro')
with (lab / 'proguard-rules.pro').open('a', encoding='utf-8') as rules:
    rules.write('\n# Preserve SVG/barcode implementations and dynamic report access.\n')
    for package in ('org.apache.batik.**', 'org.apache.xmlgraphics.**',
                    'org.krysalis.barcode4j.**', 'net.sourceforge.barbecue.**',
                    'com.google.zxing.**', 'org.apache.xml.serializer.**',
                    'org.apache.xalan.**', 'org.apache.xpath.**'):
        rules.write('-keep class '+package+' { *; }\n')
(lab / 'scope.json').write_text(json.dumps({
    'source': str(erp), 'copy': str(lab),
    'changes': ['exclude bundled obsolete report engines', 'merge SPI services',
                'compile reports after ERP classes', 'do not reuse old Jasper resources',
                'validated shrinking rules'],
    'signing': 'production signing removed; laboratory signing only',
    'reports': 'original report sources copied without modifications'
}, indent=2))
print(lab)

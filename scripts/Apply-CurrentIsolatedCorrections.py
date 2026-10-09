"""Apply current delivery proposals only to a fresh, sanitized laboratory build."""
import argparse
import hashlib
import json
from pathlib import Path
import re
import shutil
import xml.etree.ElementTree as ET

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('--build', type=Path, required=True)
args = parser.parse_args()
fork = Path(__file__).resolve().parents[1]
build = args.build.resolve()
if not build.is_relative_to((fork.parent / 'laboratorio').resolve()):
    raise SystemExit('Use a laboratory build only')
if not (build / 'scope.json').exists():
    raise SystemExit('Prepare a sanitized isolated build first')
backup = build / 'current-corrections-before'
backup.mkdir(exist_ok=False)
names = ('Reports/boletoA4.jrxml', 'Reports/Pedido_Venda_SemObs8Cm.jrxml', 'pom.xml')
for name in names:
    dest = backup / name
    dest.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(build / name, dest)
boleto = build / names[0]
text = boleto.read_text(encoding='utf-8-sig')
assert text.count('$F{beneficiario}.getNossoNumero()') == 2
text = text.replace('$F{beneficiario}.getNossoNumero()', '$F{nossoNumero}')
assert '<field name="nossoNumero"' not in text
text = text.replace('<field name="documentoBeneficiario"', '<field name="nossoNumero" class="java.lang.String"/>\n\t<field name="documentoBeneficiario"', 1)
boleto.write_text(text, encoding='utf-8')
sale = build / names[1]
text = sale.read_text(encoding='utf-8-sig')
start, end = text.index('<detail>'), text.index('</detail>')
detail = text[start:end]
price_ids = {'92364a19-3a51-4a47-88d0-47fbdb2a8b03', 'd8506c9a-2570-4c4d-8ff3-14f7830d1407',
             '0d3e4c50-4227-48b6-a63a-66fd12de0a3c', 'e3dbf25e-43c4-454d-b765-623806b2a03e'}
found = set()
def float_price(match):
    tag = match.group()
    if not any(identity in tag for identity in price_ids):
        return tag
    found.update(identity for identity in price_ids if identity in tag)
    tag = re.sub(r' stretchType="[^"]*"', '', tag)
    if 'positionType=' in tag:
        return re.sub(r'positionType="[^"]*"', 'positionType="Float"', tag)
    return tag.replace('<reportElement ', '<reportElement positionType="Float" ', 1)
detail = re.sub(r'<reportElement\b[^>]*>', float_price, detail)
assert found == price_ids, 'Unknown current sale structure; review instead of replacing whole report'
sale.write_text(text[:start] + detail + text[end:], encoding='utf-8')

# The old Maven plugin can emit a Groovy expression treating an unresolved package
# as a script property. Fork a JVM with the complete application classpath instead.
ns = {'m': 'http://maven.apache.org/POM/4.0.0'}
tag = '{' + ns['m'] + '}'
ET.register_namespace('', ns['m'])
tree = ET.parse(build / 'pom.xml')
plugins = tree.getroot().find('m:build/m:plugins', ns)
for plugin in list(plugins):
    if plugin.findtext('m:artifactId', namespaces=ns) == 'jasperreports-maven-plugin':
        plugins.remove(plugin)
plugin = ET.SubElement(plugins, tag + 'plugin')
for key, value in [('groupId', 'org.codehaus.mojo'), ('artifactId', 'exec-maven-plugin'), ('version', '3.1.0')]:
    ET.SubElement(plugin, tag + key).text = value
execution = ET.SubElement(ET.SubElement(plugin, tag + 'executions'), tag + 'execution')
for key, value in [('id', 'compile-reports-isolated-classpath'), ('phase', 'process-classes')]:
    ET.SubElement(execution, tag + key).text = value
ET.SubElement(ET.SubElement(execution, tag + 'goals'), tag + 'goal').text = 'exec'
config = ET.SubElement(execution, tag + 'configuration')
ET.SubElement(config, tag + 'executable').text = '${java.home}/bin/java'
arguments = ET.SubElement(config, tag + 'arguments')
for value in ('-Djava.awt.headless=true', '-classpath'):
    ET.SubElement(arguments, tag + 'argument').text = value
ET.SubElement(arguments, tag + 'classpath')
for value in ('labvalidation.CompileReports', '${project.basedir}/Reports', '${project.build.outputDirectory}/Reports'):
    ET.SubElement(arguments, tag + 'argument').text = value
ET.indent(tree, space='  ')
tree.write(build / 'pom.xml', encoding='utf-8', xml_declaration=True)
target = build / 'src/main/java/labvalidation/CompileReports.java'
target.parent.mkdir(parents=True, exist_ok=True)
shutil.copy2(fork / 'test-fixtures/engine/CompileReports.java', target)
changes = {name: {'before': hashlib.sha256((backup / name).read_bytes()).hexdigest(),
                  'after': hashlib.sha256((build / name).read_bytes()).hexdigest()} for name in names}
(build / 'current-corrections.json').write_text(json.dumps(changes, indent=2), encoding='utf-8')
print('Applied laboratory-only report and compiler proposals')

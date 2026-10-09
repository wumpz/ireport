"""Prepare a proposed layout fix in a new laboratory folder; preserve original files."""
from pathlib import Path
import re, shutil, json, hashlib

lab=Path(__file__).resolve().parents[2]/'laboratorio'
source=lab/'roundtrip-modern-01/reference/Pedido_Venda_SemObs8Cm.jrxml'
target=lab/'sale-layout-fix-01'
if target.exists():raise SystemExit('Refusing to overwrite layout experiment')
target.mkdir()
text=source.read_text(encoding='utf-8')
start=text.index('<detail>');end=text.index('</detail>',start)
detail=text[start:end]
changed=0
def float_price(match):
    global changed
    tag=match.group(0)
    if 'y="20"' not in tag:return tag
    tag=re.sub(r' stretchType="[^"]*"','',tag)
    changed+=1
    return tag.replace('<reportElement ','<reportElement positionType="Float" ',1)
detail=re.sub(r'<reportElement\b[^>]*>',float_price,detail)
assert changed==4, f'Unexpected price row shape: {changed}'
text=text[:start]+detail+text[end:]
(target/source.name).write_text(text,encoding='utf-8')
for file in (lab/'final-jar-tests/recompiled').glob('*.jasper'):
    if file.stem!=source.stem:shutil.copy2(file,target/file.name)
(target/'proposal.json').write_text(json.dumps({'source':str(source),'source_sha256':hashlib.sha256(source.read_bytes()).hexdigest(),'changed_elements':changed,'scope':'laboratory proposal only; no ERP/report originals modified'},indent=2))
print(target)

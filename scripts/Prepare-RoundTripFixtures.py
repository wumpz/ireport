"""Create isolated ERP-derived fixtures with modern JasperReports properties."""
from pathlib import Path
import re
import shutil
import json

lab = Path(__file__).resolve().parents[2] / "laboratorio"
source = lab / "fixtures" / "athene-snapshot"
destination = lab / "roundtrip-modern-01"
if destination.exists():
    raise SystemExit("Refusing to overwrite an existing experiment")
names = ["Pedido_Venda_SemObs8Cm", "Historico_Caixa_Sintetico",
         "Historico_Caixa_Sintetico_Entradas", "Produto_Ficha",
         "Produto_Ficha_AcoesProducao", "Produto_Ficha_MatPrima"]
for folder in ("work", "reference", "original", "evidence"):
    (destination / folder).mkdir(parents=True)
for name in names:
    original = source / (name + ".jrxml")
    shutil.copy2(original, destination / "original" / original.name)
    xml = original.read_text(encoding="utf-8-sig")
    def modern_field(match):
        block = re.sub(r'\s+(?:isStretchWithOverflow|textAdjust)="[^"]*"', '', match.group(0), count=1)
        block = block.replace("<textField", '<textField textAdjust="StretchHeight"', 1)
        block = re.sub(r'\s+stretchType="[^"]*"', '', block, count=1)
        block = block.replace("<reportElement", '<reportElement stretchType="ContainerHeight"', 1)
        if re.search(r'<font\b', block):
            block = re.sub(r'(<font\b[^>]*\bsize=")[^"]*"', r'\g<1>10.5"', block, count=1)
            if not re.search(r'<font\b[^>]*\bsize=', block):
                block = block.replace('<font', '<font size="10.5"', 1)
        else:
            block = re.sub(r'(<textElement\b[^>]*>)', r'\1<font size="10.5"/>', block, count=1)
        return block
    xml, count = re.subn(r'<textField\b.*?</textField>', modern_field, xml, count=1, flags=re.S)
    if count != 1 or 'size="10.5"' not in xml:
        raise AssertionError(name + ": modern fixture not injected")
    for folder in ("work", "reference"):
        (destination / folder / original.name).write_text(xml, encoding="utf-8")
(destination / "manifest.json").write_text(json.dumps({"reports": names,
    "properties": ["textAdjust=StretchHeight", "stretchType=ContainerHeight", "font size=10.5"],
    "scope": "Copies only; no ERP file changes"}, indent=2), encoding="utf-8")
print(destination)

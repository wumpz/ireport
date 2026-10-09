"""Apply reviewable proposals only inside the laboratory source build."""
from pathlib import Path
import hashlib, json, shutil, xml.etree.ElementTree as ET

base=Path(__file__).resolve().parents[2]
lab=base/'laboratorio/erp-build-01'
backup=lab/'report-proposals-before'
backup.mkdir(exist_ok=False)
for name in ('boletoA4.jrxml','Pedido_Venda_SemObs8Cm.jrxml'):
    shutil.copy2(lab/'Reports'/name,backup/name)
report=lab/'Reports/boletoA4.jrxml'
text=report.read_text(encoding='utf-8-sig')
old='$F{beneficiario}.getNossoNumero()'
assert text.count(old)==2
text=text.replace(old,'$F{nossoNumero}')
marker='<field name="documentoBeneficiario"'
assert marker in text
text=text.replace(marker,'<field name="nossoNumero" class="java.lang.String"/>\n\t'+marker,1)
report.write_text(text,encoding='utf-8')
ET.parse(report)
shutil.copy2(base/'laboratorio/sale-layout-fix-01/Pedido_Venda_SemObs8Cm.jrxml',lab/'Reports/Pedido_Venda_SemObs8Cm.jrxml')
result={name:{'before':hashlib.sha256((backup/name).read_bytes()).hexdigest(),
              'after':hashlib.sha256((lab/'Reports'/name).read_bytes()).hexdigest()}
        for name in ('boletoA4.jrxml','Pedido_Venda_SemObs8Cm.jrxml')}
(lab/'report-proposals.json').write_text(json.dumps(result,indent=2))
print('Applied two report proposals in laboratory only')

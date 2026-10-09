"""Compare all XML attributes, element order and meaningful text after Jasper normalization."""
from pathlib import Path
import xml.etree.ElementTree as ET
import json
import difflib

root = Path(__file__).resolve().parents[2] / "laboratorio" / "roundtrip-modern-01"
def structure(path):
    def visit(node):
        return [node.tag, sorted(node.attrib.items()),
                node.text if node.text and node.text.strip() else None,
                [visit(child) for child in node],
                node.tail if node.tail and node.tail.strip() else None]
    return visit(ET.parse(path).getroot())
results = []
for before in sorted((root / "evidence").glob("*.before.xml")):
    name = before.name.removesuffix(".before.xml")
    after = root / "evidence" / (name + ".after.xml")
    binary = root / "evidence" / (name + ".binary.xml")
    expected, restored, compiled = map(structure, (before, after, binary))
    same = expected == restored == compiled
    if not same:
        diff = difflib.unified_diff(json.dumps(expected, indent=2).splitlines(),
            json.dumps(restored, indent=2).splitlines(), fromfile="before", tofile="after")
        (root / "evidence" / (name + ".diff.txt")).write_text("\n".join(diff), encoding="utf-8")
    xml = ET.parse(after).getroot()
    elements = list(xml.iter())
    modern = [e for e in elements if e.attrib.get("textAdjust") == "StretchHeight"]
    stretch = [e for e in elements if e.attrib.get("stretchType") == "ContainerHeight"]
    fractional = [e for e in elements if e.tag.endswith("}font") and e.attrib.get("size") == "10.5"]
    row = {"report": name, "complete_structure_equal": same,
        "modern_text": len(modern), "container_height": len(stretch), "fractional_fonts": len(fractional),
        "subreports": sum(e.tag.endswith("}subreport") for e in elements)}
    results.append(row)
    print(json.dumps(row))
if len(results) != 6:
    raise AssertionError("Expected six independently audited report files")
(root / "evidence" / "comparison.json").write_text(json.dumps(results, indent=2), encoding="utf-8")
if not all(r["complete_structure_equal"] and r["modern_text"] and r["container_height"] and r["fractional_fonts"] for r in results):
    raise SystemExit("FAIL: inspect per-report differences")
print("PASS: all six complete normalized XML structures and modern properties preserved")

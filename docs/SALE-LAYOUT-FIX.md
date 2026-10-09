# Sale layout correction — 2026-10-07

Scope: a new laboratory copy only. No existing ERP report, source, POM or delivered jar was changed.

`scripts/Prepare-SaleLayoutFix.py` prepares `laboratorio/sale-layout-fix-01/Pedido_Venda_SemObs8Cm.jrxml`. Four detail-row elements (quantity, multiplication sign, unit price and extended price) now use `positionType="Float"` and no longer stretch with the tallest element. Their horizontal placement and expressions stay unchanged. The row moves below the growing description, retaining its normal gap.

Verification:

- The geometry regression check fails on the earlier laboratory template: description overlaps price at y=184.
- The corrected template compiles with the ERP classes available.
- The full laboratory runtime and the final reduced, signed laboratory runtime both pass real-bean PDF filling, zero/one/80-item cases and explicit eight-page pagination.
- A geometry check confirms that the rendered price boxes do not intersect the long-description boxes, in continuous and paginated output.
- Visual inspection of the one-item PNG confirms readable accented description lines and a separate quantity/price row.

Evidence: `full-test.log`, `small-signed-test.log`, `full-pdf/sale-layout-1.png`, and the PDF directories in that laboratory folder. Source hash and exact scope are recorded in `proposal.json`.

This proposal is not installed in the ERP. Printer length, footer and cutter acceptance remain pending. This correction does not claim to fix the different printed template shown in the original photo.

The tab-restoration warning was also investigated: JrxmlVisualView, JrxmlTextView and JrxmlPreviewView all explicitly return `TopComponent.PERSISTENCE_NEVER`. Restoration is intentionally disabled by the inherited editor implementation; automatic restoration would require a separate design and restart validation. No speculative serialization change was retained.

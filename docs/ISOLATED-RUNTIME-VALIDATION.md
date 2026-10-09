# Isolated runtime candidate

Initial validation on 2026-10-06 with Java 17. The user requested that ProGuard be handled last. The subsequent designer, layout, signing and shrinking results are recorded in PRODUCTION-READINESS.md; the initial results below retain their original scope.

## Scope

`scripts/Prepare-IsolatedRuntime.py` reads the delivered full ERP jar and creates an unsigned laboratory jar. It replaces the conflicting embedded JasperReports, Groovy and PDF classes with official JasperReports 6.21.4, fonts 6.21.4, Groovy 3.0.20 and OpenPDF 1.3.30.jaspersoft.3. Service registrations and report extensions are merged. The application main manifest is preserved, obsolete signing digests removed, and dependency manifests do not replace the application entry point.

This experiment does not modify the ERP assembly, POM, source, reports, delivered jars or signing configuration. It does not demonstrate a complete ERP Maven build or authorize deployment.

## Results

Candidate: `laboratorio/runtime-candidate-02/candidate-full.jar`, with provenance and hashes beside it.

Without external runtime dependency overrides, `FinalJarReportSmoke` passed PDF generation and content checks for:

- Historico_Caixa_Sintetico, including its subreport;
- Produto_Ficha, including its two distinct subreports;
- Pedido_Venda_SemObs8Cm, including the expected monetary total.

These six report binaries were compiled in the earlier laboratory runtime test with the ERP classes available. They are not evidence that the remaining native editor classpath test has passed.

`BoletoRuntimeSmoke` passed offline rendering of the library's embedded Sicoob template, barcode rendering, and merging two copies into a two-page PDF with beneficiary and payer text preserved. All data are synthetic and marked as invalid for payment. No bank service or registration is invoked. This is a rendering compatibility test, not banking or financial validation. The library emits a Log4j fallback warning; rendering succeeds.

Evidence: `full-pdf.log`, `boleto-test.log`, their PDF output directories, and `erp-integrity.json`. All 3,102 protected ERP source/report/POM files match the initial SHA-256 baseline.

## Remaining order

1. Complete native NetBeans editor opening/classpath/recompilation checks.
2. Complete additional layout cases and inspect generated PDF appearance.
3. Validate shrinking with ProGuard, then any signing checks on laboratory artifacts.
4. Record deployment requirements without applying changes to the protected ERP.

Physical printing and the production application's complete execution remain separate acceptance checks. The candidate is unsigned and must not replace a production jar.

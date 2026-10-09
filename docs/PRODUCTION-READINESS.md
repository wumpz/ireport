# Production acceptance — 2026-10-07

## Decision

Latest corrected laboratory build 2026-10-08: current copied sources compile 215 reports and pass 30 offline unit tests. Full and optimized/obfuscated JARs each pass six runtime groups, unsigned and temporarily signed. A forked compiler with the complete application classpath resolves a Groovy expression that the old Maven compiler silently emitted incorrectly. Relevant ERP originals remain unchanged; applying these proposals and resolving the production signing certificate remain pending. See CURRENT-CORRECTIONS-VALIDATION.md. The audited designer NBM with 91 dependencies is published as RC1; see BINARY-DISTRIBUTION.md.

Latest delivery check 2026-10-08: copies of the current ERP full/small JARs fail release gates for missing Barcode4J, reduced Groovy/reflection dependencies, boletoA4 expression and strict signature verification (certificate ExtendedKeyUsage disallows code signing). The isolated candidate's direct Jasper print exports pass spool submission, and offline migration rollback restores 315 files exactly. Physical width and barcode/QR readings were confirmed by the user for previous PDF prints. These results do not approve the current ERP delivery; see CURRENT-DELIVERY-VALIDATION.md.

Latest ProGuard update 2026-10-08: experiment 03 completes optimization/obfuscation and passes six offline runtime groups, including all 215 packaged binaries, subreports, barcodes and failure recovery. ANTLR analysis input and platform XML API keep rules resolve the previous failures. This approves the exercised report paths, not the entire obfuscated ERP; see OBFUSCATION-VALIDATION.md.

Latest designer update 2026-10-08: selective migration now preserves 174 unrelated files and native editor/preview/Options pass. The XML provider failure was reproduced with current Woodstox as well; excluding its transitive service provider resolves the NetBeans SVG loader conflict. Java 21/25 each pass 12 tests without skips. The new NBM has 91 extension libraries and SHA-256 `8eacb06ec7d49b816a7dd6a0d5fc6c255c9255e94d0054d1e30d948a4d994119`. See SELECTIVE-MIGRATION-VALIDATION.md. Legacy companion modules remain quarantined and unsupported by this validation.

Update 2026-10-08: additional autonomous tests passed for Unicode paths, engine failure recovery, controlled designer migration, restart and native preview recovery. Direct overlay on the legacy plugin set failed because of companion module identity requirements and stale XML libraries. Enabling optimization and obfuscation also failed ProGuard preverification because the supplied OLAP hierarchy lacks ANTLR. These configurations are not production-approved; see AUTONOMOUS-VALIDATION.md.

Update 2026-10-07: the long-description correction now passes in a separate laboratory copy, including the signed reduced runtime. See SALE-LAYOUT-FIX.md. Original ERP reports remain unchanged; production release gates below still apply. The legacy editor explicitly uses PERSISTENCE_NEVER, so tab restoration is an inherited unsupported behavior, not a newly established regression.

The laboratory passes are evidence for the updated designer and isolated report runtime. They do **not** certify the current delivered ERP jar or physical printing for production. Do not deploy the laboratory ERP jars. No protected ERP file is changed by this task.

## Confirmed

- Apache NetBeans 31: final plugin builds pass on JDK 21 and JDK 25, with 10 automated tests on each. The final NBM matches the native validation profile: 2,626 module classes with Java 17-compatible bytecode and 92 extension libraries. The 215-template designer corpus test checks XML loading and canonical rewriting. Separate isolated ERP source validation compiles all 215 templates and passes 30 selected unit tests; see SOURCE-BUILD-VALIDATION.md.
- Native visual editor: sale opens with the laboratory application jar in iReport Classpath; `Report Inspector > report name > Compile Report` generates its Jasper binary. `Tools > Options > iReport` and its Classpath panel open without closing the IDE.
- The native compiled sale binary runs with the coherent Java 17 laboratory runtime, including the real `Util.MetodosUteis` method. Cash and product parents also render their subreports.
- Native Jasper-to-JRXML conversion and complete structural comparison previously passed for all six templates; see ROUNDTRIP-VALIDATION.md.
- Synthetic sale data: zero rows gives zero pages (existing template behavior); one long item renders one continuous page; 80 items render one continuous page; explicitly enabling pagination gives eight pages. PDF content checks retain the first/last items and accented text.
- Offline Sicoob boleto rendering, barcode rendering and PDF merging pass in the full coherent jar.
- A temporary self-signed laboratory certificate signs the full jar; `jarsigner -verify` succeeds and the signed jar runs all three report parents. Expected self-signed, expiry and timestamp warnings do not constitute production certificate validation. Production keystore and TSA were not used.
- The final round-3 reduced jar is also signed and verified with the same temporary certificate. Its signed form passes all report/layout data checks and offline boleto/barcode/PDF-merge checks (`small-signed-runtime.log`, `small-signed-boleto.log`, `small-signing-verify.log`).
- Packaging audit verifies the application Main-Class is preserved, ZIP names are unique, and representative JasperReports, Groovy and OpenPDF classes match official dependencies.

## Material findings

1. **The original sale layout overlaps very long descriptions and prices.** Fixed vertical positions caused this; compilation and text extraction alone did not detect it. Evidence: `runtime-candidate-02/layout-pdf/sale-layout-1.png` and `sale-paginated-last.png`. The floating-position proposal passes in laboratory copies, including the source build; see SALE-LAYOUT-FIX.md and SOURCE-BUILD-VALIDATION.md. Originals remain unchanged and physical printer acceptance remains outstanding.
2. The delivered ERP jars embed older JasperReports/PDF classes from java-boletos and need coherent assembly before consuming newly compiled reports. Installing the editor plugin alone does not replace the ERP runtime. See FINAL-JAR-VALIDATION.md.
3. Existing ProGuard rules remove reflective/dynamic dependencies. Round 1 loses the Hibernate Validator generated JBoss logging implementation. Round 2 restores validation but loses OpenPDF's reflected `Jpeg(Image)` constructor. The laboratory rules preserve the relevant validation/logging/EL and PDF packages. **Round 3 passes** all three parent reports, native sale expression access, empty/long/80-item sale cases, explicit eight-page pagination, offline Sicoob barcode rendering and two-page PDF merging. Evidence: `small-round3-runtime.log` and `small-round3-boleto.log`. Full jar: 265,368,470 bytes; reduced round-3 jar: 197,657,115 bytes. This validates those paths, not every ERP function.
4. NetBeans logs still warn that the legacy multi-view description is not serializable. Opening/compilation passes; restoration of editor tabs across restarts is not approved by this run. The IDE Janitor also attempted to clean older laboratory profiles. Keep laboratory evidence outside disposable IDE profiles.

## Protection and artifact

Final SHA-256 check: all 538 protected report/POM files remain identical. Seven unrelated ERP source/form/test files differ from the earlier 3,102-file baseline during concurrent work. This task performed no ERP writes and did not revert those changes. Evidence: `runtime-candidate-02/protected-integrity-final.json`.

Final plugin artifact is staged at `laboratorio/artifacts-production-candidate/ireport-designer-6.0-SNAPSHOT.nbm`, with SHA-256 beside it. It has not been installed in the user's daily ERP NetBeans profile.

## Release gates

- Review and apply the laboratory layout proposal before releasing the changed report, and verify physical printing.
- Review and apply the validated assembly, dependency and shrinking proposals, then run application acceptance with real data providers. The isolated source build is documented in SOURCE-BUILD-VALIDATION.md; the original ERP delivery is not updated by these tests.
- Verify the actual production signing certificate and complete signing pipeline.
- Print representative sales on the affected user's printer, confirming paper length, margins, page footer and cutter behavior. This task does not have that printer or its driver configuration.
- Verify restart/tab restoration if it is required in the designer workflow.

Rollback: preserve the old plugin backup and old report/ERP delivery; never replace them with unsigned laboratory candidates. The plugin backup is in `laboratorio/backup-plugin-5.5.0`.

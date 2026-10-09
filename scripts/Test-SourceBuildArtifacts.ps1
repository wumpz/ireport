param([ValidateSet('full','full-signed','small','small-signed')][string]$Variant = 'full')
$ErrorActionPreference = 'Stop'
$labRoot = Join-Path (Split-Path (Split-Path $PSScriptRoot)) 'laboratorio/erp-build-01'
$java = 'C:/Program Files/Java/jdk-17/bin/java.exe'
$javac = 'C:/Program Files/Java/jdk-17/bin/javac.exe'
$artifactName = switch ($Variant) {
    'full' { 'AtheneSistema.jar' }
    'full-signed' { 'AtheneSistema-signed-test.jar' }
    'small' { 'AtheneSistema-small.jar' }
    'small-signed' { 'AtheneSistema-small-signed-test.jar' }
}
$artifact = Join-Path $labRoot "target/$artifactName"
$classes = Join-Path $labRoot 'harness-classes'
New-Item -ItemType Directory -Force $classes | Out-Null
$fixtures = Join-Path (Split-Path $PSScriptRoot) 'test-fixtures/engine'
if ($Variant -eq 'full') {
    & $javac -encoding UTF-8 -cp $artifact -d $classes "$fixtures/FinalJarReportSmoke.java" "$fixtures/BoletoRuntimeSmoke.java" "$fixtures/PackagedReportsAudit.java" "$fixtures/BarcodeComponentsSmoke.java" "$fixtures/CompileCorpusAudit.java"
    if ($LASTEXITCODE -ne 0) { throw 'Harness compilation failed' }
}
$classpath = "$classes;$artifact"
function Invoke-Check([string]$Name, [string[]]$Arguments, [string]$WorkingDirectory) {
    Push-Location $WorkingDirectory
    try {
        & $java '-Djava.awt.headless=true' -cp $classpath @Arguments *> (Join-Path $labRoot "$Variant-$Name.log")
        $resultCode = $LASTEXITCODE
    } finally { Pop-Location }
    Get-Content (Join-Path $labRoot "$Variant-$Name.log") -Tail 5
    if ($resultCode -ne 0) { throw "$Variant $Name failed; see log" }
}
$reports = Join-Path $labRoot 'target/classes/Reports'
Invoke-Check 'packaged-reports' @('PackagedReportsAudit', $artifact, $reports) $labRoot
Invoke-Check 'runtime' @('FinalJarReportSmoke', $reports, (Join-Path $labRoot "$Variant-pdf"), 'layout-fixed') $reports
Invoke-Check 'boleto' @('BoletoRuntimeSmoke', (Join-Path $labRoot "$Variant-boleto-pdf"), 'source-build') $labRoot
Invoke-Check 'barcodes' @('BarcodeComponentsSmoke', (Join-Path $labRoot "$Variant-barcodes")) $labRoot
$fixed = Join-Path (Split-Path $labRoot) 'sale-layout-fix-01'
Invoke-Check 'fixed-layout' @('FinalJarReportSmoke', $fixed, (Join-Path $labRoot "$Variant-fixed-pdf"), 'layout-fixed') $fixed
Write-Output "PASS source-build $Variant artifacts"

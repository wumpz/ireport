param([Parameter(Mandatory=$true)][string]$Artifact,
      [Parameter(Mandatory=$true)][string]$EvidenceDirectory,
      [string]$SourceBuild = '')
$ErrorActionPreference = 'Stop'
$fork = Split-Path $PSScriptRoot -Parent
$lab = [IO.Path]::GetFullPath((Join-Path (Split-Path $fork -Parent) 'laboratorio'))
$artifactPath = [IO.Path]::GetFullPath($Artifact)
$evidence = [IO.Path]::GetFullPath($EvidenceDirectory)
foreach ($path in @($artifactPath, $evidence)) {
    if (!$path.StartsWith($lab + [IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) { throw 'Use laboratory paths only' }
}
if (Test-Path -LiteralPath $evidence) { throw 'Use a fresh evidence directory' }
if (!(Test-Path -LiteralPath $artifactPath -PathType Leaf)) { throw 'Artifact missing' }
New-Item -ItemType Directory -Path "$evidence/classes" -Force | Out-Null
$java = 'C:/Program Files/Java/jdk-17/bin/java.exe'
$javac = 'C:/Program Files/Java/jdk-17/bin/javac.exe'
if (!$SourceBuild) { $SourceBuild = Join-Path $lab 'erp-build-01' }
$SourceBuild = [IO.Path]::GetFullPath($SourceBuild)
if (!$SourceBuild.StartsWith($lab + [IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) { throw 'Source build must stay within laboratory' }
$baseline = Join-Path $SourceBuild 'target/AtheneSistema.jar'
$fixtures = Join-Path $fork 'test-fixtures/engine'
$names = @('FinalJarReportSmoke','BoletoRuntimeSmoke','PackagedReportsAudit','BarcodeComponentsSmoke','FailureRecoverySmoke')
$sources = @($names | ForEach-Object { Join-Path $fixtures "$_.java" })
& $javac -encoding UTF-8 -cp $baseline -d "$evidence/classes" @sources
if ($LASTEXITCODE -ne 0) { throw 'Harness compilation failed' }
$classpath = "$evidence/classes;$artifactPath"
function Invoke-Check([string]$Name, [string[]]$Arguments, [string]$WorkingDirectory) {
    Push-Location $WorkingDirectory
    try {
        & $java '-Djava.awt.headless=true' -cp $classpath @Arguments *> "$evidence/$Name.log"
        $code = $LASTEXITCODE
    } finally { Pop-Location }
    Get-Content "$evidence/$Name.log" -Tail 5
    if ($code -ne 0) { throw "$Name failed; see evidence log" }
}
$reports = Join-Path $SourceBuild 'target/classes/Reports'
Invoke-Check 'packaged-reports' @('PackagedReportsAudit', $artifactPath, $reports) $evidence
Invoke-Check 'runtime' @('FinalJarReportSmoke', $reports, "$evidence/pdf", 'layout-fixed') $reports
Invoke-Check 'boleto' @('BoletoRuntimeSmoke', "$evidence/boleto-pdf", 'source-build') $evidence
Invoke-Check 'barcodes' @('BarcodeComponentsSmoke', "$evidence/barcodes") $evidence
$fixed = Join-Path $lab 'sale-layout-fix-01'
Invoke-Check 'fixed-layout' @('FinalJarReportSmoke', $fixed, "$evidence/fixed-pdf", 'layout-fixed') $fixed
New-Item -ItemType Directory -Path "$evidence/negative" | Out-Null
Set-Content -LiteralPath "$evidence/negative/invalid.jrxml" -Value '<jasperReport><broken></jasperReport>' -Encoding utf8
Copy-Item -LiteralPath "$fork/test-fixtures/reports/barcode-components.jrxml" -Destination "$evidence/negative/barcode-components.jrxml"
Invoke-Check 'failure-recovery' @('FailureRecoverySmoke', "$evidence/negative") $evidence
Write-Output 'PASS experimental artifact: six runtime groups'

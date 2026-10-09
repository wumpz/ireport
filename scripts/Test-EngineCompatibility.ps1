param([Parameter(Mandatory=$true)][string]$JavaHome, [switch]$ErpGroovy, [switch]$SaveGroovy)
$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path $PSScriptRoot -Parent
$moduleRoot = Join-Path $taskRoot 'ireport-designer'
[xml]$testReport = Get-Content (Join-Path $moduleRoot 'target/surefire-reports/TEST-com.jaspersoft.ireport.designer.ModernReportCompatibilityTest.xml') -Raw
$classpath = ($testReport.testsuite.properties.property | Where-Object name -eq 'java.class.path').value
$classpath = (($classpath -split ';') | Where-Object { $_ -notmatch '[\\/]org[\\/]netbeans[\\/]' -and $_ -notmatch '[\\/]target[\\/](test-)?classes$' }) -join ';'
if ($ErpGroovy) {
    $classpath = (($classpath -split ';') | Where-Object { $_ -notmatch '[\\/]org[\\/]apache[\\/]groovy[\\/]' }) -join ';'
    $erpGroovyJar = Join-Path $env:USERPROFILE '.m2/repository/org/codehaus/groovy/groovy/3.0.20/groovy-3.0.20.jar'
    if (!(Test-Path -LiteralPath $erpGroovyJar)) { throw "Dependency missing: $erpGroovyJar" }
    $classpath += ";$erpGroovyJar"
}
$output = Join-Path $moduleRoot 'target/engine-smoke'
New-Item -ItemType Directory -Path $output -Force | Out-Null
& (Join-Path $JavaHome 'bin/javac.exe') --release 17 -proc:none -cp $classpath -d $output (Join-Path $taskRoot 'test-fixtures/engine/EngineCompatibilitySmoke.java')
if ($LASTEXITCODE -ne 0) { throw 'Engine smoke compilation failed' }
$javaOptions = @('-Dgroovy.target.bytecode=17', '-Dgroovy.target.indy=false')
if ($SaveGroovy) { $javaOptions += '-Direport.engine.saveGroovy=true' }
& (Join-Path $JavaHome 'bin/java.exe') @javaOptions -cp "$output;$classpath" EngineCompatibilitySmoke (Join-Path $moduleRoot 'target/compatibility-results')
if ($LASTEXITCODE -ne 0) { throw 'Engine compatibility failed' }

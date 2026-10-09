param(
    [Parameter(Mandatory=$true)][string]$JavaHome,
    [string]$NetBeansVersion = 'RELEASE310',
    [switch]$WindowsTrust
)
$ErrorActionPreference = 'Stop'
$repoRoot = Split-Path $PSScriptRoot -Parent
$oldJavaHome = $env:JAVA_HOME
$oldMavenOpts = $env:MAVEN_OPTS
Push-Location $repoRoot
try {
    $env:JAVA_HOME = $JavaHome
    if ($WindowsTrust) {
        $env:MAVEN_OPTS = "$oldMavenOpts -Djavax.net.ssl.trustStoreType=Windows-ROOT -Djavax.net.ssl.trustStore=NONE"
    }
    & mvn -B -ntp -s build-settings.xml -pl ireport-designer -am "-Dnetbeans.version=$NetBeansVersion" package
    if ($LASTEXITCODE -ne 0) { throw 'Designer build failed; inspect Maven diagnostics.' }
} finally {
    Pop-Location
    $env:JAVA_HOME = $oldJavaHome
    $env:MAVEN_OPTS = $oldMavenOpts
}

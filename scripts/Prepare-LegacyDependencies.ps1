param(
    [Parameter(Mandatory=$true)][string]$JavaHome,
    [switch]$WindowsTrust
)
$ErrorActionPreference = 'Stop'
$repoRoot = Split-Path $PSScriptRoot -Parent
$archivePath = Join-Path $repoRoot 'libraries.zip'
$extractPath = Join-Path $repoRoot '.lab/upstream-libraries'
$oldJavaHome = $env:JAVA_HOME
$oldMavenOpts = $env:MAVEN_OPTS
try {
    $env:JAVA_HOME = $JavaHome
    if ($WindowsTrust) {
        $env:MAVEN_OPTS = "$oldMavenOpts -Djavax.net.ssl.trustStoreType=Windows-ROOT -Djavax.net.ssl.trustStore=NONE"
    }
    Expand-Archive -LiteralPath $archivePath -DestinationPath $extractPath -Force
    $artifacts = @(
        @('net.sf.jasperreports','jasperreports-chart-themes','5.6.0-SNAPSHOT','jasperreports-chart-themes-5.6.0.jar'),
        @('net.sf.jasperreports','jasperreports-extensions','3.5.3','jasperreports-extensions-3.5.3.jar'),
        @('net.sf.jasperreports','jasperreports-htmlcomponent','5.0.1','jasperreports-htmlcomponent-5.0.1.jar'),
        @('com.jaspersoft.jasperserver','jasperserver-common-ws','4.7.1','js_jasperserver-common-ws-4.7.1.jar'),
        @('com.jaspersoft.connectors.hadoop','js-hive-datasource','1.0.4','js-hive-datasource-1.0.4.jar'),
        @('mondrian','mondrian','3.2.0-13661-JS-3','mondrian-3.2.0-13661-JS.jar'),
        @('rex','rex','0.8.1','rex-0.8.1.jar'),
        @('nickyb','sqleonardo','2009.03.rc1','sqleonardo-2009.03.rc1.jar')
    )
    # Use a minimal POM so preparation does not resolve the entire reactor.
    $bootstrapPom = Join-Path $extractPath 'bootstrap-pom.xml'
    '<project xmlns="http://maven.apache.org/POM/4.0.0"><modelVersion>4.0.0</modelVersion><groupId>local.bootstrap</groupId><artifactId>legacy-libraries</artifactId><version>1</version></project>' |
        Set-Content -LiteralPath $bootstrapPom -Encoding utf8
    $manifest = foreach ($artifact in $artifacts) {
        $filePath = Join-Path $extractPath "libraries/$($artifact[3])"
        & mvn -B -ntp -f $bootstrapPom org.apache.maven.plugins:maven-install-plugin:3.1.2:install-file "-Dfile=$filePath" "-DgroupId=$($artifact[0])" "-DartifactId=$($artifact[1])" "-Dversion=$($artifact[2])" '-Dpackaging=jar' '-DgeneratePom=true' | Out-Host
        if ($LASTEXITCODE -ne 0) { throw "Failed to prepare $($artifact[1])" }
        [pscustomobject]@{ Coordinates="$($artifact[0]):$($artifact[1]):$($artifact[2])"; SHA256=(Get-FileHash -LiteralPath $filePath -Algorithm SHA256).Hash }
    }
    $manifest | Export-Csv -LiteralPath (Join-Path $extractPath 'manifest.csv') -NoTypeInformation -Encoding utf8
} finally {
    $env:JAVA_HOME = $oldJavaHome
    $env:MAVEN_OPTS = $oldMavenOpts
}

param([Parameter(Mandatory=$true)][string]$Destination, [string[]]$AnalysisLibraries = @(), [string]$SourceBuild = '')
$ErrorActionPreference = 'Stop'
$fork = Split-Path $PSScriptRoot -Parent
$lab = [IO.Path]::GetFullPath((Join-Path (Split-Path $fork -Parent) 'laboratorio'))
$destinationPath = [IO.Path]::GetFullPath($Destination)
if (!$destinationPath.StartsWith($lab + [IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) { throw 'Experiment must stay within the laboratory' }
if (Test-Path -LiteralPath $destinationPath) { throw 'Use a fresh experiment directory' }
foreach ($library in $AnalysisLibraries) {
    if (!(Test-Path -LiteralPath $library -PathType Leaf)) { throw "Missing analysis library: $library" }
}
if (!$SourceBuild) { $SourceBuild = Join-Path $lab 'erp-build-01' }
$source = [IO.Path]::GetFullPath($SourceBuild)
if (!$source.StartsWith($lab + [IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) { throw 'Source build must stay within laboratory' }
New-Item -ItemType Directory -Path "$destinationPath/target" -Force | Out-Null
Copy-Item -LiteralPath "$source/target/AtheneSistema.jar" -Destination "$destinationPath/target/AtheneSistema.jar"
$rules = Get-Content -LiteralPath "$source/proguard-rules.pro" -Raw
$rules = $rules -replace '(?m)^-dontobfuscate\s*$','' -replace '(?m)^-dontoptimize\s*$',''
$rules += "`n-useuniqueclassmembernames`n-dontusemixedcaseclassnames`n-optimizationpasses 1`n-printmapping $($destinationPath.Replace('\','/'))/mapping.txt`n"
# Some old fat-JAR dependencies bundle JAXP API classes also provided by java.xml.
# JVM parent-first loading uses the platform definitions; their names must match.
$rules += "`n-keep class javax.xml.** { *; }`n-keep class org.w3c.dom.** { *; }`n-keep class org.xml.sax.** { *; }`n"
foreach ($library in $AnalysisLibraries) {
    $rules += "`n-libraryjars '$([IO.Path]::GetFullPath($library).Replace('\','/'))'`n"
}
Set-Content -LiteralPath "$destinationPath/proguard-rules.pro" -Value $rules -Encoding utf8
[xml]$pom = Get-Content -LiteralPath "$source/pom.xml" -Raw
$ns = [System.Xml.XmlNamespaceManager]::new($pom.NameTable)
$ns.AddNamespace('m','http://maven.apache.org/POM/4.0.0')
$plugins = $pom.SelectSingleNode('/m:project/m:build/m:plugins',$ns)
foreach ($child in @($plugins.ChildNodes)) {
    if ($child.SelectSingleNode('m:artifactId',$ns).InnerText -ne 'proguard-maven-plugin') { [void]$plugins.RemoveChild($child) }
}
foreach ($name in @('dependencies','repositories','pluginRepositories')) {
    $node = $pom.SelectSingleNode("/m:project/m:$name",$ns)
    if ($node) { [void]$pom.DocumentElement.RemoveChild($node) }
}
$pom.Save("$destinationPath/pom.xml")
Write-Output 'Prepared only; run the ProGuard Maven goal in the experiment directory.'
Write-Output 'No application startup, production signing, or baseline artifact replacement.'

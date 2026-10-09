param(
    [Parameter(Mandatory=$true)][string]$Name,
    [Parameter(Mandatory=$true)][string]$NetBeansHome,
    [Parameter(Mandatory=$true)][string]$JavaHome,
    [string]$DiagnosticFile
)
$ErrorActionPreference = 'Stop'
if ($Name -notmatch '^[a-z0-9-]+$') { throw 'Invalid laboratory profile name' }
$taskRoot = Split-Path $PSScriptRoot -Parent
$labRoot = Join-Path (Split-Path $taskRoot -Parent) 'laboratorio'
$profile = Join-Path $labRoot "userdir-$Name"
if (!(Test-Path -LiteralPath (Join-Path $profile 'config/Modules/com-jaspersoft-ireport.xml'))) { throw 'Stage a fresh profile with New-LabProfile.ps1 first' }
$arguments = @('--userdir', "`"$profile`"", '--cachedir', "`"$(Join-Path $labRoot "cache-$Name")`"", '--jdkhome', "`"$JavaHome`"", '--nosplash', '-J-Djavax.net.ssl.trustStoreType=Windows-ROOT', '-J-Djavax.net.ssl.trustStore=NONE')
if ($DiagnosticFile) {
    $resolvedDiagnostic = (Resolve-Path -LiteralPath $DiagnosticFile).Path
    $allowedRoot = [IO.Path]::GetFullPath((Split-Path $taskRoot -Parent)) + [IO.Path]::DirectorySeparatorChar
    if (!$resolvedDiagnostic.StartsWith($allowedRoot, [StringComparison]::OrdinalIgnoreCase)) { throw 'Diagnostics may open only laboratory files' }
    $arguments += "`"-J-Direport.diagnostics.file=$resolvedDiagnostic`""
}
Start-Process -FilePath (Join-Path $NetBeansHome 'bin/netbeans64.exe') -ArgumentList $arguments -WindowStyle Hidden -PassThru

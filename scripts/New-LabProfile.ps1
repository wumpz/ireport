param([Parameter(Mandatory=$true)][string]$Name)
$ErrorActionPreference = 'Stop'
if ($Name -notmatch '^[a-z0-9-]+$') { throw 'Use a simple lowercase laboratory profile name' }
$taskRoot = Split-Path $PSScriptRoot -Parent
$labRoot = Join-Path (Split-Path $taskRoot -Parent) 'laboratorio'
$profile = Join-Path $labRoot "userdir-$Name"
if (Test-Path -LiteralPath $profile) { throw "Profile already exists: $profile" }
$nbm = Join-Path $taskRoot 'ireport-designer/target/ireport-designer-6.0-SNAPSHOT.nbm'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$archive = [IO.Compression.ZipFile]::OpenRead($nbm)
try {
    foreach ($entry in $archive.Entries) {
        if (!$entry.FullName.StartsWith('netbeans/')) { continue }
        $relative = $entry.FullName.Substring(9)
        if (!$relative -or $relative.EndsWith('/')) { continue }
        $destination = [IO.Path]::GetFullPath((Join-Path $profile $relative))
        if (!$destination.StartsWith([IO.Path]::GetFullPath($profile) + [IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) { throw 'Invalid NBM entry path' }
        New-Item -ItemType Directory -Path (Split-Path $destination -Parent) -Force | Out-Null
        [IO.Compression.ZipFileExtensions]::ExtractToFile($entry, $destination, $false)
    }
} finally { $archive.Dispose() }
New-Item -ItemType Directory -Path (Join-Path $profile 'var') -Force | Out-Null
New-Item -ItemType File -Path (Join-Path $profile 'var/imported') | Out-Null
Write-Output $profile
Write-Output 'Staged only. Launch NetBeans with --userdir and --cachedir under this laboratory.'

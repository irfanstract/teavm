param(
    [string]$GradleVersion = '8.5'
)

$destDir = Join-Path (Get-Location) 'gradle\wrapper'
if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir -Force | Out-Null }

# Try to download gradle-wrapper.jar from Gradle's GitHub source for the matching tag
$wrapperUrl = "https://raw.githubusercontent.com/gradle/gradle/v$GradleVersion/gradle-wrapper/src/main/resources/gradle-wrapper.jar"
Write-Host "Attempting to download gradle-wrapper.jar from $wrapperUrl"
try {
    Invoke-WebRequest -Uri $wrapperUrl -OutFile (Join-Path $destDir 'gradle-wrapper.jar') -UseBasicParsing -ErrorAction Stop
    Write-Host "Downloaded gradle-wrapper.jar to $destDir"
    return
} catch {
    Write-Host "Could not download gradle-wrapper.jar from GitHub; falling back to extracting distribution."
}

$dist = "https://services.gradle.org/distributions/gradle-$GradleVersion-bin.zip"
$tempZip = Join-Path $env:TEMP "gradle-$GradleVersion.zip"
$extractDir = Join-Path $env:TEMP "gradle-$GradleVersion-extract"

Write-Host "Downloading Gradle $GradleVersion from $dist..."
Invoke-WebRequest -Uri $dist -OutFile $tempZip -UseBasicParsing

Write-Host "Extracting wrapper JAR..."
if (Test-Path $extractDir) { Remove-Item $extractDir -Recurse -Force }
Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::ExtractToDirectory($tempZip, $extractDir)

# Try known locations inside the distribution for a wrapper jar or shared wrapper
$candidates = @()
$candidates += Join-Path $extractDir "gradle-$GradleVersion\lib\gradle-wrapper.jar"
$candidates += Join-Path $extractDir "gradle-$GradleVersion\lib\gradle-wrapper-shared-$GradleVersion.jar"
$candidates += "$extractDir\gradle-$GradleVersion\lib\gradle-wrapper-shared-*.jar"

$found = $false
foreach ($candidate in $candidates) {
    $matchlist = Get-ChildItem -Path $candidate -ErrorAction SilentlyContinue
    if ($matchlist) {
        Copy-Item -Path $matchlist[0].FullName -Destination (Join-Path $destDir 'gradle-wrapper.jar') -Force
        Write-Host "Copied $($matchlist[0].Name) to gradle-wrapper.jar"
        $found = $true
        break
    }
}

if (-not $found) {
    Write-Host "Warning: could not find a wrapper JAR inside the distribution. The wrapper may still work if you have Gradle installed system-wide."
}

Write-Host "Cleanup..."
# Remove-Item $tempZip -Force
# Remove-Item $extractDir -Recurse -Force

Write-Host "gradle-wrapper.jar installation attempt finished. Check $destDir"

param(
    [string]$GradleVersion = '8.5',
    [string[]]$Args = @(':core:build'),
    [string]$JavaHome = ''
)

$distUrl = "https://services.gradle.org/distributions/gradle-$GradleVersion-bin.zip"
$destRoot = Join-Path (Get-Location) '.gradle-dist'
$destDir = Join-Path $destRoot "gradle-$GradleVersion"

if (-not (Test-Path $destDir)) {
    Write-Host "Downloading Gradle $GradleVersion to $destDir..."
    $tempZip = Join-Path $env:TEMP "gradle-$GradleVersion.zip"
    Invoke-WebRequest -Uri $distUrl -OutFile $tempZip -UseBasicParsing
    Add-Type -AssemblyName System.IO.Compression.FileSystem
    [System.IO.Compression.ZipFile]::ExtractToDirectory($tempZip, $destRoot)
    # Remove-Item $tempZip -Force
}

$gradleBat = Join-Path $destDir 'bin\gradle.bat'
if (-not (Test-Path $gradleBat)) { throw "gradle.bat not found in $destDir" }

if (-not $JavaHome) {
    # If system Java is too new (class file major > 65) Gradle may fail. Provide a JDK 17 fallback by downloading Temurin 17.
    $javaVersion = & java -XshowSettings:properties -version 2>&1 | Out-String
    if ($LASTEXITCODE -ne 0 -or $javaVersion -match 'openjdk version') {
        # leave JavaHome empty to use system Java if available
    }
}

if (-not $JavaHome) {
    $jdkDir = Join-Path (Get-Location) '.jdks\temurin-17'
    Write-Host "DEBUG JdkDir=$jdkDir"
    if (-not (Test-Path $jdkDir)) {
        Write-Host "Downloading Temurin JDK 17..."
        $jdkZip = Join-Path $env:TEMP 'temurin-17.zip'
        $jdkUrl = 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.8+7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.8_7.zip'
        Invoke-WebRequest -Uri $jdkUrl -OutFile $jdkZip -UseBasicParsing
        Add-Type -AssemblyName System.IO.Compression.FileSystem
        $extractTarget = (Join-Path (Get-Location) '.jdks')
        if (-not (Test-Path $extractTarget)) { New-Item -ItemType Directory -Path $extractTarget | Out-Null }
        [System.IO.Compression.ZipFile]::ExtractToDirectory($jdkZip, $extractTarget)
        # Remove-Item $jdkZip -Force
    }
    $JavaHome = (Get-ChildItem -Path (Join-Path (Get-Location) '.jdks') -Directory | Where-Object { $_.Name -like 'jdk*' -or $_.Name -like 'temurin*' } | Select-Object -First 1).FullName
}

$argString = $Args -join ' '
if ($JavaHome) {
    Write-Host "Running $gradleBat $argString with JAVA_HOME=$JavaHome"
    $env:JAVA_HOME = $JavaHome
    & $gradleBat "--no-daemon" "--console=plain" $Args
} else {
    Write-Host "Running $gradleBat $argString with system Java"
    & $gradleBat $Args
}

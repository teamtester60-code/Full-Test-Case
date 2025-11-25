# إعدادات متغيره
$backendDeployPath = "D:\Repos\Publish\POS\18-08-2025_POS\18-08-2025_POSBack"
$frontendDeployPath = "D:\Repos\Publish\POS\18-08-2025_POS\18-08-2025_POSFront"

# إعدادات ثابته
$downloadUrl = "https://github.com/Abdalla-Tarek/Release/releases/download/v1.0.0/POS.rar"
$deployRoot = "C:\Deploy"
$localRar    = Join-Path $deployRoot "ReleasePackage.rar"
$tempPath    = Join-Path $deployRoot "Temp"
$backupRoot  = "C:\Deploy\Backup"
$timestamp   = Get-Date -Format "yyyy_MM_dd_HHmmss"
$backupPath  = Join-Path $backupRoot $timestamp
$winrarExe = "C:\Program Files\WinRAR\WinRAR.exe"
$frontPort = "4200"
# أسماء الفولدرات جوه الأرشيف
$backendFolderName = "POS\POSBack"
$frontendFolderName = "POS\POSFront"

# تأكد إن C:\Deploy موجود ولو مش موجود اعمله
if (-not (Test-Path $deployRoot)) {
    New-Item -ItemType Directory -Path $deployRoot | Out-Null
    Write-Host "Created folder: $deployRoot"
}

# تحميل آخر نسخة
Write-Host "Downloading latest release..."
Invoke-WebRequest -Uri $downloadUrl -OutFile $localRar -UseBasicParsing
Write-Host "Downloading Done..."

if ((Get-Item $localRar).Length -lt 100kb) {
    Write-Host "Download failed. Probably got an HTML page instead of RAR."
    Read-Host "Press ENTER to exit"
    exit 1
}

if (-Not (Test-Path $localRar)) {
    Write-Host "Error: Download failed, file not found at $localRar"
    Read-Host "Press ENTER to exit"
    exit 1
}
Write-Host "File downloaded successfully: $localRar"

# فك الضغط
Write-Host "Extracting with WinRAR..."
if (Test-Path $tempPath) { Remove-Item $tempPath -Recurse -Force }
New-Item -ItemType Directory -Path $tempPath | Out-Null

Start-Process -FilePath $winrarExe -ArgumentList "x","-y",$localRar,$tempPath -Wait -NoNewWindow

Write-Host "Extracted content:"
Get-ChildItem $tempPath -Recurse

# تحقق من وجود الفولدرات بعد الفك
if (-Not (Test-Path "$tempPath\$backendFolderName")) {
    Write-Host "Error: Backend folder not found in extracted files!"
    Read-Host "Press ENTER to exit"
    exit 1
}
if (-Not (Test-Path "$tempPath\$frontendFolderName")) {
    Write-Host "Error: Frontend folder not found in extracted files!"
    Read-Host "Press ENTER to exit"
    exit 1
}

# تجهيز فولدر Backup
# Write-Host "Creating backup at $backupPath..."
# New-Item -ItemType Directory -Path $backupPath | Out-Null

# Import-Module WebAdministration

# # --- Backup ---
# if (Test-Path $backendDeployPath) {
#     Copy-Item "$backendDeployPath" "$backupPath\" -Recurse -Force
# }
# if (Test-Path "$frontendDeployPath") {
#     Copy-Item "$frontendDeployPath" "$backupPath\" -Recurse -Force
# }
# Import-Module WebAdministration

# --- Stop IIS if running ---
iisreset /stop
Write-Host "IIS stopped..."


# --- Backend ---
Write-Host "Cleaning old backend files (except appsettings.json , StaticFiles,license.rar)..."
Get-ChildItem $backendDeployPath -Recurse | ForEach-Object {
    if ($_.FullName -notlike "*appsettings.json" -and $_.FullName -notlike "*license.rar" -and $_.FullName -notlike "*StaticFiles*") {
        Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue
    }
}
Write-Host "sleep 8 seconds..."
Start-Sleep -Seconds 8


Write-Host "Copying new backend files..."
Get-ChildItem "$tempPath\$backendFolderName" -Recurse | ForEach-Object {
    $relativePath = $_.FullName.Substring(("$tempPath\$backendFolderName").Length).TrimStart('\')
    $dest = Join-Path $backendDeployPath $relativePath
    $destDir = Split-Path $dest -Parent

    if ($_.PSIsContainer) {
        if ($_.Name -ne "StaticFiles") {
            if (-not (Test-Path $dest)) { New-Item -ItemType Directory -Path $dest | Out-Null }
        }
    } else {
        if ($_.Name -eq "appsettings.json") {
            if (Test-Path $dest) {
                Write-Host "Skipped existing appsettings.json"
            } else {
                if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir -Force | Out-Null }
                Copy-Item $_.FullName $dest -Force
                Write-Host "Added missing appsettings.json"
            }
        }
        elseif ($_.FullName -like "*\StaticFiles\*") {
            if (Test-Path $dest) {
                Write-Host "Skipped existing file in StaticFiles: $relativePath"
            } else {
                if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir -Force | Out-Null }
                Copy-Item $_.FullName $dest -Force
                Write-Host "Added missing file in StaticFiles: $relativePath"
            }
        }
        elseif ($_.Name -eq "license.rar") {
            if (Test-Path $dest) {
                Write-Host "Skipped existing file in license.rar"
            }
        }
        else {
            if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir -Force | Out-Null }
            Copy-Item $_.FullName $dest -Force
        }
    }
}

# --- Frontend ---

Write-Host "Cleaning old frontend files (except assets\Config)..."
# امسح كل الملفات اللي مش جوه assets\Config
Get-ChildItem $frontendDeployPath -Force | ForEach-Object {
    if ($_.Name -ieq "assets" -and $_.PSIsContainer) {
        # جوه assets → امسح كل حاجة ما عدا Config
        Get-ChildItem $_.FullName -Force | ForEach-Object {
            if ($_.Name -ieq "Config" -and $_.PSIsContainer) {
                Write-Host "Keeping folder: $($_.FullName)"
            } else {
                Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue
            }
        }
    }
    elseif ($_.Name -ieq "Config" -and $_.PSIsContainer -and ($_.Parent -like "*assets")) {
        # حماية مضاعفة لو Config ظهر لوحده
        Write-Host "Keeping folder: $($_.FullName)"
    }
    else {
        # أي حاجة تانية في Root تتشال
        Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue
    }
}
Write-Host "sleep 3 seconds..."
Start-Sleep -Seconds 3

Write-Host "Copying new frontend files..."
Get-ChildItem "$tempPath\$frontendFolderName" -Recurse | ForEach-Object {
    $relativePath = $_.FullName.Substring(("$tempPath\$frontendFolderName").Length).TrimStart('\')
    $dest = Join-Path $frontendDeployPath $relativePath
    $destDir = Split-Path $dest -Parent

    if ($_.PSIsContainer) {
        if ($_.FullName -notlike "*\assets\Config*") {
            if (-not (Test-Path $dest)) { New-Item -ItemType Directory -Path $dest | Out-Null }
        }
    } else {
        if ($_.FullName -like "*\assets\Config\*") {
            if (Test-Path $dest) {
                Write-Host "Skipped existing file in assets\Config: $relativePath"
            } else {
                if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir -Force | Out-Null }
                Copy-Item $_.FullName $dest -Force
                Write-Host "Added missing file in assets\Config: $relativePath"
            }
        }
        else {
            if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir -Force | Out-Null }
            Copy-Item $_.FullName $dest -Force
        }
    }
}
# --- start IIS if running ---
iisreset /start
Write-Host "IIS started..."

Write-Host "Deployment complete!"

# افتح المتصفح الافتراضي على localhost:$frontPort
Start-Process "http://localhost:56740/api/Screen/PostScreen"
Start-Sleep -Seconds 15
$cacheBuster = Get-Random
Start-Process "http://localhost:$frontPort/?v=$cacheBuster"
# Read-Host "Press ENTER to exit"

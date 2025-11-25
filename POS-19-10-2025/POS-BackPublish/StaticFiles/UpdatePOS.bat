@echo off
setlocal

:: احصل على مسار الملف الحالي (مكان وجود الباتش)
set "currentDir=%~dp0"

:: شغل PowerShell script من نفس المسار
powershell -ExecutionPolicy Bypass -File "%currentDir%UpdatePOS.ps1"

:: وقف الشاشة لحد ما المستخدم يدوس أي زرار
pause

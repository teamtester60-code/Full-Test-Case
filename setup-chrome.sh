#!/bin/bash
set -e

# تحديث الحزم الأساسية وتثبيت الأدوات
apt-get update && apt-get install -y wget unzip curl xvfb google-chrome-stable

# الحصول على نسخة Chrome الحالية
CHROME_VERSION=$(google-chrome --version | grep -oP '\d+\.\d+')
echo "Chrome version: $CHROME_VERSION"

# تحميل ChromeDriver المطابق
LATEST_DRIVER=$(curl -s "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$CHROME_VERSION")
echo "ChromeDriver version: $LATEST_DRIVER"

wget -q "https://chromedriver.storage.googleapis.com/$LATEST_DRIVER/chromedriver_linux64.zip" -O chromedriver.zip
unzip chromedriver.zip -d /usr/local/bin/
chmod +x /usr/local/bin/chromedriver

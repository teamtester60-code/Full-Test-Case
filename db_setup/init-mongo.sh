#!/bin/bash

# اسم قاعدة البيانات الذي يتوقعه التطبيق
DB_NAME="pos_db"

echo "Starting data restoration for $DB_NAME..."

# 🛑 هذا هو التعديل الضروري. المسار داخل الحاوية يعكس المسار النسبي الذي دفعته
# /docker-entrypoint-initdb.d/dump/pos_db
mongorestore --host localhost --port 27017 --drop /docker-entrypoint-initdb.d/dump/pos_db

echo "Data restoration complete."
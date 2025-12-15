#!/bin/bash

# 🛑 التأكد من أن اسم مجلد النسخة الاحتياطية هو "pos_db"
DB_NAME="pos_db"

echo "Starting data restoration for $DB_NAME..."

# يستخدم --dir /docker-entrypoint-initdb.d/dump/
# ويفترض أن اسم المجلد الفرعي داخله هو pos_db
mongorestore --host localhost --port 27017 --drop /docker-entrypoint-initdb.d/dump/

echo "Data restoration complete."
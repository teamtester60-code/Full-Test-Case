package com.ferpfirstcode.utils.dataReader;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


public class DataBaseReader {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    // الاتصال بقاعدة البيانات
    public static void connect() {
        String connectionString = "mongodb://localhost:27017"; // غيره حسب رابط سيرفرك
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase("Quiz"); // اسم قاعدة البيانات
    }

    // قراءة وثيقة (Document) واحدة بناءً على مفتاح وقيمة (مثل رقم الطلب)
    public static Document getDocumentByField(String collectionName, String fieldName, String value) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return (Document) collection.find(Filters.eq(fieldName, value)).first();
    }
    public static String getPinByUsername(String collectionName, String username) {
    MongoCollection<Document> collection = database.getCollection(collectionName);
    
    // البحث عن الوثيقة التي تطابق اسم المستخدم
    Document userDoc = (Document) collection.find(Filters.eq("username", username)).first();
    
    if (userDoc != null) {
        return ((Document) userDoc).getString("Pin"); // نفترض أن الحقل في MongoDB اسمه "pin"
    }
    return null;
}


public static String getAdminPin() {
    if (database == null) connect();
    // الوصول إلى مجموعة applicationUsers
    MongoCollection<Document> collection = database.getCollection("applicationUsers");
    
    // البحث عن المستخدم الذي اسمه "Admin"
    Document userDoc = (Document) collection.find(Filters.eq("UserName", "admin")).first();
    
    if (userDoc != null && ((Document) userDoc).containsKey("Pin")) {
        // جلب قيمة الـ Pin (تأكد من مطابقة حالة الأحرف كما في الصورة)
        return ((Document) userDoc).getString("Pin"); 
    }
    return null;
}

    // إغلاق الاتصال
    public static void close() {
        if (mongoClient != null) mongoClient.close();
    }

}

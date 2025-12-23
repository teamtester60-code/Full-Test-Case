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

    public static void connect() {
        if (mongoClient != null) {
            return;
        }
        String connectionString = PropertyReader.getProperty("mongo.uri");
        if (connectionString.isBlank()) {
            connectionString = "mongodb://localhost:27017";
        }
        String dbName = PropertyReader.getProperty("mongo.db");
        if (dbName.isBlank()) {
            dbName = "Quiz";
        }
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(dbName);
    }

    public static Document getDocumentByField(String collectionName, String fieldName, String value) {
        if (database == null) connect();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.find(Filters.eq(fieldName, value)).first();
    }

    public static String getPinByUsername(String collectionName, String username) {
        if (database == null) connect();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document userDoc = collection.find(Filters.eq("username", username)).first();
        if (userDoc != null) {
            return userDoc.getString("Pin");
        }
        return null;
    }

    public static String getAdminPin() {
        if (database == null) connect();
        MongoCollection<Document> collection = database.getCollection("applicationUsers");
        Document userDoc = collection.find(Filters.eq("UserName", "admin")).first();
        if (userDoc != null && userDoc.containsKey("Pin")) {
            return userDoc.getString("Pin");
        }
        return null;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }

}

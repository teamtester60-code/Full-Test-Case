package com.ferpfirstcode.utils.dataReader;

import java.util.List;

import org.bson.Document;

import com.ferpfirstcode.utils.logs.LogsManager;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


public class DataBaseReader {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static void connect() {
    if (mongoClient != null) return;

    String connectionString =
            System.getenv("MONGO_URI") != null
            ? System.getenv("MONGO_URI")
            : PropertyReader.getProperty("mongo.uri");

    if (connectionString == null || connectionString.isBlank()) {
        connectionString = "mongodb://localhost:27017";
    }

    String dbName =
            System.getenv("MONGO_DB") != null
            ? System.getenv("MONGO_DB")
            : PropertyReader.getProperty("mongo.db");

    if (dbName == null || dbName.isBlank()) {
        dbName = "Quiz";
    }

    LogsManager.info("🔌 Connecting to MongoDB URI: " + connectionString);
    LogsManager.info("📦 Using database: " + dbName);

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


    public static Double getLastPayAmountBySerialNumber() {
    if (database == null) connect();

    MongoCollection<Document> collection = database.getCollection("POSOrders");

    // آخر Order اتعمل
    Document order = collection.find()
            .sort(new Document("CreationTime", -1))
            .limit(1)
            .first();

    if (order == null) {
        throw new RuntimeException("❌ No orders found in POSOrders collection");
    }

    Number serialNumber = (Number) order.get("SerialNumber");
    long serial = serialNumber.longValue();

    List<Document> payments = order.getList("OrderPayments", Document.class);
    if (payments == null || payments.isEmpty()) {
        throw new RuntimeException("❌ OrderPayments is empty for SerialNumber = " + serial);
    }

    Document lastPayment = payments.get(payments.size() - 1);

    Object payAmountObj = lastPayment.get("PayAmount");
    if (payAmountObj == null) {
        throw new RuntimeException("❌ PayAmount is null for SerialNumber = " + serial);
    }
    
    Number payAmount = (Number) payAmountObj;
    LogsManager.info("Last Order SerialNumber = " + serial);
    LogsManager.info("Last PayAmount = " + payAmount.doubleValue());
    return payAmount.doubleValue();
}

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }

}

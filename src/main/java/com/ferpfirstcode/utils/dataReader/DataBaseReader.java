package com.ferpfirstcode.utils.dataReader;

import java.util.List;

import com.ferpfirstcode.pages.components.OrderPaymentDB;
import org.bson.Document;

import com.ferpfirstcode.utils.logs.LogsManager;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.types.Decimal128;

import static com.mongodb.client.model.Filters.eq;
import static javax.management.Query.eq;



public class DataBaseReader {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static synchronized void connect() {
        if (mongoClient != null) return;

        // 1) System properties (mvn -Dmongo.uri=...)
        String connectionString = System.getProperty("mongo.uri");

        // 2) Env vars (GitHub Actions env / secrets)
        if (connectionString == null || connectionString.isBlank()) {
            connectionString = System.getenv("MONGO_URI");
        }

        // 3) properties file fallback
        if (connectionString == null || connectionString.isBlank()) {
            connectionString = PropertyReader.getProperty("mongo.uri");
        }

        if (connectionString == null || connectionString.isBlank()) {
            connectionString = "mongodb://localhost:27017";
        }

        // DB name (same priority)
        String dbName = System.getProperty("mongo.db");

        if (dbName == null || dbName.isBlank()) {
            dbName = System.getenv("MONGO_DB");
        }

        if (dbName == null || dbName.isBlank()) {
            dbName = PropertyReader.getProperty("mongo.db");
        }

        if (dbName == null || dbName.isBlank()) {
            dbName = "Quiz";
        }

        // Don't leak credentials in logs
        LogsManager.info("🔌 Connecting to MongoDB...");
        LogsManager.info("📦 Using database: " + dbName);

        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(dbName);
    }


    public static Document getDocumentByField(String collectionName, String fieldName, String value) {
        if (database == null) connect();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.find(eq(fieldName, value)).first();
    }

    public static String getPinByUsername(String collectionName, String username) {
        if (database == null) connect();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document userDoc = collection.find(eq("username", username)).first();
        if (userDoc != null) {
            return userDoc.getString("Pin");
        }
        return null;
    }

    public static String getAdminPin() {
        if (database == null) connect();
        MongoCollection<Document> collection = database.getCollection("applicationUsers");
        Document userDoc = collection.find(eq("UserName", "admin")).first();
        if (userDoc != null && userDoc.containsKey("Pin")) {
            return userDoc.getString("Pin");
        }
        return null;
    }




    public static Double getPayAmountByOrderNumber(long orderNumber) {
        try {
            MongoDatabase db = mongoClient.getDatabase("Smile");   // تأكد الاسم صحيح لنفس DB اللي التست يكتب فيها
            MongoCollection<Document> col = db.getCollection("POSOrders");

            Document doc = col.find(eq("OrderNumber", (int) orderNumber)).first();
            if (doc == null) {
                LogsManager.warn("[DB] POSOrders not found | OrderNumber=" + orderNumber);
                return 0.0;
            }

            Object paymentsObj = doc.get("OrderPayments");
            if (!(paymentsObj instanceof List<?> payments) || payments.isEmpty()) {
                LogsManager.warn("[DB] OrderPayments empty/missing | OrderNumber=" + orderNumber);
                return 0.0;
            }

            double sum = 0.0;
            for (Object p : payments) {
                if (p instanceof Document payDoc) {
                    sum += toDouble(payDoc.get("PayAmount"));
                }
            }

            return sum;

        } catch (Exception e) {
            LogsManager.error("[DB] getPayAmountByOrderNumber error: " + e.getMessage());
            return 0.0;
        }
    }

    private static double toDouble(Object v) {
        if (v == null) return 0.0;
        if (v instanceof Decimal128 d) return d.bigDecimalValue().doubleValue();
        if (v instanceof Number n) return n.doubleValue();
        String s = v.toString().trim();
        if (s.isEmpty()) return 0.0;
        s = s.replaceAll("[^0-9.\\-]", "");
        if (s.isEmpty()) return 0.0;
        return Double.parseDouble(s);
    }



    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }

}

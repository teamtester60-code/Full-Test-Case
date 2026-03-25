package com.ferpfirstcode.utils.dataReader;

import com.ferpfirstcode.pages.components.Order;
import com.ferpfirstcode.utils.logs.LogsManager;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DataBaseReader {

    private static final String DEFAULT_CONNECTION = "mongodb://localhost:27017";
    private static final String DEFAULT_DB_NAME = "Smile";
    private static final String POS_ORDERS_COLLECTION = "POSOrders";
    private static final String APPLICATION_USERS_COLLECTION = "applicationUsers";

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private DataBaseReader() {
        // utility class
    }

    public static synchronized void connect() {
        if (mongoClient != null && database != null) {
            return;
        }

        String connectionString = firstNonBlank(
                System.getProperty("mongo.uri"),
                System.getenv("MONGO_URI"),
                PropertyReader.getPropertyOrNull("mongo.uri"),
                DEFAULT_CONNECTION
        );

        String dbName = firstNonBlank(
                System.getProperty("mongo.db"),
                System.getenv("MONGO_DB"),
                PropertyReader.getPropertyOrNull("mongo.db"),
                DEFAULT_DB_NAME
        );

        LogsManager.info("🔌 Connecting to MongoDB...");
        LogsManager.info("📦 Using database: " + dbName);

        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(dbName);
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }
        return null;
    }

    private static MongoDatabase getDatabase() {
        if (database == null) {
            connect();
        }
        return database;
    }

    private static MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }

    public static Document getDocumentByField(String collectionName, String fieldName, String value) {
        return getCollection(collectionName).find(eq(fieldName, value)).first();
    }

    public static String getPinByUsername(String collectionName, String username) {
        Document userDoc = getCollection(collectionName).find(eq("username", username)).first();
        if (userDoc != null) {
            return userDoc.getString("Pin");
        }
        return null;
    }

    public static String getAdminPin() {
        Document userDoc = getCollection(APPLICATION_USERS_COLLECTION)
                .find(eq("UserName", "admin"))
                .first();

        if (userDoc != null && userDoc.containsKey("Pin")) {
            return userDoc.getString("Pin");
        }
        return null;
    }

    public static Document getOrderByOrderNumber(long orderNumber) {
        try {
            return getCollection(POS_ORDERS_COLLECTION)
                    .find(eq("OrderNumber", (int) orderNumber))
                    .first();
        } catch (Exception e) {
            LogsManager.error("[DB] getOrderByOrderNumber error: " + e.getMessage());
            return null;
        }
    }

    public static String getOrderDebugInfo(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);

            if (doc == null) {
                return "[DB] No document found for OrderNumber=" + orderNumber;
            }

            Object paymentsObj = doc.get("OrderPayments");
            return "[DB] Found document for OrderNumber=" + orderNumber +
                    " | OrderPayments=" + paymentsObj +
                    " | FullDoc=" + doc.toJson();

        } catch (Exception e) {
            return "[DB] Debug error for OrderNumber=" + orderNumber + " | error=" + e.getMessage();
        }
    }

    public static Double getOrderTotalByOrderNumber(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);

            if (doc == null) {
                LogsManager.warn("[DB] POSOrders not found | OrderNumber=" + orderNumber);
                return 0.0;
            }

            return toDouble(doc.get("Total"));

        } catch (Exception e) {
            LogsManager.error("[DB] getOrderTotalByOrderNumber error: " + e.getMessage());
            return 0.0;
        }
    }



    public static java.time.LocalDateTime getOrderCreationDateTime(long orderNumber) {
        try {

            Document doc = getOrderByOrderNumber(orderNumber);

            if (doc == null) {
                LogsManager.warn("[DB] Order not found | OrderNumber=" + orderNumber);
                return null;
            }

            java.util.Date creationTime = doc.getDate("CreationTime");

            if (creationTime == null) {
                LogsManager.warn("[DB] CreationTime missing | OrderNumber=" + orderNumber);
                return null;
            }

            return creationTime.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();

        } catch (Exception e) {
            LogsManager.error("[DB] getOrderCreationDateTime error: " + e.getMessage());
            return null;
        }
    }




    public static Double getOrderSubTotalByOrderNumber(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);

            if (doc == null) {
                LogsManager.warn("[DB] POSOrders not found | OrderNumber=" + orderNumber);
                return 0.0;
            }

            return toDouble(doc.get("SubTotal"));

        } catch (Exception e) {
            LogsManager.error("[DB] getOrderSubTotalByOrderNumber error: " + e.getMessage());
            return 0.0;
        }
    }



    public static Document getLatestOrderByFilter(String cashierName, double expectedTotal) {

        try {

            MongoCollection<Document> col = getCollection("POSOrders");

            Document filter = new Document()
                    .append("CashierName", cashierName)
                    .append("Total", expectedTotal);

            Document doc = col.find(filter)
                    .sort(new Document("CreationTime", -1))
                    .first();

            if (doc == null) {
                LogsManager.warn("[DB] No order found | Cashier=" + cashierName +
                        " | Total=" + expectedTotal);
            }

            return doc;

        } catch (Exception e) {
            LogsManager.error("[DB] getLatestOrderByFilter error: " + e.getMessage());
            return null;
        }
    }

    public static Double getPayAmountByOrderNumber(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);

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
            for (Object payment : payments) {
                if (payment instanceof Document payDoc) {
                    sum += toDouble(payDoc.get("PayAmount"));
                }
            }

            return sum;

        } catch (Exception e) {
            LogsManager.error("[DB] getPayAmountByOrderNumber error: " + e.getMessage());
            return 0.0;
        }
    }

    public static Double getPaymentAmountFieldByOrderNumber(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);

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
            for (Object payment : payments) {
                if (payment instanceof Document payDoc) {
                    sum += toDouble(payDoc.get("Amount"));
                }
            }

            return sum;

        } catch (Exception e) {
            LogsManager.error("[DB] getPaymentAmountFieldByOrderNumber error: " + e.getMessage());
            return 0.0;
        }
    }

    private static double toDouble(Object value) {
        if (value == null) return 0.0;

        if (value instanceof Decimal128 decimal128) {
            return decimal128.bigDecimalValue().doubleValue();
        }

        if (value instanceof Number number) {
            return number.doubleValue();
        }

        String str = value.toString().trim();
        if (str.isEmpty()) return 0.0;

        str = str.replaceAll("[^0-9.\\-]", "");
        if (str.isEmpty()) return 0.0;

        return Double.parseDouble(str);
    }



    public static Order getLatestOrderByFilter(String cashierName,
                                                        double expectedTotal,
                                                        int secondsWindow) {

        try {

            Instant windowStart = Instant.now().minusSeconds(secondsWindow);

            Document filter = new Document("CashierName", cashierName)
                    .append("CreationTime",
                            new Document("$gte", Date.from(windowStart)));

            Document doc = getCollection("POSOrders")
                    .find(filter)
                    .sort(new Document("CreationTime", -1))
                    .limit(20)
                    .into(new java.util.ArrayList<>())
                    .stream()
                    .filter(d -> {

                        double dbTotal = toDouble(d.get("Total"));

                        return Math.abs(dbTotal - expectedTotal) < 0.01;

                    })
                    .findFirst()
                    .orElse(null);

            if (doc == null) {
                LogsManager.warn("[DB] No matching order found");
                return null;
            }

            long orderNumber = ((Number) doc.get("OrderNumber")).longValue();

            double total = toDouble(doc.get("Total"));

            Date creation = doc.getDate("CreationTime");

            LocalDateTime creationTime =
                    creation.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();

            return new Order(orderNumber, total, creationTime);

        } catch (Exception e) {

            LogsManager.error("[DB] getLatestOrderByFilter error: " + e.getMessage());

            return null;
        }
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }
}
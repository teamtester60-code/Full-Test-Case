package com.ferpfirstcode.utils.dataReader;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.testng.Assert;

import com.ferpfirstcode.pages.components.Order;
import com.ferpfirstcode.pojos.ProductData;
import com.ferpfirstcode.utils.logs.LogsManager;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts; // 🔥 هذا هو المفقود
import org.bson.Document;

public class DataBaseReader {

    private static final String DEFAULT_CONNECTION = "mongodb://localhost:27017";
    private static final String DEFAULT_DB_NAME = "Quiz";

    private static final String POS_ORDERS_COLLECTION = "POSOrders";
    private static final String APPLICATION_USERS_COLLECTION = "applicationUsers";
    private static final String POS_SETTINGS_COLLECTION = "POSSettings";
    private static final String ORDER_TYPES_COLLECTION = "POSOrderTypes";
    private static final String POS_PRICING_COLLECTION = "POSProductPricingClasses";
    private static final String POS_PRODUCT_TYPES_COLLECTION = "POSProductTypes";

    public static MongoClient mongoClient;
    private static MongoDatabase database;

    private DataBaseReader() {
        // utility class
    }

    // ==========================================
    // 1. Connection Management (Optimized)
    // ==========================================
    public static synchronized void connect() {
        if (mongoClient != null && database != null) {
            return;
        }

        String connectionString = firstNonBlank(
                System.getProperty("mongo.uri"),
                System.getenv("MONGO_URI"),
                PropertyReader.getPropertyOrNull("db.connectionString"),
                DEFAULT_CONNECTION
        );

        String dbName = firstNonBlank(
                System.getProperty("mongo.db"),
                System.getenv("MONGO_DB"),
                PropertyReader.getPropertyOrNull("db.name"),
                DEFAULT_DB_NAME
        );

        LogsManager.info("🔌 Connecting to MongoDB...");
        LogsManager.info("📦 Using database: " + dbName);

        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(dbName);
    }



    @Step("Get Document ID from Database using Serial: {serial}")
    public static String getDocumentIdBySerial(String serial) {
        try {
            MongoCollection<Document> posOrders = getCollection("POSOrders");
            Document doc = posOrders.find(Filters.eq("Serial", serial)).first();

            if (doc != null) {
                String documentId = doc.getObjectId("_id").toString();
                LogsManager.info("Successfully retrieved Document ID [" + documentId + "] for Serial [" + serial + "]");
                return documentId;
            }

            LogsManager.error("Critical Error: Document not found in DB for Serial [" + serial + "]");
            return null;

        } catch (Exception e) {
            LogsManager.error("Database query failed while fetching Document ID for Serial [" + serial + "]: " + e.getMessage());
            throw e;
        }
    }

    @Step("Get exact Order Total from Database using Serial: {serial}")
    public static Double getOrderTotalBySerial(String serial) {
        try {
            MongoCollection<Document> posOrders = getCollection("POSOrders");
            Document orderDoc = posOrders.find(Filters.eq("Serial", serial)).first();

            if (orderDoc == null) {
                throw new RuntimeException("Critical Error: Order with Serial [" + serial + "] was NOT found in DB!");
            }

            Object totalObj = orderDoc.get("Total");
            if (totalObj == null) {
                throw new RuntimeException("Critical Error: Order found, but 'Total' field is missing!");
            }

            Double exactTotal = null;

            // Handle MongoDB Decimal128 format safely
            if (totalObj instanceof org.bson.types.Decimal128) {
                exactTotal = ((org.bson.types.Decimal128) totalObj).doubleValue();
            } else if (totalObj instanceof Number) {
                exactTotal = ((Number) totalObj).doubleValue();
            } else {
                throw new RuntimeException("Critical Error: 'Total' field is not a valid number format!");
            }

            LogsManager.info("Mapped Serial [" + serial + "] to Order Total: " + exactTotal);
            return exactTotal;

        } catch (Exception e) {
            LogsManager.error("DB Query failed for Serial [" + serial + "]: " + e.getMessage());
            throw e;
        }
    }


    @Step("Get exact Order Number from Database using Serial: {serial}")
    public static String getOrderNumberBySerial(String serial) {
        try {
            // 1. الاتصال بـ Collection الطلبات (تأكد أن الاسم مطابق لما لديك في الداتا بيز)
            MongoCollection<Document> posOrders = getCollection("POSOrders");

            // 2. البحث عن الطلب الذي يحمل هذا الـ Serial الفريد
            Document orderDoc = posOrders.find(Filters.eq("Serial", serial)).first();

            // 3. التحقق الحاسم (Guard Clause) في حالة عدم وصول الطلب للداتا بيز
            if (orderDoc == null) {
                throw new RuntimeException("❌ Critical Error: Order with Serial [" + serial + "] was NOT found in DB!");
            }

            // 4. استخراج رقم الطلب (استخدمنا Object ثم String.valueOf لتجنب أي ClassCastException)
            Object orderNumObj = orderDoc.get("OrderNumber");
            if (orderNumObj == null) {
                throw new RuntimeException("❌ Critical Error: Order found, but 'OrderNumber' field is missing!");
            }

            String exactOrderNumber = String.valueOf(orderNumObj);
            LogsManager.info("✅ Mapped Serial [" + serial + "] to Order Number: " + exactOrderNumber);

            return exactOrderNumber;

        } catch (Exception e) {
            LogsManager.error("❌ DB Query failed for Serial [" + serial + "]: " + e.getMessage());
            throw e; // نرمي الخطأ لكي يفشل التست فوراً إذا حدثت مشكلة
        }
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

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            LogsManager.info("🔌 MongoDB connection closed.");
        }
    }

    // ==========================================
    // 2. Setup Data (Level 2 Fast Injection)
    // ==========================================
    // ==========================================
    // 2. Setup Data (Safe Injection)
    // ==========================================
    @Step("Configure POS Settings safely via Database")
    public static void configurePOSSettingsForTest() {
        MongoCollection<Document> posSettings = getCollection(POS_SETTINGS_COLLECTION);

        // Filter by finding a document that already has an _id (ensures we don't update a blank space)
        Bson filter = Filters.exists("_id", true);

        Bson update = Updates.combine(
                Updates.set("UseDailyStock", false),
                Updates.set("ShowProductsAvalQty", false),
                Updates.set("PriceIncludesTax", true)
        );

        // 🚨 REMOVED upsert(true) to prevent creating corrupted/partial documents 🚨
        posSettings.updateOne(filter, update);
        LogsManager.info("✅ Database Setup: POS Settings updated safely (No Upsert).");
    }

    @Step("Configure Order Types safely via Database")
    public static void configureOrderTypesForTest() {
        MongoCollection<Document> orderTypes = getCollection(ORDER_TYPES_COLLECTION);

        // Update ONLY active order types, ignore system-hidden ones to prevent crashes
        Bson filter = Filters.ne("IsDeleted", true);

        Bson update = Updates.combine(
                Updates.set("PaymentByAnotherUser", true),
                Updates.set("HasDiscount", false)
        );

        orderTypes.updateMany(filter, update);
        LogsManager.info("✅ Database Setup: Active Order Types updated safely.");
    }

    @Step("Enable Daily Stock and Available Quantity Settings via Database")
    public static void enableDailyStockSettings() {
        MongoCollection<Document> posSettings = getCollection(POS_SETTINGS_COLLECTION);

        Bson filter = Filters.exists("_id", true);
        Bson update = Updates.combine(
                Updates.set("UseDailyStock", true),
                Updates.set("ShowProductsAvalQty", true)
        );

        // 🚨 REMOVED upsert(true)
        posSettings.updateOne(filter, update);
        LogsManager.info("✅ Database Setup: Daily Stock settings enabled safely.");
    }

    @Step("Enable Change Order Type After Save Setting via Database")
    public static void enableChangeOrderTypeAfterSaveSetting() {
        MongoCollection<Document> posSettings = getCollection(POS_SETTINGS_COLLECTION);

        Bson filter = Filters.exists("_id", true);
        Bson update = Updates.combine(
                Updates.set("ChangeOrderTypeAfterSave", true)
        );

        // 🚨 REMOVED upsert(true)
        posSettings.updateOne(filter, update);
        LogsManager.info("✅ Database Setup: ChangeOrderTypeAfterSave enabled safely.");
    }

    // ==========================================
    // 3. User & Order Data Extractors (Restored)
    // ==========================================
    public static Document getDocumentByField(String collectionName, String fieldName, String value) {
        return getCollection(collectionName).find(eq(fieldName, value)).first();
    }

    public static String getPinByUsername(String collectionName, String username) {
        Document userDoc = getCollection(collectionName).find(eq("username", username)).first();
        return userDoc != null ? userDoc.getString("Pin") : null;
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
            LocalDate today = LocalDate.now();
            Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endOfDay = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

            return getCollection(POS_ORDERS_COLLECTION)
                    .find(and(
                            eq("OrderNumber", (int) orderNumber),
                            gte("CreationTime", startOfDay),
                            lt("CreationTime", endOfDay)
                    ))
                    .first();
        } catch (Exception e) {
            LogsManager.error("[DB] getOrderByOrderNumber error: " + e.getMessage());
            return null;
        }
    }

    public static String getOrderDebugInfo(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);
            if (doc == null) return "[DB] No document found for OrderNumber=" + orderNumber;
            Object paymentsObj = doc.get("OrderPayments");
            return "[DB] Found document for OrderNumber=" + orderNumber + " | OrderPayments=" + paymentsObj;
        } catch (Exception e) {
            return "[DB] Debug error for OrderNumber=" + orderNumber + " | error=" + e.getMessage();
        }
    }

    public static Double getOrderTotalByOrderNumber(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);
            if (doc == null) return 0.0;
            return toDouble(doc.get("Total"));
        } catch (Exception e) {
            LogsManager.error("[DB] getOrderTotalByOrderNumber error: " + e.getMessage());
            return 0.0;
        }
    }

    public static java.time.LocalDateTime getOrderCreationDateTime(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);
            if (doc == null) return null;
            java.util.Date creationTime = doc.getDate("CreationTime");
            if (creationTime == null) return null;
            return creationTime.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            LogsManager.error("[DB] getOrderCreationDateTime error: " + e.getMessage());
            return null;
        }
    }

    public static Double getOrderSubTotalByOrderNumber(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);
            if (doc == null) return 0.0;
            return toDouble(doc.get("SubTotal"));
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static Double getPayAmountByOrderNumber(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);
            if (doc == null) return 0.0;
            Object paymentsObj = doc.get("OrderPayments");
            if (!(paymentsObj instanceof List<?> payments) || payments.isEmpty()) return 0.0;
            double sum = 0.0;
            for (Object payment : payments) {
                if (payment instanceof Document payDoc) sum += toDouble(payDoc.get("PayAmount"));
            }
            return sum;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static Double getPaymentAmountFieldByOrderNumber(long orderNumber) {
        try {
            Document doc = getOrderByOrderNumber(orderNumber);
            if (doc == null) return 0.0;
            Object paymentsObj = doc.get("OrderPayments");
            if (!(paymentsObj instanceof List<?> payments) || payments.isEmpty()) return 0.0;
            double sum = 0.0;
            for (Object payment : payments) {
                if (payment instanceof Document payDoc) sum += toDouble(payDoc.get("Amount"));
            }
            return sum;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static Document getLatestOrderByFilter(String cashierName, double expectedTotal) {
        try {
            Document filter = new Document("CashierName", cashierName).append("Total", expectedTotal);
            return getCollection("POSOrders").find(filter).sort(new Document("CreationTime", -1)).first();
        } catch (Exception e) {
            return null;
        }
    }

    public static Order getLatestOrderByFilter(String cashierName, double expectedTotal, int secondsWindow) {
        try {
            Instant windowStart = Instant.now().minusSeconds(secondsWindow);
            Document filter = new Document("CashierName", cashierName)
                    .append("CreationTime", new Document("$gte", Date.from(windowStart)));

            Document doc = getCollection("POSOrders").find(filter).sort(new Document("CreationTime", -1))
                    .limit(20).into(new java.util.ArrayList<>()).stream()
                    .filter(d -> Math.abs(toDouble(d.get("Total")) - expectedTotal) < 0.01)
                    .findFirst().orElse(null);

            if (doc == null) return null;
            long orderNumber = ((Number) doc.get("OrderNumber")).longValue();
            double total = toDouble(doc.get("Total"));
            LocalDateTime creationTime = doc.getDate("CreationTime").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return new Order(orderNumber, total, creationTime);
        } catch (Exception e) {
            return null;
        }
    }

    private static double toDouble(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Decimal128 decimal128) return decimal128.bigDecimalValue().doubleValue();
        if (value instanceof Number number) return number.doubleValue();
        String str = value.toString().replaceAll("[^0-9.\\-]", "");
        return str.isEmpty() ? 0.0 : Double.parseDouble(str);
    }

    // ==========================================
    // 4. Advanced Products Merging (Optimized)
    // ==========================================
    @Step("Fetch Complete Products (Prices from PricingClasses + Names from ProductTypes)")
    public static List<ProductData> getCompleteProductsFromDB() {
        List<ProductData> finalProductList = new ArrayList<>();

        try {
            MongoCollection<Document> pricingCollection = getCollection(POS_PRICING_COLLECTION);
            Map<String, ProductData> pricingMap = new HashMap<>();

            for (Document priceDoc : pricingCollection.find()) {
                String productDocId = priceDoc.getString("ProductDocumentId");
                if (productDocId == null) continue;

                Double basePrice = priceDoc.get("Price") != null ? Double.parseDouble(String.valueOf(priceDoc.get("Price"))) : 0.0;

                List<ProductData.VolumeData> volumesList = new ArrayList<>();
                List<Document> volumesArray = priceDoc.getList("ProductPricingClassVolumes", Document.class);
                if (volumesArray != null) {
                    for (Document vol : volumesArray) {
                        String volId = vol.get("VolumeId") != null ? String.valueOf(vol.get("VolumeId")) : "";
                        Double volPrice = vol.get("Price") != null ? Double.parseDouble(String.valueOf(vol.get("Price"))) : 0.0;
                        volumesList.add(new ProductData.VolumeData(volId, volPrice));
                    }
                }
                pricingMap.put(productDocId, new ProductData("Unknown Name", basePrice, volumesList));
            }

            MongoCollection<Document> productTypesCollection = getCollection(POS_PRODUCT_TYPES_COLLECTION);

            for (Document productType : productTypesCollection.find()) {
                List<Document> productGroups = productType.getList("ProductGroups", Document.class);
                if (productGroups == null) continue;

                for (Document group : productGroups) {
                    List<Document> products = group.getList("Products", Document.class);
                    if (products == null) continue;

                    for (Document product : products) {
                        Object rawId = product.get("_id");
                        if (rawId == null) continue;

                        String productId = rawId.toString();
                        String productName = product.getString("Name");

                        if (pricingMap.containsKey(productId)) {
                            ProductData pd = pricingMap.get(productId);
                            pd.name = productName;
                            finalProductList.add(pd);
                        }
                    }
                }
            }

            StringBuilder allureReportData = new StringBuilder();
            for (ProductData pd : finalProductList) {
                allureReportData.append("🛒 Product: ").append(pd.name).append(" | 💰 Base Price: ").append(pd.basePrice).append("\n");
            }
            Allure.addAttachment("📜 Database Products (" + finalProductList.size() + " Items)", "text/plain", allureReportData.toString());

        } catch (Exception e) {
            Assert.fail("🚨 Database query failed: " + e.getMessage());
        }

        return finalProductList;
    }

//    public static String getSerialByTotalAmount(double expectedTotalAmount, int timeoutSeconds) {
//        long startTime = System.currentTimeMillis();
//        long timeoutMillis = timeoutSeconds * 1000L;
//
//        while (System.currentTimeMillis() - startTime < timeoutMillis) {
//            // البحث عن الطلب
//            Document orderDoc = getCollection("POSOrders")
//                    .find(Filters.eq("TotalAmount", expectedTotalAmount))
//                    .first();
//
//            if (orderDoc != null && orderDoc.containsKey("Serial")) {
//                return orderDoc.getString("Serial");
//            }
//
//            // 🔍 إضافة Debugging: طباعة آخر 3 طلبات دخلت الداتا بيز لنرى ما يحدث!
//            if (System.currentTimeMillis() % 3000 < 500) { // طباعة كل 3 ثوانٍ فقط لتجنب إغراق الـ Logs
//                Document lastEntry = getCollection("POSOrders").find().sort(Sorts.descending("CreationTime")).first();
//                LogsManager.info("🔍 [DB Debug] Last Order in DB is: " + (lastEntry != null ? lastEntry.toJson() : "Empty"));
//            }
//
//            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
//        }
//        throw new RuntimeException("❌ Timeout: الطلب غير موجود! تأكد أن النظام يرسل الـ API فعلياً.");
//    }


    private static double getDoubleFromMongo(Object val) {
        if (val == null) return 0.0;
        if (val instanceof org.bson.types.Decimal128) return ((org.bson.types.Decimal128) val).doubleValue();
        if (val instanceof Number) return ((Number) val).doubleValue();
        return 0.0;
    }
    public static Double waitForPaymentAmountFromDBBySerial(String exactSerial, double expectedAmount, double delta, int timeoutSeconds) {
        long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        double lastKnownAmount = 0.0;

        while (System.currentTimeMillis() < endTime) {
            try {
                Document orderDoc = getCollection("POSOrders")
                        .find(Filters.eq("Serial", exactSerial))
                        .first();

                if (orderDoc != null) {
                    // 1. استخراج المصفوفة OrderPayments
                    List<Document> payments = orderDoc.getList("OrderPayments", Document.class);

                    // 2. التحقق من وجود مصفوفة وبها بيانات
                    if (payments != null && !payments.isEmpty()) {
                        // نأخذ القيمة من أول عملية دفع في المصفوفة
                        Document firstPayment = payments.get(0);

                        if (firstPayment.containsKey("PayAmount")) {
                            lastKnownAmount = getDoubleFromMongo(firstPayment.get("PayAmount"));

                            // التحقق
                            if (Math.abs(lastKnownAmount - expectedAmount) <= delta) {
                                LogsManager.info("🎯 DB Validation Success: Amount matches (" + lastKnownAmount + ")");
                                return lastKnownAmount;
                            }
                        }
                    } else {
                        LogsManager.warn("⚠️ Document found, but 'OrderPayments' list is empty!");
                    }
                }
            } catch (Exception e) {
                LogsManager.error("⚠️ Error querying DB: " + e.getMessage());
            }
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        LogsManager.warn("⏳ Timeout reached. Returning last known amount: " + lastKnownAmount);
        return lastKnownAmount;
    }

    // أضف هذه الدالة داخل DataBaseReader
    private static double safeCastToDouble(Object value) {
        if (value instanceof org.bson.types.Decimal128) {
            return ((org.bson.types.Decimal128) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        }
        return 0.0;
    }
}
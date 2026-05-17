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

import org.bson.Document;
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
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

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
            // Start of today
            LocalDate today = LocalDate.now();
            Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Start of tomorrow
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

    @Step("Fetch Product Names from MongoDB and merge with API Prices")
    public static List<ProductData> getProductsWithNamesFromDB(Map<String, Double> idToPriceMap) {
        List<ProductData> finalProductList = new ArrayList<>();
        
        // 1. الاتصال بقاعدة البيانات (عدّل الرابط واسم القاعدة لتطابق بيئتك)
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("Smile"); // اسم الداتا بيز
            MongoCollection<Document> productsCollection = database.getCollection("POSProductPricingClasses"); // اسم الكولكشن

            // 2. تجميع كل الـ IDs لتحويلها إلى ObjectIds للبحث عنها دفعة واحدة
            List<ObjectId> objectIds = new ArrayList<>();
            for (String idStr : idToPriceMap.keySet()) {
                objectIds.add(new ObjectId(idStr));
            }

            // 3. الاستعلام: جلب كل المنتجات التي الـ _id الخاص بها موجود في القائمة
            Iterable<Document> dbProducts = productsCollection.find(Filters.in("_id", objectIds));

            // 4. دمج البيانات (الاسم من الـ DB والسعر من الـ Map الخاص بالـ API)
            for (Document doc : dbProducts) {
                String id = doc.getObjectId("_id").toHexString();
                String name = doc.getString("Name"); // تأكد أن حقل الاسم في Mongo اسمه Name أو name
                Double price = idToPriceMap.get(id);

                if (name != null && price != null) {
                    finalProductList.add(new ProductData(name, price, new ArrayList<>()));
                }
            }
        } catch (Exception e) {
            Assert.fail("🚨 Database connection or query failed: " + e.getMessage());
        }

        Allure.step("✅ Merged DB Names with API Prices. Total valid products: " + finalProductList.size());
        return finalProductList;
    }


    @Step("Fetch Product Names from MongoDB and merge with API Prices")
public static List<ProductData> getProductsWithNamesFromDB_POSProductPricingClasses(Map<String, Double> idToPriceMap) {
    List<ProductData> finalProductList = new ArrayList<>();
    
    Allure.step("🔍 1. Received " + idToPriceMap.size() + " IDs from API.");

    try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
        MongoDatabase database = mongoClient.getDatabase("Smile"); 
        
        // 🚨 1. نضع هنا اسم الـ Collection الجذري (الأساسي) الذي يحتوي على الشجرة
        MongoCollection<Document> productTypesCollection = database.getCollection("POSProductTypes"); 

        // 2. جلب كل البيانات (في بيئات الـ Testing هذا سريع جداً)
        Iterable<Document> allProductTypes = productTypesCollection.find();

        int dbFoundCount = 0;
        
        // 3. اختراق الشجرة (Looping through the nested arrays)
        for (Document productType : allProductTypes) {
            
            // الدخول إلى مستوى ProductGroups
            // ⚠️ تأكد من حالة الأحرف (ProductGroups أم productGroups) لتطابق Robo 3T
            List<Document> productGroups = productType.getList("ProductGroups", Document.class);
            if (productGroups == null) continue;
            
            for (Document group : productGroups) {
                
                // الدخول إلى مستوى Products
                // ⚠️ تأكد من حالة الأحرف (Products أم products)
                List<Document> products = group.getList("Products", Document.class);
                if (products == null) continue;
                
                for (Document product : products) {
                    // استخراج الـ ID للمنتج الداخلي
                    Object rawId = product.get("_id");
                    if (rawId == null) continue;
                    
                    // تحويل الـ ID لنص لسهولة المقارنة
                    String productId = rawId.toString(); 
                    String productName = product.getString("Name");
                    
                    // 💡 هنا السحر: هل هذا المنتج الداخلي موجود في خريطة أسعار الـ API؟
                    if (idToPriceMap.containsKey(productId)) {
                        dbFoundCount++;
                        Double price = idToPriceMap.get(productId);
                        
                        if (productName != null) {
                            finalProductList.add(new ProductData(productName, price, new ArrayList<>()));
                        }
                    }
                }
            }
        }
        
       Allure.step("🔍 2. Found " + dbFoundCount + " matching products inside the nested arrays.");
        Allure.step("🔍 3. Successfully merged " + finalProductList.size() + " products.");

    } catch (Exception e) {
        Assert.fail("🚨 Database query failed: " + e.getMessage());
    }


    StringBuilder allureReportData = new StringBuilder();
    for (ProductData pd : finalProductList) {
        String line = " Product: " + pd.name + " | Price: " + pd.basePrice;
        System.out.println(line);
        allureReportData.append(line).append("\n");
    }
    Allure.step("==========================================");
    Allure.addAttachment("📜 Merged Products (DB + API)", "text/plain", allureReportData.toString());
    

    Allure.step("✅ Merged DB Names with API Prices. Total valid products: " + finalProductList.size());
    return finalProductList;
}



@Step("Fetch Complete Products (Prices from PricingClasses + Names from ProductTypes)")
public static List<ProductData> getCompleteProductsFromDB() {
    List<ProductData> finalProductList = new ArrayList<>();
    
    try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
        MongoDatabase database = mongoClient.getDatabase("Smile"); // تأكد من اسم الداتا بيز
        
        // =========================================================
        // الخطوة 1: قراءة الأسعار والأحجام من جدول التسعير
        // =========================================================
        MongoCollection<Document> pricingCollection = database.getCollection("POSProductPricingClasses");
        
        // خريطة لربط (ProductDocumentId) ببيانات المنتج (السعر والأحجام)
        Map<String, ProductData> pricingMap = new HashMap<>(); 
        
        for (Document priceDoc : pricingCollection.find()) {
            String productDocId = priceDoc.getString("ProductDocumentId");
            if (productDocId == null) continue;
            
            // استخراج السعر الأساسي
            Double basePrice = priceDoc.get("Price") != null ? Double.parseDouble(String.valueOf(priceDoc.get("Price"))) : 0.0;
            
            // استخراج الأحجام (إن وجدت)
            List<ProductData.VolumeData> volumesList = new ArrayList<>();
            List<Document> volumesArray = priceDoc.getList("ProductPricingClassVolumes", Document.class);
            if (volumesArray != null) {
                for (Document vol : volumesArray) {
                    String volId = vol.get("VolumeId") != null ? String.valueOf(vol.get("VolumeId")) : "";
                    Double volPrice = vol.get("Price") != null ? Double.parseDouble(String.valueOf(vol.get("Price"))) : 0.0;
                    volumesList.add(new ProductData.VolumeData(volId, volPrice));
                }
            }
            
            // حفظ المنتج في الخريطة باسم مؤقت حتى نجلب اسمه الحقيقي
            pricingMap.put(productDocId, new ProductData("Unknown Name", basePrice, volumesList));
        }
        
        // =========================================================
        // الخطوة 2: قراءة الأسماء الحقيقية من الشجرة ودمجها
        // =========================================================
        MongoCollection<Document> productTypesCollection = database.getCollection("POSProductTypes");
        
        for (Document productType : productTypesCollection.find()) {
            List<Document> productGroups = productType.getList("ProductGroups", Document.class);
            if (productGroups == null) continue;
            
            for (Document group : productGroups) {
                List<Document> products = group.getList("Products", Document.class);
                if (products == null) continue;
                
                for (Document product : products) {
                    Object rawId = product.get("_id");
                    if (rawId == null) continue;
                    
                    String productId = rawId.toString(); // هذا هو نفسه الـ ProductDocumentId
                    String productName = product.getString("Name");
                    
                    // 💡 الدمج: إذا كان هذا المنتج له تسعير، قم بتحديث اسمه وإضافته للقائمة النهائية
                    if (pricingMap.containsKey(productId)) {
                        ProductData pd = pricingMap.get(productId);
                        pd.name = productName; // وضع الاسم الحقيقي
                        finalProductList.add(pd);
                    }
                }
            }
        }
        
        // =========================================================
        // 💡 إرفاق النتيجة في تقرير Allure
        // =========================================================
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
}
package com.ferpfirstcode.pojos; // تأكد من اسم الباكدج الخاص بك

import java.util.List;

// 👇 استبدل الكلاس القديم بهذا الكود 👇
public class ProductData {
    public String name;
    public Double basePrice;
    public List<VolumeData> volumes;

    // ✅ هذا هو الـ Constructor الذي يطلبه الكومبايلر!
    public ProductData(String name, Double basePrice, List<VolumeData> volumes) {
        this.name = name;
        this.basePrice = basePrice;
        this.volumes = volumes;
    }

    // كلاس الحجم الداخلي
    public static class VolumeData {
        public String volumeId;
        public Double price;

        public VolumeData(String volumeId, Double price) {
            this.volumeId = volumeId;
            this.price = price;
        }
    }
}
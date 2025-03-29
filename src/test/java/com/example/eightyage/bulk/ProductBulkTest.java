//package com.example.eightyage.bulk;
//
//import com.example.eightyage.domain.product.entity.Category;
//import com.example.eightyage.domain.product.entity.Product;
//import com.example.eightyage.domain.product.entity.SaleState;
//import com.example.eightyage.domain.product.repository.ProductBulkRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.UUID;
//
//@SpringBootTest
//@ActiveProfiles("ci")
//public class ProductBulkTest {
//
//    @Autowired
//    private ProductBulkRepository productBulkRepository;
//
//    @Test
//    void 제품_더미데이터_생성() {
//
//        int insertCount;
//
//        if ("ci".equals(System.getProperty("spring.profiles.active"))) {
//            insertCount = 100; // CI에서는 데이터 적게
//        } else {
//            insertCount = 1000000; // 로컬, 개발 서버 등에서는 많게
//        }
//
//        Random random = new Random();
//
//        List<Product> batchList = new ArrayList<>();
//
//        for (int i = 0; i < insertCount; i++) {
//            Category randomCategory = Category.values()[random.nextInt(Category.values().length)];
//            Product product = Product.builder()
//                            .category(randomCategory)
//                            .name(UUID.randomUUID().toString())
//                            .saleState(random.nextBoolean() ? SaleState.FOR_SALE : SaleState.SOLD_OUT)
//                            .build();
//
//            batchList.add(product);
//
//            if (batchList.size() == insertCount / 100) {
//                productBulkRepository.bulkInsertProduct(batchList);
//                batchList.clear();
//
//                sleep(500);
//            }
//        }
//
//        if (!batchList.isEmpty()) {
//            productBulkRepository.bulkInsertProduct(batchList);
//            batchList.clear();
//        }
//    }
//
//    private static void sleep(int millis) {
//        try {
//            Thread.sleep(millis);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}

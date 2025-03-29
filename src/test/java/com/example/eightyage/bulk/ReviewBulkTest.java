//package com.example.eightyage.bulk;
//
//import com.example.eightyage.domain.product.entity.Product;
//import com.example.eightyage.domain.review.entity.Review;
//import com.example.eightyage.domain.review.repository.ReviewBulkRepository;
//import com.example.eightyage.domain.user.entity.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@SpringBootTest
//@ActiveProfiles("ci")
//public class ReviewBulkTest {
//
//    @Autowired
//    private ReviewBulkRepository reviewBulkRepository;
//
//    @Test
//    void 리뷰_더미데이터_생성() {
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
//        List<Review> batchList = new ArrayList<>();
//
//        for (int i = 0; i < insertCount; i++) {
//            User user = User.builder().id(1L).build();
//            Product product = new Product((long) (random.nextInt(100) + 1));
//
//            Review review = new Review(user, product, random.nextDouble() * 5, "content" + i);
//            batchList.add(review);
//
//            if (batchList.size() == insertCount / 100) {
//                reviewBulkRepository.bulkInsertReviews(batchList);
//                batchList.clear();
//
////                sleep(500);
//            }
//        }
//
//        if (!batchList.isEmpty()) {
//            reviewBulkRepository.bulkInsertReviews(batchList);
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

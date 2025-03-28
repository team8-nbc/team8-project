//package com.example.eightyage.bulk;
//
//import com.example.eightyage.domain.review.entity.Review;
//import com.example.eightyage.domain.review.repository.ReviewBulkRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.ArrayList;
//import java.util.List;
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
//        List<Review> batchList = new ArrayList<>();
//
//        for (int i = 0; i < insertCount; i++) {
//            Review review = new Review();
//            batchList.add(review);
//
//            if (batchList.size() == insertCount) {
//                reviewBulkRepository.bulkInsertReviews(batchList);
//                batchList.clear();
//
//                sleep(500);
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

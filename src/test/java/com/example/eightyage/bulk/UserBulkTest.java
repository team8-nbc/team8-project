//package com.example.eightyage.bulk;
//
//import com.example.eightyage.domain.user.entity.User;
//import com.example.eightyage.domain.user.entity.UserRole;
//import com.example.eightyage.domain.user.repository.UserBulkRepository;
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
//public class UserBulkTest {
//
//    @Autowired
//    private UserBulkRepository userBulkRepository;
//
//    @Test
//    void 유저_데이터_백만건_생성() {
//
//        int insertCount;
//
//        if ("ci".equals(System.getProperty("spring.profiles.active"))) {
//            insertCount = 100; // CI에서는 데이터 적게
//        } else {
//            insertCount = 1000000; // 로컬, 개발 서버 등에서는 많게
//        }
//
//        List<User> batchList = new ArrayList<>();
//
//        for (int i = 0; i < insertCount; i++) {
//            User user = User.builder()
//                    .email(i + "@email.com")
//                    .nickname("nickname" + i)
//                    .password("password")
//                    .userRole(UserRole.ROLE_USER)
//                    .build();
//            batchList.add(user);
//
//            if (batchList.size() == insertCount) {
//                userBulkRepository.bulkInsertUsers(batchList);
//                batchList.clear();
//
//                sleep(500);
//            }
//        }
//
//        if (!batchList.isEmpty()) {
//            userBulkRepository.bulkInsertUsers(batchList);
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

package com.triple.homework.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByReviewIdAndDelYn(String reviewId, Character delYn);

    boolean existsByUserIdAndPlaceIdAndDelYn(String userId, String placeId, Character delYn);

    Review findByReviewIdAndDelYn(String reviewId, Character delYn);

    int countByPlaceIdAndDelYn(String placeId, Character delYn);
}

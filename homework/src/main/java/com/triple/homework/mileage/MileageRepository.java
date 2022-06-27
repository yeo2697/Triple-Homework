package com.triple.homework.mileage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface MileageRepository extends JpaRepository<Mileage, Long> {
    Mileage findByReviewIdAndMileageLogAndDelYn(String reviewId, String mileageLog, Character delYn);

    LinkedList<Mileage> findByReviewIdAndDelYn(String reviewId, Character delYn);

    @Query(value = "select SUM(mileage_value) from tbl_mileage where user_id = :id and del_yn = 'N' group by user_id", nativeQuery = true)
    Long getTotalMileages(@Param(value = "id") String userId);
}

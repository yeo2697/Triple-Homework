package com.triple.homework.review;

import java.util.ArrayList;

public interface ReviewService {
    Review select(String reviewId);

    void insert(Review review, ArrayList<String> photos) throws Exception;

    void update(Review review, ArrayList<String> photos) throws Exception;

    void delete(String reviewId) throws Exception;
}

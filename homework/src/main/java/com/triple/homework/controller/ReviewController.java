package com.triple.homework.controller;

import com.triple.homework.mileage.MileageService;
import com.triple.homework.place.Place;
import com.triple.homework.place.PlaceService;
import com.triple.homework.review.Review;
import com.triple.homework.review.ReviewService;
import com.triple.homework.user.User;
import com.triple.homework.user.UserService;
import com.triple.homework.user.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@RestController
public class ReviewController {

    private final UserService userService;
    private final PlaceService placeService;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(UserService userService, PlaceService placeService, ReviewService reviewService) {
        this.userService = userService;
        this.placeService = placeService;
        this.reviewService = reviewService;
    }

    @PostMapping(value = "/events")
    public ResponseEntity reviewEvents(@RequestBody HashMap param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        try{
            // type 의 Value 가 REVIEW 가 아닐경우 BAD_REQUEST
            if( !param.get("type").toString().equals("REVIEW") ) throw new Exception("400");

            String action = param.get("action").toString();
            String reviewId = param.get("reviewId").toString();
            String content = param.get("content").toString();
            ArrayList<String> attachedPhotoIds = (ArrayList<String>) param.get("attachedPhotoIds");
            String userId = param.get("userId").toString();
            String placeId = param.get("placeId").toString();

            // Review 생성
            Review review = new Review();
            review.setReviewId(reviewId);
            review.setUserId(userId);
            review.setPlaceId(placeId);
            review.setReviewContent(content);
            review.setReviewPhotos(String.join(",", attachedPhotoIds));

            if(action.equals("ADD")){
                review.setCreateBy(userId);
                review.setCreateDt(System.currentTimeMillis());

                reviewService.insert(review, attachedPhotoIds);
            }else if(action.equals("MOD")){
                reviewService.update(review, attachedPhotoIds);
            }else if(action.equals("DEL")){
                reviewService.delete(reviewId);
            }else{
                // 잘못된 action 값 BAD_REQUEST
                throw new Exception("400");
            }

            return new ResponseEntity("CREATED", headers, HttpStatus.CREATED);
        }catch (Exception e){
            if(e.getMessage().equals("400")) return new ResponseEntity("BAD_REQUEST", headers, HttpStatus.BAD_REQUEST);
            else if(e.getMessage().equals("404")) return new ResponseEntity("NOT_FOUND", headers, HttpStatus.NOT_FOUND);
            else if(e.getMessage().equals("409")) return new ResponseEntity("CONFLICT", headers, HttpStatus.CONFLICT);

            return new ResponseEntity("INTERNAL_SERVER_ERROR", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

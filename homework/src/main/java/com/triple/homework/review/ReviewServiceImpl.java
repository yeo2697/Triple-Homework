package com.triple.homework.review;

import com.triple.homework.mileage.Mileage;
import com.triple.homework.mileage.MileageRepository;
import com.triple.homework.mileage.MileageLog;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MileageRepository mileageRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, MileageRepository mileageRepository) {
        this.reviewRepository = reviewRepository;
        this.mileageRepository = mileageRepository;
    }

    @Override
    public Review select(String reviewId){
        return reviewRepository.findByReviewIdAndDelYn(reviewId, 'N');
    }

    //
    @Override
    @Transactional
    public void insert(Review review, ArrayList<String> photos) throws Exception {
        // 존재하는 review_id 로 요청이 올 경우 중복메세지 반환
        if(reviewRepository.existsByReviewIdAndDelYn(review.getReviewId(), 'N')) throw new Exception("409");
        // 사용자는 장소에 하나의 리뷰만 작성 가능
        if(reviewRepository.existsByUserIdAndPlaceIdAndDelYn(review.getUserId(), review.getPlaceId(), review.getDelYn()))  throw new Exception("409");

        // 특정 장소의 리뷰 개수 조회
        int reviewCount = reviewRepository.countByPlaceIdAndDelYn(review.getPlaceId(), 'N');

        // 리뷰 저장
        reviewRepository.save(review);

        // 1자 이상 텍스트 업로드 마일리지
        if(review.getReviewContent().length() > 0){
            Mileage mileage = new Mileage();
            mileage.setUserId(review.getUserId());
            mileage.setReviewId(review.getReviewId());
            mileage.setMileageLog(MileageLog.UPLOAD_TEXT.name());
            mileage.setMileageValue(1L);
            mileage.setCreateBy("Administrator");
            mileage.setCreateDt(System.currentTimeMillis());

            mileageRepository.save(mileage);
        }

        // 1장 이상 사진 업로드 마일리지
        if(photos.size() > 0){
            Mileage photosMileage = new Mileage();
            photosMileage.setUserId(review.getUserId());
            photosMileage.setReviewId(review.getReviewId());
            photosMileage.setMileageLog(MileageLog.UPLOAD_PHOTOS.name());
            photosMileage.setMileageValue(1L);
            photosMileage.setCreateBy("Administrator");
            photosMileage.setCreateDt(System.currentTimeMillis());

            mileageRepository.save(photosMileage);
        }

        // 첫 리뷰 보너스 마일리지
        if(reviewCount == 0){
            Mileage bonusMileage = new Mileage();
            bonusMileage.setUserId(review.getUserId());
            bonusMileage.setReviewId(review.getReviewId());
            bonusMileage.setMileageLog(MileageLog.FIRST_REVIEW.name());
            bonusMileage.setMileageValue(1L);
            bonusMileage.setCreateBy("Administrator");
            bonusMileage.setCreateDt(System.currentTimeMillis());

            mileageRepository.save(bonusMileage);
        }
    }

    @Override
    @Transactional
    public void update(Review review, ArrayList<String> photos) throws Exception {
        // review_id 로 수정할 레코드 미조회시 NOT_FOUND
        if(!reviewRepository.existsByReviewIdAndDelYn(review.getReviewId(), 'N')) throw new Exception("404");

        // 수정할 레코드 가져오기
        Review prevReview = reviewRepository.findByReviewIdAndDelYn(review.getReviewId(), 'N');

        // 기존 텍스트 길이
        int prevTextLength;

        if(prevReview.getReviewContent().equals("")) prevTextLength = 0;
        else prevTextLength = prevReview.getReviewContent().length();

        // 업데이트될 텍스트 길이
        int textLength = review.getReviewContent().length();

        // 기존 사진 길이
        int prevPhotosLength;

        if(prevReview.getReviewPhotos().equals("")) prevPhotosLength = 0;
        else prevPhotosLength = prevReview.getReviewPhotos().split(",").length;

        // 업데이트될 사진 길이
        int photosLength = photos.size();

        prevReview.setReviewContent(review.getReviewContent());
        prevReview.setReviewPhotos(review.getReviewPhotos());
        prevReview.setUpdateBy(review.getUserId());
        prevReview.setUpdateDt(System.currentTimeMillis());

        reviewRepository.save(prevReview);

        // Text 관련 수정사항
        if(prevTextLength < 1 && textLength > 0){
            // 이전 텍스트 X && 업데이트 텍스트 O => 마일리지 + 1
            Mileage mileage = new Mileage();
            mileage.setReviewId(prevReview.getReviewId());
            mileage.setUserId(prevReview.getUserId());
            mileage.setMileageLog(MileageLog.UPLOAD_TEXT.name());
            mileage.setMileageValue(1L);
            mileage.setCreateBy("Administrator");
            mileage.setCreateDt(System.currentTimeMillis());

            mileageRepository.save(mileage);
        }else if(prevTextLength > 0 && textLength < 1){
            // 이전 텍스트 O && 업데이트 텍스트 X => 마일리지 1점 회수
            Mileage mileage = mileageRepository.findByReviewIdAndMileageLogAndDelYn(prevReview.getReviewId(), MileageLog.UPLOAD_TEXT.name(), 'N');
            mileage.setDelYn('Y');
            mileage.setUpdateBy("Administrator");
            mileage.setUpdateDt(System.currentTimeMillis());

            mileageRepository.save(mileage);
        }

        // Photos 관련 수정사항
        if(prevPhotosLength < 1 && photosLength > 0){
            // 이전 리뷰 사진 x && 업데이트 리뷰 사진 O => 마일리지 + 1
            Mileage mileage = new Mileage();
            mileage.setReviewId(prevReview.getReviewId());
            mileage.setUserId(prevReview.getUserId());
            mileage.setMileageLog(MileageLog.UPLOAD_PHOTOS.name());
            mileage.setMileageValue(1L);
            mileage.setCreateBy("Administrator");
            mileage.setCreateDt(System.currentTimeMillis());

            mileageRepository.save(mileage);
        }else if(prevPhotosLength > 0 && photosLength < 1){
            // 이전 리뷰 사진 O && 업데이트 리뷰 사진 X => 마일리지 1점 회수
            Mileage mileage = mileageRepository.findByReviewIdAndMileageLogAndDelYn(prevReview.getReviewId(), MileageLog.UPLOAD_PHOTOS.name(), 'N');
            mileage.setDelYn('Y');
            mileage.setUpdateBy("Administrator");
            mileage.setUpdateDt(System.currentTimeMillis());

            mileageRepository.save(mileage);
        }
    }

    @Override
    @Transactional
    public void delete(String reviewId) throws Exception {
        // 해당 record 미존재시, NOT_FOUND
        if(!reviewRepository.existsByReviewIdAndDelYn(reviewId, 'N')) throw new Exception("404");

        // 기존 review select
        Review review = reviewRepository.findByReviewIdAndDelYn(reviewId, 'N');

        // review del_yn 값 Y로 변경
        review.setDelYn('Y');
        review.setUpdateBy(review.getUserId());
        review.setUpdateDt(System.currentTimeMillis());
        // 변경 사항 저장
        reviewRepository.save(review);

        // review id로 마일리지 조회
        LinkedList<Mileage> mileages = mileageRepository.findByReviewIdAndDelYn(review.getReviewId(), 'N');

        if(mileages.size() < 1) return;

        // review id로 조회된 마일리지 모두 회수
        for(Mileage mileage : mileages){
            mileage.setDelYn('Y');
            mileage.setUpdateBy("Administrator");
            mileage.setUpdateDt(System.currentTimeMillis());

            mileageRepository.save(mileage);
        }
    }
}

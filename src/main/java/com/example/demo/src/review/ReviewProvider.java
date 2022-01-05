package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.GetStoreReviewRes;
import com.example.demo.src.review.model.GetUserReviewRes;
import com.example.demo.src.store.model.GetStoreCategoryRes;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.Constant.recordsPerPage;

@Service
public class ReviewProvider {
    private final ReviewDao reviewDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }


    public List<GetUserReviewRes> getUserReviews(int userIdx) throws BaseException {
        try{
            List<GetUserReviewRes> getReviewRes = reviewDao.getUserReviews(userIdx);
            // 페이지가 존재하지 않으면
            return getReviewRes;
        }catch (Exception e){
                throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<GetStoreReviewRes> getStoreReviews(int storeIdx) throws BaseException {
        try{
            List<GetStoreReviewRes> getReviewRes = reviewDao.getStoreReviews(storeIdx);
            // 페이지가 존재하지 않으면
            return getReviewRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

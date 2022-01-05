package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.review.model.GetStoreReviewRes;
import com.example.demo.src.review.model.GetUserReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app")
public class ReivewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    public ReivewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService) {
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    /**
     * 음식점 등록 =>jwt 발급
     * [Post]/Reviews
     * 이름 중복 검사 - provider 단
     * ReviewPassword 정규표현식
     * @return
     */
//    @ResponseBody
//    @PostMapping("owners/{ownerIdx}/Reviews")
//    public BaseResponse<PostReviewRes> createReview(@PathVariable int ownerIdx,@RequestBody PostReviewReq postReviewReq) throws BaseException {
//        //jwt에서 idx 추출.
//        int ownerIdxByJwt = jwtService.getOwnerIdx();
//        //userIdx와 접근한 유저가 같은지 확인
//        if(ownerIdx != ownerIdxByJwt){
//            return new BaseResponse<>(INVALID_USER_JWT);
//        }
//        //문법적 오류
//
//        try{
//            PostReviewRes postReviewRes= ReviewService.createReview(ownerIdx,postReviewReq);
//            return new BaseResponse<>(postReviewRes);
//
//        }catch(BaseException exception){
//            return new BaseResponse<>(exception.getStatus());
//        }
//
//    }


    /*
    유저의 리뷰 보기
     */
    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("/users/{userIdx}/reviews")
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetUserReviewRes>> getUserReviews(@PathVariable("userIdx")int userIdx) {
        try {
            List<GetUserReviewRes> getReviewRes = reviewProvider.getUserReviews(userIdx);
            return new BaseResponse<>(getReviewRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
        가게의 리뷰 보기
         */
    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("/stores/{storeIdx}/reviews")
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetStoreReviewRes>> getStoreReviews(@PathVariable("storeIdx")int storeIdx) {
        try {
            List<GetStoreReviewRes> getStoreReviewRes = reviewProvider.getStoreReviews(storeIdx);
            return new BaseResponse<>(getStoreReviewRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}

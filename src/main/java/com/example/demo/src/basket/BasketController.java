package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.basket.model.GetBasketRes;
import com.example.demo.src.basket.model.PostBasketReq;
import com.example.demo.src.basket.model.PostBasketRes;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@Controller
@RequestMapping("/app")
public class BasketController {

    private final BasketService basketService;
    private final BasketProvider basketProvider;
    private final JwtService jwtService;

    @Autowired
    public BasketController(BasketService basketService, BasketProvider basketProvider, JwtService jwtService) {
        this.basketService = basketService;
        this.basketProvider = basketProvider;
        this.jwtService = jwtService;
    }

    /**
     * 장바구니 조회->user jwt인증 필요
     */
    @ResponseBody
    @GetMapping("/basket/")
    public BaseResponse<GetBasketRes> getRecentBasket(@RequestParam("userIdx") int userIdx) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        // Get Basket
        try {
            GetBasketRes getBasketRes = basketProvider.getRecentBasket(userIdx);
            //없으면 에러로 처리 안하고 텍스트를 내보냄
            if(getBasketRes==null){
                String sentence = "텅";
                return new BaseResponse(sentence);
            }
            return new BaseResponse<>(getBasketRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 유저 장바구니 추가 -> url
     * 1. 장바구니 추가
     * 2. 나머지 장바구니는 delete 해야한다.
     */
    @ResponseBody
    @PostMapping("/basket/")
    public BaseResponse<PostBasketRes> createBasket(@RequestParam("userIdx") int userIdx, @RequestParam("storeIdx") int storeIdx, PostBasketReq postBasketReq) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        // Get Basket
        try {
            PostBasketRes postBasketRes = basketService.addBasket(userIdx,storeIdx,postBasketReq);
            return new BaseResponse<>(postBasketRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

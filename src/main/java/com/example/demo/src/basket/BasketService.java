package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.src.basket.model.GetBasketRes;
import com.example.demo.src.basket.model.PostBasketReq;
import com.example.demo.src.basket.model.PostBasketRes;
import com.example.demo.src.store.model.PostStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.DELETE_FAIL_ERROR;

@Service
public class BasketService {
    private final BasketDao basketDao;
    private final BasketProvider basketProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BasketService(BasketDao basketDao, BasketProvider basketProvider, JwtService jwtService) {
        this.basketDao = basketDao;
        this.basketProvider = basketProvider;
        this.jwtService = jwtService;
    }

    /**
     *
     *
     * @param userIdx
     * @param storeIdx
     * @param postBasketReq
     * @return
     */
    @Transactional
    public PostBasketRes addBasket(int userIdx, int storeIdx, PostBasketReq postBasketReq) throws BaseException {
        try {
            //이전 장바구니 storeIdx 확인
            GetBasketRes getBasketRes = basketProvider.getRecentBasket(userIdx);
            //다른 가게에서 메뉴 추가?
            if(getBasketRes.getStoreIdx()!=storeIdx){
                //이전 장바구니 모두 삭제한다.
                int result = basketDao.deleteAllBasket(userIdx);
                if(result==0){
                    throw new BaseException(DELETE_FAIL_ERROR);
                }
            }

            //장바구니 생성
            int basketIdx = basketDao.addBasket(userIdx,storeIdx,postBasketReq);

            return new PostBasketRes(basketIdx);
        } catch (Exception e) {
            System.out.println("StoreService.createStore");
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

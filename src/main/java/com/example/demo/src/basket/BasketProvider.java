package com.example.demo.src.basket;

import com.example.demo.config.BaseException;
import com.example.demo.src.basket.model.GetBasketRes;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class BasketProvider {
    private final BasketDao basketDao;
    private final JwtService jwtService;

    @Autowired
    public BasketProvider(BasketDao basketDao, JwtService jwtService) {
        this.basketDao = basketDao;
        this.jwtService = jwtService;
    }

    public GetBasketRes getRecentBasket(int userIdx) throws BaseException {
        try{
            GetBasketRes getBasketRes = basketDao.getRecentBasket(userIdx);
            return getBasketRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

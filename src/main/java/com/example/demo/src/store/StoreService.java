package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.store.model.PostStoreReq;
import com.example.demo.src.store.model.PostStoreRes;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR;

@Service
public class StoreService {
    private final StoreDao storeDao;
    private final StoreProvider storeProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StoreService(StoreDao storeDao, StoreProvider storeProvider, JwtService jwtService) {
        this.storeDao = storeDao;
        this.storeProvider = storeProvider;
        this.jwtService = jwtService;
    }

    public PostStoreRes createStore(PostStoreReq postStoreReq) throws BaseException {
        //음식점명 중복 검사
        if (storeProvider.checkStoreName(postStoreReq.getStoreName()) == 1) {
            throw new BaseException(BaseResponseStatus.POST_STORE_EXIST_STORE_NAME);
        }
        String pwd;
        try {
            pwd = new AES128(Secret.STORE_INFO_PASSWORD_KEY).encrypt(postStoreReq.getStorePassword());
            postStoreReq.setStorePassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try {
            int storeIdx = storeDao.createStore(postStoreReq);
            System.out.println("storeIdx = " + storeIdx);
            return new PostStoreRes(storeIdx);
        } catch (Exception e) {
            System.out.println("StoreService.createStore");
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

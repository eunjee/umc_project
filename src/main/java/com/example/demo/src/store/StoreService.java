package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.likeStore.model.PostLikeStoreReq;
import com.example.demo.src.store.model.PostStoreReq;
import com.example.demo.src.store.model.PostStoreRes;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

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

            String jwt = jwtService.createStoreJwt(storeIdx);
            return new PostStoreRes(storeIdx,jwt);
        } catch (Exception e) {
            System.out.println("StoreService.createStore");
            throw new BaseException(DATABASE_ERROR);
        }
    }
//-------------찜 수 증가-----------------
    public void addLikeStoreCount(PostLikeStoreReq postLikeStoreReq) throws BaseException {
        try {
            int result = storeDao.addLikeStoreCount(postLikeStoreReq);
            if(result==0){
                throw new BaseException(DELETE_FAIL_ERROR);
            }
        } catch (Exception e) {
            System.out.println("StoreService.createStore");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void minusLikeStoreCount(PostLikeStoreReq postLikeStoreReq) throws BaseException {
        try {
            int result = storeDao.minusLikeStoreCount(postLikeStoreReq);
            if(result==0){
                throw new BaseException(DELETE_FAIL_ERROR);
            }
        } catch (Exception e) {
            System.out.println("StoreService.createStore");
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

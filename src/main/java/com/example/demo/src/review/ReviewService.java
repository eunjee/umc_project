package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.likeStore.model.PostLikeStoreReq;
import com.example.demo.src.store.StoreDao;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.src.store.model.PostStoreReq;
import com.example.demo.src.store.model.PostStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewService {
    private final StoreDao storeDao;
    private final StoreProvider storeProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewService(StoreDao storeDao, StoreProvider storeProvider, JwtService jwtService) {
        this.storeDao = storeDao;
        this.storeProvider = storeProvider;
        this.jwtService = jwtService;
    }
    @Transactional
    public PostStoreRes createStore(int ownerIdx,PostStoreReq postStoreReq) throws BaseException {
        //음식점명 중복 검사
        if (storeProvider.checkStoreName(postStoreReq.getStoreName()) == 1) {
            throw new BaseException(BaseResponseStatus.POST_STORE_EXIST_STORE_NAME);
        }

        try {
            int storeIdx = storeDao.createStore(ownerIdx,postStoreReq);
            return new PostStoreRes(storeIdx);
        } catch (Exception e) {
            System.out.println("StoreService.createStore");
            throw new BaseException(DATABASE_ERROR);
        }
    }
//-------------찜 수 증가-----------------
    @Transactional
    public void addLikeStoreCount(PostLikeStoreReq postLikeStoreReq) throws BaseException {
        try {
            int result = storeDao.addLikeStoreCount(postLikeStoreReq);
            if(result==0){
                throw new BaseException(DELETE_FAIL_ERROR);
            }
        } catch (BaseException e) {
            if(e.getStatus().getCode()==5003){
                throw e;
            }
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional
    public void minusLikeStoreCount(PostLikeStoreReq postLikeStoreReq) throws BaseException {
        try {
            int result = storeDao.minusLikeStoreCount(postLikeStoreReq);
            if(result==0){
                throw new BaseException(MINUS_LIKE_COUNT_ERROR);
            }
        } catch (Exception e) {
            System.out.println("StoreService.createStore");
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

package com.example.demo.src.likeStore;

import com.example.demo.config.BaseException;
import com.example.demo.src.likeStore.model.PostLikeStoreReq;
import com.example.demo.src.likeStore.model.PostLikeStoreRes;
import com.example.demo.src.store.StoreService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.DELETE_FAIL_ERROR;

@Service
public class LikeStoreService {
    private final StoreService storeService;
    private final LikeStoreProvider likeStoreProvider;
    private final LikeStoreDao likeStoreDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LikeStoreService(StoreService storeService, LikeStoreProvider likeStoreProvider, LikeStoreDao likeStoreDao, JwtService jwtService) {
        this.storeService = storeService;
        this.likeStoreProvider = likeStoreProvider;
        this.likeStoreDao = likeStoreDao;
        this.jwtService = jwtService;
    }


    @Transactional
    public PostLikeStoreRes addLikeStore(int userIdx, PostLikeStoreReq postLikeStoreReq) throws BaseException {
        try {
            //찜 추가
            int lsIdx = likeStoreDao.addLikeStore(userIdx,postLikeStoreReq);
            //store의 찜 개수 증가시키기
            storeService.addLikeStoreCount(postLikeStoreReq);
            return new PostLikeStoreRes(lsIdx);
        } catch (Exception e) {
            System.out.println("StoreService.createStore");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deleteLikeStore(int userIdx, PostLikeStoreReq postLikeStoreReq) throws BaseException {
        try {
            //찜 제거
            int result = likeStoreDao.deleteLikeStore(userIdx,postLikeStoreReq);
            if(result==0){
                throw new BaseException(DELETE_FAIL_ERROR);
            }
            //store의 찜 개수 감소시키기
            storeService.minusLikeStoreCount(postLikeStoreReq);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

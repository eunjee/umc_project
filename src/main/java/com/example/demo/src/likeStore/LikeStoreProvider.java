package com.example.demo.src.likeStore;

import com.example.demo.config.BaseException;
import com.example.demo.src.likeStore.model.GetLikeStoreRes;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class LikeStoreProvider {
    private final LikeStoreDao likeStoreDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LikeStoreProvider(LikeStoreDao likeStoreDao, JwtService jwtService) {
        this.likeStoreDao = likeStoreDao;
        this.jwtService = jwtService;
    }

    /**
     * 찜한 가게 모두 조회
     * @param userIdx
     * @return
     * @throws BaseException
     */
    public List<GetLikeStoreRes> getLikeStores(int userIdx) throws BaseException {
        try {
            List<GetLikeStoreRes> getLikeStoreRes = likeStoreDao.getLikeStores();
            return getLikeStoreRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

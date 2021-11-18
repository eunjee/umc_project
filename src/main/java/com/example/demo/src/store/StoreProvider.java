package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.GetStoreCategoryRes;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.INVALID_CATEGORY_NAME;

@Service
public class StoreProvider {
    private final StoreDao storeDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StoreProvider(StoreDao storeDao, JwtService jwtService) {
        this.storeDao = storeDao;
        this.jwtService = jwtService;
    }


    public List<GetStoreRes> getStores() throws BaseException {
        try{
            List<GetStoreRes> getStoreRes = storeDao.getStores();
            return getStoreRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkCategory(String categoryName)throws BaseException{
        try {
            return storeDao.checkCategory(categoryName);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<GetStoreCategoryRes> getStoresByCategoryName(String categoryName) throws BaseException {
        try{
            //해당 카테고리명이 있는지를 확인
            if(checkCategory(categoryName)==0){
                throw new BaseException(INVALID_CATEGORY_NAME);
            }
            //있으면 목록 확인
            List<GetStoreCategoryRes> getStoreCategoryRes = storeDao.getStoresByCategory(categoryName);
            return getStoreCategoryRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkStoreName(String storeName) throws BaseException {
        try{
            return storeDao.checkStoreName(storeName);
        }catch(Exception e){
            System.out.println("StoreProvider.checkStoreName");
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

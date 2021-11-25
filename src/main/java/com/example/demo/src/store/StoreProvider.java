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

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.Constant.recordsPerPage;

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


    public List<GetStoreRes> getStores(int pageNum) throws BaseException {
        try{
//            if(checkLastIdx(pageNum*recordsPerPage)==0){
//                throw new BaseException(INVALID_STORE_LAST_IDX);
//            }
            List<GetStoreRes> getStoreRes = storeDao.getStores(pageNum);
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


    public List<GetStoreCategoryRes> getStoresByCategoryName(String categoryName,int lastIdx) throws BaseException {
        try{
            //해당 카테고리명이 있는지를 확인
            if(checkCategory(categoryName)==0){
                throw new BaseException(INVALID_CATEGORY_NAME);
            }
//            //해당 last_idx가 Store 테이블 범위 내에 있는 지 확인
//            if(checkLastIdx(categoryName,lastIdx)==0){
//                throw new BaseException(INVALID_STORE_LAST_IDX);
//            }
            //있으면 목록 확인
            List<GetStoreCategoryRes> getStoreCategoryRes = storeDao.getStoresByCategory(categoryName,lastIdx);
            return getStoreCategoryRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    private int checkLastIdx(String categoryName,int last_idx) throws BaseException {
//        try {
//            return storeDao.checkLastIdx(categoryName,last_idx);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

    public int checkStoreName(String storeName) throws BaseException {
        try{
            return storeDao.checkStoreName(storeName);
        }catch(Exception e){
            System.out.println("StoreProvider.checkStoreName");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //가게 이름 얻어오기
    public String getStoreName(int storeIdx) throws BaseException {
        try {
            return storeDao.getStoreName(storeIdx);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //더 이상 보여줄 레코드가 있는지 없는지를 알려준다.
    public boolean hasNext(String categoryName, int lastIdx) throws BaseException {
        try {
            if(storeDao.hasNextRecord(categoryName,lastIdx)<3){
                return false;
            }
            return true;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean hasNext(int pageNum) throws BaseException {
        try {

            if(storeDao.hasNextRecord(pageNum)<3){
                return false;
            }
            return true;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

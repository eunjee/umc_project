package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.category.model.PostCategoryStoreReq;
import com.example.demo.src.category.model.PostCategoryStoreRes;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class CategoryService {
    private final CategoryDao categoryDao;
    private final CategoryProvider categoryProvider;
    private final StoreProvider storeProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CategoryService(CategoryDao categoryDao, CategoryProvider categoryProvider, StoreProvider storeProvider, JwtService jwtService) {
        this.categoryDao = categoryDao;
        this.categoryProvider = categoryProvider;
        this.storeProvider = storeProvider;
        this.jwtService = jwtService;
    }

    public PostCategoryStoreRes addStoreCategory(PostCategoryStoreReq postCSReq) throws BaseException {
        //카테고리 존재여부
        if(categoryProvider.checkCategory(postCSReq.getCategoryIdx())==0){
            throw new BaseException(NOT_EXIST_CATEGORY_NUM);
        }
        //음식점명 존재여부
        if(storeProvider.checkStoreName(postCSReq.getStoreName())==0){
            throw new BaseException(NOT_EXIST_STORE_NAME);
        }
        //Create실행
        try{
            PostCategoryStoreRes postCSRes = categoryDao.addStoreCategory(postCSReq);
            return postCSRes;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}

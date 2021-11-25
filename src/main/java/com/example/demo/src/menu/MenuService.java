package com.example.demo.src.menu;

import com.example.demo.config.BaseException;
import com.example.demo.src.menu.model.PatchMenuReq;
import com.example.demo.src.menu.model.PostMenuReq;
import com.example.demo.src.menu.model.PostMenuRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.DELETE_FAIL_ERROR;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuProvider menuProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MenuService(MenuDao menuDao, MenuProvider menuProvider, JwtService jwtService) {
        this.menuDao = menuDao;
        this.menuProvider = menuProvider;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public PostMenuRes addStoreMenu(int storeIdx, PostMenuReq postMenuReq) throws BaseException {
        //메뉴명 중복 검사
        //검사 통과 시 로직 실행
        try {
            int menuIdx = menuDao.addStoreMenu(storeIdx,postMenuReq);
            return new PostMenuRes(menuIdx);
        } catch (Exception e) {
            System.out.println("StoreService.createStore");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteMenu(int storeIdx, int menuIdx) throws BaseException {
        try{
            int result = menuDao.deleteMenu(storeIdx,menuIdx);
            if(result==0){
                throw new BaseException(DELETE_FAIL_ERROR);
            }
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyMenuPrice(int storeIdx, int menuIdx, PatchMenuReq patchMenuReq) throws BaseException {
        try{
            //menuIdx 있는지 확인 validation

            int result = menuDao.modifyMenuPrice(storeIdx,menuIdx,patchMenuReq);
            if(result==0){
                throw new BaseException(DELETE_FAIL_ERROR);
            }
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

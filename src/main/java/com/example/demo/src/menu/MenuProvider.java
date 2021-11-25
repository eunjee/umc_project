package com.example.demo.src.menu;

import com.example.demo.config.BaseException;
import com.example.demo.src.menu.model.GetStoreMenuRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class MenuProvider {
    private final MenuDao menuDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MenuProvider(MenuDao menuDao, JwtService jwtService) {
        this.menuDao = menuDao;
        this.jwtService = jwtService;
    }
    public List<GetStoreMenuRes> getStoreMenu(int storeIdx) throws BaseException {
        try {
            List<GetStoreMenuRes> storeMenuRes = menuDao.getStoreMenu(storeIdx);
            return storeMenuRes;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

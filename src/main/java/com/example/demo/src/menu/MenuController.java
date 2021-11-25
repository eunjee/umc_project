package com.example.demo.src.menu;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.MenuResponse;
import com.example.demo.src.menu.model.PatchMenuReq;
import com.example.demo.src.store.StoreProvider;
import com.example.demo.src.menu.model.GetStoreMenuRes;
import com.example.demo.src.menu.model.PostMenuReq;
import com.example.demo.src.menu.model.PostMenuRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_STORE_JWT;
import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@Controller
@RequestMapping("/app")
public class MenuController {
    private final StoreProvider storeProvider;
    private final MenuProvider menuProvider;
    private final MenuService menuService;
    private final JwtService jwtService;

    @Autowired
    public MenuController(StoreProvider storeProvider, MenuProvider menuProvider, MenuService menuService, JwtService jwtService) {
        this.storeProvider = storeProvider;
        this.menuProvider = menuProvider;
        this.menuService = menuService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/stores/{storeIdx}/menus")
    public MenuResponse<List<GetStoreMenuRes>,String> getStoreMenuRes(@PathVariable int storeIdx){
        try {
            List<GetStoreMenuRes> storeMenuRes = menuProvider.getStoreMenu(storeIdx);
            String storeName = storeProvider.getStoreName(storeIdx);
            return new MenuResponse<>(storeMenuRes, storeName);
        }catch(BaseException exception){
            return new MenuResponse<>(exception.getStatus());
        }
    }


    /**
     * 메뉴 추가하기
     */
    @ResponseBody
    @PostMapping("/stores/{storeIdx}/menus")
    public BaseResponse<PostMenuRes> addStoreMenu(@PathVariable int storeIdx, @RequestBody PostMenuReq postMenuReq){
        //menuName, price 체크

        try{
            PostMenuRes postMenuRes= menuService.addStoreMenu(storeIdx,postMenuReq);
            return new BaseResponse<>(postMenuRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping("/stores/{storeIdx}/menus/{menuIdx}")
    public BaseResponse<String> deleteStoreMenu(@PathVariable("storeIdx") int storeIdx, @PathVariable("menuIdx") int menuIdx) throws BaseException {
        try {

            //jwt에서 idx 추출.
            int storeIdxByJwt = jwtService.getStoreIdx();
            //storeIdx와 접근한 유저가 같은지 확인
            if(storeIdx != storeIdxByJwt){
                return new BaseResponse<>(INVALID_STORE_JWT);
            }

            //삭제 수행
            menuService.deleteMenu(storeIdx,menuIdx);
            String result = "메뉴를 삭제했습니다.";
            return new BaseResponse<>(result);
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 음식 가격 수정
     * @param storeIdx
     * @param menuIdx
     * @param patchMenuReq
     * @return
     */
    @ResponseBody
    @PatchMapping("/stores/{storeIdx}/menus/{menuIdx}")
    public BaseResponse<String> modifyMenuPrice(@PathVariable("storeIdx") int storeIdx, @PathVariable("menuIdx") int menuIdx, PatchMenuReq patchMenuReq){
        try {

            //jwt에서 idx 추출.
            int storeIdxByJwt = jwtService.getStoreIdx();
            //storeIdx와 접근한 유저가 같은지 확인
            if(storeIdx != storeIdxByJwt){
                return new BaseResponse<>(INVALID_STORE_JWT);
            }

            if(patchMenuReq.getPrice()==0){
                return new BaseResponse<>(BaseResponseStatus.PATCH_MENU_PRICE_EMPTY);
            }
            //삭제 수행
            menuService.modifyMenuPrice(storeIdx,menuIdx,patchMenuReq);
            String result = "메뉴가격을 수정했습니다.";
            return new BaseResponse<>(result);
//            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

package com.example.demo.src.likeStore;

import com.example.demo.config.LikeStoreResponse;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.likeStore.model.GetLikeStoreRes;
import com.example.demo.src.likeStore.model.PostLikeStoreReq;
import com.example.demo.src.likeStore.model.PostLikeStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@Controller
@RequestMapping("/app")
public class LikeStoreController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final LikeStoreProvider likeStoreProvider;
    @Autowired
    private final LikeStoreService likeStoreService;
    @Autowired
    private final JwtService jwtService;

    public LikeStoreController(LikeStoreProvider likeStoreProvider, LikeStoreService likeStoreService, JwtService jwtService) {
        this.likeStoreProvider = likeStoreProvider;
        this.likeStoreService = likeStoreService;
        this.jwtService = jwtService;
    }

    /**
     * 찜한 가게 조회
     */
    @ResponseBody
    @GetMapping("/likeStores/{userIdx}")
    public LikeStoreResponse<List<GetLikeStoreRes>, Integer> getLikeStores(@PathVariable("userIdx") int userIdx) throws BaseException {
        // @PathVariable RESTful(URL)에서 명시된 파라미터({})를 받는 어노테이션, 이 경우 userId값을 받아옴.
        //  null값 or 공백값이 들어가는 경우는 적용하지 말 것
        //  .(dot)이 포함된 경우, .을 포함한 그 뒤가 잘려서 들어감

        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new LikeStoreResponse<>(INVALID_USER_JWT);
        }
        // Get Users
        try {
            List<GetLikeStoreRes> getLikeStoreRes = likeStoreProvider.getLikeStores(userIdx);
            return new LikeStoreResponse<>(getLikeStoreRes,userIdx);
        } catch (BaseException exception) {
            return new LikeStoreResponse<>((exception.getStatus()));
        }
    }

    /**
     * 찜한 가게 추가
     * -> 찜한 가게 수 증가 시키기
     */
    @ResponseBody
    @PostMapping("/likeStores/{userIdx}")
    public BaseResponse<PostLikeStoreRes> addLikeStores(@PathVariable("userIdx") int userIdx, PostLikeStoreReq postLikeStoreReq) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        // Get Users
        try {
            PostLikeStoreRes postLikeStoreRes = likeStoreService.addLikeStore(userIdx,postLikeStoreReq);
            return new BaseResponse<>(postLikeStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 찜한 가게 삭제
     * -> 찜한 가게 수 감소 시키기
     */
    @ResponseBody
    @DeleteMapping("/likeStores/{userIdx}")
    public BaseResponse<String> deleteLikeStores(@PathVariable("userIdx") int userIdx, PostLikeStoreReq postLikeStoreReq) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        // Get Users
        try {
            likeStoreService.deleteLikeStore(userIdx,postLikeStoreReq);
            String result="찜한 가게에서 삭제했습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

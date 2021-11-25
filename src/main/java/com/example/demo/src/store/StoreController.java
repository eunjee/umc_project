package com.example.demo.src.store;

import com.example.demo.config.*;
import com.example.demo.src.store.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.utils.ValidationRegex.isRegexPassword;

@RestController
@RequestMapping("/app")
public class StoreController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final StoreProvider storeProvider;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    public StoreController(StoreProvider storeProvider, StoreService storeService, JwtService jwtService) {
        this.storeProvider = storeProvider;
        this.storeService = storeService;
        this.jwtService = jwtService;
    }

    /**
     * 음식점 등록 =>jwt 발급
     * [Post]/stores
     * 이름 중복 검사 - provider 단
     * storePassword 정규표현식
     * @return
     */
    @ResponseBody
    @PostMapping("/stores")
    public BaseResponse<PostStoreRes> createStore(@RequestBody PostStoreReq postStoreReq){
        //음식점 이름 입력여부
        if(postStoreReq.getStoreName()==null){
            return new BaseResponse<>(BaseResponseStatus.POST_STORE_EMPTY_STORE_NAME);
        }
        //password 입력 여뷰
        if(postStoreReq.getStorePassword()==null){
            return new BaseResponse<>(BaseResponseStatus.POST_EMPTY_PASSWORD);
        }
        //password 정규표현식
        if(!isRegexPassword(postStoreReq.getStorePassword())){
            return new BaseResponse<>(BaseResponseStatus.POST_INVALID_PASSWORD);
        }
        try{
            PostStoreRes postStoreRes= storeService.createStore(postStoreReq);
            return new BaseResponse<>(postStoreRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

   /*
   로그인 과정 필요
    */


    /*
    페이징 사용
    방법2- 페이지 번호 주기
     */
    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("/stores")
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public StoreResponse<List<GetStoreRes>,Boolean> getStores(@RequestParam(defaultValue="1") int pageNum) {
        try {
            List<GetStoreRes> getStoreRes = storeProvider.getStores(pageNum);
            boolean hasNext = storeProvider.hasNext(pageNum);
            return new StoreResponse<>(getStoreRes,hasNext);

        } catch (BaseException exception) {
            return new StoreResponse<>((exception.getStatus()));
        }
    }

    /*
    페이징 처리
     방법3- 최근 본 idx를 기준으로 paging
     한 페이지에 보여주는 레코드 : 3개
     */

    //Query String
    @ResponseBody   // return되는 자바 객체를 JSON으로 바꿔서 HTTP body에 담는 어노테이션.
    //  JSON은 HTTP 통신 시, 데이터를 주고받을 때 많이 쓰이는 데이터 포맷.
    @GetMapping("/store") // (GET) 127.0.0.1:9000/app/users
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public StoreResponse<List<GetStoreCategoryRes>,Boolean> getStoreCategories(@RequestParam String categoryName, @RequestParam(defaultValue="0") int lastIdx) {
        //  @RequestParam은, 1개의 HTTP Request 파라미터를 받을 수 있는 어노테이션(?뒤의 값). default로 RequestParam은 반드시 값이 존재해야 하도록 설정되어 있지만, (전송 안되면 400 Error 유발)
        //  지금 예시와 같이 required 설정으로 필수 값에서 제외 시킬 수 있음
        //  defaultValue를 통해, 기본값(파라미터가 없는 경우, 해당 파라미터의 기본값 설정)을 지정할 수 있음
        try {

            List<GetStoreCategoryRes> getStoreCategoryRes = storeProvider.getStoresByCategoryName(categoryName,lastIdx);
            boolean hasNext = storeProvider.hasNext(categoryName,lastIdx);
            //null값이면 메시지를 반환한다.
            if(getStoreCategoryRes.isEmpty()){
                String message =categoryName+"가게가 없습니다.";
                return new StoreResponse(message,false);
            }
            return new StoreResponse<>(getStoreCategoryRes,hasNext);
        } catch (BaseException exception) {
            return new StoreResponse<>((exception.getStatus()));
        }
    }

}

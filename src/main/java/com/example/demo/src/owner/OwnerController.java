package com.example.demo.src.owner;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.owner.model.PostLoginReq;
import com.example.demo.src.owner.model.PostLoginRes;
import com.example.demo.src.owner.model.PostOwnerReq;
import com.example.demo.src.owner.model.PostOwnerRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/owners")
public class OwnerController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final OwnerProvider ownerProvider;
    @Autowired
    private final OwnerService ownerService;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    public OwnerController(OwnerProvider ownerProvider, OwnerService ownerService, JwtService jwtService) {
        this.ownerProvider = ownerProvider;
        this.ownerService = ownerService;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }
    @ResponseBody
    @PostMapping("/sign-up")    // POST 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<PostOwnerRes> createOwner(@RequestBody PostOwnerReq postOwnerReq) {
        //  @RequestBody란, 클라이언트가 전송하는 HTTP Request Body(우리는 JSON으로 통신하니, 이 경우 body는 JSON)를 자바 객체로 매핑시켜주는 어노테이션
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        // 이름 값이 존재하는지 확인
        if(postOwnerReq.getOwnerName() ==null){
            return new BaseResponse<>(USERS_EMPTY_USER_NAME);
        }
        //이름 값에 특수문자가 있는지 확인
        if(!isRegexName(postOwnerReq.getOwnerName())){
            return new BaseResponse<>(USERS_INVALID_USER_NAME);
        }
        //비밀번호 형식이 맞는지 확인 (소문자, 숫자, 특수기호 하나이상, 8-20자)
        if (!isRegexPassword(postOwnerReq.getOwnerPassword())) {
            return new BaseResponse<>(POST_INVALID_PASSWORD);
        }
        //email에 값이 존재하는지, 빈 값으로 요청하지는 않았는지 검사합니다. 빈값으로 요청했다면 에러 메시지를 보냅니다.
        if (postOwnerReq.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //email 정규표현: 입력받은 이메일이 email@domain.xxx와 같은 형식인지 검사합니다. 형식이 올바르지 않다면 에러 메시지를 보냅니다.
        if (!isRegexEmail(postOwnerReq.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        // phoneNum에 값이 존재하는지, 빈 값으로 요청하지는 않았는지 검사합니다. 빈값으로 요청했다면 에러 메시지를 보냅니다.
        if (postOwnerReq.getPhoneNum() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
        }
        //phoneNum 정규표현: 입력받은 이메일이 email@domain.xxx와 같은 형식인지 검사합니다. 형식이 올바르지 않다면 에러 메시지를 보냅니다.
        if (!isRegexPhoneNum(postOwnerReq.getPhoneNum())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }

        try {
            PostOwnerRes postOwnerRes = ownerService.createOwner(postOwnerReq);
            return new BaseResponse<>(postOwnerRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /owner/logIn
     */
    @ResponseBody
    @PostMapping("/log-in")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try {
            // TODO: 로그인 값들에 대한 형식적인 validation 처리해주셔야합니다!
            //이메일 형식 확인
            if(postLoginReq.getEmail()==null){
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }
            //email 정규표현: 입력받은 이메일이 email@domain.xxx와 같은 형식인지 검사합니다. 형식이 올바르지 않다면 에러 메시지를 보냅니다.
            if (!isRegexEmail(postLoginReq.getEmail())) {
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }
            //비밀번호 형식이 맞는지 확인 (소문자, 숫자, 특수기호 하나이상, 8-20자)
            if (!isRegexPassword(postLoginReq.getOwnerPassword())) {
                return new BaseResponse<>(POST_INVALID_PASSWORD);
            }
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = ownerProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}

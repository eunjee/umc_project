package com.example.demo.src.owner;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.owner.model.PostOwnerReq;
import com.example.demo.src.owner.model.PostOwnerRes;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OwnerService {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final OwnerDao OwnerDao;
    private final OwnerProvider OwnerProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    @Autowired //readme 참고
    public OwnerService(OwnerDao OwnerDao, OwnerProvider OwnerProvider, JwtService jwtService) {
        this.OwnerDao = OwnerDao;
        this.OwnerProvider = OwnerProvider;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    }

    public PostOwnerRes createOwner(PostOwnerReq postOwnerReq) throws BaseException {
        //중복 확인: 해당 이메일을 가진 유저가 있는지 확인합니다. 중복될 경우, 에러 메시지를 보냅니다.
        if (OwnerProvider.checkEmail(postOwnerReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        // 중복 확인: 해당 핸드폰 번호를 가진 유저가 있는지 확인합니다. 중복될 경우, 에러 메시지를 보냅니다.
        if (OwnerProvider.checkPhoneNum(postOwnerReq.getPhoneNum()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_PHONE_NUMBER);
        }
        String pwd;
        try {
            // 암호화: postOwnerReq에서 제공받은 비밀번호를 보안을 위해 암호화시켜 DB에 저장합니다.
            // ex) password123 -> dfhsjfkjdsnj4@!$!@chdsnjfwkenjfnsjfnjsd.fdsfaifsadjfjaf
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postOwnerReq.getOwnerPassword()); // 암호화코드
            postOwnerReq.setOwnerPassword(pwd);
        } catch (Exception ignored) { // 암호화가 실패하였을 경우 에러 발생
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try {
            int ownerIdx = OwnerDao.createOwner(postOwnerReq);

//  *********** 해당 부분은 7주차 수업 후 주석해제하서 대체해서 사용해주세요! ***********
//            //jwt 발급.
            String jwt = jwtService.createOwnerJwt(ownerIdx);
            return new PostOwnerRes(ownerIdx,jwt);
//  *********************************************************************
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

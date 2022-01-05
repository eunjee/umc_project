package com.example.demo.src.owner;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.owner.model.Owner;
import com.example.demo.src.owner.model.PostLoginReq;
import com.example.demo.src.owner.model.PostLoginRes;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.FAILED_TO_LOGIN;

@Service
public class OwnerProvider {
    private final OwnerDao ownerDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public OwnerProvider(OwnerDao ownerDao, JwtService jwtService) {
        this.ownerDao = ownerDao;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }

    public int checkEmail(String email) throws BaseException {
        try {
            return ownerDao.checkEmail(email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPhoneNum(String phoneNum) throws BaseException {
        try {
            return ownerDao.checkPhoneNum(phoneNum);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        Owner owner = ownerDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(owner.getOwnerPassword()); // 복호화
            // 회원가입할 때 비밀번호가 복호화되어 저장되었기 떄문에 로그인을 할때도 복호화된 값끼리 비교를 해야합니다.
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (postLoginReq.getOwnerPassword().equals(password)) { //비말번호가 일치한다면 userIdx를 가져온다.
            Owner ownerDTO = ownerDao.getPwd(postLoginReq);
            //탈퇴한 회원이면 체크
            if(ownerDTO.getStatus().equals("withdraw")){
                throw new BaseException(WITHDRAW_ERROR);
            }
            int ownerIdx = ownerDTO.getOwnerIdx();
//  *********** 해당 부분은 7주차 - JWT 수업 후 주석해제 및 대체해주세요!  **************** //
            String jwt = jwtService.createOwnerJwt(ownerIdx);
            return new PostLoginRes(ownerIdx,jwt);
//  **************************************************************************

        } else { // 비밀번호가 다르다면 에러메세지를 출력한다.
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }
}

package com.example.demo.src.owner;

import com.example.demo.src.owner.model.Owner;
import com.example.demo.src.owner.model.PostLoginReq;
import com.example.demo.src.owner.model.PostOwnerReq;
import com.example.demo.src.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
@Repository
public class OwnerDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createOwner(PostOwnerReq postOwnerReq) {
        String createOwnerQuery = "insert into Owner (ownerName,ownerPassword,phoneNum,email) VALUES (?,?,?,?)"; // 실행될 동적 쿼리문
        Object[] createUserParams = new Object[]{postOwnerReq.getOwnerName(),postOwnerReq.getOwnerPassword(),postOwnerReq.getPhoneNum(),postOwnerReq.getEmail()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createOwnerQuery, createUserParams);
        // email -> postUserReq.getEmail(), password -> postUserReq.getPassword(), nickname -> postUserReq.getNickname() 로 매핑(대응)시킨다음 쿼리문을 실행한다.
        // 즉 DB의 User Table에 (email, password, nickname)값을 가지는 유저 데이터를 삽입(생성)한다.

        String lastInsertIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from Owner where email = ?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        String checkEmailParams = email; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public int checkPhoneNum(String phoneNum) {
        String checkPhoneNumQuery = "select exists(select phoneNum from Owner where phoneNum = ?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
        String checkPhoneNumParams = phoneNum; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkPhoneNumQuery,
                int.class,
                checkPhoneNumParams);
    }

    public Owner getPwd(PostLoginReq postLoginReq) {
        String getPwdQuery = "select * from Owner where email = ?"; // 해당 email을 만족하는 User의 정보들을 조회한다.
        String getPwdParams = postLoginReq.getEmail(); // 주입될 email값을 클라이언트의 요청에서 주어진 정보를 통해 가져온다.

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new Owner(
                        rs.getInt("ownerIdx"),
                        rs.getString("ownerName"),
                        rs.getString("ownerPassword"),
                        rs.getString("email"),
                        rs.getString("phoneNum"),
                        rs.getString("status")
                ), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getPwdParams
        );
    }
}

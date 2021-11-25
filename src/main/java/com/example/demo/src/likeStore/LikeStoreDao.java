package com.example.demo.src.likeStore;

import com.example.demo.src.likeStore.model.GetLikeStoreRes;
import com.example.demo.src.likeStore.model.PostLikeStoreReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LikeStoreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetLikeStoreRes> getLikeStores() {
        String getUsersQuery = "select ls.storeIdx,s.storeName,s.minOrderPrice,s.avgRate,s.deliveryTip,group_concat(m.menuName) as menuNames from LikeStore ls inner join Store s on s.storeIdx=ls.storeIdx inner join Menu m on m.storeIdx=ls.storeIdx where ls.deleteFlag !='y' group by ls.storeIdx";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs, rowNum) -> new GetLikeStoreRes(
                        rs.getInt("ls.storeIdx"),
                        rs.getString("s.storeName"),
                        rs.getInt("s.minOrderPrice"),
                        rs.getFloat("s.avgRate"),
                        rs.getInt("s.deliveryTip"),
                        rs.getString("menuNames")) // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
        );
    }

    public int addLikeStore(int userIdx, PostLikeStoreReq postLikeStoreReq) {
        String createLSQuery = "insert into LikeStore (storeIdx,userIdx) VALUES ((select storeIdx from Store where storeName=?),?)";
        Object[] createLSParams = new Object[]{postLikeStoreReq.getStoreName(),userIdx};
        this.jdbcTemplate.update(createLSQuery, createLSParams);
        // email -> postUserReq.getEmail(), password -> postUserReq.getPassword(), nickname -> postUserReq.getNickname() 로 매핑(대응)시킨다음 쿼리문을 실행한다.
        // 즉 DB의 User Table에 (email, password, nickname)값을 가지는 유저 데이터를 삽입(생성)한다.

        String lastInsertIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int deleteLikeStore(int userIdx, PostLikeStoreReq postLikeStoreReq) {
        String modifyLSQuery = "update LikeStore set updateAt=CURRENT_TIMESTAMP(),deleteFlag='y' where storeIdx = (select storeIdx from Store where storeName=?)"; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        return this.jdbcTemplate.update(modifyLSQuery, postLikeStoreReq.getStoreName());
    }
}

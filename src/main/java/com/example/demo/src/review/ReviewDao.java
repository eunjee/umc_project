package com.example.demo.src.review;

import com.example.demo.src.likeStore.model.PostLikeStoreReq;
import com.example.demo.src.review.model.GetStoreReviewRes;
import com.example.demo.src.review.model.GetUserReviewRes;
import com.example.demo.src.store.model.GetStoreCategoryRes;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.src.store.model.PostStoreReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.Constant.recordsPerPage;

@Repository
public class ReviewDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserReviewRes>getUserReviews(int userIdx) {
        String getStoresQuery = "select * from Review r inner join Order o on o.orderIdx = r.orderIdx where o.userIdx = ?"; //User 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
        return this.jdbcTemplate.query(getStoresQuery,
                (rs, rowNum) -> new GetUserReviewRes(
                        rs.getString("storeName"),
                        rs.getString("content"),
                        rs.getInt("rate")
                        ),userIdx
                );
    }

    public List<GetStoreReviewRes> getStoreReviews(int storeIdx) {
        String getStoresQuery = "select * from Review r inner join Order o on o.orderIdx = r.orderIdx where o.storeIdx = ?"; //User 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
        return this.jdbcTemplate.query(getStoresQuery,
                (rs, rowNum) -> new GetStoreReviewRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("content"),
                        rs.getInt("rate")
                ),storeIdx
        );
    }
}

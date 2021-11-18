package com.example.demo.src.store;

import com.example.demo.src.store.model.GetStoreCategoryRes;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.src.store.model.PostStoreReq;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StoreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetStoreRes> getStores() {
        String getStoresQuery = "select * from Store"; //User 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
        return this.jdbcTemplate.query(getStoresQuery,
                (rs, rowNum) -> new GetStoreRes(
                        rs.getInt("storeIdx"),
                        rs.getString("storeName"),
                        rs.getString("storeInfo"),
                        rs.getInt("minOrderPrice"),
                        rs.getFloat("avgRate"),
                        rs.getInt("recentCommentCount"),
                        rs.getInt("deliveryTip"), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                        rs.getInt("recentOrderCount"),
                        rs.getInt("totalReviewCount")
                        )
                );
    }

    public List<GetStoreCategoryRes> getStoresByCategory(String categoryName) {
        //categoryIdx 받기
        String query = "select categoryIdx from Category where categoryName=?";
        String param = categoryName;
        int categoryIdx = this.jdbcTemplate.queryForObject(query,int.class,param);
        //store 목록 받기
        return this.jdbcTemplate.query("select * from Category c inner join StoreCategory sc on sc.categoryIdx=c.categoryIdx inner join Store s on sc.storeIdx=s.storeIdx where c.categoryIdx =?",
                (rs,rowNum)-> new GetStoreCategoryRes(
                        rs.getInt("categoryIdx"),
                        rs.getString("categoryName"),
                        rs.getInt("storeIdx"),
                        rs.getString("storeName")
                ),categoryIdx

        );
    }

    public int checkCategory(String categoryName) {
        String checkCategoryQuery = "select exists(select categoryIdx from Category where categoryName = ?)";
        String checkCategoryParams = categoryName;
        return this.jdbcTemplate.queryForObject(checkCategoryQuery,
                int.class,
                checkCategoryParams);
    }

//---------음식점 등록 관련 함수--------------
    public int checkStoreName(String storeName) {
        String checkStoreNameQuery = "select exists (select storeName from Store where storeName = ?)";
        String checkStoreNameParams =storeName;
        return this.jdbcTemplate.queryForObject(checkStoreNameQuery,
                int.class,checkStoreNameParams);
    }
    /**
     * 음식점 등록
     * @param postStoreReq
     * @return
     */
    public int createStore(PostStoreReq postStoreReq) {
        String createStoreQuery = "insert into Store (storeName,storeInfo,minOrderPrice,deliveryTip,storePassword) VALUES (?,?,?,?,?)";
        Object[] createStoreParams = new Object[]{postStoreReq.getStoreName(),postStoreReq.getStoreInfo(), postStoreReq.getMinOrderPrice(),postStoreReq.getDeliveryTip(),postStoreReq.getStorePassword()};
        this.jdbcTemplate.update(createStoreQuery,createStoreParams);
        String lastInsertQuery = "select last_insert_id()";//가장 마지막에 삽입된(생성된)id값을 가져온다.
        return this.jdbcTemplate.queryForObject(lastInsertQuery,int.class);
    }


}

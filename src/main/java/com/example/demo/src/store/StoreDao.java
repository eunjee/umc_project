package com.example.demo.src.store;

import com.example.demo.src.likeStore.model.PostLikeStoreReq;
import com.example.demo.src.store.model.*;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.config.Constant.recordsPerPage;

@Repository
public class StoreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetStoreRes> getStores(int pageNum) {
        int offset = pageNum*recordsPerPage;
        Object[] params = new Object[]{recordsPerPage,offset};
        String getStoresQuery = "select * from Store limit ? offset ?"; //User 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
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
                        ),params
                );
    }

    public List<GetStoreCategoryRes> getStoresByCategory(String categoryName,int lastIdx) {
        //categoryIdx 받기
        String query = "select categoryIdx from Category where categoryName=?";
        String param = categoryName;
        int categoryIdx = this.jdbcTemplate.queryForObject(query,int.class,param);
        Object[] storeParams = new Object[]{categoryIdx,recordsPerPage,lastIdx};
        //store 목록 받기
        return this.jdbcTemplate.query("select * from Category c inner join StoreCategory sc on sc.categoryIdx=c.categoryIdx inner join Store s on sc.storeIdx=s.storeIdx where c.categoryIdx =? limit ? offset ?",
                (rs,rowNum)-> new GetStoreCategoryRes(
                        rs.getInt("categoryIdx"),
                        rs.getString("categoryName"),
                        rs.getInt("storeIdx"),
                        rs.getString("storeName")
                ),storeParams

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

    //이름 가져오기
    public String getStoreName(int storeIdx) {
        return this.jdbcTemplate.queryForObject("select storeName from Store where storeIdx=?",String.class,storeIdx);
    }

    public int addLikeStoreCount(PostLikeStoreReq postLikeStoreReq) {
        String addLikeCountQuery = "update Store set likeCount=likeCount+1, updateAt=CURRENT_TIMESTAMP() where storeName = ? "; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.

        return this.jdbcTemplate.update(addLikeCountQuery, postLikeStoreReq.getStoreName());    }

    public int minusLikeStoreCount(PostLikeStoreReq postLikeStoreReq) {
        String addLikeCountQuery = "update Store set likeCount=likeCount-1, updateAt=CURRENT_TIMESTAMP() where storeName = ? "; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.

        return this.jdbcTemplate.update(addLikeCountQuery, postLikeStoreReq.getStoreName());
    }

//    public int checkLastIdx(String categoryName, int lastIdx) {
//        String checkStoreNameQuery = "select exists (select storeName from Store where storeIdx = ?)";
//        return this.jdbcTemplate.queryForObject(checkStoreNameQuery,
//                int.class,lastIdx);
//    }

    public int hasNextRecord(String categoryName, int lastIdx) {
        String checkStoreQuery = "select count(*) (select *\n" +
                "from Store\n" +
                "inner join StoreCategory SC on Store.storeIdx = SC.storeIdx\n" +
                "inner join Category C on SC.categoryIdx = C.categoryIdx\n" +
                "where categoryName=? \n" +
                "orders limit ? offset ?)";
        Object[] params=new Object[]{categoryName,recordsPerPage,lastIdx};
        return this.jdbcTemplate.queryForObject(checkStoreQuery,
                int.class,params);
    }

    public int hasNextRecord(int pageNum) {
        int storeIdx= recordsPerPage*pageNum;
        String checkStoreQuery = "select count(*) (select storeName from Store orders limit ? offset ?)";
        Object[] params= new Object[]{storeIdx,recordsPerPage};
        return this.jdbcTemplate.queryForObject(checkStoreQuery,
                int.class,params);
    }
}

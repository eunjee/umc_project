package com.example.demo.src.category;

import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.src.category.model.PostCategoryStoreReq;
import com.example.demo.src.category.model.PostCategoryStoreRes;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CategoryDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetCategoryRes> getCategories() {

        String getUsersQuery = "select * from Category"; //User 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
        return this.jdbcTemplate.query(getUsersQuery,
                (rs, rowNum) -> new GetCategoryRes(
                        rs.getInt("categoryIdx"),
                        rs.getString("categoryName")) // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
        );
    }

    public PostCategoryStoreRes addStoreCategory(PostCategoryStoreReq postCSReq) {

        String createStoreCategoryQuery = "insert into StoreCategory (storeIdx,categoryIdx) VALUES (?,?)";
        Object[] createStoreCategoryParams = new Object[]{postCSReq.getStoreIdx(),postCSReq.getCategoryIdx()};
        this.jdbcTemplate.update(createStoreCategoryQuery,createStoreCategoryParams);

        String lastInsertQuery = "select last_insert_id()";//가장 마지막에 삽입된(생성된)id값을 가져온다.
        int storeIdx = this.jdbcTemplate.queryForObject(lastInsertQuery,int.class);
        return new PostCategoryStoreRes(storeIdx);
    }

    public int checkCategory(int categoryIdx) {
        String checkCategoryQuery = "select exists (select categoryIdx from Category where categoryIdx = ?)";
        int checkCategoryParams = categoryIdx;
        return this.jdbcTemplate.queryForObject(checkCategoryQuery,
                int.class,checkCategoryParams);
    }
}

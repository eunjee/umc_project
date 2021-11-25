package com.example.demo.src.menu;

import com.example.demo.src.menu.model.GetStoreMenuRes;
import com.example.demo.src.menu.model.PatchMenuReq;
import com.example.demo.src.menu.model.PostMenuReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MenuDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetStoreMenuRes> getStoreMenu(int storeIdx) {
        return this.jdbcTemplate.query("select menuIdx, menuName, price " +
                        "from Menu where storeIdx =?",
                (rs,rowNum)-> new GetStoreMenuRes(
                        rs.getInt("menuIdx"),
                        rs.getString("menuName"),
                        rs.getInt("price")
                ),
                storeIdx
        );
    }

    public int addStoreMenu(int storeIdx, PostMenuReq postMenuReq) {
        String createMenuQuery = "insert into Menu (menuName,menuCategory,price,menuFlag,menuInfo) VALUES (?,?,?,?,?)";
        Object[] createMenuParams = new Object[]{postMenuReq.getMenuName(),postMenuReq.getMenuCategory(), postMenuReq.getPrice(),postMenuReq.getMenuFlag(),postMenuReq.getMenuFlag()};
        this.jdbcTemplate.update(createMenuQuery,createMenuParams);
        String lastInsertQuery = "select last_insert_id()";//가장 마지막에 삽입된(생성된)id값을 가져온다.
        return this.jdbcTemplate.queryForObject(lastInsertQuery,int.class);
    }

    public int deleteMenu(int storeIdx, int menuIdx) {
        String modifyMenuQuery = "update Menu set updateAt=CURRENT_TIMESTAMP(),availableFlag='delete' where storeIdx = ? and menuIdx=?"; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        Object[] params = new Object[]{storeIdx,menuIdx};
        return this.jdbcTemplate.update(modifyMenuQuery, params);
    }

    public int modifyMenuPrice(int storeIdx, int menuIdx, PatchMenuReq patchMenuReq) {
        String modifyMenuQuery = "update Menu set updateAt=CURRENT_TIMESTAMP(),price=? where storeIdx = ? and menuIdx=?"; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        Object[] params = new Object[]{patchMenuReq.getPrice(),storeIdx,menuIdx};
        return this.jdbcTemplate.update(modifyMenuQuery, params);
    }
}

package com.example.demo.src.basket;

import com.example.demo.src.basket.model.GetBasketRes;
import com.example.demo.src.basket.model.PostBasketReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Arrays;

@Repository
public class BasketDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 삭제가 안된 장바구니 && 가장 최근의 장바구니만을 가지고 온다.
     * @param userIdx
     * @return
     */
    public GetBasketRes getRecentBasket(int userIdx) {

        return this.jdbcTemplate.queryForObject("select b.basketIdx, b.userIdx,b.storeIdx,s.storeName,group_concat(m.menuName) as menuName,sum(b.amount) as amount,sum(m.price*b.amount) as totalPrice \n" +
                        "from Basket b\n" +
                        "inner join Menu m on m.menuIdx = b.menuIdx\n" +
                        "inner join Store s on s.storeIdx = b.storeIdx\n" +
                        "where b.userIdx = ? and deleteFlag='n' \n"+
                        "group by b.userIdx;",
                (rs, rowNum) -> new GetBasketRes(
                        rs.getInt("b.basketIdx"),
                        rs.getInt("b.userIdx"),
                        rs.getInt("b.storeIdx"),
                        rs.getString("s.storeName"),
                        Arrays.asList(rs.getString("menuName").split(",")),
                        rs.getInt("amount"),
                        rs.getInt("totalPrice")
                ),
                userIdx
        );
    }

    public int addBasket(int userIdx, int storeIdx,PostBasketReq postBasketReq) {
        String createStoreQuery = "insert into Basket(userIdx,storeIdx,menuIdx,amount) VALUES (?,?,(select menuIdx from Menu where storeIdx=? and menuName=?),?)";
        Object[] createStoreParams = new Object[]{userIdx,storeIdx,storeIdx,postBasketReq.getMenuName(),postBasketReq.getAmount()};
        this.jdbcTemplate.update(createStoreQuery,createStoreParams);
        String lastInsertQuery = "select last_insert_id()";//가장 마지막에 삽입된(생성된)id값을 가져온다.
        return this.jdbcTemplate.queryForObject(lastInsertQuery,int.class);
    }

    //이전 장바구니 모두 삭제
    public int deleteAllBasket(int userIdx) {
        String modifyUserQuery = "update Basket set updateAt=CURRENT_TIMESTAMP(),deleteFlag='y' where userIdx=?  "; // 해당 userIdx를 만족하는 User를 해당 nickname으로 변경한다.
        return this.jdbcTemplate.update(modifyUserQuery, userIdx);
    }
}

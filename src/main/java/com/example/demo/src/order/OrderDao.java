package com.example.demo.src.order;

import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.MenuDTO;
import com.example.demo.src.order.model.PostOrderReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetOrderRes> getOrders(int userIdx) {
        return this.jdbcTemplate.query("select * \n" +
                        "from Orders o \n" +
                        "inner join Store s on s.storeIdx = o.storeIdx \n" +
                        "inner join OrderMenu om on o.orderIdx = om.orderIdx \n" +
                        "inner join Menu m on om.menuIdx = m.menuIdx \n" +
                        "where o.userIdx = ?",
                (rs, rowNum) -> new GetOrderRes(
                        rs.getInt("orderIdx"),
                        rs.getString("storeName"),
                        rs.getString("menuName"),
                        rs.getInt("totalPrice"),
                        rs.getInt("totalAmount"),
                        rs.getString("payment")
                ),
                userIdx
        );
    }

    public int createOrder(int userIdx, int storeIdx) {
        String createStoreQuery = "insert into Order(userIdx,storeIdx) VALUES (?,?)";
        Object[] params = new Object[]{userIdx, storeIdx};
        this.jdbcTemplate.update(createStoreQuery, params);
        String lastInsertQuery = "select last_insert_id()";//가장 마지막에 삽입된(생성된)id값을 가져온다.
        return this.jdbcTemplate.queryForObject(lastInsertQuery, int.class);
    }


    public void createOrderMenu(int orderIdx, List<MenuDTO> menuList) {
        //삽입
        String createStoreQuery = "insert into OrderMenu(orderIdx,menuIdx,amount) VALUES (?,(select menuIdx from Menu where menuName=?),?)";
        Object[] params = new Object[]{orderIdx, '0',0};
        for (MenuDTO dto : menuList) {
            params[1] = dto.getMenuName();
            params[2] =dto.getAmount();
            this.jdbcTemplate.update(createStoreQuery, params);
        }
    }

    public int updatePayment(int orderIdx, PostOrderReq postOrderReq) {
        String createStoreQuery = "update Orders set ownerComment=?, deliveryComment=?, payment =?,updateAt = CURRENT_TIMESTAMP() where orderIdx=? and deleteFlag='n'";
        Object[] params = new Object[]{postOrderReq.getOwnerComment(),postOrderReq.getDeliveryComment(),postOrderReq.getPayment(),orderIdx};
        return this.jdbcTemplate.update(createStoreQuery, params);
    }
}

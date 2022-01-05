package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.MenuDTO;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderProvider orderProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OrderService(OrderDao orderDao, OrderProvider orderProvider, JwtService jwtService) {
        this.orderDao = orderDao;
        this.orderProvider = orderProvider;
        this.jwtService = jwtService;
    }

        @Transactional
        public PostOrderRes createOrder(int userIdx, PostOrderReq postOrderReq) throws BaseException {

            try {
                //오더 테이블 생성
                int storeIdx = postOrderReq.getStoreIdx();
                int orderIdx = orderDao.createOrder(userIdx,storeIdx);
                //OrderMenu 생성
                List<MenuDTO> menuList = postOrderReq.getMenuList();
                orderDao.createOrderMenu(orderIdx,menuList);

                //결제방식추가
                if(orderDao.updatePayment(orderIdx,postOrderReq)==0){
                    throw new BaseException(DATABASE_ERROR);
                }

                return new PostOrderRes(orderIdx);
            } catch (Exception e) {
                System.out.println("StoreService.createStore");
                throw new BaseException(DATABASE_ERROR);
            }
        }

}

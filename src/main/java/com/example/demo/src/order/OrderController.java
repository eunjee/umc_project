package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/users")
public class OrderController {
    private final OrderService orderService;
    private final OrderProvider orderProvider;
    private final JwtService jwtService;

    @Autowired
    public OrderController(OrderService orderService, OrderProvider orderProvider, JwtService jwtService) {
        this.orderService = orderService;
        this.orderProvider = orderProvider;
        this.jwtService = jwtService;
    }
    
    //주문내역 조회
    @ResponseBody
    @GetMapping("/{userIdx}/orders")
    public BaseResponse<List<GetOrderRes>> getOrders(@PathVariable("userIdx") int userIdx) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        // Get Order
        try {
            List<GetOrderRes> getOrderRes = orderProvider.getOrders(userIdx);
            //없으면 에러로 처리 안하고 텍스트를 내보냄
            if(getOrderRes==null){
                String sentence = "텅";
                return new BaseResponse(sentence);
            }
            return new BaseResponse<>(getOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //주문하기
    @ResponseBody
    @PostMapping("/{userIdx}/order")
    public BaseResponse<PostOrderRes> createOrder(@PathVariable("userIdx") int userIdx, @RequestBody PostOrderReq postOrderReq) throws BaseException {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        // Get Order
        try {
            PostOrderRes postOrderRes = orderService.createOrder(userIdx,postOrderReq);
            return new BaseResponse<>(postOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    
}

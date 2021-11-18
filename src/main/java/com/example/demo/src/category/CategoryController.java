package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.src.category.model.PostCategoryStoreReq;
import com.example.demo.src.category.model.PostCategoryStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.NOT_EXIST_STORE_NAME;
import static com.example.demo.config.BaseResponseStatus.POST_STORE_EMPTY_STORE_NAME;

@RestController
@RequestMapping("/app/categories")
public class CategoryController {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired  // 객체 생성을 스프링에서 자동으로 생성해주는 역할. 주입하려 하는 객체의 타입이 일치하는 객체를 자동으로 주입한다.
    // IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입)에 대한 공부하시면, 더 깊이 있게 Spring에 대한 공부를 하실 수 있을 겁니다!(일단은 모르고 넘어가셔도 무방합니다.)
    // IoC 간단설명,  메소드나 객체의 호출작업을 개발자가 결정하는 것이 아니라, 외부에서 결정되는 것을 의미
    // DI 간단설명, 객체를 직접 생성하는 게 아니라 외부에서 생성한 후 주입 시켜주는 방식
    private final CategoryProvider categoryProvider;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    public CategoryController(CategoryProvider categoryProvider, CategoryService categoryService, JwtService jwtService) {
        this.categoryProvider = categoryProvider;
        this.categoryService = categoryService;
        this.jwtService = jwtService;
    }

    @GetMapping("")
    public BaseResponse<GetCategoryRes> getCatgories(){
        //따로 문법적 검증할 필요 없음
        try {
            List<GetCategoryRes> getCategoryRes = categoryProvider.getCategories();
            return new BaseResponse(getCategoryRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 1. 카테고리가 목록에 있는지 확인 - provider
     * 2. 음식점이 있는지 확인 - provider
     * @param categoryIdx
     * @param postCSReq
     * @return
     */
    @ResponseBody
    @PostMapping("/{categoryIdx}")
    public BaseResponse<PostCategoryStoreRes> addStoreCategory(@PathVariable int categoryIdx, @RequestBody PostCategoryStoreReq postCSReq){
        //음식점 이름이 있는지 확인
        if(postCSReq.getStoreName()==null){
            return new BaseResponse<>(NOT_EXIST_STORE_NAME);
        }
        try {
            PostCategoryStoreRes postCSRes = categoryService.addStoreCategory(postCSReq);
            return new BaseResponse<>(postCSRes);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}

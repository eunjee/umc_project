package com.example.demo.utils.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FCMController {
    private final FirebaseInit init;

    @GetMapping("/app/fcmTest")
    public String fcmTest(){
        init.init();
        return "test";
    }
}

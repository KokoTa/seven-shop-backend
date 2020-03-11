package com.example.shop.api.v1;

import com.example.shop.exception.http.ForbiddenException;
import com.example.shop.other.A;
import com.example.shop.other.B;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/v1")
public class BannerController {

    @Autowired
    private A a;

    @GetMapping("/test")
    String test() throws Exception {
        B b = a.getB();
        b.say();
        throw new ForbiddenException(10000);
    }
}

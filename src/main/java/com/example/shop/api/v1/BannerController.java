package com.example.shop.api.v1;

import com.example.shop.dto.User;
import com.example.shop.exception.http.ForbiddenException;
import com.example.shop.other.testClass.A;
import com.example.shop.other.testClass.B;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import java.util.Map;

@RestController
@RequestMapping("/banner")
@Validated
public class BannerController {

    @Autowired
    private A a;

    @GetMapping("/testException")
    String testException() throws Exception {
        B b = a.getB();
        b.say();
        throw new ForbiddenException(10000);
    }

    @GetMapping("/testParams/{userId}")
    Integer testParamByPath(@PathVariable(name = "userId") Integer id) {
        return id;
    }

    @GetMapping("/testParams")
    Integer testParamByQuery(@RequestParam(name = "userId") Integer id) {
        return id;
    }

    @PostMapping("/testParams")
    User testParamByBody(@RequestBody @Validated User user) { return user; }

}

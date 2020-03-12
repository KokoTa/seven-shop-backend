package com.example.shop.api.v1;

import com.example.shop.dto.User;
import com.example.shop.exception.http.ForbiddenException;
import com.example.shop.other.testClass.A;
import com.example.shop.other.testClass.B;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    Integer testParamByPath(@PathVariable(name = "userId") @Min(10) Integer id) {
        return id;
    }

    @GetMapping("/testParams")
    String testParamByQuery(
            @RequestParam(name = "userId") @Min(10) Integer id,
            @RequestParam(name = "userName") @Length(min = 2) String name
    ) {
        return id + " " + name;
    }

    @PostMapping("/testParams")
    User testParamByBody(@RequestBody @Validated User user) { return user; }

}

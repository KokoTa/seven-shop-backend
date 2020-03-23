package com.example.shop.api.v1;

import com.example.shop.core.enumeration.LoginType;
import com.example.shop.dto.TokenDTO;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.service.AuthenticationService;
import com.example.shop.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/token")
@RestController
public class TokenController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("")
    public Map<String, Object> getToken(@RequestBody @Validated TokenDTO tokenDTO) {
        LoginType loginType = tokenDTO.getType();
        String token = null;
        Map<String, Object> map = new HashMap<>();

        switch (loginType) {
            case WX:
                token = authenticationService.code2session(tokenDTO.getAccount());
                break;
            case EMAIL:
                // 数据库里去找用户
                break;
            default:
                throw new NotFoundException(10003);
        }

        map.put("token", token);

        return map;
    }

    @PostMapping("/verify")
    public Map<String, Boolean> tokenVerify(@RequestBody Map<String, String> data) {
        Map<String, Boolean> map = new HashMap<>();
        Boolean valid = JwtToken.verify(data.get("token"));
        map.put("is_valid", valid);
        return map;
    }
}

package com.example.shop.api.v1;

import com.example.shop.core.enumeration.LoginType;
import com.example.shop.dto.TokenDTO;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/token")
@RestController
public class TokenController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("")
    public TokenDTO getToken(
            @RequestBody @Validated TokenDTO tokenDTO
    ) {

        LoginType loginType = tokenDTO.getType();
        switch (loginType) {
            case WX:
                authenticationService.code2Wx(tokenDTO.getAccount());
                break;
            case EMAIL:
                // 数据库里去找用户
                break;
            default:
                throw new NotFoundException(10003);
        }


        return null;
    }
}

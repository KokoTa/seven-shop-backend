package com.example.shop.dto;

import com.example.shop.core.enumeration.LoginType;
import com.example.shop.validators.TokenPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TokenDTO {

    @NotBlank(message = "account 不允许为空")
    private String account;

    @TokenPassword(message = "{token.password}")
    private String password;

    private LoginType type;
}

package com.example.shop.dto;

import com.example.shop.validators.PasswordEqual;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@PasswordEqual
public class UserDTO {
    private String name;

    @Range(min = 10, max = 100)
    private Integer age;

    private String password1;
    private String password2;
}


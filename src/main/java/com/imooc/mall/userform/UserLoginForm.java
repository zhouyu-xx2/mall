package com.imooc.mall.userform;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}

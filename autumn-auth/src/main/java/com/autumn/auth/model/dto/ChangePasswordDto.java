package com.autumn.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author autumn
 * @desc 修改密码
 * @date 2025/5/1 8:50
 **/
@Data
public class ChangePasswordDto {

    private Long userId;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPwd;
}

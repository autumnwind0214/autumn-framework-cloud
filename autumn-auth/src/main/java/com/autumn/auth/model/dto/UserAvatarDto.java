package com.autumn.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author autumn
 * @desc 头像上传
 * @date 2025年05月03日
 */
@Data
public class UserAvatarDto {

    @NotNull
    Long userId;

    @NotBlank
    String reviewUrl;
}

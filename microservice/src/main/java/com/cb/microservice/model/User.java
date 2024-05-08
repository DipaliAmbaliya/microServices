package com.cb.microservice.model;

import com.cb.microservice.common.UserStatus;
import com.cb.microservice.validation.ValidPassword;
import com.cb.microservice.validation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotEmpty
    @NotBlank(message = "username is mandatory")
    private String username;
    private UserStatus userStatus;
    @NotEmpty
    @ValidPhoneNumber
    private String cellPhoneNumber;
    @NotEmpty
    @ValidPassword
    private String password;
    private String productId;
    private String productName;
}

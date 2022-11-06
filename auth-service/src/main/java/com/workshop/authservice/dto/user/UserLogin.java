package com.workshop.authservice.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.workshop.authservice.configuration.DtoConfiguration;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLogin {
    @NotNull(
            groups = {DtoConfiguration.OnRequest.class, DtoConfiguration.OnResponse.class},
            message = "login field is not provided!"
    )
    private String login;

    @NotNull(
            groups = DtoConfiguration.OnRequest.class,
            message = "password field is not provided!"
    )
    private String password;

    @NotNull(
            groups = {DtoConfiguration.OnResponse.class}
    )
    private String accessToken;

    @NotNull(
            groups = {DtoConfiguration.OnResponse.class, DtoConfiguration.OnRefreshToken.class}
    )
    private String refreshToken;

}

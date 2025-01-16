package com.kosa.polarbookshop.orderservice.config;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotNull;

//카탈로그 서비스 URL에 대한 사용자 지정 속성 정의 
@ConfigurationProperties(prefix = "polar")
public record ClientPropertis(
        @NotNull URI catalogServiceUrl) {
}

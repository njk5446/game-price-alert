package com.dymun.pricealert.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "itad.api")
public class ItadProperties {
    private String key;
    private String baseUrl;
}

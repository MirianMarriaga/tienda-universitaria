package edu.unimagdalena.tienda_universitaria.security.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
@Getter @Setter
public class JwtProperties {
    private String secret;
    private long expirationSeconds;
}

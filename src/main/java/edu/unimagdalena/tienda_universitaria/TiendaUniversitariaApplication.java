package edu.unimagdalena.tienda_universitaria;

import edu.unimagdalena.tienda_universitaria.security.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class TiendaUniversitariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiendaUniversitariaApplication.class, args);
	}

}

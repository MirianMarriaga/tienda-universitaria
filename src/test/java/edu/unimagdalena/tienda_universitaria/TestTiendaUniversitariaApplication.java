package edu.unimagdalena.tienda_universitaria;

import org.springframework.boot.SpringApplication;

public class TestTiendaUniversitariaApplication {

	public static void main(String[] args) {
		SpringApplication.from(TiendaUniversitariaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

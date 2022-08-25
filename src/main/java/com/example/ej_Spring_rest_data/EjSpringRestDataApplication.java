package com.example.ej_Spring_rest_data;

import com.example.ej_Spring_rest_data.Entities.Laptop;
import com.example.ej_Spring_rest_data.Repositories.LaptopRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EjSpringRestDataApplication {

	public static void main(String[] args) {
		ApplicationContext context =  SpringApplication.run(EjSpringRestDataApplication.class, args);
		LaptopRepository repository = context.getBean(LaptopRepository.class);

		//Creo el primer laptop a mano, para probar con postman
		Laptop laptop_ej = new Laptop(null, "HP", "s512i5hd15.6", 799d, 16, 512);
		repository.save(laptop_ej);
	}

}

package school.sptech.emprestimo_service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class EmprestimoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmprestimoServiceApplication.class, args);
	}

}

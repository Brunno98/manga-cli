package br.com.brunno.mangacli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MangaCliApplication {

	public static void main(String[] args) {
		SpringApplication.run(MangaCliApplication.class, args);
	}

}

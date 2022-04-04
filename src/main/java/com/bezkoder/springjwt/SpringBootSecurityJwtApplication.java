package com.bezkoder.springjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class SpringBootSecurityJwtApplication {
	@PostConstruct
	public  void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Bangkok"));
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}

}

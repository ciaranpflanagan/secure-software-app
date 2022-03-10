package com.securesoftware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ComponentScan({"com.securesoftware.repository", "com.securesoftware.controller", "com.securesoftware.app", "com.securesoftware.model", "com.securesoftware.exception"})
public class AppApplication extends SpringBootServletInitializer {
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AppApplication.class, args);
	}
}

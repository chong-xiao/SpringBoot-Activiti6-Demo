package com.cazj;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CazjApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CazjApplication.class);
    }

    @RequestMapping(value = {"/",""})
    public String helloboot(){

        return "hello boot !!" ;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(CazjApplication.class, args);
	}

}

package com.seletivo.servidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class ServidorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServidorApplication.class, args);
    }
} 

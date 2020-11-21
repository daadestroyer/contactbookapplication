package com.boot.smartcontactapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;


@SpringBootApplication

public class SmartcontactapplicationApplication {

    public static void main(String[] args) {

        SpringApplication.run(SmartcontactapplicationApplication.class, args);
        System.out.println("-------------");
        System.out.println("STARTED");
        System.out.println("-------------");
    }

}

package br.com.wallace.softplanchallenge.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@ComponentScan("br.com.wallace.softplanchallenge")
public class DataService {
    public static void main(String[] args) {
        SpringApplication.run(DataService.class, args);
    }
}


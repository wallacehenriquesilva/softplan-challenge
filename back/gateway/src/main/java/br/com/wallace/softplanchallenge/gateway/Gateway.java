package br.com.wallace.softplanchallenge.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@RibbonClients
@EnableZuulProxy
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
public class Gateway {
    public static void main(String[] args) {
        SpringApplication.run(Gateway.class, args);
    }
}
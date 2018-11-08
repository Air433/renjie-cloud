package com.renjie.cloud.renjie.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class RenjieUserApplication {

  public static void main(String[] args) {
    SpringApplication.run(RenjieUserApplication.class, args);
  }
}

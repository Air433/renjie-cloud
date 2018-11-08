package com.renjie.cloud.renjie.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class RenjieOrderApplication {

  public static void main(String[] args) {
    SpringApplication.run(RenjieOrderApplication.class, args);
  }
}

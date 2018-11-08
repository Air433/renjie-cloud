package com.renjie.cloud.renjieeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RenjieEurekaApplication {

  public static void main(String[] args) {
    SpringApplication.run(RenjieEurekaApplication.class, args);
  }
}

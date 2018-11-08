package com.renjie.cloud.renjieorder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ouyanggang on 2018/11/8.
 */
@RestController
public class OrderController {

  @GetMapping("/p")
  public String test(){
    return "苏杉杉";
  }
}

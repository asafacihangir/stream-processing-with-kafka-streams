package com.phoenix.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {



  @GetMapping("/message")
  public String getMessage(){
    return "Hello World";
  }





}

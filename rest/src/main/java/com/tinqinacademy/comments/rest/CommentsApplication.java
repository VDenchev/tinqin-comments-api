package com.tinqinacademy.comments.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.tinqinacademy.comments"})
public class CommentsApplication {

  public static void main(String[] args) {
    SpringApplication.run(CommentsApplication.class, args);
  }
}

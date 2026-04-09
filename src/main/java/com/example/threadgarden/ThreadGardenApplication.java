package com.example.threadgarden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ThreadGardenApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThreadGardenApplication.class, args);
    }
}

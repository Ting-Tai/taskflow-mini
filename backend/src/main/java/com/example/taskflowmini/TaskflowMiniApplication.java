package com.example.taskflowmini;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.taskflowmini.mapper")
public class TaskflowMiniApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskflowMiniApplication.class, args);
    }
}

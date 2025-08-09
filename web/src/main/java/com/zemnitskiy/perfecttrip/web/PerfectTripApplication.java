package com.zemnitskiy.perfecttrip.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.zemnitskiy.perfecttrip")
public class PerfectTripApplication {
    public static void main(String[] args) {
        SpringApplication.run(PerfectTripApplication.class, args);
    }
}

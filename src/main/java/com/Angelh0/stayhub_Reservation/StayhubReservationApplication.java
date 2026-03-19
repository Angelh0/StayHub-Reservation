package com.Angelh0.stayhub_Reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StayhubReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StayhubReservationApplication.class, args);
    }
}

package org.kolade.dronetech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DroneTechApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroneTechApplication.class, args);
    }
}

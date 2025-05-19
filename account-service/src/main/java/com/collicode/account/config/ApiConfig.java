package com.collicode.account.config;


import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.RepairResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.collicode.shared")
public class ApiConfig {

    @Bean
    public RepairResult repairFlyway(Flyway flyway) {
        // Repair Flyway's metadata table
        return flyway.repair();
    }
}

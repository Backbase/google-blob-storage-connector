package com.backbase.blob.storage.connector;

import com.backbase.audit.client.EnableAuditClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAuditClient
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
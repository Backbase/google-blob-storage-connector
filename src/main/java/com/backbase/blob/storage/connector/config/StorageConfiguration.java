package com.backbase.blob.storage.connector.config;

import com.backbase.buildingblocks.context.ContextScoped;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@ContextScoped
@Validated
@Data
@Configuration
@ConfigurationProperties(prefix = "backbase.content.connector.google")
public class StorageConfiguration {
    private String projectId;
    private String rootBucketName;
    private int timeoutInMinutes;
}

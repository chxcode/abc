package com.abc.cx.upload;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;



@Data
@Configuration
public class LocalUploadConfig {
    @Value("${upload.local.base-path}")
    String basePath;

    @Value("${upload.local.base-url}")
    String baseUrl;
}

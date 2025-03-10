package com.example.demo.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Configuration
public class FileStorageConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        
        // 设置临时文件目录
        String tempDir = System.getProperty("user.dir") + File.separator + "temp";
        File tempFolder = new File(tempDir);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
        factory.setLocation(tempDir);
        
        // 设置文件大小限制
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        
        return factory.createMultipartConfig();
    }
}
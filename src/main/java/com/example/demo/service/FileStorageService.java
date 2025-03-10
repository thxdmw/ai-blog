package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService() {
        this.rootLocation = Paths.get("uploaded-files");
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("无法初始化存储位置", e);
        }
    }

    public void store(MultipartFile file, String directory) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file");
        }

        Path targetLocation = rootLocation;
        if (directory != null && !directory.isEmpty()) {
            targetLocation = rootLocation.resolve(directory);
            Files.createDirectories(targetLocation);
        }

        // 规范化文件名并创建目标路径
        String fileName = Paths.get(file.getOriginalFilename())
            .normalize()
            .toFile()
            .getName();
        Path destinationFile = targetLocation.resolve(fileName);

        // 使用try-with-resources确保资源正确关闭
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public List<Map<String, Object>> getFileTree() {
        return getFileTreeFromDirectory(rootLocation.toFile(), "");
    }

    private List<Map<String, Object>> getFileTreeFromDirectory(File directory, String relativePath) {
        List<Map<String, Object>> fileTree = new ArrayList<>();
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                Map<String, Object> node = new HashMap<>();
                String path = relativePath.isEmpty() ? file.getName() : relativePath + "/" + file.getName();
                
                node.put("name", file.getName());
                node.put("path", path);
                node.put("isDirectory", file.isDirectory());
                
                if (file.isDirectory()) {
                    node.put("children", getFileTreeFromDirectory(file, path));
                } else {
                    node.put("type", getFileType(file.getName()));
                    node.put("size", file.length());
                }
                
                fileTree.add(node);
            }
        }
        
        return fileTree;
    }

    private String getFileType(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1).toLowerCase();
        }
        
        if (extension.matches("(jpg|jpeg|png|gif|bmp)")) {
            return "image";
        } else if (extension.matches("(pdf)")) {
            return "pdf";
        } else if (extension.matches("(txt|md|java|js|html|css|xml|json)")) {
            return "text";
        } else {
            return "other";
        }
    }

    public byte[] getFileContent(String filePath) throws IOException {
        Path path = rootLocation.resolve(filePath).normalize();
        
        // 验证文件是否在rootLocation目录下
        if (!path.startsWith(rootLocation)) {
            throw new IOException("无法访问此路径");
        }
        
        // 检查文件是否存在
        if (!Files.exists(path)) {
            throw new IOException("文件不存在");
        }
        
        // 确保是文件而不是目录
        if (!Files.isRegularFile(path)) {
            throw new IOException("不是一个有效的文件");
        }
        
        return Files.readAllBytes(path);
    }

    public String getFileMimeType(String filePath) throws IOException {
        Path path = rootLocation.resolve(filePath).normalize();
        
        // 验证文件是否在rootLocation目录下
        if (!path.startsWith(rootLocation)) {
            throw new IOException("无法访问此路径");
        }
        
        String fileName = path.getFileName().toString();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1).toLowerCase();
        }
        
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "json":
                return "application/json";
            case "xml":
                return "application/xml";
            default:
                return "application/octet-stream";
        }
    }
}
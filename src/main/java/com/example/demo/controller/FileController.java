package com.example.demo.controller;

import com.example.demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class FileController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public String index() {
        return "preview";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam(value = "directory", required = false) String directory,
            RedirectAttributes redirectAttributes) {
        try {
            fileStorageService.store(file, directory);
            redirectAttributes.addFlashAttribute("message", "文件上传成功: " + file.getOriginalFilename());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "文件上传失败: " + e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/files")
    @ResponseBody
    public List<Map<String, Object>> getFileTree() {
        return fileStorageService.getFileTree();
    }

    @GetMapping("/preview")
    public String previewPage() {
        return "preview";
    }
    
    @GetMapping("/preview/{filePath}")
    public ResponseEntity<byte[]> previewFile(@PathVariable String filePath) {
        System.out.println(filePath);
        try {
            byte[] fileContent = fileStorageService.getFileContent(filePath);
            String mimeType = fileStorageService.getFileMimeType(filePath);
            
            String filename = filePath.substring(filePath.lastIndexOf('/') + 1);
            // 对中文文件名进行URL编码，解决HTTP头部中文编码问题
            String encodedFilename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename(encodedFilename).build());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
package com.example.blackbirdlofi.controller;

import com.example.blackbirdlofi.service.firebase.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
public class FileUploadController {

    private final FirebaseStorageService storageService;

    @Autowired
    public FileUploadController(FirebaseStorageService storageService) {
        this.storageService = storageService;
    }

    // 파일 업로드 화면
    @GetMapping("/usr/home/uploadFormTest")
    public String showUploadForm(Model model) {
        List<String> files = storageService.listAllFiles();
        model.addAttribute("files", files);
        return "usr/home/uploadFormTest";  // 업로드 폼 JSP 페이지를 반환
    }

    // 파일 업로드 처리
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String fileUrl = storageService.uploadFile(file);
            model.addAttribute("message", "File uploaded successfully");
            model.addAttribute("fileUrl", fileUrl);
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        return "usr/home/uploadResultTest";  // 업로드 결과를 보여줄 JSP 페이지
    }

    // 업로드된 파일 목록 조회
    @GetMapping("/uploadedFiles")
    public String listUploadedFiles(Model model) {
        List<String> files = storageService.listAllFiles();
        model.addAttribute("files", files);
        return "usr/home/uploadFormTest";  // 파일 목록을 보여줄 JSP 페이지
    }

    // 파일 다운로드 처리
    @GetMapping("/download")
    public void downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        try {
            byte[] fileBytes = storageService.downloadFile(fileName);
            response.setContentType("audio/wav");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            OutputStream os = response.getOutputStream();
            os.write(fileBytes);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
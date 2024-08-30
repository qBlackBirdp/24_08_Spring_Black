package com.example.blackbirdlofi.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/usr/home/test")
    public String showTestPage() {
        return "usr/home/test";  // "WEB-INF/jsp/usr/home/test.jsp" 페이지를 렌더링
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest request) {
        if (file.isEmpty()) {
            model.addAttribute("message", "파일을 선택해주세요.");
            return "usr/home/test";
        }

        try {
            String uploadDirPath = request.getServletContext().getRealPath("/") + "uploads/";
            File uploadDir = new File(uploadDirPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            File dest = new File(uploadDirPath + fileName);
            file.transferTo(dest);

            String fileUrl = request.getContextPath() + "/uploads/" + fileName;
            model.addAttribute("fileUrl", fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "파일 업로드 중 오류가 발생했습니다.");
            return "usr/home/test";
        }

        return "usr/home/test";
    }
}

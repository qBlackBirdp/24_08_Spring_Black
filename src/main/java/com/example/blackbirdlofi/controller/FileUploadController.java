package com.example.blackbirdlofi.controller;

import com.example.blackbirdlofi.JPAentity.Sample;
import com.example.blackbirdlofi.service.SampleService;
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
    private final SampleService sampleService;

    @Autowired
    public FileUploadController(FirebaseStorageService storageService, SampleService sampleService) {
        this.storageService = storageService;
        this.sampleService = sampleService;
    }

    // 파일 업로드 화면
    @GetMapping("/usr/home/uploadForm")
    public String showUploadForm(Model model) {
        List<String> files = storageService.listAllFiles();
        model.addAttribute("files", files);
        return "usr/home/uploadForm";  // 업로드 폼 JSP 페이지를 반환
    }

    // 파일 업로드 처리
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("bpm") int bpm,
                             @RequestParam("genres") String genres,
                             @RequestParam("oneShot") boolean oneShot,
                             Model model) {
        try {
            String fileUrl = storageService.uploadFile(file);

            Sample sample = new Sample();
            sample.setSName(file.getOriginalFilename());
            sample.setBpm(bpm);
            sample.setGenres(genres);
            sample.setOneShot(oneShot);
            sample.setUrl(fileUrl);

            // DB에 저장
            sampleService.saveSample(sample);

            model.addAttribute("message", "File uploaded successfully");
            model.addAttribute("fileUrl", fileUrl);
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        return "usr/home/uploadResult";
    }

    // 업로드된 파일 목록 조회
    @GetMapping("/uploadedFiles")
    public String listUploadedFiles(Model model) {
        List<String> files = storageService.listAllFiles();
        model.addAttribute("files", files);
        return "usr/home/uploadForm";  // 파일 목록을 보여줄 JSP 페이지
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
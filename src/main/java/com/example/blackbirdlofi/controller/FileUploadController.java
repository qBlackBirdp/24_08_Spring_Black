package com.example.blackbirdlofi.controller;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.JPAentity.Sample;
import com.example.blackbirdlofi.service.MemberService;
import com.example.blackbirdlofi.service.SampleService;
import com.example.blackbirdlofi.service.firebase.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    private final MemberService memberService;

    @Autowired
    public FileUploadController(FirebaseStorageService storageService, SampleService sampleService, MemberService memberService) {
        this.storageService = storageService;
        this.sampleService = sampleService;
        this.memberService = memberService;
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
                             @RequestParam(name = "bpm", required = false, defaultValue = "0") int bpm,
                             @RequestParam("genres") String genres,
                             @RequestParam("isOneShot") boolean isOneShot,
                             Authentication authentication,
                             Model model) {
        System.err.println("==================파일 컨트롤러 작동=====================");
        // OAuth2User로 캐스팅하여 로그인된 사용자 정보 가져오기
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");  // Google 또는 Spotify에서 이메일 가져오기
            System.out.println("OAuth2 로그인 사용자 이메일: " + email);

            // 이메일을 기반으로 Member 객체 찾기
            Member member = memberService.getMemberByEmail(email);  // memberService를 통해 DB에서 사용자 정보 가져오기
            int userId = member.getId();  // 사용자 ID 가져오기
            try {
                String fileUrl = storageService.uploadFile(file);
                System.out.println("업로드된 파일 URL: " + fileUrl);

                Sample sample = new Sample();
                sample.setUserId(userId);
                sample.setSName(file.getOriginalFilename());
                sample.setBpm(bpm);
                sample.setGenres(genres);
                sample.setIsOneShot(isOneShot);
                sample.setUrl(fileUrl);

                System.out.println("생성된 샘플: " + sample.toString());

                // DB에 저장
                sampleService.saveSample(sample);
                System.out.println("======== 샘플이 DB에 성공적으로 저장되었습니다. ==========");

                model.addAttribute("message", "File uploaded successfully");
                model.addAttribute("fileUrl", fileUrl);
            } catch (IOException e) {
                System.err.println("!!!!파일 업로드 실패!!!! : " + e.getMessage());
                model.addAttribute("message", "Failed to upload file: " + e.getMessage());
            }
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
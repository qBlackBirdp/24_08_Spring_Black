package com.example.blackbirdlofi.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FirebaseStorageService {

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Bucket bucket = StorageClient.getInstance().bucket();
        bucket.create(fileName, file.getBytes(), file.getContentType());
        return fileName; // 파일 이름을 반환 (필요시 전체 URL 반환 가능)
    }

    public byte[] downloadFile(String fileName) {
        Blob blob = StorageClient.getInstance().bucket().get(fileName);
        return blob.getContent();
    }

    public List<String> listAllFiles() {
        List<String> fileNames = new ArrayList<>();
        Bucket bucket = StorageClient.getInstance().bucket();

        for (Blob blob : bucket.list().iterateAll()) {
            fileNames.add(blob.getName());
        }

        return fileNames;
    }
}
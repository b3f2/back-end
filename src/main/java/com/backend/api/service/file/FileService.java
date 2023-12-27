package com.backend.api.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void upload(MultipartFile file, String fileName);
    void delete(String fileName);
}
package com.backend.api.service.file;


import com.backend.api.exception.FileUploadFailureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class LocalFileService implements FileService {

    @Value("${upload.image.location}")
    private String location;

    @PostConstruct
    void postConstruct() {
        File dir = new File(location);
        if(!dir.exists()) {
            dir.mkdir();
        }
    }

    @Override
    public void upload(MultipartFile file, String fileName) {
        try {
            file.transferTo(new File(location + fileName));
        } catch (IOException e) {
            throw new FileUploadFailureException(e.getMessage());
        }
    }

    @Override
    public void delete(String fileName) {
        new File(location + fileName).delete();
    }
}

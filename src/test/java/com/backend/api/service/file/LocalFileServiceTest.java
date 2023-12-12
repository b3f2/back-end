package com.backend.api.service.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class LocalFileServiceTest {
    LocalFileService localFileService = new LocalFileService();
    String testLocation = new File("src/test/resources/static").getAbsolutePath() + "/";

    @BeforeEach
    void beforeEach() throws IOException {
        ReflectionTestUtils.setField(localFileService, "location", testLocation);
        FileUtils.cleanDirectory(new File(testLocation));
    }

    @Test
    void uploadTest() {
        // given
        MultipartFile file = new MockMultipartFile("myFile", "myFile.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        String filename = "testFile.txt";

        // when
        localFileService.upload(file, filename);

        // then
        assertThat(isExists(testLocation + filename)).isTrue();
    }

    @Test
    void deleteTest() {
        //given
        MultipartFile file = new MockMultipartFile("myFile", "myFile.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        String fileName = "testFile.txt";
        localFileService.upload(file, fileName);
        boolean before = isExists(testLocation+fileName);

        //when
        localFileService.delete(fileName);

        //then
        boolean after = isExists(testLocation+fileName);
        assertThat(before).isTrue();
        assertThat(after).isFalse();
    }

    boolean isExists(String filePath) {
        return new File(filePath).exists();
    }
}

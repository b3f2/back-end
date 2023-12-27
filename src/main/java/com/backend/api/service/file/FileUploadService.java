package com.backend.api.service.file;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.backend.api.dto.post.ImageDto;
import com.backend.api.exception.UnsupportedImageFormatException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final AwsS3UploadService s3UploadService;

    //원본 + 리사잉징 이미지 업로드
    public ImageDto uploadImage(MultipartFile file, String dirName) throws IOException {
        //파일명 생성
        String filename = dirName + "/" + createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        InputStream inputStream = file.getInputStream();

        //원본 이미지 업로드
        s3UploadService.uploadFile(inputStream, objectMetadata, filename);

        if(Objects.equals(dirName, "post")){
            //리사이징 이미지 파일
            String resizedFilename = filename.replace(dirName, dirName+"-resized");

            BufferedImage srcImage = ImageIO.read(file.getInputStream());
            int srcHeight = srcImage.getHeight();
            int srcWidth = srcImage.getWidth();
            double dWidth;
            double dHeight;

            if(srcWidth == srcHeight) {
                dWidth = 290;
                dHeight = 290;
            } else if(srcWidth > srcHeight) {
                dWidth = 290;
                dHeight = ((double) srcHeight / (double)srcWidth) * 290;
            } else {
                dHeight = 290;
                dWidth = ((double) srcWidth / (double) srcHeight) * 290;
            }

            Image imgTarget = srcImage.getScaledInstance((int) dWidth, (int) dHeight, Image.SCALE_SMOOTH);
            int pixels[] = new int[(int) (dWidth * dHeight)];
            PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, (int) dWidth, (int) dHeight, pixels, 0, (int) dWidth);
            try {
                pg.grabPixels();
            } catch(InterruptedException e) {
                throw new IOException(e.getMessage());
            }

            BufferedImage destImg;
            if(checkImageType(file).toUpperCase().equals("PNG")) {
                destImg = new BufferedImage((int) dWidth, (int) dHeight, BufferedImage.TYPE_INT_ARGB);
            } else {
                destImg = new BufferedImage((int) dWidth, (int) dHeight, BufferedImage.TYPE_INT_RGB);
            }

            destImg.setRGB(0, 0, (int) dWidth, (int) dHeight, pixels, 0, (int) dWidth);

            ByteArrayOutputStream uploadOs = new ByteArrayOutputStream();
            ImageIO.write(destImg, checkImageType(file), uploadOs);

            InputStream is = new ByteArrayInputStream(uploadOs.toByteArray());
            ObjectMetadata ob = new ObjectMetadata();
            ob.setContentType(checkImageType(file));
            ob.setContentLength(uploadOs.size());

            //리사이징 이미지 업로드
            s3UploadService.uploadFile(is, ob, resizedFilename);
        }

        //원본 이미지만 db에 저장
        return ImageDto.builder()
                .imageName(filename)
                .imgUrl(s3UploadService.getFileUrl(filename))
                .build();
    }

    //이미지 파일명 중복 방지를 위해 UUID로 랜덤 생성
    private String createFileName (String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    //파일 유혀성 검사 - 확장자
    private String getFileExtension(String fileName) {
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
       String idxFileName = fileName.substring(fileName.lastIndexOf("."));
       if(!fileValidate.contains(idxFileName)) {
           throw new UnsupportedImageFormatException();
       }
       return fileName.substring(fileName.lastIndexOf("."));
    }

    //이미지 확장자 분리 - 리사이징 이미지용
    private String checkImageType(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file.getInputStream());

        if(!mimeType.startsWith("image/")) {
            throw new UnsupportedImageFormatException();
        }
        return mimeType.substring(6);
    }
}

package org.hkust.msit6000o.dao;

import org.springframework.web.multipart.MultipartFile;

// S3 file upload interface
// https://github.com/faizakram/spring-boot-with-aws-s3/blob/master/src/main/java/spring/boot/aws/service/FileUploadService.java
public interface FileDao {
    // some basic needs for implementing the file upload service

    // upload the file
    boolean fileUpload(MultipartFile uploadFile);

    // get S3 url by name
    String getFileUrl(String filename);

}

package org.hkust.msit6000o.dao;

import org.hkust.msit6000o.model.outputFile;

import java.util.List;

// S3 file upload interface
// https://github.com/faizakram/spring-boot-with-aws-s3/blob/master/src/main/java/spring/boot/aws/service/FileUploadService.java
public interface FileDao {
    // some basic needs for implementing the file upload service

    // upload the file
    String fileUpload(String uploadFile);

    // get S3 url by name
    String getFileUrl(String filename);

    // get all file
    List<outputFile> getAllFile();

}

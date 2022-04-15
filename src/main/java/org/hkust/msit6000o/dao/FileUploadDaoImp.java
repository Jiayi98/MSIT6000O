package org.hkust.msit6000o.dao;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

// S3 file upload implementations,
// https://github.com/faizakram/spring-boot-with-aws-s3/blob/master/src/main/java/spring/boot/aws/service/FileUploadServiceImpl.java
@Repository("S3")
public class FileUploadDaoImp implements FileDao{

    @Override
    public boolean fileUpload(MultipartFile uploadFile) {
        return false;
    }

    @Override
    public String getFileUrl(String filename) {
        return null;
    }
}

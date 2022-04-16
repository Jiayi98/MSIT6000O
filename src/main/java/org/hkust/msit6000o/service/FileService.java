package org.hkust.msit6000o.service;

import org.hkust.msit6000o.dao.FileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

// file upload and S3 connection service
@Service
public class FileService {

    private final FileDao fileDao;
    @Value("${filePath}")
    private String filePath;

    @Autowired
    public FileService(@Qualifier("S3") FileDao fileUploadDao) { // dependency injection
        this.fileDao = fileUploadDao;
    }

    //upload file to backend
    public void uploadFile(MultipartFile file, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file.getBytes());
        out.flush();
        out.close();

        // Pending FileUploadImp implementation
        // uploadFileToS3(file);
    }

    //upload file to S3
    public boolean uploadFileToS3(String filename){
        return fileDao.fileUpload(filePath + filename);
    }

    //Get url from S3 by filename
    public String getFileUrl(String filename){
        return fileDao.getFileUrl(filename);
    }

}

package org.hkust.msit6000o.service;

import org.hkust.msit6000o.dao.FileDao;
import org.hkust.msit6000o.model.outputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// file upload and S3 connection service
@Service
public class FileService {

    private final FileDao fileDao;
    private final SparkService sparkService;
    @Value("${filePath}")
    private String filePath;

    @Autowired
    public FileService(@Qualifier("S3") FileDao fileUploadDao, SparkService sparkService) { // dependency injection
        this.fileDao = fileUploadDao;
        this.sparkService = sparkService;
    }

    //upload file to backend
    public String uploadFile(MultipartFile file, String fileName) throws Exception {
        String temp_filename = sparkService.sparkProcessing(file, fileName);
        return uploadFileToS3(temp_filename);
    }

    //upload file to S3
    public String uploadFileToS3(String filename){
        return fileDao.fileUpload(filePath + filename);
    }

    //Get url from S3 by filename
    public String getFileUrl(String filename){
        return fileDao.getFileUrl(filename);
    }

    //Get All file
    public List<outputFile> getAllFile(){
        return fileDao.getAllFile();
    }



}

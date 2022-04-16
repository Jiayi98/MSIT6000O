package org.hkust.msit6000o.service;

import org.hkust.msit6000o.dao.FileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        // 1. 调用spark来process 文件
        // 2. 将上一步的结果上传到s3
        String temp_filename = file.getOriginalFilename().replace(".csv", "_result.txt");
        System.out.println("上传成功啦，进入s3处理函数");
//        process(convertMultiPartFileToFile(file), temp_filename);
        uploadFileToS3(temp_filename);
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            System.out.println("Error converting multipartFile to file");
        }
        return convertedFile;
    }

    public void process(File file, String temp_filename) throws FileNotFoundException {
        //数据分析
        //将结果写入txt文件
        // spark.........write(filePath)


    }

    //upload file to S3
    public boolean uploadFileToS3(String filename){
        return fileDao.fileUpload(filePath + "test.txt");
    }

    //Get url from S3 by filename
    public String getFileUrl(String filename){
        return fileDao.getFileUrl(filename);
    }



}

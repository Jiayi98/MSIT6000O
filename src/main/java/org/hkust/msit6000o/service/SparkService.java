package org.hkust.msit6000o.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class SparkService {

    public String sparkProcessing(MultipartFile file, String fileName){
        // 1. 调用spark来process 文件
        // 2. 将上一步的结果上传到s3
        String temp_filename = file.getOriginalFilename().replace(".csv", "_result.txt");
        System.out.println("上传成功啦，进入s3处理函数");
//        process(convertMultiPartFileToFile(file), temp_filename);
        return  temp_filename;

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
}

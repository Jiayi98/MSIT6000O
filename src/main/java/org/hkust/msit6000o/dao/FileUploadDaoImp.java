package org.hkust.msit6000o.dao;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.*;
import org.hkust.msit6000o.model.outputFile;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;


import java.io.File;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;


// S3 file upload implementations,
// https://github.com/faizakram/spring-boot-with-aws-s3/blob/master/src/main/java/spring/boot/aws/service/FileUploadServiceImpl.java
@Repository("S3")
public class FileUploadDaoImp implements FileDao {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    /*
     upload intermediate file (spark output file) to S3 bucket
     param: filePath is the location of spark output file (under /resources/static...)
     return: boolean value
     */
    @Override
    public String fileUpload(String filePath) {
        File targetFile = new File(filePath);

        DateTime jobTime = new DateTime(System.currentTimeMillis(),
                DateTimeZone.forTimeZone(TimeZone.getTimeZone("asia/shanghai")));
        DateTimeFormatter parser1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");

        String fileName = targetFile.getName() + "_" + parser1.print(jobTime);

        try {
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, targetFile));
        } catch (SdkClientException e) {
            System.out.println("File Uploading exception");
            return null;
        }
        // delete the intermediate file
        targetFile.delete();
        return getFileUrl(fileName);
    }

    @Override
    public List<outputFile> getAllFile() {
        if (!s3Client.doesBucketExistV2(bucketName)) {
            System.out.println("No Bucket Found");
            return null;
        }
        return s3Client.listObjectsV2(bucketName).getObjectSummaries().
                stream()
                .map(file -> {
                    String myKey = file.getKey();
                    String url = getFileUrl(myKey);
                    return new outputFile(myKey, url);
                })
                .collect(Collectors.toList());

    }


    //get file url
    @Override
    public String getFileUrl(String filename) {
        return s3Client.getUrl(bucketName, filename).toString();
    }


}

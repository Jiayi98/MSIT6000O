package org.hkust.msit6000o.dao;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;


// S3 file upload implementations,
// https://github.com/faizakram/spring-boot-with-aws-s3/blob/master/src/main/java/spring/boot/aws/service/FileUploadServiceImpl.java
@Repository("S3")
public class FileUploadDaoImp implements FileDao{

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
    public boolean fileUpload(String filePath) {
        File targetFile = new File(filePath);

        DateTime jobTime = new DateTime(System.currentTimeMillis(),
                DateTimeZone.forTimeZone(TimeZone.getTimeZone("asia/shanghai")));
        DateTimeFormatter parser1 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");

        String fileName = targetFile.getName() + "+" + parser1.print(jobTime);

        try {
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, targetFile));
        } catch (SdkClientException e) {
            System.out.println("File Uploading exception");
            return false;
        }
        // delete the intermediate file
        targetFile.delete();
        return true;
    }

    public void getAllFilesInS3(String fileName) {
        // list all files in s3

        ObjectListing listing = s3Client.listObjects(bucketName);
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();

        while (listing.isTruncated()) {
            listing = s3Client.listNextBatchOfObjects (listing);
            summaries.addAll(listing.getObjectSummaries());
        }

    }


    @Override
    public String getFileUrl(String filename) {
        return null;
    }
}

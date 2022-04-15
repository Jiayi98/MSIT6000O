package org.hkust.msit6000o.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;


@Controller
public class FileUploadController {
    @Value("${filePath}")
    private String filePath;
    @GetMapping("/upload")
    public String uploading() {
        return "uploading";
    }
    //处理文件上传
    @PostMapping("/uploading")
    public @ResponseBody String uploading(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest request) {
        try {
            uploadFile(file.getBytes(), filePath, file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件上传失败!");
            return "Failed!";
        }
        System.out.println("FileUpload文件上传成功!");
        return "Successfully Uploaded!";
    }
    public void  uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}

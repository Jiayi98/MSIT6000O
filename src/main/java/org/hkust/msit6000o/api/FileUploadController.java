package org.hkust.msit6000o.api;

import org.hkust.msit6000o.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@Controller
public class FileUploadController {
    private final FileService fileService;

    @Autowired
    public FileUploadController(FileService fileUploadService) {
        this.fileService = fileUploadService;
    }

    @GetMapping("/upload")
    public String uploading() {
        return "uploading";
    }

    //处理文件上传
    @PostMapping("/uploading")
    public @ResponseBody
    String uploading(@RequestParam("file") MultipartFile file,
                     HttpServletRequest request) {
        try {
            fileService.uploadFile(file, file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件上传失败!");
            return "Failed!";
        }
        System.out.println("FileUpload文件上传成功!");
        return "Successfully Uploaded!";
    }

}

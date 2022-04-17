package org.hkust.msit6000o.api;

import org.hkust.msit6000o.model.outputFile;
import org.hkust.msit6000o.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


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

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/history")
    public String history() {
        return "history";
    }

    //处理文件上传
    @PostMapping("/uploading")
    @ResponseBody

    public String uploading(@RequestParam("file") MultipartFile file,
                     HttpServletRequest request) {
        String resultURL = "";
        try {
            resultURL = fileService.uploadFile(file, file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件上传失败!");
            return resultURL;
        }
        System.out.println("FileUpload文件上传成功!");
//        return "Successfully Uploaded!";
        return "Sucessfully uploaded! \n You can find the report here: <a href=" + resultURL + ">RESULT</a>";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<outputFile> getAllFiles(){
        return fileService.getAllFile();
    }
}

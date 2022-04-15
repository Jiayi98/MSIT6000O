package org.hkust.msit6000o.api;

import org.hkust.msit6000o.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/retrieve")
@RestController
public class FileRetrieveController {
    private final FileService fileService;

    @Autowired
    public FileRetrieveController(FileService fileUploadService) {
        this.fileService = fileUploadService;
    }

    // get S3 url by filename
    @GetMapping(path = "{filename}")
    public String getFileUrl(@PathVariable("filename") String filename) {
        return fileService.getFileUrl(filename);
    }

}

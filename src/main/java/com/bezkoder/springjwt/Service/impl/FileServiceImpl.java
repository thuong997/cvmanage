package com.bezkoder.springjwt.Service.impl;

import com.bezkoder.springjwt.Service.FileService;
import com.bezkoder.springjwt.Utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.util.Date;


@Service
public class FileServiceImpl implements FileService {
    private FileUtils fileManager = new FileUtils();
    private String linkFolder = "/tmp/fileCV/";

    @Override
    public String uploadFileCv(MultipartFile file) {

        String nameFile = new Date().getTime() + "." + fileManager.getFormatFile(file.getOriginalFilename());

        String path = linkFolder + nameFile;

        try {
            fileManager.createNewMultiPartFile(path, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO save link file to database
        return nameFile;

    }

    @Override
    public File downloadFileCv(String cvUrl) throws IOException {
        String path = linkFolder  + cvUrl;
        return new File(path);
    }


}

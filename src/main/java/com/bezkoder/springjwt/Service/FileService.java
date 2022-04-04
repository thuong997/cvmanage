package com.bezkoder.springjwt.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
public interface FileService {

    String uploadFileCv(MultipartFile FILECV) throws IOException;

    File downloadFileCv(String cvUrl) throws IOException;
}
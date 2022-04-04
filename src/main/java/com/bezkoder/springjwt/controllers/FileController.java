package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.Service.FileService;
import com.bezkoder.springjwt.Utils.ErrorCode;
import com.bezkoder.springjwt.Utils.FileUtils;
import com.bezkoder.springjwt.security.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;


@RestController
@RequestMapping("HO112/file")
public class FileController {

	@Autowired
	private FileService fileService;

	@PostMapping()
	public ResponseEntity<?> upLoadFileCV(@RequestParam(name = "fileCv") MultipartFile fileCv) throws IOException {

		if (!new FileUtils().isTypeFileOfCv(fileCv)) {
			throw new CustomException(HttpStatus.BAD_REQUEST)
					.addError(ErrorCode.ERR_UPLOAD_FILE)
					.withMessage(ErrorCode.ERR_UPLOAD_FILE.getMessage());
		}
		return new ResponseEntity<>(fileService.uploadFileCv(fileCv), HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<?> downloadFileCv(@RequestParam(name = "fileCv") String cvUrl) throws IOException {
		File fileCv = fileService.downloadFileCv(cvUrl);
		InputStreamResource fileCvStream;
		try {
			fileCvStream = new InputStreamResource(new FileInputStream(fileCv));
		}
		catch (Exception c)
		{
			c.printStackTrace();
			throw new CustomException(HttpStatus.BAD_REQUEST)
					.addErrors(Collections.singletonList(ErrorCode.ERR_FILE_NOT_FOUND));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String.format("attachment; filename =\"%s\"",cvUrl));
		headers.add("Cache-Control","no-cache, no-store, must-revalidate");
		headers.add("Pragma","no-cache");
		headers.add("Expires","0");
		return ResponseEntity
				.ok()
				.headers(headers)
				.contentLength(fileCv.length())
				.contentType(MediaType.parseMediaType("application/txt"))
				.body(fileCvStream);
	}
}

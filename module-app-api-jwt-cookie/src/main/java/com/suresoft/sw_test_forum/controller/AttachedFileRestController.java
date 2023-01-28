package com.suresoft.sw_test_forum.controller;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/attached-files")
public class AttachedFileRestController {
    @Value("${module-app-api.url}")
    private String moduleAppApiUrl;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAttachedFile(MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isImageFileValid(files);
        String imageUrl = null;

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            // 대체 가능
            // File savedFile = new File("./upload/", savedFileName);
            // FileCopyUtils.copy(file.getBytes(), savedFile);
            Path path = Paths.get("./upload/ckeditor/" + savedFileName);
            Files.write(path, file.getBytes());

            imageUrl = "{"
                    + "\"url\":\"" + moduleAppApiUrl + "/api/attached-files/view-ckeditor-image/" + savedFileName
                    + "\"}";
        }

        return new ResponseEntity<>(imageUrl, HttpStatus.CREATED);
    }

    @GetMapping("/download/{savedFileName}")
    public ResponseEntity<?> downloadAttachedFile(@PathVariable("savedFileName") String savedFileName) throws Exception {
        // 파일 이름이 한글인 경우 인코딩이 깨지지 않도록 변경
        String encodedSavedFileName = URLEncoder.encode(savedFileName, "UTF-8").replace("+", "%20");

        // 헤더 추가
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodedSavedFileName.substring(42));
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        // MimeType 추가, application/octet-stream은 text/plain 타입을 제외한 기본 값
        MediaType mediaType = MediaType.parseMediaType("application/octet-stream");

        // 다운로드 파일 추가
        Path path = Paths.get("./upload/" + savedFileName);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentType(mediaType)
                .body(resource);
    }

    @GetMapping("/view-image/{savedFileName}")
    public ResponseEntity<?> viewImageAttachedFile(@PathVariable("savedFileName") String savedFileName) throws Exception {
        // 파일 이름이 한글인 경우 인코딩이 깨지지 않도록 변경
        String encodedSavedFileName = URLEncoder.encode(savedFileName, "UTF-8").replace("+", "%20");

        // 헤더 추가
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + encodedSavedFileName.substring(42));
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        // MimeType 추가, application/octet-stream은 text/plain 타입을 제외한 기본 값
        MediaType mediaType = MediaType.parseMediaType("image/jpeg");

        // 다운로드 파일 추가
        Path path = Paths.get("./upload/" + savedFileName);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentType(mediaType)
                .body(resource);
    }

    @GetMapping("/view-pdf/{savedFileName}")
    public ResponseEntity<?> viewPdfAttachedFile(@PathVariable("savedFileName") String savedFileName) throws Exception {
        // 파일 이름이 한글인 경우 인코딩이 깨지지 않도록 변경
        String encodedSavedFileName = URLEncoder.encode(savedFileName, "UTF-8").replace("+", "%20");

        // 헤더 추가
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + encodedSavedFileName.substring(42));
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        // MimeType 추가, application/octet-stream은 text/plain 타입을 제외한 기본 값
        MediaType mediaType = MediaType.parseMediaType("application/pdf");

        // 다운로드 파일 추가
        Path path = Paths.get("./upload/" + savedFileName);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentType(mediaType)
                .body(resource);
    }

    @GetMapping("/view-video/{savedFileName}")
    public ResponseEntity<?> viewVideoAttachedFile(@PathVariable("savedFileName") String savedFileName) throws Exception {
        // 파일 이름이 한글인 경우 인코딩이 깨지지 않도록 변경
        String encodedSavedFileName = URLEncoder.encode(savedFileName, "UTF-8").replace("+", "%20");

        // 헤더 추가
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + encodedSavedFileName.substring(42));
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        // MimeType 추가, application/octet-stream은 text/plain 타입을 제외한 기본 값
        MediaType mediaType = MediaType.parseMediaType("video/mp4");

        // 다운로드 파일 추가
        Path path = Paths.get("./upload/" + savedFileName);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentType(mediaType)
                .body(resource);
    }

    @GetMapping("/view-ckeditor-image/{savedFileName}")
    public ResponseEntity<?> viewCkeditorImage(@PathVariable("savedFileName") String savedFileName) throws Exception {
        // 파일 이름이 한글인 경우 인코딩이 깨지지 않도록 변경
        String encodedSavedFileName = URLEncoder.encode(savedFileName, "UTF-8").replace("+", "%20");

        // 헤더 추가
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + encodedSavedFileName.substring(42));
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        // MimeType 추가, application/octet-stream은 text/plain 타입을 제외한 기본 값
        MediaType mediaType = MediaType.parseMediaType("image/jpeg");

        // 다운로드 파일 추가
        Path path = Paths.get("./upload/ckeditor/" + savedFileName);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentType(mediaType)
                .body(resource);
    }
}
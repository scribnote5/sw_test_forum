package com.suresoft.sw_test_forum.common.validation;

import com.suresoft.sw_test_forum.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
public class FileValidator {
    /**
     * file type이 유효한지 검사
     *
     * @param files
     * @return
     * @throws IOException
     */
    public static String isFileValid(MultipartFile[] files) throws IOException {
        Tika tika = new Tika();
        String result = "valid";

        for (MultipartFile file : files) {
            String mimeType = tika.detect(file.getBytes());
            String extension = FileUtil.getFileExtension(file.getOriginalFilename());

            if (FileType.invalidMimeTypeSet.contains(mimeType)) {
                log.info("Check mimeType: " + extension);
                result = "파일 " + file.getOriginalFilename() + " [mime type: " + mimeType + "]은 악의적이고 위험하다고 추측되어 파일 업로드를 지원하지 않습니다.";
                break;
            }

            if (FileType.invalidExtensionSet.contains(extension)) {
                log.info("Check mimeType: " + extension);
                result = "파일 " + file.getOriginalFilename() + " [mime type: " + mimeType + "]은 악의적이고 위험하다고 추측되어 파일 업로드를 지원하지 않습니다.";
                break;
            }
        }

        return result;
    }

    public static String isImageFileValid(MultipartFile[] files) throws IOException {
        Tika tika = new Tika();
        String result = "valid";

        for (MultipartFile file : files) {
            String mimeType = tika.detect(file.getBytes());
            String extension = FileUtil.getFileExtension(file.getOriginalFilename());

            log.info("Check mimeType: " + extension);

            if (!FileType.validImageTypeSet.contains(mimeType)) {
                result = "파일 " + file.getOriginalFilename() + " [mime type: " + mimeType + "]은 이미지 파일이 아닌 것으로 추측되어 파일 업로드를 지원하지 않습니다.";
                log.error("MimeType error: " + extension);
                break;
            }
        }

        return result;
    }
}
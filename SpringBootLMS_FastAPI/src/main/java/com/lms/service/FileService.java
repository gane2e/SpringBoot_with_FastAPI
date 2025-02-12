package com.lms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws  Exception{

        UUID uuid = UUID.randomUUID();

        /* 원본파일명 : test.jpg => jpg (확장자 읽기)  */
        String extenstion = originalFileName.substring(originalFileName.lastIndexOf("."));

        /* uuid + 확장자명으로 신규파일명 생성 */
        String savedFileName = uuid.toString() + extenstion;

        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        /* FileOutputStream => 바이트 단위의 출력을 내보내는 클래스 */
        /* 생성자로 파일이 저장될 위치와 파일의 이름을 넘겨 파일에 쓸 파일 출력 스트림을 만든다. */
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        log.info("fos" + fos.toString());
        fos.write(fileData); /* fileData를 출력스트림에 입력 */
        fos.close();

        return savedFileName;
    }

    /**/
    public void deleteFile(String filePath) throws Exception{

        /* 파일이 저장된 경로를 이용하여 파일 객체를 생성 */
        File deletedFile = new File(filePath);

        /* 해당 파일이 존재하면 삭제한다. */
        if (deletedFile.exists()) {
            deletedFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}

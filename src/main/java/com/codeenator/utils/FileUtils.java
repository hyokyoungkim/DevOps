package com.codeenator.utils;

import com.codeenator.dao.FileDAO;
import com.ithows.AppConfig;
import com.ithows.ResultMap;
import com.ithows.util.DateTimeUtils;
import com.ithows.util.crypto.CryptoManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileUtils {

    /**
     * 파일 확장자 가져오기
     * @param name  파일 이름
     * @return 
     */
    public static String getExtension(String name) {
        int index = name.lastIndexOf(".") + 1;
        
        return name.substring(index);
    }
    
    
    /**
     * 임시 파일 업로드 경로 가져오기
     * @return      파일 업로드 경로
     */
    public static String getTempPath() {
        String filePath = AppConfig.getConf("file_dir");
        String tempPath = AppConfig.getConf("temp_dir");
        
        return String.format("%s%s", filePath, tempPath);
    }
    
    
    /**
     * 파일 업로드 경로 가져오기
     * - 경로는 file 디렉터리에서 업로드 날짜를 연도/월/일 따로 암호화한 후 앞에 10자리만 사용
     * @return      파일 업로드 경로
     */
    public static String getUploadPath() {
        String filePath = AppConfig.getConf("file_dir");
        String date = DateTimeUtils.getTimeDateNow("yyyy/MM/dd");
        String[] dates = date.split("/");
        
        StringBuilder sb = new StringBuilder();
        sb.append(filePath);
        
        try {
            for (String d : dates) {
                String cipher = CryptoManager.md5(d).substring(0, 10);

                sb.append("/").append(cipher);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return sb.toString();
    }
    

    /**
     * 이름 변경
     * @param src   원본 이름
     * @param dest  변경하고 싶은 이름
     * @return 
     */
    public static String rename(String src, String dest) {
        Objects.requireNonNull(src, "source is NULL");
        Objects.requireNonNull(dest, "destination is NULL");
        
        int index = src.lastIndexOf(".");
        String extension = src.substring(index);            // 확장자
        
        StringBuilder sb = new StringBuilder();
        sb.append(dest).append(extension);
        
        return sb.toString();
    }
    
    
    /**
     * 파일 이름 변경
     * @param src   원본 파일
     * @param dest  변경하고 싶은 파일
     * @return 
     */
    public static boolean renameTo(File src, File dest) {
        boolean isSucceed = false;
        
        // File.renameTo()가 실패할 때를 고려
        if (!(isSucceed = src.renameTo(dest))) {
            byte[] buffer = new byte[1024];
            
            try (FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest)) {
                
                int read = 0;
                while ((read = fis.read(buffer)) != -1) {
                    fos.write(read);
                }
                
                src.delete();
            } catch (IOException e) {
                e.printStackTrace();
                
                return false;
            }
            
            return dest.exists();
        }
        
        return isSucceed;
    }
    
    
    /**
     * 첨부파일 UUID 생성 (중복 확인)
     * - 안정성을 위해 추가
     * @return 
     */
    public static String createUUID() throws SQLException {
        String uuid;
        boolean isUsed = true;
        
        do {
            uuid = RandomUtils.createUUID();
            
            ResultMap fileMap = FileDAO.selectFileByUUID(uuid);                 // 첨부파일 조회
            if (fileMap == null) {
                isUsed = false;
            }
        } while (isUsed);
        
        return uuid;
    }
    
    
    /**
     * 파일 목록에서 경로를 제외하고 파일 이름으로 목록 구성
     * - /file/5531a58348/e45ee7ce7e/c51ce410c1/6f2153de-f535-4859-93ee-e3c62d70369b.png -> 6f2153de-f535-4859-93ee-e3c62d70369b.png
     * @param fileList  파일 목록
     * @return          파일 목록
     */
    public static List<String> convertName(List<String> fileList) {
        return fileList.stream()
                .map(item -> item.substring(item.lastIndexOf("/") + 1))
                .collect(Collectors.toList());
    }
    
    
    /**
     * 파일 복사
     * @param src           원본 경로
     * @param dest          복사할 경로
     * @param isDelete      원본 파일 삭제 여부
     */
    public static void copy(String src, String dest, boolean isDelete) {
        File source = new File(src);
        File destination = new File(dest);
        
        copy(source, destination, isDelete);
    }
    
    
    /**
     * 파일 복사
     * @param src           원본 파일
     * @param dest          복사 파일
     * @param isDelete      원본 파일 삭제 여부
     */
    public static void copy(File src, File dest, boolean isDelete) {
        try {
            File directory = dest.getParentFile();
            
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            if (isDelete) {
                src.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 파일 삭제
     * @param filePath      파일 경로
     * @return              삭제 여부
     */
    public static boolean delete(String filePath) {
        File file = new File(filePath);
        
        return delete(file);
    }
    

    /**
     * 파일 삭제
     * @param file          파일
     * @return              삭제 여부
     */
    public static boolean delete(File file) {
        if (file.exists()) {
            file.delete();
        }
        
        return false;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.service;

import com.ithows.AppConfig;
import com.ithows.FileInfo;
import com.ithows.JakartaUpload;
import com.ithows.ResultMap;
import com.ithows.dao.FileDAO;
import com.ithows.dao.UserDAO;
import com.ithows.util.DateTimeUtils;
import com.ithows.util.ImageUtils;
import com.ithows.util.KeyGenerator;
import com.ithows.util.UtilDoc;
import com.ithows.util.UtilFile;
import com.ithows.util.UtilJSON;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ksyuser
 */
public class FileManager {

    /**
     * 이미지 업로드 하기. $$ 정책 : 같은 파일(유저, 파일명, 파일 타입이 같은)이 있으면 업데이트 하고 없으면 추가
     */
    public static String saveImageFile(HttpSession session, HttpServletRequest request, String userIdStr) throws Exception {

        String imgDir = UploadConst.getImageUploadDir(session, userIdStr); // 업로드할 디렉터리 (시스템경로)
        String tempDir = UploadConst.getTempUploadDir(session, userIdStr); // 업로드할 임시디렉터리 (URL 경로)

        // 1. 업로드 객체 생성과 함께 업로드 처리 완료 
//        JakartaUpload mRequest = new JakartaUpload(request, 100 * 1024 * 1024, imgDir, "UTF-8");  // 100MB 업로드
        JakartaUpload mRequest = new JakartaUpload(request, 100 * 1024 * 1024, tempDir, "UTF-8");  // 100MB 업로드

        // 2. 파일info에서 업로드 파일명을 받아 옴. 업로드 페이지의 <input type="file" name="qs_file" ...>와 name을 맞추어 주어야 한다.
        FileInfo fInfo = mRequest.getFileInfo("file");
        String saveFileName = fInfo.getFileName();

        if (saveFileName == null || saveFileName.equals("")) {
            saveFileName = "default";
        }

        if (saveFileName != null) {

            // 기존 파일을 찾아오는 로직 
            String oldFileName = FileDAO.getFilePath(saveFileName);

            if (oldFileName.equals("") || oldFileName == null) {
                Path srcFile = Paths.get(tempDir + saveFileName);
                Path movePath = Paths.get(imgDir);
                Files.move(srcFile, movePath.resolve(srcFile.getFileName()));

            } else {

                Path srcFile = Paths.get(tempDir + saveFileName);
                saveFileName = FilenameUtils.getBaseName(saveFileName) + "_" + DateTimeUtils.getTimeDateNow2() + "." + FilenameUtils.getExtension(saveFileName);
                Path tgtFile = Paths.get(imgDir + saveFileName);
                Files.move(srcFile, tgtFile);
            }

            // 서버상 저장 상대 경로를 가져옴
            String dir = UploadConst.getImageUploadDir(userIdStr);

            // 3. 첨부파일 경로 mov table에 업데이트
//            System.out.println("url -- " +  imgDir );
//            System.out.println("dir -- " + dir );
//            System.out.println("saveFileName -- "+ saveFileName );
//            System.out.println("size -- "+ fInfo.getFileSize() );
//            System.out.println("userIdStr -- "+ userIdStr );
//            System.out.println("type -- "+ FILETYPE_IMAGE );  // (1:이미지,2:ppt,3:지도,4:일반파일)
            FileDAO.insertFile(saveFileName, fInfo.getFileSize(), FileDAO.FILETYPE_IMAGE, userIdStr, imgDir + saveFileName, dir + "/" + saveFileName);

        }
        return saveFileName;
    }

    /**
     * 파일 업로드 하기. $$ 정책 : 같은 파일(유저, 파일명, 파일 타입이 같은)이 있으면 업데이트 하고 없으면 추가
     */
    public static String saveArchiveFile(HttpSession session, HttpServletRequest request, String userIdStr) throws Exception {

        String fileDir = UploadConst.getFileUploadDir(session, userIdStr); // 업로드할 디렉터리 (URL 경로)
        String tempDir = UploadConst.getTempUploadDir(session, userIdStr); // 업로드할 임시디렉터리 (URL 경로)

        // 1. 업로드 객체 생성과 함께 업로드 처리 완료 
//        JakartaUpload mRequest = new JakartaUpload(request, 1 * 1024 * 1024 * 1024, fileDir, "UTF-8");  // 1GB 업로드
        JakartaUpload mRequest = new JakartaUpload(request, 1 * 1024 * 1024 * 1024, tempDir, "UTF-8");  // 1GB 업로드

        FileInfo fInfo = mRequest.getFileInfo("file");
        String saveFileName = fInfo.getFileName();

        if (saveFileName == null || saveFileName.equals("")) {
            saveFileName = "default";
        }

        if (saveFileName != null) {

            // 기존 파일을 찾아오는 로직 
            String oldFileName = FileDAO.getFilePath(saveFileName);

            if (oldFileName.equals("") || oldFileName == null) {
                Path srcFile = Paths.get(tempDir + saveFileName);
                Path movePath = Paths.get(fileDir);
                Files.move(srcFile, movePath.resolve(srcFile.getFileName()));

            } else {

                Path srcFile = Paths.get(tempDir + saveFileName);
                saveFileName = FilenameUtils.getBaseName(saveFileName) + "_" + DateTimeUtils.getTimeDateNow2() + "." + FilenameUtils.getExtension(saveFileName);
                Path tgtFile = Paths.get(fileDir + saveFileName);
                Files.move(srcFile, tgtFile);
            }

            // 서버상 저장 상대 경로를 가져옴
            String dir = UploadConst.getFileUploadDir(userIdStr);

            // 3. 첨부파일 경로 mov table에 업데이트
//            System.out.println("url -- " +  fileDir );
//            System.out.println("dir -- " + dir );
//            System.out.println("saveFileName -- "+ saveFileName );
//            System.out.println("size -- "+ fInfo.getFileSize() );
//            System.out.println("userIdStr -- "+ userIdStr );
//            System.out.println("type -- "+ FILETYPE_ARCHIVE );  // (1:이미지,2:ppt,3:지도,4:일반파일)
//            
            FileDAO.insertFile(saveFileName, fInfo.getFileSize(), FileDAO.FILETYPE_ARCHIVE, userIdStr, fileDir + saveFileName, dir + "/" + saveFileName);

        }

        return saveFileName;
    }


    public static String deleteArchiveFile(String userIdStr, long fileId) {

        String resStr = "";

        try {
            ResultMap result = FileDAO.getFile(fileId);
            if (userIdStr.equals(result.getString("fileUserName")) || UserDAO.checkAdmin(userIdStr)) {

                UtilFile.deleteFile(result.getString("filePath"));
                FileDAO.deleteFile(fileId);
                resStr = result.getString("fileName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resStr;
    }

}

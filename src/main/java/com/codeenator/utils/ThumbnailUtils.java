package com.codeenator.utils;

import com.codeenator.dao.FileDAO;
import com.ithows.AppConfig;
import com.ithows.service.UploadConst;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class ThumbnailUtils {
                                                                                // 썸네일 크기
    private static final int THUMBNAIL_WIDTH = 230;                             // 너비
    private static final int THUMBNAIL_HEIGHT = 150;                            // 높이
    
    private static final int PADDING = 15;
    private static final int ALBUM_TITLE_WIDTH = THUMBNAIL_WIDTH - (PADDING * 2);// 앨범 타이틀 크기
    
                                                                                // 폰트 설정
    private static final String FONT = "Gothic_A1/GothicA1-Regular.ttf";        // 폰트
    
    private static final Color ALBUM_FONT_COLOR = Color.BLACK;                  // 앨범 폰트 색     
    private static final int ALBUM_FONT_SIZE = 14;                              // 앨범 폰트 크키
    
    private static final Color CODEE_FONT_COLOR = new Color(0x5F00FF);          // 코디네이터 폰트 색 (보라색)
    private static final int CODEE_FONT_SIZE = 12;                              // 코디네이터 폰트 크기

    /**
     * 기본 썸네일 이미지에 앨범 이름, 코디네이터 이름을 붙임
     * @param album         앨범 이름
     * @return              썸네일 이름
     * @throws IOException
     * @throws SQLException 
     */
    public static String createThumbnail(String album) throws IOException, SQLException, FontFormatException {
        // 썸네일 경로
        String contextPath = UploadConst.contextPath();
        String imagePath = contextPath + AppConfig.getConf("thumbnail_dir");
        String imageName = "default_thumbnail.png";
        
        // 확장자
        String extesion = FileUtils.getExtension(imageName);
        String uuid = FileUtils.createUUID();
        
        // 업로드 경로
        String tempPath = FileUtils.getTempPath();
        String uploadPath = FileUtils.getUploadPath();
        String uploadName = uuid + "." + extesion;
        File outputFile = new File(contextPath + tempPath, uploadName);
        
        // 업로드 디렉터리가 없는 경우 생성
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        
        // 폰트 가져오기
        String fontPath = contextPath + AppConfig.getConf("font_dir");
        File fontFile = new File(fontPath, FONT);
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        
        File inputFile = new File(imagePath, imageName);
        BufferedImage image = ImageIO.read(inputFile);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setFont(font.deriveFont((float) ALBUM_FONT_SIZE));           // 앨범 타이틀 폰트 설정
        graphics2D.setColor(ALBUM_FONT_COLOR);
        
        int y = ALBUM_FONT_SIZE + PADDING;
        
        // 앨범 이름 입력
        List<String> lineList = splitAlbumName(graphics2D, album);              // 앨범 이름 나누기
        int length = lineList.size();
        for (int i = 0; i < length; i++) {
            String line = lineList.get(i);
            
            if (i == 0) {
                // 첫 번째 줄
                int x = THUMBNAIL_WIDTH - graphics2D.getFontMetrics().stringWidth(line) - PADDING;
                graphics2D.drawString(line, x, y);

                y += graphics2D.getFontMetrics().getHeight();
            } else if (i == 1) {
                // 두 번째 줄
                if (length > 2) {
                    line = line.substring(0, line.length() - 3) + "...";
                }
                
                int x = THUMBNAIL_WIDTH - graphics2D.getFontMetrics().stringWidth(line) - PADDING;
                graphics2D.drawString(line, x, y);

                break;
            }
        }
        
//        // 코디네이터 이름 입력 (닉네임 변경 고려)
//        graphics2D.setFont(font.deriveFont((float) CODEE_FONT_SIZE));        // 코디네이터 폰트 설정
//        graphics2D.setColor(CODEE_FONT_COLOR);
//        
//        String codee = "by " + codeenator;
//        int codeeX = THUMBNAIL_WIDTH - graphics2D.getFontMetrics().stringWidth(codee) - PADDING;
//        int codeeY = THUMBNAIL_HEIGHT - PADDING;
//        graphics2D.drawString(codee, codeeX, codeeY);
        
        ImageIO.write(image, extesion, outputFile);                             // 파일 저장
        graphics2D.dispose();        

        if (outputFile.exists()) {
            long size = Files.size(outputFile.toPath());
            FileDAO.insertFile(uuid, uploadPath, imageName, uploadName, size);      // 파일 정보 저장
            
            return uploadPath + "/" + uploadName;
        } else {
            return null;
        }
    }
    
    
    /**
     * 앨범 이름 나누기
     * @param graphics2D
     * @param album         앨범 이름
     * @return 
     */
    public static List<String> splitAlbumName(Graphics2D graphics2D, String album) {
        List<String> lineList = new ArrayList<>();
                
        String currentLine = "";
        String[] words = album.split("\\s+");
        for (String word : words) {
            String line = currentLine + word + " ";
            
            int lineWidth = graphics2D.getFontMetrics().stringWidth(line);
            if (lineWidth > ALBUM_TITLE_WIDTH) {
                // 앨범 타이틀 너비보다 큰 경우
                lineList.add(currentLine);
                
                currentLine = word + " ";
            } else {
                currentLine = line;
            }
        }
        
        lineList.add(currentLine);                                              // 마지막 라인
        
        return lineList;
    }   
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.util;

import com.ithows.BaseDebug;
import com.ithows.FileInfo;
import com.ithows.JakartaUpload;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author JJanga
 */
public class ImageUtils {

    private static final int UPLOAD_STD_SIZE = 1440;

    public static BufferedImage rotateBufferedImage(BufferedImage bi) {
        BufferedImage bi2 = new BufferedImage(bi.getHeight(), bi.getWidth(), bi.getType());
        AffineTransform tx = new AffineTransform();
        tx.translate(bi.getHeight() / 2, bi.getWidth() / 2);
        tx.rotate(Math.toRadians(90.0));//(radian,arbit_X,arbit_Y)
        tx.translate(-bi.getWidth() / 2, -bi.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        op.filter(bi, bi2);//(sourse,destination)
        return bi2;
    }

    private static String reSaveAsImage(String dir, String fileName) {
        String targetName = null;
        try {
            File file = new File(dir + fileName);
            BufferedImage biSrc = ImageIO.read(file);

            targetName = "_" + fileName;
            int width = biSrc.getWidth();
            int height = biSrc.getHeight();
            /*
             * 어떤 경로로 업로드 되는 이미지던지 큰 쪽이 1024 이하는 업로드 가능하다.(주민증도 가능)
             */
            if (width > height && width > UPLOAD_STD_SIZE) { //어느 한쪽이 클 경우 1024보다 크다면 큰 쪽은 1024로, 나머지는 1024를 기준으로 줄여준다.                                  
                height = getStandardSize(width, height);
                width = UPLOAD_STD_SIZE;
            } else if (height > width && height > UPLOAD_STD_SIZE) {
                width = getStandardSize(height, width);
                height = UPLOAD_STD_SIZE;
            }
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(biSrc, 0, 0, width, height, null);
            g.dispose();
            ImageIO.write(resizedImage, "JPG", new File(dir + targetName));
            if (file.exists()) {
                file.delete();
            }
        } catch (IOException e) {
            BaseDebug.log(e);
        }
        return targetName;
    }

    //이미지 사이즈 줄이기.(먼저 들어온 것_더 큰쪽_ 기준으로 줄이기)
    private static int getStandardSize(int n1, int n2) {
        float m1 = (float) n1;
        float m2 = (float) n2;
        float ratio = ((float) UPLOAD_STD_SIZE / m1) * m2;
        return (int) ratio;
    }

    // 이미지 크기 얻기 
    public static int[] getImageSize(String path) {
        if(!UtilFile.checkExist(path)){
            System.out.println("No Map Image File");
            return null;
        }
        
        int[] result = new int[2] ;
        String suffix = FilenameUtils.getExtension(path);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(new File(path));
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                result[0] = width;
                result[1] = height;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                reader.dispose();
            }
        } else {
            System.out.println("No reader found for given format: " + suffix);
        
        }
        return result;
    }

    /**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     *
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
    public static void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    /**
     * Resizes an image by a percentage of original size (proportional).
     *
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param percent a double number specifies percentage of the output image
     * over the input image.
     * @throws IOException
     */
    public static void resize(String inputImagePath,
            String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }
    
    
    public static void main(String[] args) {
        String path = "C:\\Users\\mailt\\Desktop\\ttt.jpg";
        int[] size = getImageSize(path);
        System.out.println("size = " + size[0] + " , " + size[1] );
    }
}

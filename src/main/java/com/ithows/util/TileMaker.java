/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.util;

/**
 * Class TileMaker
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;


public class TileMaker implements Runnable {

    private BufferedImage image;
    private String filename;
    private int rows, columns;
    private BufferedImage[][] smallImages;
    private int smallWidth;
    private int smallHeight;
    private String targetDir;
    private int imageCounter;


    public TileMaker(String filename, String TargetDir, int sWidth, int sHeight) {

        this.targetDir = TargetDir;
        this.filename = filename;
        this.image = null;
        try {
            this.image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.smallWidth = sWidth;
        this.smallHeight = sHeight;
        this.rows = image.getHeight() / this.smallHeight;
        this.columns = image.getWidth() / this.smallWidth;

        if (image.getHeight() > this.rows * this.smallHeight) {
            this.rows++;
        }
        if (image.getWidth() > this.columns * this.smallWidth) {
            this.columns++;
        }

        smallImages = new BufferedImage[columns][rows];
    }

    public static int getLevelNumber(int nCount) {
        for (int i = 0; i < 100; i++) {
            if (Math.pow(2, i) > nCount) {
                return i;
            }
        }
        return -1;
    }

    public static void initDirectory(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
            return ;
        }

        String[] files = dir.list();
        for (int i = 0, len = files.length; i < len; i++) {
            File f = new File(dir, files[i]);
            if (f.isDirectory()) {
                initDirectory(f);
            } else {
                f.delete();
            }
        }
        dir.delete();
        dir.mkdirs();
        
    }

    //    TileMaker.process(mapFileName);
    public static void process(String inputImageName) {

        File imageFile = new File(inputImageName);
        if (!imageFile.exists() || !imageFile.isFile()) {
            System.out.println("Main Image is not yet generated.");
            return;
        }

        BufferedImage tImage = null;
        try {
            tImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 타겟이미지의 width, height 중에 높은 값을 지정한다.
        int nCount = (tImage.getWidth() > tImage.getHeight() ? tImage.getWidth() : tImage.getHeight()) / 256 + 1;
        int nLevel = getLevelNumber(nCount);

        DecimalFormat dformat = new DecimalFormat("0.##");
        long start;
        long end;

        start = System.currentTimeMillis(); // 시작시간

        // ImageResizer.resize(inputImageName + ".jpg",  inputImageName + "_10.jpg" , 10);
        String tgtPath = FilenameUtils.getFullPath(inputImageName) + FilenameUtils.getName(inputImageName) + "_/tiles" ;
        initDirectory(new File(tgtPath));
        
        TileMaker image = new TileMaker(inputImageName,  tgtPath + "/" + nLevel, 256, 256);



        new Thread((Runnable) image).start();

        try {
            double percent = 1;

            for (int i = 1; i <= nLevel; i++) {
                percent = Math.pow(0.5, i);
                ImageUtils.resize(inputImageName, FilenameUtils.getBaseName(inputImageName) + "_" + dformat.format(percent) + "." + FilenameUtils.getExtension(inputImageName), percent);
                new Thread((Runnable) new TileMaker(FilenameUtils.getBaseName(inputImageName) + "_" + dformat.format(percent) + "." + FilenameUtils.getExtension(inputImageName),
                        tgtPath + "/" + (nLevel - i), 256, 256)).start();
            }
            
        } catch (IOException ex) {
            System.out.println("Error resizing the image.");
            ex.printStackTrace();
        }

        end = System.currentTimeMillis();  //종료시간
        System.out.println("Images saved Time: " + (end - start) + " milliseconds");
    }

    public void run() {
        this.createSmallImages();
        this.createDirectoryForSaving();
        this.saveSmallImages();
    }

    private void createSmallImages() {
        imageCounter = 0;
        Graphics2D g2d = null;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {

                if (image.getHeight() - y * smallHeight < this.smallHeight) {
                    smallImages[x][y] = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
                    g2d = (Graphics2D) smallImages[x][y].getGraphics();
                    g2d.setColor(new Color(0, 0, 0, 0));
                    g2d.fillRect(0, 0, 256, 256);

                    if (image.getWidth() - x * smallWidth < this.smallWidth) {
//                        smallImages[x][y] = image.getSubimage(x * smallWidth, y * smallHeight, image.getWidth() - x * smallWidth, image.getHeight() - y * smallHeight);
                        g2d.drawImage(image.getSubimage(x * smallWidth, y * smallHeight, image.getWidth() - x * smallWidth, image.getHeight() - y * smallHeight), 0, 0, null);
                    } else {
//                        smallImages[x][y] = image.getSubimage(x * smallWidth, y * smallHeight, smallWidth, image.getHeight() - y * smallHeight);
                        g2d.drawImage(image.getSubimage(x * smallWidth, y * smallHeight, smallWidth, image.getHeight() - y * smallHeight), 0, 0, null);
                    }
                } else if (image.getWidth() - x * smallWidth < this.smallWidth) {
                    smallImages[x][y] = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
                    g2d = (Graphics2D) smallImages[x][y].getGraphics();
                    g2d.setColor(new Color(0, 0, 0, 0));
                    g2d.fillRect(0, 0, 256, 256);
                    g2d.drawImage(image.getSubimage(x * smallWidth, y * smallHeight, image.getWidth() - x * smallWidth, smallHeight), 0, 0, null);
//                    smallImages[x][y] = image.getSubimage(x * smallWidth, y * smallHeight, image.getWidth() - x * smallWidth, smallHeight);
                } else {
                    smallImages[x][y] = image.getSubimage(x * smallWidth, y * smallHeight, smallWidth, smallHeight);
                }
                imageCounter++;
            }
        }
        System.out.println("Images created: " + imageCounter);

    }

    private void createDirectoryForSaving() {
        if (!(new File(this.targetDir).mkdirs())) {
            System.err.println("Directory could not be created");
        }
    }

    private void saveSmallImages() {
        imageCounter = 0;
        for (int x = 0; x < columns; x++) {
            new File(targetDir + "/" + x).mkdirs();

            for (int y = 0; y < rows; y++) {
                try {
                    ImageIO.write(smallImages[x][y], "png", new File(targetDir + "/" + x + "/tile-"
                            + y + ".png"));
                    imageCounter++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Images saved: " + imageCounter);

    }
    
    public static void main(String[] args) {
        String imageFilePath = "C:\\Users\\mailt\\Desktop\\airplain.jpg";
        TileMaker.process(imageFilePath);
    }

}
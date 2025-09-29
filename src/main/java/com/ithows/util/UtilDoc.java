/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.util;


import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.sl.draw.Drawable;
import org.apache.poi.sl.extractor.SlideShowExtractor;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;


import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;


/**
 *
 * @author ksyuser
 */
public class UtilDoc {

     public static void main(String[] args) {
        String fileName = "uploadFiles/ddsd/190906 프로젝트 개설신청 방법.hwp";
        String targetPath = "C:\\Users\\mailt\\Desktop\\";
        
         System.out.println("1 <<<<<< "  + FilenameUtils.getExtension(fileName));
         System.out.println("2 <<<<<< "  + FilenameUtils.getBaseName(fileName));
         System.out.println("3 <<<<<< "  + FilenameUtils.getFullPath(fileName));
         System.out.println("4 <<<<<< "  + FilenameUtils.getPrefix(fileName));
         System.out.println("5 <<<<<< "  + FilenameUtils.getPath(fileName));
         System.out.println("6 <<<<<< "  + FilenameUtils.getName(fileName));
        
//        try {
////            UtilDoc.convertPdf (fileName, targetPath + "Sample_convert.pdf");
////              UtilDoc.convertMapToPPT(fileName);
////              UtilDoc.convertMapToPDF(fileName);
//              UtilDoc.convertMapToImage(fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    
    public static ArrayList<String> convertPptToImages(String fileName, String targetPath) throws Exception {

        File file=new File(fileName);
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
        
        ArrayList<String> savedImgList = new ArrayList<>(); //저장된 이미지 경로를 저장하는 List 객체

        //getting the dimensions and size of the slide
        Dimension pgsize = ppt.getPageSize();
        java.util.List<XSLFSlide> slide = ppt.getSlides();

        // set the font
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        for (String s : new String[]{"resource/fonts/malgun.ttf","resource/fonts/NanumBarunGothic.ttf","resource/fonts/NanumGothic.ttf"}) {
//            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(s));
//            ge.registerFont(font);
//        }
        Map<String,String> fallbackMap = new HashMap<String,String>();
        fallbackMap.put("돋움", "나눔고딕");

        double zoom = 2; // magnify it by 2

        for (int i = 0; i < slide.size(); i++) {
            BufferedImage img = unicodeRendering(targetPath +  "slide_" + i + ".png", slide.get(i), pgsize, fallbackMap, zoom);
            savedImgList.add( "slide_" + i + ".png");
        }

        return savedImgList;
    }


    public static String convertPptToPdf(String fileName) {
        
        String filePath = FilenameUtils.getFullPath(fileName) ;
        String fileBaseName = FilenameUtils.getBaseName(fileName);
        String targetName = filePath +  fileBaseName + ".pdf";
        String res = "";

        try{
            File file=new File(fileName);
            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));

            //getting the dimensions and size of the slide
            Dimension pgsize = ppt.getPageSize();
            java.util.List<XSLFSlide> slide = ppt.getSlides();


            // set the font
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    //        for (String s : new String[]{"resource/fonts/malgun.ttf","resource/fonts/NanumBarunGothic.ttf","resource/fonts/NanumGothic.ttf"}) {
    //            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(s));
    //            ge.registerFont(font);
    //        }
            Map<String,String> fallbackMap = new HashMap<String,String>();
            fallbackMap.put("돋움", "나눔고딕");


            double zoom = 2; // magnify it by 2

            FileOutputStream outStream = new FileOutputStream(new File(targetName));
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outStream);
            document.open();
            document.setPageSize(new Rectangle( (int)(pgsize.width * zoom), (int) (pgsize.height * zoom) ));

            for (int i = 0; i < slide.size(); i++) {
                BufferedImage img = unicodeRendering("", slide.get(i), pgsize, fallbackMap, zoom);

                Image image = Image.getInstance(img, null);
                document.newPage();
                image.setAbsolutePosition(0, 0);
                document.add(image);
            }

            document.close();
            writer.close();
            
            res = fileBaseName + ".pdf";
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return res;
        
    }



    public static BufferedImage unicodeRendering(String fileName, XSLFSlide slide, Dimension pgsize,  Map<String,String> fallbackMap, double zoom) throws Exception {

        // render it
        AffineTransform at = new AffineTransform();
        at.setToScale(zoom, zoom);

        BufferedImage img = new BufferedImage((int)Math.ceil(pgsize.width * zoom),
                (int)Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setRenderingHint(Drawable.FONT_FALLBACK, fallbackMap);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        graphics.setTransform(at);
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
        slide.draw(graphics);


        if(fileName != null && !fileName.equals("")) {
            FileOutputStream out = new FileOutputStream(fileName);
            javax.imageio.ImageIO.write(img, "png", out);
            out.close();
        }

        return img;
    }

    public static void printPPTInfo(String fileName) throws IOException, OpenXML4JException {

        PrintStream out = System.out;


        FileInputStream is = new FileInputStream(fileName);
        try (XMLSlideShow ppt = new XMLSlideShow(is)) {
            is.close();

            // Get the document's embedded files.
            for (PackagePart p : ppt.getAllEmbeddedParts()) {
                String type = p.getContentType();
                // typically file name
                String name = p.getPartName().getName();
                out.println("Embedded file (" + type + "): " + name);

                InputStream pIs = p.getInputStream();
                // make sense of the part data
                pIs.close();

            }

            // Get the document's embedded files.
            for (XSLFPictureData data : ppt.getPictureData()) {
                String type = data.getContentType();
                String name = data.getFileName();
                out.println("Picture (" + type + "): " + name);

                InputStream pIs = data.getInputStream();
                // make sense of the image data
                pIs.close();
            }

            // size of the canvas in points
            Dimension pageSize = ppt.getPageSize();
            out.println("Pagesize: " + pageSize);

            for (XSLFSlide slide : ppt.getSlides()) {
                for (XSLFShape shape : slide) {
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape txShape = (XSLFTextShape) shape;
                        out.println(txShape.getText());
                    } else if (shape instanceof XSLFPictureShape) {
                        XSLFPictureShape pShape = (XSLFPictureShape) shape;
                        XSLFPictureData pData = pShape.getPictureData();
                        out.println(pData.getFileName());
                    } else {
                        out.println("Process me: " + shape.getClass());
                    }
                }
            }
        }
    }

    public String docxFileContentParser(String fileName){

        try{
            FileInputStream fs = new FileInputStream(new File(fileName));
            OPCPackage d = OPCPackage.open(fs);
            if(fileName.endsWith(".docx")){
                XWPFWordExtractor xw = new XWPFWordExtractor(d);
                return xw.getText();
            }else if(fileName.endsWith(".pptx")){
                SlideShowExtractor xp = new SlideShowExtractor(new XMLSlideShow(d));
                return xp.getText();
            }else if(fileName.endsWith(".xlsx")){
                XSSFExcelExtractor xe = new XSSFExcelExtractor(d);
                xe.setFormulasNotResults(true);
                xe.setIncludeSheetNames(true);
                return xe.getText();
            }
        }catch(Exception e){
            System.out.println("# DocxFileParser Error :"+e.getMessage());
        }
        return "";
    }    
    

    public static ArrayList<String> conversionPdf2Img(String fileName, String resultImgPath) {


        ArrayList<String> savedImgList = new ArrayList<>(); //저장된 이미지 경로를 저장하는 List 객체

        try {

            File file=new File(fileName);
            FileInputStream is = new FileInputStream(file);
            PDDocument pdfDoc = PDDocument.load(is); //Document 생성
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);

            Files.createDirectories(Paths.get(resultImgPath)); //PDF 2 Img에서는 경로가 없는 경우 이미지 파일이 생성이 안되기 때문에 디렉토리를 만들어준다.

            //순회하며 이미지로 변환 처리
            for (int i=0; i<pdfDoc.getPages().getCount(); i++) {
                String imgFileName = resultImgPath + "/Page" + i + ".png";

                //DPI 설정
                BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);

                // 이미지로 만든다.
                ImageIOUtil.writeImage(bim, imgFileName , 300);

                //저장 완료된 이미지를 list에 추가한다.
                savedImgList.add(imgFileName);
            }
            pdfDoc.close(); //모두 사용한 PDF 문서는 닫는다.
        }  catch (FileNotFoundException e) {
            System.out.println("Pdf file not found. exception message = " + e.getMessage() );
        }     catch (IOException e) {
            System.out.println("Change fail pdf to image. IOException message = " + e.getMessage() );
        }
        return savedImgList;
    }

    public static String PdfFileParser(String pdffilePath) throws FileNotFoundException, IOException
    {
        String content;
        FileInputStream fi = new FileInputStream(new File(pdffilePath));
        PDFParser parser = new PDFParser((RandomAccessRead) fi);
        parser.parse();
        COSDocument cd = parser.getDocument();
        PDFTextStripper stripper = new PDFTextStripper();
        content = stripper.getText(new PDDocument(cd));
        cd.close();
        return content;
    }
    
}

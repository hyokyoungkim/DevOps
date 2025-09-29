/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.util;


import com.ithows.service.ProcessCall;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author ksyuser
 */
public class UtilFile {
    
//         System.out.println("1 <<<<<< "  + FilenameUtils.getExtension(fileName));   // 확장자
//         System.out.println("2 <<<<<< "  + FilenameUtils.getBaseName(fileName));   // 파일명만(확장자 제외)
//         System.out.println("3 <<<<<< "  + FilenameUtils.getFullPath(fileName));   // 파열경로
//         System.out.println("4 <<<<<< "  + FilenameUtils.getPrefix(fileName));     // 드라이브 경로
//         System.out.println("4 <<<<<< "  + FilenameUtils.getPath(fileName));     // 드라이브 뺀 나머지 경로 
//         System.out.println("6 <<<<<< "  + FilenameUtils.getName(fileName));     // 파일명만(확장자 포함)

//         File.separator   // 경로 구분자
//         String rootPath =  System.getProperty("user.home") + "/Desktop";   // 바탕화면 경로    
//         String rootPath = System.getProperty("user.dir");   // 사용자 경로
//         System.lineSeparator();   // 개행문자

//         String servletPath = session.getServletContext().getRealPath("/");

    
    public static void main(String[] args) throws Exception {
        String classPathStr = findSource(UtilFile.class);
        System.out.println("result : " + classPathStr);
        
       // System.out.println("count lines : " + getFileLines(AppConfig.getContextPath() + "/data/kt_measured.csv"));
        
    }


    // 클래스 파일의 물리적 경로 찾기
    public static String findSource(Class<?> clazz) {
        String resourceToSearch = '/' + clazz.getName().replace(".", "/") + ".class";
        String className = clazz.getName();
        String sourcePath = clazz.getResource(resourceToSearch).toString();
        // Optional, Remove junk
        return sourcePath.replace("file:/", "").replaceAll(resourceToSearch, "");
    }


    public static long getFreeSpace() {
        long freeSpace = 0 ;
        try {
            freeSpace = FileSystemUtils.freeSpaceKb("/home/pi");
            System.out.println(freeSpace + "kb");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return freeSpace;

    }

    public static ArrayList<String> getFileNamesInZip(String zipFilePath) throws Exception {
        ZipFile zip = new ZipFile(zipFilePath);

        ArrayList<String> fileNames = new ArrayList<String>();

        for (Enumeration e = zip.entries(); e.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            if (!entry.isDirectory()) {
                fileNames.add(entry.getName());

            }
        }

        return fileNames;
    }

    public static boolean checkShapeFile(String zipFilePath) throws Exception {

        ArrayList<String> fileNames = getFileNamesInZip(zipFilePath);
        Map<String, Integer> map = new HashMap<String, Integer>();
        boolean result = true;
        int num = 0;

        for (int i = 0; i < fileNames.size(); i++) {
//            System.out.println("file " + i + " : " + fileNames.get(i));
//            System.out.println("getExtension " + i + " : " + FilenameUtils.getExtension(fileNames.get(i)));
//            System.out.println("getBaseName " + i + " : " + FilenameUtils.getBaseName(fileNames.get(i)));

            String orgName = FilenameUtils.getBaseName(fileNames.get(i));

            if (FilenameUtils.getExtension(fileNames.get(i)).equals("shp")
                    || FilenameUtils.getExtension(fileNames.get(i)).equals("shx")
                    || FilenameUtils.getExtension(fileNames.get(i)).equals("dbf")) {
                if (map.get(orgName) == null) {
                    map.put(orgName, 1);

                } else {
                    num = map.get(orgName) + 1;
                    map.put(orgName, num);
                }

            }
        }

        for (Map.Entry<String, Integer> elem : map.entrySet()) {
//            System.out.println( String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()) );
            if (elem.getValue() < 3) {
                result = false;
                break;
            }
        }

        return result;
    }

    // unzip 기능에 버그가 있어서 라즈베리파이의 경우는 커맨드 명령을 사용한다.
    // 권한에 대한 문제도 피해 갈 수 있다
    public static void unzip(String zipFilePath, String destDirStr, boolean deleteFile) {

        
//        System.out.println("zipFilePath >>>>  " + zipFilePath);
//        System.out.println("destDirStr >>>>  " + destDirStr);
        
        String OS = System.getProperty("os.name").toLowerCase();
        
        if(OS.indexOf("win") >= 0){ 
             try {
                 net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(zipFilePath);
                 zipFile.extractAll(destDirStr);
                 Thread.sleep(1000);

             } catch (Exception e) {
                 e.printStackTrace();
             } 
        
        }else{
            String[] commandLine = new String[3];
            commandLine[0] = "/bin/sh";
            commandLine[1] = "-c";
            commandLine[2] = "sudo unzip " + zipFilePath + " -d \"" + destDirStr + "\"";
            
            ProcessCall.normalCallCommand(commandLine);
        }
        



        if (deleteFile) {
            new File(zipFilePath).delete();
        }

    }



    public static void makeDir(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            makeDir(file);
        }
        return;
    }

    // 특정 파일의 생성을 위해 디렉토리를 생성
    public static String makeDirForFile(String fileFullName) {
        String filePath = FilenameUtils.getFullPath(fileFullName);
        if(!checkExist(filePath)){
            makeDir(filePath);
        }
        return filePath;
    }

    public static void makeDir(File file) {
        file.mkdirs();
    }


    public static void deleteDir(String dirPath) {
        File dir = new File(dirPath);
        if(!dir.exists() || !dir.isDirectory()){
            return;
        }
        deleteDir(dir);
    }
    
    public static void deleteDir(File dir) {
        
        if(!dir.exists() || !dir.isDirectory()){
            return;
        }
        
        File[] contents = dir.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        dir.delete();
    }
    
    public static boolean deleteFile(String fileName) {
        boolean flag = false;
        File file = new File(fileName);
        if (file.exists()) {
            try{
                flag = Files.deleteIfExists(Paths.get(fileName));
                System.out.println(fileName + " delete - " + flag);
            }catch(Exception ex){
            }
        }
        return flag;
    }
    
    public static boolean renameFile(String fileName, String newName) {
        boolean flag = false;
        File file = new File(fileName);
        File newFile = new File(newName);
        if (file.exists()) {
            file.renameTo(newFile);
            flag = true;
        }
        return flag;
    }

    public static boolean checkExist(String fileName) {
        boolean flag = false;
        File file = new File(fileName);
        if (file.exists()) {
            flag = true;
        }
        return flag;
    }

    public static void copyFile(String source, String dest) throws IOException { 
        if (!checkExist(source)) {
            throw new IOException("Not exist file");
            
        }
        Files.copy(new File(source).toPath(), new File(dest).toPath()); 
    }    
    
    public static void moveFile(String source, String dest) throws IOException { 
        copyFile(source, dest);
        deleteFile(source);
    }    
    
    // 같은 이름의 파일이 존재할 때만 이름을 바꾼다
//    public static String renameFile(String fileName) {
//        
//    }
 
    
    //압축파일 작성
    // 디렉토리의 경우 하위 폴더 압축은 안 됨
    public static boolean zip(String sourcePath, String zipFileName ) throws IOException{
        File file = new File(sourcePath);
        String files[] = null;


        //파일이 디렉토리 일경우 리스트를 읽어오고
        //파일이 디렉토리가 아니면 첫번째 배열에 파일이름을 넣는다.
        if( file.isDirectory() ){
            files = file.list();
        }else{
            files = new String[1];
            files[0] = FilenameUtils.getName(sourcePath);
        }

        sourcePath = FilenameUtils.getFullPath(sourcePath);

        //buffer size
        int size = 1024;
        byte[] buf = new byte[size];

        FileInputStream fis = null;
        ZipArchiveOutputStream zos = null;
        BufferedInputStream bis = null;

        try {
            // Zip 파일생성
            zos = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
            for( int i=0; i < files.length; i++ ){
                //해당 폴더안에 다른 폴더가 있다면 지나간다.
                if( new File(sourcePath + files[i]).isDirectory() ){
                    continue;
                }
                //encoding 설정
                zos.setEncoding("UTF-8");

                System.out.println(sourcePath +  files[i]);
                //buffer에 해당파일의 stream을 입력한다.
                fis = new FileInputStream(sourcePath +  files[i]);
                bis = new BufferedInputStream(fis,size);

                //zip에 넣을 다음 entry 를 가져온다.
                zos.putArchiveEntry(new ZipArchiveEntry(files[i]));


                //준비된 버퍼에서 집출력스트림으로 write 한다.
                int len;
                while((len = bis.read(buf,0,size)) != -1){
                    zos.write(buf,0,len);
                }

                bis.close();
                fis.close();
                zos.closeArchiveEntry();

            }
            zos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }finally{
            if( zos != null ){
                zos.close();
            }
            if( fis != null ){
                fis.close();
            }
            if( bis != null ){
                bis.close();
            }
        }

        return true;
    }

    
    /**
     * 디렉토리의 파일 리스트 받기  
     * 파일리스트만 가져 옴 (디렉토리는 제외) 
     * @param tgtPath
     * @param extention : "json,csv"  - 복수 지정 가능
     * @return 
     */
    public static String[] getFileList(String tgtPath, String extention) {
        
        if(UtilFile.checkExist(tgtPath) == false){
            return null;
        }
        
        File dir = new File(tgtPath);
        String[] filenames = dir.list();
        ArrayList<String> list = new ArrayList<String>();
        
        for (int i = 0; i < filenames.length; i++) {
            File file = new File(tgtPath + filenames[i]);
            if (!file. isDirectory()) {
                if (extention.equals("")){ 
                    list.add(filenames[i]);
                }else if( checkExtension(filenames[i],extention) ) {
                    list.add(filenames[i]);
                }
            }
        }
        
        String [] array;
        array = list.toArray(new String[0]);
        
        return array;
    }
    
    private static boolean checkExtension(String fileName, String extension){
        if(fileName.equals("")){
            return false;
        }
        String srcExt = FilenameUtils.getExtension(fileName);
        
        if(srcExt.equals("")){
            return false;
        }
        
        if(extension.contains(srcExt) ){
            return true;
        }
        
        return false;
    }
    
    public static String[] fileReadLines(String filename)
    {

        FileReader fileReader = null;
        List<String> lines = new ArrayList<String>();

        try {
            fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                lines.add(line);
            }
            bufferedReader.close();

        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        } finally {
            try {
                fileReader.close();
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }

        return (String[])lines.toArray(new String[lines.size()]);
    }

    public static long getFileLines(String fileName){
        
        long len_lines = 0;
        try {
            Path path;
            path = Paths.get(fileName);
            len_lines = Files.lines(path).count();
            
        } catch (Exception e) {
            
        }
            
        return len_lines;
    }
    
    
    // 텍스트 파일의 내용을 바꾸기 기계적으로 바꿈.
    // modifyFile("leafletPage/leaflet.html", "var mapMaxZoom = " , "        var mapMaxZoom = " + nLevel + " ;");
    public static void modifyFile(String filePath, String oldString, String newString) {
        File fileToBeModified = new File(filePath);

        BufferedReader reader = null;
        FileWriter writer = null;

        String contentStr = "";

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent

            String line = reader.readLine();

            while (line != null) {

                //Replacing oldString with newString in the oldContent
                if(line.contains(oldString)){
                    contentStr = contentStr + newString + System.lineSeparator();
                }else {
                    contentStr = contentStr + line + System.lineSeparator();
                }

                line = reader.readLine();
            }

//            String newContent = oldContent.replaceAll(oldString, newString);

            //Rewriting the input text file with newContent
            writer = new FileWriter(fileToBeModified);
            writer.write(contentStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closing the resources
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void modifyStringInFile(String filePath, String oldString, String newString) {
        File fileToBeModified = new File(filePath);

        BufferedReader reader = null;
        FileWriter writer = null;

        String contentStr = "";

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent

            String line = reader.readLine();

            while (line != null) {

                //Replacing oldString with newString in the oldContent
                if(line.contains(oldString)){
                    contentStr = contentStr + newString + System.lineSeparator();
                }else {
                    contentStr = contentStr + line + System.lineSeparator();
                }

                line = reader.readLine();
            }

//            String newContent = oldContent.replaceAll(oldString, newString);

            //Rewriting the input text file with newContent
            writer = new FileWriter(fileToBeModified);
            writer.write(contentStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closing the resources
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public static void writeJSonToFile(String jsonStr, String fileName) {
        UtilJSON.writeJsonToFile(jsonStr, fileName);
    }
    
    public static JSONObject readTextToJSonObject(String fileName) {

        JSONObject jObj = null;
        StringBuffer readBuffer = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                readBuffer.append(line);
            }
            
            jObj = new JSONObject(readBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jObj;
    }
    
    public static JSONArray readTextToJSonArray(String fileName) {

        JSONArray array = null;
        StringBuffer readBuffer = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                readBuffer.append(line);
            }
            
            array = new JSONArray(readBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return array;
    }
    
    
    // CSV 파일을 읽어서 스트링배열 리스트로 담아 줌
    // |를 분리자로 지정시 "\\|" 로 지정해 주어야 함
    public static List<String[]> readFromCsvFile(String separator, String fileName) {
        if(separator.equals("")){
            separator = ",";
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            List<String[]> list = new ArrayList<>();
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] array = line.split(separator);
                list.add(array);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public static String writeToTextFile(String textStr, String filName) {
        try (FileWriter writer = new FileWriter(filName)) {
            writer.append(textStr);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return filName;
    }
    
    // 스트링배열 리스트를 csv로 저장
    public static void writeToCsvFile(List<String[]> thingsToWrite, String separator, String fileName) {
        
        if(separator.equals("")){
            separator = ",";
        }
        
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String[] strings : thingsToWrite) {
                for (int i = 0; i < strings.length; i++) {
                    writer.append(strings[i]);
                    if (i < (strings.length - 1))
                        writer.append(separator);
                }
                writer.append(System.lineSeparator());
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    // 텍스트 파일 합치기 
    //  mergeTextFiles("C:\\Users\\mailt\\Desktop\\내비게이션용DB\\", "C:\\Users\\mailt\\Desktop\\내비게이션용DB\\건물_merge.txt");
    public static void mergeTextFiles(String srcPath, String outFileName) {
        
        try{
                // create instance of directory
             File dir = new File(srcPath);

             // create obejct of PrintWriter for output file
             PrintWriter pw = new PrintWriter(outFileName);

             // Get list of all the files in form of String Array
             String[] fileNames = dir.list();
             String line = "";
             
             // loop for reading the contents of all the files 
             // in the directory GeeksForGeeks
             for (String fileName : fileNames) {
                 System.out.println("Reading from " + fileName);

                // create instance of file from Name of 
                // the file stored in string Array
                File f = new File(dir, fileName);

                // create object of BufferedReader
                BufferedReader br = new BufferedReader(new FileReader(f));
                while ((line = br.readLine()) != null) {
                    pw.println(line);
                }
                pw.flush();
             }
          
        }catch(Exception ex){
            Logger.getLogger(UtilFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    

    /**
     * 파일의 인코딩 변환
     * transformEncoding(path+ filenames[i], "MS949", path + "utf_" + FilenameUtils.getBaseName(filenames[i]) + ".csv" , "UTF-8");
     * @param srcFileName
     * @param srcEncoding
     * @param tgtFileName
     * @param tgtEncoding
     * @throws IOException 
     */
    public static void transformEncoding(String srcFileName, String srcEncoding, String tgtFileName, String tgtEncoding) throws IOException {
        File source = new File(srcFileName);
        File target = new File(tgtFileName);
        
        if(!source.exists()){
            return;
        }
        
        
        BufferedReader br = null;
        BufferedWriter bw = null;
        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(source), srcEncoding));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), tgtEncoding));
            
//            bw.write("district_code|sid|sig|emd|ri|san|jibun_no1|jibun_no2|road_code|basement|building_no1|building_no2|id|sid_eng|sig_eng|emd_eng|ri_eng|move_type|building_manage_no|jusoemd_code\r\n");
//            bw.write("jusoemd_code|sid|sig|emd|road_code|road_name|basement|building_no1|building_no2|post_code|id|building_name|building_use|district_code|district_name|level_high|level_low|building_type|building_count|building_name2|building_history|building_history2|building_live|centerX|centetY|enterX|entetY|sid_eng|sig_eng|emd_eng|road_name_eng|emd_type|move_type\r\n");
            
            char[] buffer = new char[16384];
            int read;
            while ((read = br.read(buffer)) != -1)
                bw.write(buffer, 0, read);
        } finally {
            try {
                if (br != null)
                    br.close();
            } finally {
                if (bw != null)
                    bw.close();
            }
        }
    }
    
    
    /**
     * 두 개 텍스트 파일이 같은지 확인
     * @param srcFileName
     * @param tgtFileName
     * @return 
     */
    public static boolean checkSameFile(String srcFileName, String tgtFileName){
        File source = new File(srcFileName);
        File target = new File(tgtFileName);
        
        if(!source.exists() || !target.exists() ){
            return false;
        }
        
        if(source.length() != target.length() ){
            return false;
        }
        
        
        BufferedReader sourcebr = null;
        BufferedReader targetbr = null;
        String sourceLine = "";
        String targetLine = "";
        
        
        try{
            sourcebr = new BufferedReader(new FileReader(source));
            targetbr = new BufferedReader(new FileReader(target));
            
            char[] sourceBuffer = new char[16384];
            char[] targetBuffer = new char[16384];
            
            int sourceRead;
            int targetRead;
            while ((sourceRead = sourcebr.read(sourceBuffer)) != -1){
                targetRead = targetbr.read(targetBuffer);
                sourceLine = new String(sourceBuffer);
                targetLine = new String(targetBuffer);
                if(!sourceLine.equals(targetLine)){
                    sourcebr.close();
                    targetbr.close();
                    return false;
                }
            }
                
        }catch(Exception ex){    
            
        } finally {
            try {
                if (sourcebr != null)
                    sourcebr.close();
                
                if (targetbr != null)
                    targetbr.close();
            } catch(Exception ex){
                
            }
        }
        
        return true;
    }
    
    
    private static boolean checkSameFileInDir(String srcFileName, String tgtPath){
        
        String[] filenames = getFileList(tgtPath, "") ;
        for (int i=0; i < filenames.length; i++) {
           if (checkSameFile(srcFileName, tgtPath + filenames[i]) ) {
                return true;
            }
        }
        
        return false;
    }
    
    public static void copyUniqueFile(String srcPath, String tgtPath){
        String path = "C:\\Users\\mailt\\Desktop\\station_data\\seoul_kor\\";
        
        if(UtilFile.checkExist(tgtPath)){
            UtilFile.deleteDir(tgtPath);
        }
        UtilFile.makeDir(tgtPath);
        
        String[] filenames = getFileList(srcPath, "") ;
        for (int i=0; i < filenames.length; i++) {
            
            if (checkSameFileInDir(srcPath + filenames[i], tgtPath)) {
                continue;
            }
            
            try {
                copyFile(srcPath + filenames[i], tgtPath + filenames[i]);
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }
    
    public static String getJarPath(){
        String jarDir = "";
        // @@ 패키지의 경로를 얻는 방법
        try {
            CodeSource codeSource = UtilFile.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            jarDir = jarFile.getParentFile().getPath();

            System.out.println("Class Path - " + MACVendorFetcher.class.getResource(MACVendorFetcher.class.getSimpleName() + ".class"));
            System.out.println("Class FullName - " + MACVendorFetcher.class.getName());
            System.out.println("Class Name - " + MACVendorFetcher.class.getSimpleName());
            System.out.println("Jar Path - " + jarDir); //  .getClass().getResource("mac-vendors.txt").toURI());
            System.out.println("Resource Path 1 - " + MACVendorFetcher.class.getResource("/resources/mac-vendors.txt")); //  .getClass().getResource("mac-vendors.txt").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jarDir;
    }
    
    
    public static String addDateTimeInFilenameWithPath(String fileName){
        String newFileName = "";
        newFileName = FilenameUtils.getFullPath(fileName) + FilenameUtils.getBaseName(fileName) + 
                "_" + DateTimeUtils.getTimeDateNow2() + "." + FilenameUtils.getExtension(fileName) ;
        
        return newFileName;
    }
    
    public static String addDateTimeInFilename(String fileName){
        String newFileName = "";
        newFileName = FilenameUtils.getBaseName(fileName) + 
                "_" + DateTimeUtils.getTimeDateNow2() + "." + FilenameUtils.getExtension(fileName) ;
        
        return newFileName;
    }
    
    public static String addDateInFilenameWithPath(String fileName){
        String newFileName = "";
        newFileName = FilenameUtils.getFullPath(fileName) + FilenameUtils.getBaseName(fileName) + 
                "_" + DateTimeUtils.getDateNow() + "." + FilenameUtils.getExtension(fileName) ;
        
        return newFileName;
    }
    
    public static String addDateInFilename(String fileName){
        String newFileName = "";
        newFileName = FilenameUtils.getBaseName(fileName) + 
                "_" + DateTimeUtils.getDateNow() + "." + FilenameUtils.getExtension(fileName) ;
        
        return newFileName;
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////
    
    public void delete(File file) throws IOException {
        if (!file.delete() && file.exists()) {
            throw new IOException("failed to delete " + file);
        }
    }

    public boolean exists(File file) {
        return file.exists();
    }

    public long size(File file) {
        return file.length();
    }

    public void rename(File from, File to) throws IOException {
        delete(to);
        if (!from.renameTo(to)) {
            throw new IOException("failed to rename " + from + " to " + to);
        }
    }

    public void deleteContents(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            int length = files.length;
            int i = 0;
            while (i < length) {
                File file = files[i];
                if (file.isDirectory()) {
                    deleteContents(file);
                }
                if (file.delete()) {
                    i++;
                } else {
                    throw new IOException("failed to delete " + file);
                }
            }
            return;
        }
        throw new IOException("not a readable directory: " + directory);
    }
}

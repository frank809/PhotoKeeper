package com.frankapp;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Formatter;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Metadata;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.drew.metadata.exif.ExifSubIFDDirectory;


/**
 * Created by j34liu on 2016/11/1.
 * from:http://www.javacreed.com/how-to-generate-sha1-hash-value-of-file/
 */
public class filesKeeper {

    private dbKeeper dbsir = new dbKeeper();

    public static String sha1(final File file)
            throws NoSuchAlgorithmException, IOException
    {
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

        try (InputStream is = new BufferedInputStream(new FileInputStream(file)))
        {
            final byte[] buffer = new byte[1024];
            for (int read = 0; (read = is.read(buffer)) != -1;)
            {
                messageDigest.update(buffer, 0, read);
            }
        }

        // Convert the byte to hex format
        try (Formatter formatter = new Formatter())
        {
            for (final byte b : messageDigest.digest())
            {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

    public static Date getFileDate(File jpgfile){
//        File jpegFile = new File("d:\\test.jpg");
        try {
//            Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
//            for (Directory directory : metadata.getDirectories()) {
//                for (Tag tag : directory.getTags()) {
//                    System.out.println(tag);
//                }
//            }

            Metadata metadata  = JpegMetadataReader.readMetadata(jpgfile);
            ExifSubIFDDirectory directory;
            directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);


            try {
                Date date = directory.getDateOriginal();
                System.out.println(date);
                return date;
            }catch(NullPointerException e){
                e.printStackTrace();
                System.out.println("Date would be modify time.");
                return getFileLastModifiedTime(jpgfile);
//                return date.toString();
            }

        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Date(0);
    }

    private static Date getFileLastModifiedTime(File file) {

//        Calendar cal = Calendar.getInstance();
//        long time = file.lastModified();
//        SimpleDateFormat formatter = new
//                SimpleDateFormat("yyyy_MM_dd_HH_mm");
//        cal.setTimeInMillis(time);
//
//        // 输出：修改时间[2] 2009-08-17 10:32:38
//        return formatter.format(cal.getTime());
        Date date = new Date(file.lastModified());
        System.out.println(date.toString());
        return date;

    }

    public void runTidy(String srcBaseDir, String desBaseDir) {

        File file = new File(srcBaseDir);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("The folder is empty.");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("[Folder]:" + file2.getAbsolutePath());
                        runTidy(file2.getAbsolutePath(),desBaseDir);
                    } else {
                        if(file2.getName().toUpperCase().endsWith("JPG") || file2.getName().toUpperCase().endsWith("JPEG")){
                            //System.out.println("[File]:" + file2.getAbsolutePath());
                            try{
                                String sha1Value = sha1(file2.getAbsoluteFile());
                                Date dateDes = getFileDate(file2);
                                System.out.println("[FileName]:"+file2.getAbsolutePath()+"[SHA1]:"+sha1(file2.getAbsoluteFile()));
                                if(this.dbsir.saveSHA2db(sha1Value, dateDes.toString(),file2.getAbsolutePath())){
                                    System.out.println("Start copy....");
                                    String fileExt = file2.getName().substring(file2.getName().lastIndexOf("."));
                                    copyFileOnDate(file2,desBaseDir,sha1Value+fileExt,dateDes);
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            System.out.println("[File]:" + file2.getAbsolutePath());
                        }

                    }
                }
            }
        } else {
            System.out.println("The folder is not exist.");
        }
    }

    private void copyFileOnDate(File srcFile, String desBaseDir,String desFilename, Date fileDate ) throws IOException
    /**
     *
     */
    {
        //create target file path.
        Path srcPath = Paths.get(srcFile.getAbsolutePath());

        Calendar cal = Calendar.getInstance();
        cal.setTime(fileDate);

        Path desPath = Paths.get(desBaseDir+"\\"+cal.get(Calendar.YEAR)+"\\"+(cal.get(Calendar.MONTH)+1)+"\\"+desFilename);
        System.out.println("DestPath:"+desPath.toString());

        try {
            Files.copy(srcPath,desPath, StandardCopyOption.REPLACE_EXISTING);
        }catch(NoSuchFileException ne){

            Files.createDirectories(desPath.getParent());
            Files.copy(srcPath,desPath, StandardCopyOption.REPLACE_EXISTING);
        }catch(Exception e){
            e.printStackTrace();
        }

        //
    }

}
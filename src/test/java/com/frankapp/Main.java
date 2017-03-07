package com.frankapp;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Iterator;


public class Main {

    public static void main(String[] args) throws IOException {

        filesKeeper files = new filesKeeper();
        files.runTidy("src\\test\\resources","target\\out");

//
//        Path path = Paths.get("d:\\temp\\demo\\test.jpg");
//        Path pathd = Paths.get("d:\\temp\\demo\\haha\\dd\\alskdjfiejflasdjkf.rtf");
//        System.out.println(path.toString());
//        System.out.println(path.getFileName());
//        System.out.println(path.getParent());
//        System.out.println(path.getRoot());
//        try {
//            Files.copy(path,pathd,StandardCopyOption.REPLACE_EXISTING);
//        }catch(NoSuchFileException ne){
//
//            Files.createDirectories(pathd.getParent());
//            Files.copy(path,pathd,StandardCopyOption.REPLACE_EXISTING);
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }



    }
}

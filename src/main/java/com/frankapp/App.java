package com.frankapp;

import static java.lang.System.exit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        for(String s: args){
            System.out.println(s);
        }
        if(args.length <2){
            System.out.println("Please give 2 param.");
            exit(0);
        }
        String sourceDir = args[0];
        String targetDir = args[1];
        filesKeeper files = new filesKeeper();
        files.runTidy(sourceDir,targetDir);
        System.out.println( "Hello World!" );
    }
}

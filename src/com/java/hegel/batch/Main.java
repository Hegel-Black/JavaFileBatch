package com.java.hegel.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    
    public final static String TESTDIR = "F:/Downloads/Test/";
    public final static String TESTFILE = "F:/Downloads/Test/demo.txt";

    public static void main(String[] args) {
    	
//    	delAndRename(TESTDIR);
        System.out.println("Success!");
    }
    
    // First del test.java, then rename test.java_bak to test.java
    private static void delAndRename(String dirPath) {
    	File file = new File(dirPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subFile : files) {
                String subPath = replacePathForWin(subFile.getPath());
                if (subFile.isDirectory()) {
                	delAndRename(subPath);
                } else {
                	if (subFile.getName().endsWith("_bak")) {
						String real = subPath.substring(0, subPath.length()-4);
						System.out.println(real);
						if (fileIsExist(real)) {
							new File(real).delete();
						}
						subFile.renameTo(new File(real));
					}
                }
            }
        }
    }
    
    // used for Eclipse workspace, create _bak file.
    private static void setWorkList(String rootPath) {
        File rootFile = new File(rootPath);
        File[] files = rootFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println(file.getName());
                workForDir(rootPath+"/"+file.getName()+"/src");
            }
        }
    }
    
    // used for Android Project, create _bak file.
    private static void workForAndroid(String path) {
        System.out.println("-----" + path + "-----");
        long start = System.currentTimeMillis();
        
        readFromSrc(path + "/Android.mk");
        workForDir(path + "/src");
        
        long end = System.currentTimeMillis();
        float cost = (float)(end - start) / 1000;
        System.out.println("time: " + cost + "s");
    }
    
    private static void workForDir(String dirPath) {
        File file = new File(dirPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subFile : files) {
                String subPath = replacePathForWin(subFile.getPath());
                if (subFile.isDirectory()) {
                    workForDir(subPath);
                } else {
                    if (!subFile.getName().endsWith("_bak")) {
                        readFromSrc(subPath);
                    }
                }
            }
        }
    }
    
    private static void readFromSrc(String srcPath) {
        try {
            File bakFile = new File(srcPath+"_bak");
            if (bakFile.exists()) {
                bakFile.delete();
            }
            
            FileReader mFileReader = new FileReader(srcPath);
            BufferedReader mBufferedReader = new BufferedReader(mFileReader);
            String str = null;
            while ((str = mBufferedReader.readLine()) != null) {
                writeToDest(srcPath+"_bak", str);
            }
            
            mBufferedReader.close();
            mFileReader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void writeToDest(String destPath, String text) {
        try {
            FileWriter mFileWriter;
            if (fileIsExist(destPath)) {
                mFileWriter = new FileWriter(destPath, true);
            } else {
                mFileWriter = new FileWriter(destPath);
            }
            
            BufferedWriter mBufferedWriter = new BufferedWriter(mFileWriter);
            mBufferedWriter.write(text+"\n");
            
            mBufferedWriter.close();
            mFileWriter.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static boolean fileIsExist(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
    }
    
    private static String replacePathForWin(String winPath) {
        String unixPath = winPath.replace('\\', '/');
        return unixPath;
    }

}

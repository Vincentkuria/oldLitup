  package com.vinLitup.Litup;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

  public class Filefinder {

      static ArrayList<File> fileList=new ArrayList<>();
      static String prefileType="";

    public static ArrayList<File> loadFile(File storageDirectory,String fileType){
        //for clearing the file list to avoid adding another type of file from the previous one
        if (!prefileType.equals(fileType)){
            fileList.clear();
        }
        prefileType=fileType;

        File[] directoryFiles=storageDirectory.listFiles();

        if (!(directoryFiles ==null) && directoryFiles.length>0){

            for (int i=0;i<directoryFiles.length;i++){

                if (directoryFiles[i].isDirectory()){

                    loadFile(directoryFiles[i],fileType);

                }else{
                    String fileName=directoryFiles[i].getName().toLowerCase();
                    switch (fileType) {
                        case "PHOTO":
                            for (String extension : Extensions.photoExtensions) {
                                if (fileName.endsWith(extension)) {
                                    fileList.add(directoryFiles[i]);
                                }
                            }

                            break;
                        case "DOCUMENT":
                            Log.d("bigerror", "" + "am on DOCUMENT");
                            for (String extension : Extensions.documentsExtensions) {
                                if (fileName.endsWith(extension)) {
                                    fileList.add(directoryFiles[i]);
                                }
                            }

                            break;
                        case "AUDIO":
                            Log.d("bigerror", "" + "am on AUDIO");
                            for (String extension : Extensions.audioExtensions) {
                                if (fileName.endsWith(extension)) {
                                    fileList.add(directoryFiles[i]);
                                }
                            }

                            break;
                        case "VIDEO":
                            Log.d("bigerror", "" + "am on VIDEO");
                            for (String extension : Extensions.videoExtensions) {
                                if (fileName.endsWith(extension)) {
                                    fileList.add(directoryFiles[i]);
                                }
                            }

                            break;
                    }


                }
            }
        }
        return fileList;
    }

}

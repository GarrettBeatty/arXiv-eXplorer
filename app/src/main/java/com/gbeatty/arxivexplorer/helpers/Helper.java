package com.gbeatty.arxivexplorer.helpers;

import com.gbeatty.arxivexplorer.paper.details.PaperDetailsPresenter;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    public static String convertDateToLocale(String dateStr){
        if(dateStr == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            // handle exception here !
        }

        try{
            String myString = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
            return myString;
        } catch (Exception e){
            return "";
        }
    }

    public static void deleteFilesDir(File dir) {
        try {
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            String name = dir.getName();
            PaperDetailsPresenter.setPaperDownloaded(name, false);
            return dir.delete();
        } else {
            return false;
        }
    }
}

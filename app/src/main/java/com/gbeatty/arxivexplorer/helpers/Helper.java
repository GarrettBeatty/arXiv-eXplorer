package com.gbeatty.arxivexplorer.helpers;

import android.util.Log;

import com.gbeatty.arxivexplorer.paper.details.PaperDetailsPresenter;

import java.io.File;

public class Helper {

    public static void deleteFilesDir(File dir) {
        try {
            Log.d("deleting dir", dir.getAbsolutePath());
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

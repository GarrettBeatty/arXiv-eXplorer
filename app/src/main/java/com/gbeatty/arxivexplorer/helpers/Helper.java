package com.gbeatty.arxivexplorer.helpers;

import android.content.Context;

import com.gbeatty.arxivexplorer.models.Paper;

import java.io.File;
import java.util.List;

public class Helper {

    public static void deleteFilesDir(Context context) {
        try {
            File dir = context.getFilesDir();
            deleteDir(dir);
        } catch (Exception e) {}
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
        } else if(dir!= null && dir.isFile()) {
            String name = dir.getName();
            List<Paper> ps = Paper.find(Paper.class, "paper_id = ?", name);
            Paper p = ps.get(0);
            p.setDownloaded(false);
            p.save();
            return dir.delete();
        } else {
            return false;
        }
    }
}

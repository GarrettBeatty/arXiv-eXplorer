package com.gbeatty.arxivexplorer.base;

import com.gbeatty.arxivexplorer.models.Paper;

import java.util.ArrayList;

public interface BaseView {

    void switchToPapersFragment(ArrayList<Paper> papers, String tag, String query, int maxResult);
    void showLoading();
    void dismissLoading();
    void errorLoading();
}

package com.gbeatty.arxivexplorer.paper.details;

import com.gbeatty.arxivexplorer.paper.base.PapersViewBase;

import java.io.File;

interface PaperDetailsView extends PapersViewBase {
    void viewDownloadedPaper(File downloadedFile);

    File getFilesDir();

    void showLoading();

    void dismissLoading();

    void errorLoading();

    void setDownloadedIcon();

    void setNotDownloadedIcon();
}

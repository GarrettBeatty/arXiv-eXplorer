package com.gbeatty.arxivexplorer.browse.paper.details;

import com.gbeatty.arxivexplorer.browse.paper.base.PapersViewBase;

import java.io.File;

interface PaperDetailsView extends PapersViewBase {
    void viewDownloadedPaper(File downloadedFile);
    File getFilesDir();
    void showLoading();
    void dismissLoading();
    void errorLoading();
}

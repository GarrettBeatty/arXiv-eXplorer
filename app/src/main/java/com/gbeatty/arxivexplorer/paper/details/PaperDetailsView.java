package com.gbeatty.arxivexplorer.paper.details;

import com.gbeatty.arxivexplorer.paper.base.PapersViewBase;

interface PaperDetailsView extends PapersViewBase {

    void showDeletePaperIcon();

    void hideDeletePaperIcon();

    void showDeleteSuccessfulToast();

    void sharePaperURL(String text);
}

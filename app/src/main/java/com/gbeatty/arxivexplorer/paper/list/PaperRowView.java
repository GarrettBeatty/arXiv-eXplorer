package com.gbeatty.arxivexplorer.paper.list;

import com.gbeatty.arxivexplorer.paper.base.PapersViewBase;

interface PaperRowView extends PapersViewBase {

    void hideSummary();

    void showSummary();

    void hidePublishedDate();

    void showLastUpdatedDate();

    void hideLastUpdatedDate();

    void showPublishedDate();

    void hideLatexSummary();

    void showLatexSummary();
}

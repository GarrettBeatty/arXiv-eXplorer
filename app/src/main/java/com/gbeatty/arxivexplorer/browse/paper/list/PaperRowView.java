package com.gbeatty.arxivexplorer.browse.paper.list;

import com.gbeatty.arxivexplorer.browse.paper.base.PapersViewBase;

interface PaperRowView extends PapersViewBase {

    void hideSummary();

    void showSummary();
}

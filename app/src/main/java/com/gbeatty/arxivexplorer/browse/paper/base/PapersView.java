package com.gbeatty.arxivexplorer.browse.paper.base;

import com.gbeatty.arxivexplorer.models.Paper;

public interface PapersView {

    void goToPaperDetails(Paper paper, String tag);
    void showNoPapersMessage();
    void notifyAdapter();
    void showPaginateLoading(boolean isPaginateLoading);
    void showPaginateError(boolean isPaginateError);
    void setPaginateNoMoreData(boolean isNoMoreItems);
}

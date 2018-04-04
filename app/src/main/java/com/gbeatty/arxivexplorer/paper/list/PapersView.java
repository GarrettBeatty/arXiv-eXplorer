package com.gbeatty.arxivexplorer.paper.list;

import com.gbeatty.arxivexplorer.models.Paper;

public interface PapersView{

    void goToPaperDetails(Paper paper, String tag);
    void showNoPapersMessage();
    void notifyAdapter();
    void showPaginateLoading(boolean isPaginateLoading);
    void showPaginateError(boolean isPaginateError);
    void setPaginateNoMoreData(boolean isNoMoreItems);
    void showRecyclerView();
    void setRefreshing(boolean b);

    void scrollToTop();

    void showError();

    String getTag();
}

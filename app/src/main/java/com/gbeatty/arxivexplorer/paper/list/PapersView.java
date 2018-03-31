package com.gbeatty.arxivexplorer.paper.list;

import com.gbeatty.arxivexplorer.base.BaseView;
import com.gbeatty.arxivexplorer.models.Paper;

public interface PapersView extends BaseView{

    void goToPaperDetails(Paper paper, String tag);
    void showNoPapersMessage();
    void notifyAdapter();
    void showPaginateLoading(boolean isPaginateLoading);
    void showPaginateError(boolean isPaginateError);
    void setPaginateNoMoreData(boolean isNoMoreItems);
    void showRecyclerView();
}

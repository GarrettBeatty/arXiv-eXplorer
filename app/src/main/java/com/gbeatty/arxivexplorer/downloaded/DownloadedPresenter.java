package com.gbeatty.arxivexplorer.downloaded;

import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.paper.list.PapersPresenter;
import com.gbeatty.arxivexplorer.paper.list.PapersView;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;
import com.orm.query.Select;

import java.util.ArrayList;

class DownloadedPresenter extends PapersPresenter {

    public DownloadedPresenter(PapersView view, SharedPreferencesView sharedPreferencesView) {
        super(view, sharedPreferencesView);
        setQuery(null);
    }

    @Override
    public void getPapers() {
        getView().setRefreshing(true);
        getView().setRefreshing(false);
        updatePapers(
                (ArrayList<Paper>) Select.from(Paper.class)
                        .where("downloaded = 1")
                        .orderBy("id desc")
                        .list());
    }

}

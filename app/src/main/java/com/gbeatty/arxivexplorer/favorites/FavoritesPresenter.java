package com.gbeatty.arxivexplorer.favorites;

import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.paper.list.PapersPresenter;
import com.gbeatty.arxivexplorer.paper.list.PapersView;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;
import com.orm.query.Select;

import java.util.ArrayList;

class FavoritesPresenter extends PapersPresenter {

    public FavoritesPresenter(PapersView view, SharedPreferencesView sharedPreferencesView) {
        super(view, sharedPreferencesView);
        setQuery(null);
    }

    @Override
    public void getPapers() {
        getView().setRefreshing(true);
        getView().setRefreshing(false);
        updatePapers((ArrayList<Paper>)
                Select.from(Paper.class)
                        .orderBy("id desc")
                        .list());
    }

}

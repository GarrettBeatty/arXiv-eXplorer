package com.gbeatty.arxivexplorer.paper.base;

import com.gbeatty.arxivexplorer.base.BasePresenter;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import java.util.List;

public class PapersPresenterBase extends BasePresenter {

    protected PapersPresenterBase(SharedPreferencesView sharedPreferencesView) {
        super(sharedPreferencesView);
    }

    protected boolean isPaperFavorited(String paperID) {
        return Paper.count(Paper.class, "paper_id = ? and favorited = 1", new String[]{paperID}) > 0;
    }

    private void favoritePaper(Paper paper) {
        savePaperIfDoesntExist(paper);
        List<Paper> ps = Paper.find(Paper.class, "paper_id = ?", paper.getPaperID());
        ps.get(0).setFavorited(true);
        ps.get(0).save();
    }

    private void unfavoritePaper(String paperID) {
        List<Paper> ps = Paper.find(Paper.class, "paper_id = ?", paperID);
        ps.get(0).setFavorited(false);
        ps.get(0).save();
    }

    protected void toggleFavoritePaper(Paper paper) {
        if (isPaperFavorited(paper.getPaperID())) unfavoritePaper(paper.getPaperID());
        else favoritePaper(paper);
    }

    public void savePaperIfDoesntExist(Paper paper) {
        Paper p = Paper.findById(Paper.class, paper.getId());
        if (p == null) {
            paper.save();
        }
    }
}

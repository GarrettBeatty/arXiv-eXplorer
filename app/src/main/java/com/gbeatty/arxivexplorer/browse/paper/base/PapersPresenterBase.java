package com.gbeatty.arxivexplorer.browse.paper.base;

import com.gbeatty.arxivexplorer.base.BasePresenter;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

public class PapersPresenterBase extends BasePresenter{

    public PapersPresenterBase(SharedPreferencesView sharedPreferencesView) {
        super(sharedPreferencesView);
    }

    protected boolean isPaperFavorited(String paperID) {
        return Paper.count(Paper.class, "paper_id = ?", new String[]{paperID}) > 0;
    }

    private void favoritePaper(Paper paper) {
        Paper p = new Paper(paper.getTitle(), paper.getAuthor(), paper.getSummary(), paper.getUpdatedDate(), paper.getPublishedDate(), paper.getPaperID(), paper.getPDFURL());
        p.save();
    }

    private void unfavoritePaper(String paperID) {
        Paper.deleteAll(Paper.class, "paper_id = ?", paperID);
    }

    protected void toggleFavoritePaper(Paper paper) {
        if (isPaperFavorited(paper.getPaperID())) unfavoritePaper(paper.getPaperID());
        else favoritePaper(paper);
    }

}

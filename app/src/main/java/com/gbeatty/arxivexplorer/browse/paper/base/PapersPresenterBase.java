package com.gbeatty.arxivexplorer.browse.paper.base;

import com.gbeatty.arxivexplorer.models.Paper;

public class PapersPresenterBase {

    protected boolean isPaperFavorited(Paper paper) {
        return Paper.count(Paper.class, "paper_id = ?", new String[]{paper.getPaperID()}) > 0;
    }

    private void favoritePaper(Paper paper) {
        Paper p = new Paper(paper.getTitle(), paper.getAuthor(), paper.getSummary(), paper.getUpdatedDate(), paper.getPublishedDate(), paper.getPaperID(), paper.getPDFURL());
        p.save();
    }

    private void unfavoritePaper(Paper paper) {
        Paper.deleteAll(Paper.class, "paper_id = ?", paper.getPaperID());
    }

    protected void toggleFavoritePaper(Paper paper) {
        if (isPaperFavorited(paper)) unfavoritePaper(paper);
        else favoritePaper(paper);
    }

}

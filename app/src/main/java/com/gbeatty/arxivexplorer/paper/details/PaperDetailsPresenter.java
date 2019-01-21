package com.gbeatty.arxivexplorer.paper.details;

import com.gbeatty.arxivexplorer.helpers.Helper;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.paper.base.PapersPresenterBase;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import java.io.File;

public class PaperDetailsPresenter extends PapersPresenterBase {

    private final PaperDetailsView view;
    private final Paper paper;

    PaperDetailsPresenter(PaperDetailsView view, SharedPreferencesView sharedPreferencesView, Paper paper) {
        super(sharedPreferencesView);
        this.view = view;
        this.paper = paper;
    }


    void initializeMainContent() {
        view.setTitle(paper.getTitle());
        view.setAuthors(paper.getAuthor());
        view.setPaperID("ID: " + paper.getPaperID());
        view.setLastUpdatedDate("Updated: " + Helper.convertDateToLocale(paper.getUpdatedDate()));
        view.setPublishedDate("Submitted: " + Helper.convertDateToLocale(paper.getPublishedDate()));
        view.setPaperCategories(paper.getCategories());

        view.hideSummary();
        view.hideLatexSummary();

//        view.hideTitle();
        view.hideLatexTitle();

        if (getSharedPreferenceView().isRenderLatex()) {
            view.showLatexSummary();
            view.showLatexTitle();
            view.setLatexSummary(paper.getSummary());
            view.setLatexTitle(paper.getTitle());
        } else {
            view.showSummary();
            view.showTitle();
            view.setSummary(paper.getSummary());
//            view.setTitle(paper.getTitle());
        }

    }

    void updateFavoritedMenuItem() {
        if (isPaperFavorited(paper.getPaperID())) {
            view.setFavoritedIcon();
        } else {
            view.setNotFavoritedIcon();
        }
    }

    void updateDownloadedMenuItem() {
        if (isPaperDownloaded(paper.getPaperID())) {
            view.setDownloadedIcon();
        } else {
            view.setNotDownloadedIcon();
        }
    }

    public void navigationFavoritePaperClicked() {
        toggleFavoritePaper(paper);
        updateFavoritedMenuItem();
    }

    public void updateDeletePaperMenuItem() {
        if(isPaperDownloaded(paper.getPaperID())){
            view.showDeletePaperIcon();
        }else{
            view.hideDeletePaperIcon();
        }
    }

    public void navigationDeletePaperClicked() {
        File papersPath = new File(view.getFilesDir(), "papers");
        File file = new File(papersPath, paper.getPaperID());
        Helper.deleteFilesDir(file);
        view.showDeleteSuccessfulToast();
    }

    public void navigationDownloadPaperClicked() {
        viewFile(paper, view);
        updateDownloadedMenuItem();
    }

    public void navigationSharePaperClicked() {
        view.sharePaperURL(paper.getPaperURL());
    }
}

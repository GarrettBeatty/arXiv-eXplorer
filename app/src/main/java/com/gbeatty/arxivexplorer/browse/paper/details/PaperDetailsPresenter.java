package com.gbeatty.arxivexplorer.browse.paper.details;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.browse.paper.base.PapersPresenterBase;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

class PaperDetailsPresenter extends PapersPresenterBase {

    private PaperDetailsView view;
    private Paper paper;

    PaperDetailsPresenter(PaperDetailsView view, SharedPreferencesView sharedPreferencesView, Paper paper) {
        super(sharedPreferencesView);
        this.view = view;
        this.paper = paper;
    }

    void initializeMainContent() {
        view.setTitle(paper.getTitle());
        view.setAuthors(paper.getAuthor());
        view.setSummary(paper.getSummary());
        view.setUpdatedDate("Updated: " + paper.getUpdatedDate());
        view.setPublishedDate("Published: " + paper.getPublishedDate());
    }

    void updateMenuItems() {
        if (isPaperFavorited(paper.getPaperID())) {
            view.setFavoritedIcon();
        } else {
            view.setNotFavoritedIcon();
        }
    }

    private void navigationFavoritePaperClicked() {
        toggleFavoritePaper(paper);
        updateMenuItems();
    }

    private void navigationDownloadPaperClicked() {

        File papersPath = new File(view.getFilesDir(), "papers");
        File file = new File(papersPath, paper.getPaperID());

        if (file.exists()) {
            view.viewDownloadedPaper(file);
            return;
        }

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();


        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        downloadPDFandView(file);
    }

    private void downloadPDFandView(File file) {
        view.showLoading();

        ArxivAPI.downloadFileFromURL(paper.getPDFURL(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.errorLoading();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                BufferedSink sink = Okio.buffer(Okio.sink(file));
                sink.writeAll(response.body().source());
                sink.close();
                view.dismissLoading();
                view.viewDownloadedPaper(file);
            }
        });
    }

    boolean onNavigationItemSelected(int id) {
        switch (id) {
            case R.id.menu_favorite_paper:
                navigationFavoritePaperClicked();
                return true;
            case R.id.menu_download_paper:
                navigationDownloadPaperClicked();
                return true;
        }
        return false;
    }


}

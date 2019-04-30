package com.gbeatty.arxivexplorer.paper.base;

import android.util.Log;

import com.gbeatty.arxivexplorer.base.BasePresenter;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class PapersPresenterBase extends BasePresenter {

    protected PapersPresenterBase(SharedPreferencesView sharedPreferencesView) {
        super(sharedPreferencesView);
    }


    public static void setPaperDownloaded(String paperID, boolean downloaded) {
        List<Paper> ps = Paper.find(Paper.class, "paper_id = ?", paperID);
        if (ps == null || ps.isEmpty()) return;
        Paper p = ps.get(0);
        p.setDownloaded(downloaded);
        p.save();
    }

    protected void viewFile(Paper paper, PapersViewBase view){

        Log.d("Garrett", "Beatty");

        File papersPath = new File(view.getFilesDir(), "papers");
        File file = new File(papersPath, paper.getPaperID());
        savePaperIfDoesntExist(paper);

        if (file.exists()) {
            setPaperDownloaded(paper.getPaperID(), true); //if the user reinstalls the app
            view.viewDownloadedPaper(file);
        }
        else{
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            downloadPDFandView(paper, file, view);
        }
    }

    private void downloadPDFandView(Paper paper, File file, PapersViewBase view) {
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
                setPaperDownloaded(paper.getPaperID(), true);
                view.viewDownloadedPaper(file);
            }
        });
    }

    protected boolean isPaperFavorited(String paperID) {
        return Paper.count(Paper.class, "paper_id = ? and favorited = 1", new String[]{paperID}) > 0;
    }

    protected boolean isPaperRead(String paperID) {
        return Paper.count(Paper.class, "paper_id = ? and read = 1", new String[]{paperID}) > 0;
    }

    protected boolean isPaperDownloaded(String paperID) {
        return Paper.count(Paper.class, "paper_id = ? and downloaded = 1", new String[]{paperID}) > 0;
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

package com.gbeatty.arxivexplorer.browse;

import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.network.Parser;
import com.gbeatty.arxivexplorer.paper.list.PapersPresenter;
import com.gbeatty.arxivexplorer.paper.list.PapersView;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

class BrowsePresenter extends PapersPresenter {

    private final String catKey;
    private final String shortName;

    public BrowsePresenter(PapersView view, SharedPreferencesView sharedPreferencesView, String catKey, String shortName) {
        super(view, sharedPreferencesView);
        this.catKey = catKey;
        this.shortName = shortName;
    }

    @Override
    public void getPapers() {
        downloadPapersFromCategory(catKey, shortName,
                getSharedPreferenceView().getSortOrder(),
                getSharedPreferenceView().getSortBy(),
                getSharedPreferenceView().getMaxResult());
    }

    private void downloadPapersFromCategory(String catKey, String category, String sortOrder, String sortBy, int maxResult) {
        try {
            getView().setRefreshing(true);
            ArxivAPI.searchPapersFromCategory(catKey, category,
                    sortOrder,
                    sortBy,
                    maxResult,
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (!call.isCanceled()){

                            }
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful())
                                    throw new IOException("Unexpected code " + response);
                                ArrayList<Paper> papers = Parser.parse(responseBody.byteStream());
                                responseBody.close();

                                getView().setRefreshing(false);
                                setQuery(response.request().url().toString());
                                updatePapers(papers);

                            } catch (XmlPullParserException | ParseException e) {
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

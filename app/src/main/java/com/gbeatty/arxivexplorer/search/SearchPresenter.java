package com.gbeatty.arxivexplorer.search;

import com.gbeatty.arxivexplorer.base.BasePresenter;
import com.gbeatty.arxivexplorer.helpers.Tags;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.network.Parser;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SearchPresenter extends BasePresenter{

    private SearchView view;

    public SearchPresenter(SearchView view, SharedPreferencesView sharedPreferencesView) {
        super(sharedPreferencesView);
        this.view = view;
    }

    public void buttonStartDateClicked() {
        view.showStartDatePicker(2018, 3, 3);
    }

    public void buttonEndDateClicked() {
        view.showEndDatePicker(2018, 3, 3);
    }

    public void onStartDateSet(int startYear, int starthMonth, int startDay) {
        view.setStartDateText(starthMonth + "-" + startDay + "-" + startYear);
    }

    public void onEndDateSet(int startYear, int starthMonth, int startDay) {
        view.setEndDateText(starthMonth + "-" + startDay + "-" + startYear);
    }

    public void buttonSearchClicked(){
        String title = view.getPaperTitleField();
        String summary = view.getPaperSummaryField();
        String authorsString = view.getPaperAuthorsField();
        String id = view.getPaperIDField();
        String[] authors = authorsString.split(",");
        downloadPapersFromSearch(title, authors, summary,null, null, id,
                getSharedPreferenceView().getSortOrder(),
                getSharedPreferenceView().getSortBy(),
                getSharedPreferenceView().getMaxResult());
    }

    private void downloadPapersFromSearch(String title, String[] authors, String summary, String catKey, String category, String id, String sortOrder, String sortBy, int maxResult) {
        try {
            view.showLoading();
            ArxivAPI.searchPapers(title, authors, summary, catKey, category, id,
                    sortOrder,
                    sortBy,
                    maxResult,
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            view.errorLoading();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful())
                                    throw new IOException("Unexpected code " + response);
                                ArrayList<Paper> papers = Parser.parse(responseBody.byteStream());
                                responseBody.close();
                                view.dismissLoading();
                                view.hideKeyboard();
                                view.switchToPapersFragment(papers, Tags.SEARCH_RESULTS_TAG, response.request().url().toString(), maxResult);
                            } catch (XmlPullParserException | ParseException e) {
                                view.errorLoading();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            view.errorLoading();
        }
    }

}

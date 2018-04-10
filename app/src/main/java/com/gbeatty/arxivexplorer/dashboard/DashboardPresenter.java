package com.gbeatty.arxivexplorer.dashboard;

import com.gbeatty.arxivexplorer.arxivdata.Categories;
import com.gbeatty.arxivexplorer.models.Category;
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

class DashboardPresenter extends PapersPresenter {

    public DashboardPresenter(PapersView view, SharedPreferencesView sharedPreferencesView) {
        super(view, sharedPreferencesView);
    }

    @Override
    public void getPapers() {
        downloadPapersFromDashboard(
                getToggledCatKeys().toArray(new String[0]),
                getToggledCategoriesNames().toArray(new String[0]),
                getSharedPreferenceView().getSortOrder(),
                getSharedPreferenceView().getSortBy(),
                getSharedPreferenceView().getMaxResult());
    }

    private ArrayList<String> getToggledCatKeys() {
        ArrayList<String> catKeys = new ArrayList<>();
        for (Category category : Categories.CATEGORIES) {

            if (category.getSubCategories().length != 0) {
                for (Category c : category.getSubCategories()) {
                    if (c.getCatKey().equals("all")) continue;
                    if (getSharedPreferenceView().isDashboardCategoryChecked(c.getShortName())) {
                        catKeys.add(c.getCatKey());
                    }
                }
            } else {
                if (getSharedPreferenceView().isDashboardCategoryChecked(category.getShortName())) {
                    catKeys.add(category.getCatKey());
                }
            }


        }
        return catKeys;
    }

    private ArrayList<String> getToggledCategoriesNames() {
        ArrayList<String> categoriesNames = new ArrayList<>();
        for (Category category : Categories.CATEGORIES) {

            if (category.getSubCategories().length != 0) {
                for (Category c : category.getSubCategories()) {
                    if (c.getCatKey().equals("all")) continue;
                    if (getSharedPreferenceView().isDashboardCategoryChecked(c.getShortName())) {
                        categoriesNames.add(c.getShortName());
                    }
                }
            } else {
                if (getSharedPreferenceView().isDashboardCategoryChecked(category.getShortName())) {
                    categoriesNames.add(category.getShortName());
                }
            }


        }
        return categoriesNames;
    }

    private void downloadPapersFromDashboard(String[] catKeys, String[] categories, String sortOrder, String sortBy, int maxResult) {
        try {
//            getView().setRefreshing(true);
            ArxivAPI.searchMultipleCategories(catKeys, categories,
                    sortOrder,
                    sortBy,
                    maxResult,
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            errorLoading();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful())
                                    throw new IOException("Unexpected code " + response);
                                ArrayList<Paper> papers = Parser.parse(responseBody.byteStream());
                                responseBody.close();

                                setQuery(response.request().url().toString());
                                updatePapers(papers);

                            } catch (XmlPullParserException | ParseException e) {
                                errorLoading();
                            }
                        }
                    });
        } catch (Exception e) {
            errorLoading();
        }
    }
}

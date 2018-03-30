package com.gbeatty.arxivexplorer.browse.category;

import com.gbeatty.arxivexplorer.base.BasePresenter;
import com.gbeatty.arxivexplorer.helpers.Tags;
import com.gbeatty.arxivexplorer.models.Category;
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

class CategoriesPresenter extends BasePresenter {

    private Category[] categories;
    private CategoriesView view;

    CategoriesPresenter(CategoriesView view, SharedPreferencesView sharedPreferencesView, Category[] categories) {
        super(sharedPreferencesView);
        this.view = view;
        this.categories = categories;
    }

    void onBindCategoryRowViewAtPosition(int position, CategoryRowView categoryRowView) {
        final Category category = categories[position];
        categoryRowView.setCategoryName(category.getName());
    }

    void categoryClicked(int position) {

        Category category = categories[position];

        if (category.getSubCategories().length > 0)
            view.goToSubCategories(category.getSubCategories(), Tags.SUB_CATEGORIES_TAG);
        else {
            downloadPapersFromCategory(category.getCatKey(), category.getShortName(),
                    getSharedPreferenceView().getSortOrder(),
                    getSharedPreferenceView().getSortBy(),
                    getSharedPreferenceView().getMaxResult());
        }
    }

    private void downloadPapersFromCategory(String catKey, String category, String sortOrder, String sortBy, int maxResult) {
        try {
            view.showLoading();
            ArxivAPI.searchPapersFromCategory(catKey, category,
                    sortOrder,
                    sortBy,
                    maxResult,
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (!call.isCanceled())
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
                                view.switchToPapersFragment(papers, Tags.CATEGORY_RESULTS_TAG,
                                        response.request().url().toString(), maxResult);
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


    int getCategoriesRowsCount() {
        return categories.length;
    }
}

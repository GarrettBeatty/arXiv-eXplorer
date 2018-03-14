package com.gbeatty.arxivexplorer.main;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.arxivdata.Categories;
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

class MainPresenter extends BasePresenter{

    private MainView view;


    MainPresenter(MainView view, SharedPreferencesView sharedPreferencesView) {
        super(sharedPreferencesView);
        this.view = view;
    }

    boolean onNavigationItemSelected(int id) {
        switch (id) {
            case R.id.navigation_browse:
                if (view.getCurrentFragment().getTag().equals(Tags.MAIN_CATEGORIES_TAG)) return false;
                switchToCategoriesFragment();
                return true;

//            case R.id.navigation_search:
//                if (view.getCurrentFragment().getTag().equals(Tags.SEARCH_FRAGMENT_TAG)) return false;
//                view.switchToSearchFragment(Tags.SEARCH_FRAGMENT_TAG);
//                return true;

            case R.id.navigation_favorites:
                if (view.getCurrentFragment().getTag().equals(Tags.FAVORITES_FRAGMENT_TAG)) return false;
                view.switchToFavoritesFragment((ArrayList<Paper>) Paper.listAll(Paper.class), Tags.FAVORITES_FRAGMENT_TAG);
                return true;
        }
        return false;
    }

    void switchToCategoriesFragment() {
        view.switchToCategoriesFragment(Categories.CATEGORIES, Tags.MAIN_CATEGORIES_TAG);
    }

    public boolean onOptionsItemSelected(int itemId) {
        switch (itemId){
            case R.id.menu_settings:
                view.goToSettings();
                return true;
            case R.id.menu_rating:
                view.goToRating();
                return true;
        }
        return false;
    }

    public void cancelHttpCalls() {
        for(Call call : ArxivAPI.getClient().dispatcher().queuedCalls()) {
           call.cancel();
        }
        for(Call call : ArxivAPI.getClient().dispatcher().runningCalls()) {
           call.cancel();
        }
    }

    public void onQueryTextSubmit(String query) {
        query = "\"" + query + "\"";
        downloadPapersFromSearch(query, getSharedPreferenceView().getSortOrder(), getSharedPreferenceView().getSortBy(), getSharedPreferenceView().getMaxResult());
    }

    private void downloadPapersFromSearch(String searchQuery, String sortOrder, String sortBy, int maxResult) {
        try {
            view.showLoading();
            ArxivAPI.searchAll(searchQuery,
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
//                                view.hideKeyboard();
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

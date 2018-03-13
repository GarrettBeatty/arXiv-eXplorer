package com.gbeatty.arxivexplorer.main;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.arxivdata.Categories;
import com.gbeatty.arxivexplorer.helpers.Tags;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;

import java.util.ArrayList;

import okhttp3.Call;

class MainPresenter {

    private MainView view;


    MainPresenter(MainView view) {
        this.view = view;
    }

    boolean onNavigationItemSelected(int id) {
        switch (id) {
            case R.id.navigation_browse:
                if (view.getCurrentFragment().getTag().equals(Tags.MAIN_CATEGORIES_TAG)) return false;
                switchToCategoriesFragment();
                return true;

            case R.id.navigation_search:
                if (view.getCurrentFragment().getTag().equals(Tags.SEARCH_FRAGMENT_TAG)) return false;
                view.switchToSearchFragment(Tags.SEARCH_FRAGMENT_TAG);
                return true;

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
}

package com.gbeatty.arxivexplorer.main;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.arxivdata.Categories;
import com.gbeatty.arxivexplorer.base.BasePresenter;
import com.gbeatty.arxivexplorer.helpers.Tags;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import okhttp3.Call;

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

            case R.id.navigation_dashboard:
                if (view.getCurrentFragment().getTag().equals(Tags.DASHBOARD_FRAGMENT_TAG)) return false;
                switchToDashboardFragment();
                return true;

            case R.id.navigation_favorites:
                if (view.getCurrentFragment().getTag().equals(Tags.FAVORITES_FRAGMENT_TAG)) return false;
                switchToFavoritesFragment();
                return true;
        }
        return false;
    }

    public void switchToCategoriesFragment(){
        view.switchToCategoriesFragment(Categories.CATEGORIES, Tags.MAIN_CATEGORIES_TAG);
    }

    private void switchToFavoritesFragment(){
        view.switchToFavoritesFragment(Tags.FAVORITES_FRAGMENT_TAG);
    }

    private void switchToDashboardFragment(){
        view.switchToDashboardFragment(Tags.DASHBOARD_FRAGMENT_TAG);
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

    public void onQueryTextSubmit(String searchQuery) {
        view.switchToSearchFragment(searchQuery, Tags.SEARCH_RESULTS_TAG);
    }

}

package com.gbeatty.arxivexplorer.main;


import android.support.v4.app.Fragment;

import com.gbeatty.arxivexplorer.base.BaseView;
import com.gbeatty.arxivexplorer.models.Category;
import com.gbeatty.arxivexplorer.models.Paper;

import java.util.ArrayList;

public interface MainView extends BaseView{

    void switchToCategoriesFragment(Category[] categories, String tag);

    void switchToFavoritesFragment(ArrayList<Paper> papers, String tag);

//    void switchToSearchFragment(String tag);

    Fragment getCurrentFragment();

    void goToSettings();

    void goToRating();

}

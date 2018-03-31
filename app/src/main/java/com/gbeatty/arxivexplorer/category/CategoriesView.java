package com.gbeatty.arxivexplorer.category;

import com.gbeatty.arxivexplorer.base.BaseView;
import com.gbeatty.arxivexplorer.models.Category;

public interface CategoriesView extends BaseView {
    void goToSubCategories(Category[] categories, String tag);
    void switchToBrowseFragment(String catKey, String shortName, String tag);
}

package com.gbeatty.arxivexplorer.category;

import com.gbeatty.arxivexplorer.models.Category;

interface CategoriesView {
    void goToSubCategories(Category[] categories, String tag);
    void switchToBrowseFragment(String catKey, String shortName, String tag);
}

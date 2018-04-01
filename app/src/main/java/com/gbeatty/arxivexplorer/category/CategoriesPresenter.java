package com.gbeatty.arxivexplorer.category;

import com.gbeatty.arxivexplorer.base.BasePresenter;
import com.gbeatty.arxivexplorer.helpers.Tags;
import com.gbeatty.arxivexplorer.models.Category;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

class CategoriesPresenter extends BasePresenter {

    private final Category[] categories;
    private final CategoriesView view;

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
            view.switchToBrowseFragment(category.getCatKey(), category.getShortName(), Tags.CATEGORY_RESULTS_TAG);
        }
    }



    int getCategoriesRowsCount() {
        return categories.length;
    }
}

package com.gbeatty.arxivexplorer.browse.category;

import com.gbeatty.arxivexplorer.base.BaseView;
import com.gbeatty.arxivexplorer.models.Category;

public interface CategoriesView extends BaseView {
    void goToSubCategories(Category[] categories, String tag);
}

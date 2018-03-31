package com.gbeatty.arxivexplorer.settings;

public interface SharedPreferencesView {

    String getSortOrder();
    String getSortBy();
    int getMaxResult();
    boolean isShowAbstract();
    boolean isDashboardCategoryChecked(String categoryName);
}

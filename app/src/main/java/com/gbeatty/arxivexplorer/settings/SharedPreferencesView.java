package com.gbeatty.arxivexplorer.settings;

public interface SharedPreferencesView {

    String getSortOrder();

    String getSortBy();

    int getMaxResult();

    boolean isShowAbstract();

    boolean isDashboardCategoryChecked(String categoryName);

    boolean isLastUpdatedDate();

    boolean isPublishedDate();

    boolean isRelevanceDate();

    boolean isRenderLatex();

    boolean isReadIndicator();
}

package com.gbeatty.arxivexplorer.search;

import com.gbeatty.arxivexplorer.base.BaseView;

public interface SearchView extends BaseView{

    void showStartDatePicker(int startYear, int starthMonth, int startDay);

    void showEndDatePicker(int startYear, int starthMonth, int startDay);

    void setStartDateText(String date);

    void setEndDateText(String date);

    String getPaperTitleField();

    String getPaperAuthorsField();

    String getPaperSummaryField();

    String getPaperIDField();

    void hideKeyboard();
}

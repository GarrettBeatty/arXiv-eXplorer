package com.gbeatty.arxivexplorer.search;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.base.BaseFragment;
import com.gbeatty.arxivexplorer.browse.paper.base.PapersFragment;
import com.gbeatty.arxivexplorer.models.Paper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends BaseFragment implements SearchView {
//
//    @BindView(R.id.button_start_date)
//    EditText buttonStartDate;
//    @BindView(R.id.button_end_date)
//    EditText buttonEndDate;


    @BindView(R.id.button_search)
    Button buttonSearch;

    @BindView(R.id.input_title)
    TextView title;
    @BindView(R.id.input_authors)
    TextView authors;
    @BindView(R.id.input_summary)
    TextView summary;
    @BindView(R.id.input_id)
    TextView id;

    private SearchPresenter presenter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        presenter = new SearchPresenter(this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);

//        buttonStartDate.setOnClickListener(view -> presenter.buttonStartDateClicked());
//        buttonEndDate.setOnClickListener(view -> presenter.buttonEndDateClicked());
        buttonSearch.setOnClickListener(view -> presenter.buttonSearchClicked());
        return rootView;
    }

    public void showStartDatePicker(int startYear, int starthMonth, int startDay) {
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> presenter.onStartDateSet(i, i1, i2), startYear, starthMonth, startDay);
        datePickerDialog.show();
    }

    @Override
    public void showEndDatePicker(int startYear, int starthMonth, int startDay) {
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> presenter.onEndDateSet(i, i1, i2), startYear, starthMonth, startDay);
        datePickerDialog.show();
    }

    @Override
    public void setStartDateText(String date) {
//        buttonStartDate.setText(date);
    }

    @Override
    public void setEndDateText(String date) {
//        buttonEndDate.setText(date);
    }

    @Override
    public String getPaperTitleField() {
        return String.valueOf(title.getText());
    }

    @Override
    public String getPaperAuthorsField() {
        return String.valueOf(authors.getText());
    }

    @Override
    public String getPaperSummaryField() {
        return String.valueOf(summary.getText());
    }

    @Override
    public String getPaperIDField() {
        return String.valueOf(id.getText());
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            if(getActivity().getCurrentFocus() != null){
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void switchToPapersFragment(ArrayList<Paper> papers, String tag, String query, int maxResult){
        getActivity().runOnUiThread(() -> showFragment(R.id.content, PapersFragment.newInstance(papers, query, maxResult), tag));
    }
}

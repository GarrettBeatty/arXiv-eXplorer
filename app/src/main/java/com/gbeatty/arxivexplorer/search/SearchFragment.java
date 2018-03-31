package com.gbeatty.arxivexplorer.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gbeatty.arxivexplorer.paper.list.PapersFragment;
import com.gbeatty.arxivexplorer.paper.list.PapersPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends PapersFragment {

    private static final String SEARCH_QUERY_KEY = "searchquerykey";

    private SearchPresenter presenter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String searchQuery) {
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(setArgs(searchQuery));
        return fragment;
    }

    public static Bundle setArgs(String searchQuery) {
        Bundle args = new Bundle();
        args.putString(SEARCH_QUERY_KEY, searchQuery);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String searchQuery = getArguments().getString(SEARCH_QUERY_KEY);
        presenter = new SearchPresenter(this, this, searchQuery);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected PapersPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected boolean isPaginate() {
        return true;
    }
}

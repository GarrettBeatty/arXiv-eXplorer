package com.gbeatty.arxivexplorer.browse;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gbeatty.arxivexplorer.paper.list.PapersFragment;
import com.gbeatty.arxivexplorer.paper.list.PapersPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends PapersFragment {

    private BrowsePresenter presenter;
    private static final String CATKEYKEY = "catkeykey";
    private static final String SHORTNAMEKEY = "shortnamekey";

    public BrowseFragment() {
        // Required empty public constructor
    }

    @Override
    protected PapersPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected boolean isPaginate() {
        return true;
    }

    public static BrowseFragment newInstance(String catKey, String shortName) {
        BrowseFragment fragment = new BrowseFragment();
        fragment.setArguments(setArgs(catKey, shortName));
        return fragment;
    }

    private static Bundle setArgs(String catKey, String shortName) {
        Bundle args = new Bundle();
        args.putString(CATKEYKEY, catKey);
        args.putString(SHORTNAMEKEY, shortName);

        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String catKey = getArguments().getString(CATKEYKEY);
        String shortName = getArguments().getString(SHORTNAMEKEY);
        presenter = new BrowsePresenter(this, this, catKey, shortName);
        super.onCreate(savedInstanceState);
    }

}

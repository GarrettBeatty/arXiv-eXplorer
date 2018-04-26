package com.gbeatty.arxivexplorer.browse;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gbeatty.arxivexplorer.paper.list.PapersFragment;
import com.gbeatty.arxivexplorer.paper.list.PapersPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends PapersFragment {

    private static final String CATKEYKEY = "catkeykey";
    private static final String SHORTNAMEKEY = "shortnamekey";
    private BrowsePresenter presenter;

    public BrowseFragment() {
        // Required empty public constructor
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
    public PapersPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected boolean isPaginate() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String catKey = getArguments().getString(CATKEYKEY);
        String shortName = getArguments().getString(SHORTNAMEKEY);
        presenter = new BrowsePresenter(this, this, catKey, shortName);
        super.onCreate(savedInstanceState);
    }

}

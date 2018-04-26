package com.gbeatty.arxivexplorer.favorites;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gbeatty.arxivexplorer.paper.list.PapersFragment;
import com.gbeatty.arxivexplorer.paper.list.PapersPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends PapersFragment {

    private FavoritesPresenter presenter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = new FavoritesPresenter(this, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getPapers();
    }

    @Override
    public PapersPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected boolean isPaginate() {
        return false;
    }
}

package com.gbeatty.arxivexplorer.dashboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gbeatty.arxivexplorer.paper.list.PapersFragment;
import com.gbeatty.arxivexplorer.paper.list.PapersPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends PapersFragment {

    private DashboardPresenter presenter;

    public DashboardFragment() {
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

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        presenter.onRefresh();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = new DashboardPresenter(this, this);
        super.onCreate(savedInstanceState);
    }

}

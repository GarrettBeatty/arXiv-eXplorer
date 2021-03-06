package com.gbeatty.arxivexplorer.dashboard;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

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

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public PapersPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected boolean isPaginate() {
        return true;
    }

//    @Override
//    public void onResume() {
//        presenter.onRefresh();
//        super.onResume();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = new DashboardPresenter(this, this);
        super.onCreate(savedInstanceState);
    }

}

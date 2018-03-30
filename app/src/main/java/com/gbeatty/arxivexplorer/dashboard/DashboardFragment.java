package com.gbeatty.arxivexplorer.dashboard;


import android.support.v4.app.Fragment;

import com.gbeatty.arxivexplorer.browse.paper.list.PapersFragment;
import com.gbeatty.arxivexplorer.models.Paper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends PapersFragment {

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(ArrayList<Paper> papers, String query, int maxResult) {
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(setArgs(papers, query, maxResult));
        return fragment;
    }

}

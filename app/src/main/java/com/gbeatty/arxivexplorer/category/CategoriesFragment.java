package com.gbeatty.arxivexplorer.category;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.base.BaseFragment;
import com.gbeatty.arxivexplorer.browse.BrowseFragment;
import com.gbeatty.arxivexplorer.models.Category;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesFragment extends BaseFragment implements CategoriesView {

    private static final String CAT_KEY = "catkey";
    @BindView(R.id.category_recycler_view)
    RecyclerView categoryRecyclerView;
    private CategoriesPresenter presenter;
    private Category[] categories;


    public static CategoriesFragment newInstance(Category[] categories) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putSerializable(CAT_KEY, categories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = (Category[]) getArguments().getSerializable(CAT_KEY);
        presenter = new CategoriesPresenter(this, this, categories);
        setHasOptionsMenu(false);
    }

    @Override
    public void goToSubCategories(Category[] categories, String tag) {
        showFragment(R.id.content, CategoriesFragment.newInstance(categories), tag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        categoryRecyclerView.setLayoutManager(linearLayoutManager);

        CategoriesListAdapter categoriesListAdapter = new CategoriesListAdapter(presenter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                categoryRecyclerView.getContext(), linearLayoutManager.getOrientation()
        );
        categoryRecyclerView.addItemDecoration(itemDecoration);

        categoryRecyclerView.setAdapter(categoriesListAdapter);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    @Override
    public void switchToBrowseFragment(String catKey, String shortName, String tag) {
        showFragment(R.id.content, BrowseFragment.newInstance(catKey, shortName), tag);
    }

}

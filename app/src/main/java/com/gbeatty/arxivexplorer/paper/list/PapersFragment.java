package com.gbeatty.arxivexplorer.paper.list;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.base.BaseFragment;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.paper.details.PaperDetailsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alexbykov.nopaginate.paginate.Paginate;
import ru.alexbykov.nopaginate.paginate.PaginateBuilder;

public abstract class PapersFragment extends BaseFragment implements PapersView {

    @BindView(R.id.papers_recycler_view)
    RecyclerView papersRecyclerView;
    @BindView(R.id.empty_view)
    TextView emptyView;
    private PapersPresenter presenter;
    private Paginate paginate;
    private PapersListAdapter papersListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
//    private boolean isPaginate;

    public PapersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenter();
        presenter.getPapers();
//        isPaginate = presenter.getQuery() != null;
        setHasOptionsMenu(true);
    }

    protected abstract PapersPresenter getPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflateFragment(R.layout.fragment_papers, inflater, container);
    }

    private View inflateFragment(int resId, LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(resId, container, false);

        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        papersRecyclerView.setLayoutManager(linearLayoutManager);

        papersListAdapter = new PapersListAdapter(presenter);

        papersRecyclerView.setAdapter(papersListAdapter);
        papersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());

        if (isPaginate()) {
            paginate = new PaginateBuilder()
                    .with(papersRecyclerView)
                    .setOnLoadMoreListener(presenter)
                    .setLoadingTriggerThreshold(5)
                    .build();
        }

        return rootView;
    }

    protected abstract boolean isPaginate();

    @Override
    public void onDestroy() {
        if (paginate != null) paginate.unbind();
        super.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();
        notifyAdapter();
    }

    @Override
    public void goToPaperDetails(Paper paper, String tag) {
        showFragment(R.id.content, PaperDetailsFragment.newInstance(paper), tag);
    }

    @Override
    public void showNoPapersMessage() {
        if (papersRecyclerView == null || emptyView == null) return;
        getActivity().runOnUiThread(() -> {
            papersRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        });

    }

    @Override
    public void notifyAdapter() {
        if (papersListAdapter == null) return;
        getActivity().runOnUiThread(() -> papersListAdapter.notifyDataSetChanged());
    }

    @Override
    public void showPaginateLoading(boolean isPaginateLoading) {
        getActivity().runOnUiThread(() -> {
                    if (paginate == null) return;
                    paginate.showLoading(isPaginateLoading);
                }
        );
    }

    @Override
    public void showRecyclerView() {
        if (papersRecyclerView == null || emptyView == null) return;
        getActivity().runOnUiThread(() -> {
                    papersRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
        );
    }

    @Override
    public void showPaginateError(boolean isPaginateError) {
        getActivity().runOnUiThread(() -> paginate.showError(isPaginateError));
    }

    @Override
    public void setPaginateNoMoreData(boolean isNoMoreItems) {
        getActivity().runOnUiThread(() -> paginate.setNoMoreItems(isNoMoreItems));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_paper_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return presenter.onNavigationItemSelected(id) || super.onOptionsItemSelected(item);
    }

    public void setRefreshing(boolean b) {
        if (swipeRefreshLayout == null) return;
        getActivity().runOnUiThread(() -> swipeRefreshLayout.setRefreshing(b));
    }


}

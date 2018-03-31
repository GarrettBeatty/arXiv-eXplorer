package com.gbeatty.arxivexplorer.paper.list;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
//    private boolean isPaginate;

    public PapersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenter();
//        isPaginate = presenter.getQuery() != null;
        setHasOptionsMenu(false);
    }

    protected abstract PapersPresenter getPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflateFragment(R.layout.fragment_papers, inflater, container);
    }

    protected View inflateFragment(int resId, LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(resId, container, false);

        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        papersRecyclerView.setLayoutManager(linearLayoutManager);

        papersListAdapter = new PapersListAdapter(presenter);

        papersRecyclerView.setAdapter(papersListAdapter);
        papersRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        presenter.determineContentVisibility();

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

//    @Override
//    public void onResume(){
//        presenter.updatePapers();
//        super.onResume();
//    }

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
        getActivity().runOnUiThread(() -> paginate.showLoading(isPaginateLoading));
    }

    @Override
    public void showRecyclerView() {
        if(papersRecyclerView == null || emptyView == null) return;
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

}

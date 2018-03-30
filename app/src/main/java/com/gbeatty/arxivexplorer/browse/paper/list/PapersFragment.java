package com.gbeatty.arxivexplorer.browse.paper.list;

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
import com.gbeatty.arxivexplorer.browse.paper.details.PaperDetailsFragment;
import com.gbeatty.arxivexplorer.models.Paper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alexbykov.nopaginate.paginate.Paginate;
import ru.alexbykov.nopaginate.paginate.PaginateBuilder;

public class PapersFragment extends BaseFragment implements PapersView{

    private static final String PAPERS_KEY = "paperskey";
    private static final String QUERY_KEY = "querykey";
    private static final String MAX_RESULT_KEY = "maxresultkey";

    @BindView(R.id.papers_recycler_view)
    RecyclerView papersRecyclerView;
    @BindView(R.id.empty_view)
    TextView emptyView;
    private PapersPresenter presenter;
    private Paginate paginate;
    private PapersListAdapter papersListAdapter;
    private boolean isPaginate;

    public PapersFragment() {
        // Required empty public constructor
    }

    public static PapersFragment newInstance(ArrayList<Paper> papers, String query, int maxResult) {
        PapersFragment fragment = new PapersFragment();
        fragment.setArguments(setArgs(papers, query, maxResult));
        return fragment;
    }

    public static PapersFragment newInstance(ArrayList<Paper> papers) {
        PapersFragment fragment = new PapersFragment();
        fragment.setArguments(setArgs(papers, null, 0));
        return fragment;
    }

    public static Bundle setArgs(ArrayList<Paper> papers, String query, int maxResult) {
        Bundle args = new Bundle();
        args.putSerializable(PAPERS_KEY, papers);
        args.putString(QUERY_KEY, query);
        args.putInt(MAX_RESULT_KEY, maxResult);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Paper> papers = (ArrayList<Paper>) getArguments().getSerializable(PAPERS_KEY);
        String query = getArguments().getString(QUERY_KEY);
        int maxResult = getArguments().getInt(MAX_RESULT_KEY);
        isPaginate = query != null;
        presenter = new PapersPresenter(this, this, papers, query, maxResult);
        setHasOptionsMenu(false);
    }

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

        presenter.determineContentVisibility();

        if(isPaginate){
            paginate = new PaginateBuilder()
                    .with(papersRecyclerView)
                    .setOnLoadMoreListener(presenter)
                    .setLoadingTriggerThreshold(5)
                    .build();
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        if(paginate != null) paginate.unbind();
        super.onDestroy();
    }

    @Override
    public void onResume(){
        papersListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void goToPaperDetails(Paper paper, String tag) {
        showFragment(R.id.content, PaperDetailsFragment.newInstance(paper), tag);
    }

    @Override
    public void showNoPapersMessage() {
        papersRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyAdapter(){
        getActivity().runOnUiThread(() -> papersListAdapter.notifyDataSetChanged());
    }

    @Override
    public void showPaginateLoading(boolean isPaginateLoading) {
        getActivity().runOnUiThread(() -> paginate.showLoading(isPaginateLoading));
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

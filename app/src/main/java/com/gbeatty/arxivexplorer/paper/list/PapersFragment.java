package com.gbeatty.arxivexplorer.paper.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.base.BaseFragment;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.paper.details.PaperDetailsFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

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
    private AdView adView;
    @BindView(R.id.ad_container)
    LinearLayout adContainer;
    private SharedPreferences preferences;

//    private boolean isPaginate;

    public PapersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences();
        presenter = getPresenter();
        presenter.getPapers();
        setHasOptionsMenu(true);
    }

    public abstract PapersPresenter getPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflateFragment(R.layout.fragment_papers, inflater, container);
    }

    private View inflateFragment(int resId, LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(resId, container, false);

        ButterKnife.bind(this, rootView);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        RecyclerView.LayoutManager manager;

        papersListAdapter = new PapersListAdapter(presenter);
        papersListAdapter.expandAllSections();

//        if(tabletSize){
//            manager = new GridLayoutManager(getContext(), 2);
//            papersListAdapter.setLayoutManager((GridLayoutManager) manager);
//        }else{
        manager = new LinearLayoutManager(getActivity());
//        }
        papersRecyclerView.setHasFixedSize(false);
        papersRecyclerView.setLayoutManager(manager);

        papersRecyclerView.setAdapter(papersListAdapter);
        papersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        papersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(adView == null) return;
                if (dy > 0) {
                    adView.setVisibility(View.VISIBLE);
                } else {
                    adView.setVisibility(View.GONE);
                }
            }
        });

        swipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());

        if (isPaginate()) {
            paginate = new PaginateBuilder()
                    .with(papersRecyclerView)
                    .setOnLoadMoreListener(presenter)
                    .setLoadingTriggerThreshold(9)
                    .build();
        }

        if(preferences.getBoolean("ads", true)){
            initializeAds(rootView);
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
    public void goToPaperDetails(Paper paper, String tag) {
        showFragment(R.id.content, PaperDetailsFragment.newInstance(paper), tag);
    }

    @Override
    public void showNoPapersMessage() {
        if (papersRecyclerView == null || emptyView == null || getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            papersRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        });

    }

    @Override
    public void notifyAdapter() {
        if (papersListAdapter == null || getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
//            papersRecyclerView.stopScroll();
            papersListAdapter.notifyDataSetChanged();

        });
    }

    @Override
    public void showPaginateLoading(boolean isPaginateLoading) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
                    if (paginate == null) return;
                    paginate.showLoading(isPaginateLoading);
                }
        );
    }

    @Override
    public void showRecyclerView() {
        if (papersRecyclerView == null || emptyView == null || getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
                    papersRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
        );
    }

    @Override
    public void showPaginateError(boolean isPaginateError) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> paginate.showError(isPaginateError));
    }

    @Override
    public void setPaginateNoMoreData(boolean isNoMoreItems) {
        if (getActivity() == null) return;
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
        switch (id) {
            // Check if user triggered a refresh:
            case R.id.menu_refresh:
                presenter.navigationRefreshClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRefreshing(boolean b) {
        if (swipeRefreshLayout == null || getActivity() == null) return;
        getActivity().runOnUiThread(() -> swipeRefreshLayout.setRefreshing(b));
    }

    @Override
    public void scrollToTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) papersRecyclerView
                .getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    private void initializeAds(View rootView) {
        adView = new AdView(rootView.getContext());
        //change to real unit on release
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdSize adSize = getAdSize(rootView);
        adView.setAdSize(adSize);
        adView.setVisibility(View.GONE);
        adContainer.addView(adView);

        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();


        adView.loadAd(adRequest);
    }


    private AdSize getAdSize(View rootView) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(rootView.getContext(), adWidth);
    }

    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }
}

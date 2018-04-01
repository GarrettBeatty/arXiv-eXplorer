package com.gbeatty.arxivexplorer.paper.list;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.network.Parser;
import com.gbeatty.arxivexplorer.paper.base.PapersPresenterBase;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.alexbykov.nopaginate.callback.OnLoadMoreListener;

public abstract class PapersPresenter extends PapersPresenterBase implements OnLoadMoreListener {

    private final PapersView view;
    private ArrayList<Paper> papers;
    private String query;
    private int start;

    protected PapersPresenter(PapersView view, SharedPreferencesView sharedPreferencesView) {
        super(sharedPreferencesView);
        this.view = view;
        start = 0;
    }

    void onBindPaperRowViewAtPosition(int position, final PaperRowView paperRowView) {

        final Paper paper = papers.get(position);
        paperRowView.setTitle(paper.getTitle());
        paperRowView.setAuthors(paper.getAuthor());
        //TODO determine sort by which to show
        paperRowView.setUpdatedDate("Updated: " + paper.getUpdatedDate());
        paperRowView.setPublishedDate("Published: " + paper.getPublishedDate());
        if (getSharedPreferenceView().isShowAbstract()) {
            paperRowView.showSummary();
            paperRowView.setSummary(paper.getSummary());
        } else
            paperRowView.hideSummary();
        updateIcons(paper, paperRowView);
    }

    void paperClicked(int position) {
        Paper paper = papers.get(position);
        view.goToPaperDetails(paper, paper.getPaperID());
    }

    void favoriteButtonClicked(int position, PaperRowView paperRowView) {
        Paper paper = papers.get(position);
        toggleFavoritePaper(paper);
        updateIcons(paper, paperRowView);
    }

    private void updateIcons(Paper paper, PaperRowView paperRowView) {
        if (isPaperFavorited(paper.getPaperID())) paperRowView.setFavoritedIcon();
        else paperRowView.setNotFavoritedIcon();
    }

    int getPapersRowsCount() {
        if (papers == null) return 0;
        return papers.size();
    }

//    public void determineContentVisibility() {
//        if(papers == null || papers.size() == 0){
//            view.showNoPapersMessage();
//        }
//    }

    @Override
    public void onLoadMore() {
        if (query == null) return;

        view.showPaginateError(false);
        view.showPaginateLoading(true);

        start = start + getSharedPreferenceView().getMaxResult();
        ArxivAPI.paginateQuery(query, start, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.showPaginateLoading(false);
                view.showPaginateError(true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    ArrayList<Paper> p = Parser.parse(responseBody.byteStream());
                    responseBody.close();
                    papers.addAll(p);
                    if (p.size() < getSharedPreferenceView().getMaxResult()) {
                        view.setPaginateNoMoreData(true);
                        return;
                    }
                    view.showPaginateLoading(false);
                    view.notifyAdapter();

                } catch (XmlPullParserException | ParseException e) {
                    view.showPaginateLoading(false);
                    view.showPaginateError(true);
                }
            }
        });

    }

    protected void updatePapers(ArrayList<Paper> papers) {
        this.papers = papers;
        if (papers.size() == 0) {
            view.showNoPapersMessage();
        } else
            view.showRecyclerView();
        view.notifyAdapter();
    }

    protected PapersView getView() {
        return view;
    }

    public abstract void getPapers();

    protected void setQuery(String query) {
        this.query = query;
    }

    public void onRefresh() {
        getPapers();
    }


    public boolean onNavigationItemSelected(int id) {
        switch (id) {
            // Check if user triggered a refresh:
            case R.id.menu_refresh:
                getPapers();
                return true;
        }

        // User didn't trigger a refresh, let the superclass handle this action
        return false;
    }
}

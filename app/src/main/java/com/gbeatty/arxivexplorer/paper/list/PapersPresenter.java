package com.gbeatty.arxivexplorer.paper.list;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.helpers.Tags;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.network.Parser;
import com.gbeatty.arxivexplorer.paper.base.PapersPresenterBase;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.alexbykov.nopaginate.callback.OnLoadMoreListener;

public abstract class PapersPresenter extends PapersPresenterBase implements OnLoadMoreListener {

    private final PapersView view;
    private List<Paper> papers;
    private List<String> dates;
    private String query;
    private int start;

    protected PapersPresenter(PapersView view, SharedPreferencesView sharedPreferencesView) {
        super(sharedPreferencesView);
        this.view = view;
        dates = new ArrayList<>();
        start = 0;
    }

    public void onBindHeaderViewAtPosition(int section, HeaderView view) {
        view.setHeaderDate(dates.get(section));
    }

    void onBindPaperRowViewAtPosition(int section, int absolutePosition, PaperRowView paperRowView) {

        int position = absolutePosition - (section + 1);

        if (position < 0 || position >= papers.size()) return;

        final Paper paper = papers.get(position);
        paperRowView.setTitle(paper.getTitle());
        paperRowView.setAuthors(paper.getAuthor());
        paperRowView.setPaperCategories(paper.getCategories());

        if (getSharedPreferenceView().isLastUpdatedDate()) {
            paperRowView.hidePublishedDate();
            paperRowView.showLastUpdatedDate();
            paperRowView.setLastUpdatedDate("Updated: " + paper.getUpdatedDate());
        } else {
            paperRowView.hideLastUpdatedDate();
            paperRowView.showPublishedDate();
            paperRowView.setPublishedDate("Submitted: " + paper.getPublishedDate());
        }

//        paperRowView.hideSummary();
//        paperRowView.hideLatexSummary();

        if (getSharedPreferenceView().isShowAbstract()) {

            if (getSharedPreferenceView().isRenderLatex()){
                paperRowView.hideSummary();
                paperRowView.showLatexSummary();
                paperRowView.setLatexSummary(paper.getSummary());
            }else{
                paperRowView.hideLatexSummary();
                paperRowView.showSummary();
                paperRowView.setSummary(paper.getSummary());
            }
        }else{
            paperRowView.hideSummary();
            paperRowView.hideLatexSummary();
        }
        updateIcons(paper, paperRowView);
    }

    void paperClicked(int absolutePosition, int section) {
        int position = absolutePosition - (section + 1);
        Paper paper = papers.get(position);
        view.goToPaperDetails(paper, paper.getPaperID());
    }

    void favoriteButtonClicked(int absolutePosition, int section, PaperRowView paperRowView) {
        int position = absolutePosition - (section + 1);
        Paper paper = papers.get(position);
        toggleFavoritePaper(paper);
        updateIcons(paper, paperRowView);
    }

    private void updateIcons(Paper paper, PaperRowView paperRowView) {
        if (isPaperFavorited(paper.getPaperID())) paperRowView.setFavoritedIcon();
        else paperRowView.setNotFavoritedIcon();
    }

    int getPapersRowsCount(int sectionIndex) {

        if (papers == null || dates == null) return 0;
        if (isRelevanceDate() || view.getTag() == null || view.getTag().equals(Tags.FAVORITES_FRAGMENT_TAG)
                || view.getTag().equals(Tags.DOWNLOADED_FRAGMENT_TAG) || view.getTag().equals(Tags.SEARCH_RESULTS_TAG)) return papers.size();

        String date = dates.get(sectionIndex);
        int count = 0;

        for (int i = 0; i < papers.size(); i++) {

            if (!getSharedPreferenceView().isLastUpdatedDate()) {
                if (papers.get(i).getPublishedDate().equals(date)) count++;
            } else {
                if (papers.get(i).getUpdatedDate().equals(date)) count++;
            }
        }

        return count;
    }

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

                    addToPapers(p);

                    if (p.size() < getSharedPreferenceView().getMaxResult()) {
                        view.setPaginateNoMoreData(true);
                        return;
                    }
                    view.showPaginateLoading(false);

                } catch (XmlPullParserException | ParseException e) {
                    view.showPaginateLoading(false);
                    view.showPaginateError(true);
                }
            }
        });

    }

    private void addToPapers(ArrayList<Paper> papers) {
        this.papers.addAll(papers);
        updateDates();
        view.notifyAdapter();
    }

    protected void updatePapers(ArrayList<Paper> papers) {
        this.papers = papers;
        if (papers.isEmpty()) {
            view.showNoPapersMessage();
        } else
            view.showRecyclerView();
        updateDates();
        view.setRefreshing(false);
        view.notifyAdapter();
    }

    private void updateDates() {

        dates = new ArrayList<>();

        if (view.getTag().equals(Tags.FAVORITES_FRAGMENT_TAG) || view.getTag().equals(Tags.DOWNLOADED_FRAGMENT_TAG)) {
            dates.add("Recently Added");
            return;
        }

        if (isRelevanceDate() || view.getTag().equals(Tags.SEARCH_RESULTS_TAG)) {
            dates.add("Relevance");
            return;
        }

        for (int i = 0; i < papers.size(); i++) {
            boolean found = false;
            for (int j = 0; j < dates.size(); j++) {

                if (!getSharedPreferenceView().isLastUpdatedDate()) {
                    if (dates.get(j).equals(papers.get(i).getPublishedDate())) {
                        found = true;
                        break;
                    }
                } else {
                    if (dates.get(j).equals(papers.get(i).getUpdatedDate())) {
                        found = true;
                        break;
                    }
                }

            }
            if (!getSharedPreferenceView().isLastUpdatedDate()) {
                if (!found) dates.add(papers.get(i).getPublishedDate());
            } else {
                if (!found) dates.add(papers.get(i).getUpdatedDate());
            }
        }
    }

    protected PapersView getView() {
        return view;
    }

    public abstract void getPapers();

    protected void setQuery(String query) {
        this.query = query;
    }

    public void onRefresh() {
        start = 0;
        getPapers();
    }


    public boolean onNavigationItemSelected(int id) {
        switch (id) {
            // Check if user triggered a refresh:
            case R.id.menu_refresh:
                view.setRefreshing(true);
                onRefresh();
                view.scrollToTop();
                return true;
        }

        // User didn't trigger a refresh, let the superclass handle this action
        return false;
    }

    public int getSectionCount() {
        if (dates == null) return 0;
        return dates.size();
    }

    public boolean isRelevanceDate() {
        return getSharedPreferenceView().isRelevanceDate();
    }

    public void errorLoading() {
        getView().setRefreshing(false);
        getView().showError();
        getView().showPaginateLoading(false);
    }

    public void navigationRefreshClicked() {
        view.setRefreshing(true);
        onRefresh();
        view.scrollToTop();
    }
}

package com.gbeatty.arxivexplorer.paper.list;

import com.gbeatty.arxivexplorer.helpers.Helper;
import com.gbeatty.arxivexplorer.helpers.Tags;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.network.Parser;
import com.gbeatty.arxivexplorer.paper.base.PapersPresenterBase;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
        dates = new CopyOnWriteArrayList<>();
        start = 0;
    }

    public void onBindHeaderViewAtPosition(int section, HeaderView view) {
        view.setHeaderDate(dates.get(section));
    }

    void onBindPaperRowViewAtPosition(int section, int absolutePosition, PaperRowView paperRowView) {

        int position = absolutePosition - (section + 1);

        if (position < 0 || position >= papers.size()) return;

        final Paper paper = papers.get(position);
        paperRowView.setAuthors(paper.getAuthor());
        paperRowView.setPaperCategories(paper.getCategories());
        paperRowView.setPaperID("ID: " + paper.getPaperID());

        if (getSharedPreferenceView().isLastUpdatedDate()) {
            paperRowView.hidePublishedDate();
            paperRowView.showLastUpdatedDate();
            paperRowView.setLastUpdatedDate("Updated: " + Helper.convertDateToLocale(paper.getUpdatedDate()));
        } else {
            paperRowView.hideLastUpdatedDate();
            paperRowView.showPublishedDate();
            paperRowView.setPublishedDate("Submitted: " + Helper.convertDateToLocale(paper.getPublishedDate()));
        }

        paperRowView.setTitle(paper.getTitle());

        if (getSharedPreferenceView().isReadIndicator() && isPaperRead(paper.getPaperID())) {
            paperRowView.setBackgroundColorRead();
        } else {
            paperRowView.setBackgroundColorNotRead();
        }

        paperRowView.hideLatexTitle();

        paperRowView.hideSummary();
        paperRowView.hideLatexSummary();

        if (getSharedPreferenceView().isShowAbstract()) {

            if (getSharedPreferenceView().isRenderLatex()) {
                paperRowView.showLatexSummary();
                paperRowView.showLatexTitle();
                paperRowView.setLatexSummary(paper.getSummary());
                paperRowView.setLatexTitle(paper.getTitle());
            } else {
                paperRowView.showSummary();
                paperRowView.showTitle();
                paperRowView.setSummary(paper.getSummary());
//                paperRowView.setTitle(paper.getTitle());
            }
        }
        updateIcons(paper, paperRowView);
    }

    void paperClicked(int absolutePosition, int section) {
        int position = absolutePosition - (section + 1);
        Paper paper = papers.get(position);
        paper.setRead(true);
        paper.save();
        view.goToPaperDetails(paper, paper.getPaperID());
    }

    void favoriteButtonClicked(int absolutePosition, int section, PaperRowView paperRowView) {
        int position = absolutePosition - (section + 1);
        Paper paper = papers.get(position);
        toggleFavoritePaper(paper);
        updateIcons(paper, paperRowView);
    }

    void downloadButtonClicked(int absolutePosition, int section, PaperRowView paperRowView) {
        int position = absolutePosition - (section + 1);
        Paper paper = papers.get(position);
        viewFile(paper, paperRowView);
        updateIcons(paper, paperRowView);

    }

    private void updateIcons(Paper paper, PaperRowView paperRowView) {
        if (isPaperFavorited(paper.getPaperID())) paperRowView.setFavoritedIcon();
        else paperRowView.setNotFavoritedIcon();

        if (isPaperDownloaded(paper.getPaperID())) paperRowView.setDownloadedIcon();
        else paperRowView.setNotDownloadedIcon();
    }


    int getPapersRowsCount(int sectionIndex) {

        if (papers == null || dates == null || sectionIndex > dates.size()) return 0;
        if (isRelevanceDate() || view.getTag() == null || view.getTag().equals(Tags.FAVORITES_FRAGMENT_TAG)
                || view.getTag().equals(Tags.DOWNLOADED_FRAGMENT_TAG))
            return papers.size();

        String date = dates.get(sectionIndex);
        int count = 0;

        for (Paper paper : papers) {

            if (!getSharedPreferenceView().isLastUpdatedDate()) {
                if (paper.getPublishedDate().equals(date)) count++;
            } else {
                if (paper.getUpdatedDate().equals(date)) count++;
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
                    List<Paper> p = Parser.parse(responseBody.byteStream());
                    responseBody.close();

                    if (p.size() > 0) {
                        addToPapers(p);
                        view.showPaginateLoading(false);
                    }


                    if (p.size() < getSharedPreferenceView().getMaxResult()) {
                        view.setPaginateNoMoreData(true);
                    }

                } catch (XmlPullParserException | ParseException e) {
                    view.showPaginateLoading(false);
                    view.showPaginateError(true);
                }
            }
        });

    }

    private void addToPapers(List<Paper> papers) {
        this.papers.addAll(papers);
        updateDates();
        view.notifyAdapter();
    }

    protected void updatePapers(List<Paper> papers) {
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

        dates = new CopyOnWriteArrayList<>();

        if (view == null || view.getTag() == null) return;

        if (view.getTag().equals(Tags.FAVORITES_FRAGMENT_TAG) || view.getTag().equals(Tags.DOWNLOADED_FRAGMENT_TAG)) {
            dates.add("Recently Added");
            return;
        }

        if (isRelevanceDate()) {
            dates.add("Relevance");
            return;
        }
        for (Paper paper : papers) {
            if (getSharedPreferenceView().isLastUpdatedDate()) {
                if (!dates.contains(paper.getUpdatedDate())) {
                    dates.add(paper.getUpdatedDate());
                }
            } else {
                if (!dates.contains(paper.getPublishedDate())) {
                    dates.add(paper.getPublishedDate());
                }
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

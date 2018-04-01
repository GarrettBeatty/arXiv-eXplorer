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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.alexbykov.nopaginate.callback.OnLoadMoreListener;

public abstract class PapersPresenter extends PapersPresenterBase implements OnLoadMoreListener {

    private final PapersView view;
    private ArrayList<Paper> papers;
    private List<String> dates = new ArrayList<>();
    private String query;
    private int start;

    protected PapersPresenter(PapersView view, SharedPreferencesView sharedPreferencesView) {
        super(sharedPreferencesView);
        this.view = view;
        start = 0;
    }

    public void onBindHeaderViewAtPosition(int section, PapersListAdapter.HeaderViewHolder view) {
        view.setHeaderDate(dates.get(section));
    }

    void onBindPaperRowViewAtPosition(int section, int relativePosition, int absolutePosition, final PapersListAdapter.PaperViewHolder paperRowView) {

        int position = absolutePosition - (section + 1);

        final Paper paper = papers.get(position);
        paperRowView.setTitle(paper.getTitle());
        paperRowView.setAuthors(paper.getAuthor());

        if (getSharedPreferenceView().isLastUpdatedDate()) {
            paperRowView.hidePublishedDate();
            paperRowView.showLastUpdatedDate();
            paperRowView.setLastUpdatedDate("Updated: " + paper.getUpdatedDate());
        } else {
            paperRowView.hideLastUpdatedDate();
            paperRowView.showPublishedDate();
            paperRowView.setPublishedDate("Submitted: " + paper.getPublishedDate());
        }

        if (getSharedPreferenceView().isShowAbstract()) {
            paperRowView.showSummary();
            paperRowView.setSummary(paper.getSummary());
        } else
            paperRowView.hideSummary();
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

        String date = dates.get(sectionIndex);
        int count = 0;
        for (Paper p : papers) {

            if (!getSharedPreferenceView().isLastUpdatedDate()){
                if (p.getPublishedDate().equals(date)) count++;
            }
            else{
                if (p.getUpdatedDate().equals(date)) count++;
            }
        }
        return count;
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
        if (papers.size() == 0) {
            view.showNoPapersMessage();
        } else
            view.showRecyclerView();
        updateDates();
        view.notifyAdapter();
    }

    private void updateDates() {
        for (Paper p : papers) {
            boolean found = false;
            for (int i = 0; i < dates.size(); i++) {

                if (!getSharedPreferenceView().isLastUpdatedDate()) {
                    if (dates.get(i).equals(p.getPublishedDate())) {
                        found = true;
                        break;
                    }
                } else {
                    if (dates.get(i).equals(p.getUpdatedDate())) {
                        found = true;
                        break;
                    }
                }


            }
            if (!getSharedPreferenceView().isLastUpdatedDate()) {
                if (!found) dates.add(p.getPublishedDate());
            } else {
                if (!found) dates.add(p.getUpdatedDate());
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

    public int getSectionCount() {
        if (dates == null) return 0;
        return dates.size();
    }


}

package com.gbeatty.arxivexplorer.browse.paper.base;

import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.network.Parser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.alexbykov.nopaginate.callback.OnLoadMoreListener;

public class PapersPresenter extends PapersPresenterBase implements OnLoadMoreListener {

    private PapersView view;
    private ArrayList<Paper> papers;
    private String query;
    private int start;
    private int maxResult;

    public PapersPresenter(PapersView view, ArrayList<Paper> papers, String query, int maxResult) {
        this.view = view;
        if (papers == null) this.papers = new ArrayList<>();
        else this.papers = papers;
        this.query = query;
        start = 0;
        this.maxResult = maxResult;
    }

    void onBindPaperRowViewAtPosition(int position, final PaperRowView paperRowView) {

        final Paper paper = papers.get(position);
        paperRowView.setTitle(paper.getTitle());
        paperRowView.setAuthors(paper.getAuthor());
        paperRowView.setUpdatedDate("Updated: " + paper.getUpdatedDate());
        paperRowView.setPublishedDate("Published: " + paper.getPublishedDate());
        if (isPaperFavorited(paper)) paperRowView.setFavoritedIcon();
        else paperRowView.setNotFavoritedIcon();
    }

    void paperClicked(int position) {
        Paper paper = papers.get(position);
        view.goToPaperDetails(paper, paper.getPaperID());
    }

    void favoriteButtonClicked(int position, PaperRowView paperRowView) {
        Paper paper = papers.get(position);
        toggleFavoritePaper(paper);
        if (isPaperFavorited(paper)) paperRowView.setFavoritedIcon();
        else paperRowView.setNotFavoritedIcon();
    }

    int getPapersRowsCount() {
        return papers.size();
    }

    public void determineContentVisibility() {
        if(papers.size() == 0){
            view.showNoPapersMessage();
        }
    }

    @Override
    public void onLoadMore() {
        if(query == null) return;

        view.showPaginateError(false);
        view.showPaginateLoading(true);

        start = start + maxResult;
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
                    if(p.size() < maxResult){
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
}

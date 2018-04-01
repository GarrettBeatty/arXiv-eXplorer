package com.gbeatty.arxivexplorer.paper.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gbeatty.arxivexplorer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PapersListAdapter extends RecyclerView.Adapter<PapersListAdapter.PaperViewHolder> {

    private final PapersPresenter presenter;

    PapersListAdapter(PapersPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public PaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paper_view, parent, false);
        PaperViewHolder viewHolder = new PaperViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PaperViewHolder holder, final int position) {
        presenter.onBindPaperRowViewAtPosition(position, holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return presenter.getPapersRowsCount();
    }

    public class PaperViewHolder extends RecyclerView.ViewHolder implements PaperRowView {

        @BindView(R.id.paper_title)
        TextView paperTitle;
        @BindView(R.id.paper_authors)
        TextView paperAuthors;
        @BindView(R.id.paper_published_date)
        TextView paperPublished;
        @BindView(R.id.paper_updated_date)
        TextView paperUpdated;
        @BindView(R.id.button_favorite_paper)
        ImageButton favoritePaper;
        @BindView(R.id.paper_summary)
        TextView paperSummary;
        @BindView(R.id.summary_sep) View summarySep;

        PaperViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> presenter.paperClicked(getLayoutPosition()));
            favoritePaper.setOnClickListener(view -> presenter.favoriteButtonClicked(getLayoutPosition(), this));
        }

        public void setTitle(String title) {
            paperTitle.setText(title);
        }

        public void setSummary(String summary){paperSummary.setText(summary);}

        public void setAuthors(String authors) {
            paperAuthors.setText(authors);
        }

        public void setPublishedDate(String publishedDate) {
            paperPublished.setText(publishedDate);
        }

        @Override
        public void setUpdatedDate(String updatedDate) {
            paperUpdated.setText(updatedDate);
        }

        @Override
        public void setFavoritedIcon() {
            favoritePaper.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
        }

        @Override
        public void setNotFavoritedIcon() {
            favoritePaper.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        }

        @Override
        public void hideSummary() {
            paperSummary.setVisibility(View.GONE);
            summarySep.setVisibility(View.GONE);
        }

        @Override
        public void showSummary() {
            paperSummary.setVisibility(View.VISIBLE);
            summarySep.setVisibility(View.VISIBLE);
        }
    }
}

package com.gbeatty.arxivexplorer.paper.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.ItemCoord;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.gbeatty.arxivexplorer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PapersListAdapter extends SectionedRecyclerViewAdapter<SectionedViewHolder> {

    private final PapersPresenter presenter;

    PapersListAdapter(PapersPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public SectionedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;
        SectionedViewHolder viewHolder;

        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layoutRes = R.layout.date_header;
                View h = LayoutInflater.from(parent.getContext())
                        .inflate(layoutRes, parent, false);
                viewHolder = new HeaderViewHolder(h);
                break;

            default:
                layoutRes = R.layout.paper_view;
                View p = LayoutInflater.from(parent.getContext())
                        .inflate(layoutRes, parent, false);
                viewHolder = new PaperViewHolder(p);
                break;
        }

        return viewHolder;

    }

    @Override
    public void onBindHeaderViewHolder(SectionedViewHolder holder, int section, boolean expanded) {
        presenter.onBindHeaderViewAtPosition(section, (HeaderViewHolder) holder);
    }

    @Override
    public void onBindFooterViewHolder(SectionedViewHolder holder, int section) {
        // Setup footer view, if footers are enabled (see the next section)
    }

    @Override
    public void onBindViewHolder(SectionedViewHolder holder, int section, int relativePosition, int absolutePosition) {
        presenter.onBindPaperRowViewAtPosition(section, absolutePosition, (PaperViewHolder) holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount(int sectionIndex) {
        return presenter.getPapersRowsCount(sectionIndex);
    }

    @Override
    public int getSectionCount() {
        return presenter.getSectionCount();
    }

    public class HeaderViewHolder extends SectionedViewHolder implements HeaderView {

        @BindView(R.id.paper_date_header)
        TextView dateHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setHeaderDate(String date) {
            dateHeader.setText(date);
        }
    }


    public class PaperViewHolder extends SectionedViewHolder implements PaperRowView {

        @BindView(R.id.paper_title)
        TextView paperTitle;
        @BindView(R.id.paper_authors)
        TextView paperAuthors;
        @BindView(R.id.paper_published_date)
        TextView paperPublished;
        @BindView(R.id.paper_updated_date)
        TextView paperUpdated;
        @BindView(R.id.paper_categories)
        TextView paperCategories;
        @BindView(R.id.button_favorite_paper)
        ImageButton favoritePaper;
        @BindView(R.id.paper_summary)
        TextView paperSummary;
        @BindView(R.id.summary_sep)
        View summarySep;

        PaperViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {
                ItemCoord position = getRelativePosition();
                int section = position.section();
                presenter.paperClicked(getLayoutPosition(),section);
            });
            favoritePaper.setOnClickListener(view -> {
                ItemCoord position = getRelativePosition();
                int section = position.section();
                presenter.favoriteButtonClicked(getLayoutPosition(),section , this);
            });
        }

        public void setTitle(String title) {
            paperTitle.setText(title);
        }

        public void setSummary(String summary) {
            paperSummary.setText(summary);
        }

        public void setAuthors(String authors) {
            paperAuthors.setText(authors);
        }

        public void setPublishedDate(String publishedDate) {
            paperPublished.setText(publishedDate);
        }

        @Override
        public void setLastUpdatedDate(String updatedDate) {
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
        public void setPaperCategories(String categories) {
            paperCategories.setText(categories);
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

        @Override
        public void hidePublishedDate() {
            paperPublished.setVisibility(View.GONE);
        }

        @Override
        public void showLastUpdatedDate() {
            paperUpdated.setVisibility(View.VISIBLE);
        }

        @Override
        public void hideLastUpdatedDate() {
            paperUpdated.setVisibility(View.GONE);
        }

        @Override
        public void showPublishedDate() {
            paperPublished.setVisibility(View.VISIBLE);
        }

    }
}

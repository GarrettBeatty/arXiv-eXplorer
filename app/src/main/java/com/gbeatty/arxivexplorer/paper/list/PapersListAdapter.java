package com.gbeatty.arxivexplorer.paper.list;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.ItemCoord;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.gbeatty.arxivexplorer.BuildConfig;
import com.gbeatty.arxivexplorer.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import katex.hourglass.in.mathlib.MathView;

public class PapersListAdapter extends SectionedRecyclerViewAdapter<SectionedViewHolder> {

    private final PapersPresenter presenter;
    private ProgressDialog progressDialog;

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
        @BindView(R.id.paper_id)
        TextView paperID;
        //        @BindView(R.id.paper_title_latex)
//        MathView paperTitleLatex;
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
        @BindView(R.id.button_download_paper)
        ImageButton downloadPaper;
        @BindView(R.id.paper_summary)
        TextView paperSummary;
        @BindView(R.id.paper_summary_latex)
        MathView paperSummaryLatex;
        @BindView(R.id.summary_sep)
        View summarySep;

        PaperViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {
                ItemCoord position = getRelativePosition();
                int section = position.section();
                presenter.paperClicked(getLayoutPosition(), section);
            });
            favoritePaper.setOnClickListener(view -> {
                ItemCoord position = getRelativePosition();
                int section = position.section();
                presenter.favoriteButtonClicked(getLayoutPosition(), section, this);
            });
            downloadPaper.setOnClickListener(view -> {
                ItemCoord position = getRelativePosition();
                int section = position.section();
                presenter.downloadButtonClicked(getLayoutPosition(), section, this);
            });

        }

        @Override
        public void viewDownloadedPaper(File downloadedFile) {
            Log.d("testing", "testing");
            if (itemView.getContext() == null) return;
            Uri uri = FileProvider.getUriForFile(itemView.getContext(), BuildConfig.APPLICATION_ID + ".provider", downloadedFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent intent1 = Intent.createChooser(intent, "Open With");
            try {
                itemView.getContext().startActivity(intent1);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(itemView.getContext(), "No PDF viewer found", Toast.LENGTH_SHORT).show();
            }
        }

        public void setTitle(String title) {
            paperTitle.setText(title);
        }

        public void setSummary(String summary) {
            paperSummary.setText(summary);
        }

        @Override
        public void setLatexSummary(String summary) {
            paperSummaryLatex.setDisplayText(summary);
        }

        @Override
        public void setLatexTitle(String title) {
//            paperTitleLatex.setDisplayText(title);
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
//            summarySep.setVisibility(View.GONE);
        }

        @Override
        public void showSummary() {
            paperSummary.setVisibility(View.VISIBLE);
//            summarySep.setVisibility(View.VISIBLE);
        }

        @Override
        public void hideTitle() {
            paperTitle.setVisibility(View.GONE);
        }

        @Override
        public void showTitle() {
            paperTitle.setVisibility(View.VISIBLE);

        }

        @Override
        public void setPaperID(String id) {
            paperID.setText(id);
        }

        @Override
        public void setDownloadedIcon() {
            downloadPaper.setBackgroundResource(R.drawable.ic_remove_red_eye_black_24dp);
        }

        @Override
        public void setNotDownloadedIcon() {
            downloadPaper.setBackgroundResource(R.drawable.ic_file_download_black_24dp);
        }

        @Override
        public File getFilesDir() {
            return itemView.getContext().getFilesDir();
        }

        @Override
        public void showLoading() {

            progressDialog = new ProgressDialog(itemView.getContext());
            progressDialog.setMessage("Downloading..");
            progressDialog.setTitle("Downloading PDF");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        public void errorLoading() {
            //TODO fix
//            if (itemView.getContext() == null) return;
//            itemView.getContext().runOnUiThread(() -> {
//                dismissLoading();
//                Toast.makeText(itemView.getContext(), "Error Downloading Paper", Toast.LENGTH_SHORT).show();
//            });
        }

        @Override
        public void dismissLoading() {
            progressDialog.dismiss();
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

        @Override
        public void setBackgroundColorRead() {
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.readBackground));
        }

        @Override
        public void setBackgroundColorNotRead() {
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.cardBackground));
        }


        @Override
        public void hideLatexSummary() {
            paperSummaryLatex.setVisibility(View.GONE);
//            summarySep.setVisibility(View.VISIBLE);
        }

        @Override
        public void showLatexSummary() {
            paperSummaryLatex.setVisibility(View.VISIBLE);
//            summarySep.setVisibility(View.VISIBLE);
        }

        @Override
        public void hideLatexTitle() {
//            paperTitleLatex.setVisibility(View.GONE);
        }

        @Override
        public void showLatexTitle() {
//            paperTitleLatex.setVisibility(View.VISIBLE);
        }

    }
}

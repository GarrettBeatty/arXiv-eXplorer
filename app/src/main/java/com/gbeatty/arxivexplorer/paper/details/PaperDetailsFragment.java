package com.gbeatty.arxivexplorer.paper.details;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gbeatty.arxivexplorer.BuildConfig;
import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.base.BaseFragment;
import com.gbeatty.arxivexplorer.models.Paper;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import katex.hourglass.in.mathlib.MathView;

public class PaperDetailsFragment extends BaseFragment implements PaperDetailsView {

    private static final String PAPER_KEY = "paperkey";
    @BindView(R.id.paper_title)
    TextView paperTitle;
//    @BindView(R.id.paper_title_latex)
//    MathView paperTitleLatex;
    @BindView(R.id.paper_summary)
    TextView paperSummary;
    @BindView(R.id.paper_summary_latex)
    MathView paperSummaryLatex;
    @BindView(R.id.paper_authors)
    TextView paperAuthors;
    @BindView(R.id.paper_updated_date)
    TextView paperUpdated;
    @BindView(R.id.paper_published_date)
    TextView paperPublished;
    @BindView(R.id.paper_categories)
    TextView paperCategories;
    private PaperDetailsPresenter presenter;
    private MenuItem favoritePaper;
    private MenuItem downloadedPaper;
    private ProgressDialog progressDialog;

    public PaperDetailsFragment() {
        // Required empty public constructor
    }

    public static PaperDetailsFragment newInstance(Paper paper) {
        PaperDetailsFragment fragment = new PaperDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(PAPER_KEY, paper);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper paper = (Paper) getArguments().getSerializable(PAPER_KEY);
        presenter = new PaperDetailsPresenter(this, this, paper);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_paper_detail, container, false);
        ButterKnife.bind(this, rootView);

        presenter.initializeMainContent();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_paper_details, menu);
        favoritePaper = menu.findItem(R.id.menu_favorite_paper);
        downloadedPaper = menu.findItem(R.id.menu_download_paper);
        presenter.updateFavoritedMenuItem();
        presenter.updateDownloadedMenuItem();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_favorite_paper:
                presenter.navigationFavoritePaperClicked();
                return true;
            case R.id.menu_download_paper:
                presenter.navigationDownloadPaperClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(String title) {
        paperTitle.setText(title);
    }

    @Override
    public void setAuthors(String authorsString) {
        paperAuthors.setText(authorsString);
    }

    @Override
    public void setPublishedDate(String published) {
        paperPublished.setText(published);
    }

    @Override
    public void setLastUpdatedDate(String updatedDate) {
        paperUpdated.setText(updatedDate);
    }

    @Override
    public void setFavoritedIcon() {
        favoritePaper.setIcon(R.drawable.ic_favorite_black_24dp);
    }

    @Override
    public void setNotFavoritedIcon() {
        favoritePaper.setIcon(R.drawable.ic_favorite_border_black_24dp);
    }

    @Override
    public void setPaperCategories(String categories) {
        paperCategories.setText(categories);
    }

    @Override
    public void hideLatexSummary() {
        paperSummaryLatex.setVisibility(View.GONE);
    }

    @Override
    public void showLatexSummary() {
        paperSummaryLatex.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLatexTitle() {
//        paperTitleLatex.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLatexTitle() {
//        paperTitleLatex.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSummary() {
        paperSummary.setVisibility(View.GONE);
    }

    @Override
    public void showSummary() {
        paperSummary.setVisibility(View.VISIBLE);
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
    public void setSummary(String summary) {
        paperSummary.setText(summary);
    }

    @Override
    public void setLatexSummary(String summary) {
        paperSummaryLatex.setDisplayText(summary);
    }

    @Override
    public void setLatexTitle(String title) {
//        paperTitleLatex.setDisplayText(title);
    }

    @Override
    public File getFilesDir() {
        return getActivity().getFilesDir();
    }

    @Override
    public void viewDownloadedPaper(File downloadedFile) {
        if(getActivity() == null)return;
        Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", downloadedFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent1 = Intent.createChooser(intent, "Open With");
        try {
            startActivity(intent1);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    public void showLoading(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Downloading..");
        progressDialog.setTitle("Downloading PDF");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissLoading(){
        progressDialog.dismiss();
    }

    public void errorLoading() {
        if(getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            dismissLoading();
            Toast.makeText(getContext(), "Error Downloading Paper", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void setDownloadedIcon() {
        if(getActivity() == null) return;
        getActivity().runOnUiThread(() -> downloadedPaper.setIcon(R.drawable.ic_remove_red_eye_black_24dp));
    }

    @Override
    public void setNotDownloadedIcon() {
        downloadedPaper.setIcon(R.drawable.ic_file_download_black_24dp);
    }

}

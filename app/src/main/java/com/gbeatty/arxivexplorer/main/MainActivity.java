package com.gbeatty.arxivexplorer.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.base.BaseActivity;
import com.gbeatty.arxivexplorer.browse.category.CategoriesFragment;
import com.gbeatty.arxivexplorer.browse.paper.base.PapersFragment;
import com.gbeatty.arxivexplorer.models.Category;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.search.SearchFragment;
import com.gbeatty.arxivexplorer.settings.SettingsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView {

    @BindView(R.id.navigation)
    BottomNavigationView bottomBarView;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenter(this);
        bottomBarView.setOnNavigationItemSelectedListener(item -> presenter.onNavigationItemSelected(item.getItemId()));

        if(savedInstanceState == null)
            presenter.switchToCategoriesFragment();

        goToRatingAuto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return presenter.onOptionsItemSelected(item.getItemId());
    }

    @Override
    public void switchToCategoriesFragment(Category[] categories, String tag) {
        showFragment(R.id.content, CategoriesFragment.newInstance(categories), tag);
    }

    @Override
    public void switchToSearchFragment(String tag) {
        showFragment(R.id.content, SearchFragment.newInstance(), tag);
    }

    @Override
    public void switchToFavoritesFragment(ArrayList<Paper> papers, String tag) {
        showFragment(R.id.content, PapersFragment.newInstance(papers), tag);
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content);
    }

    @Override
    public void goToSettings() {
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    @Override
    public void goToRating() {
        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(3)
                .onRatingBarFormSumbit(this::sendFeedbackEmail).build();

        ratingDialog.show();
    }

    public void goToRatingAuto() {
        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(3)
                .session(7)
                .onRatingBarFormSumbit(this::sendFeedbackEmail).build();
        ratingDialog.show();
    }

    private void sendFeedbackEmail(String feedback){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"garrettbdev@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "arXiv eXplorer Feedback");
        i.putExtra(Intent.EXTRA_TEXT   ,feedback);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        presenter.cancelHttpCalls();
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

}

package com.gbeatty.arxivexplorer.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.base.BaseFragment;
import com.gbeatty.arxivexplorer.browse.category.CategoriesFragment;
import com.gbeatty.arxivexplorer.browse.paper.base.PapersFragment;
import com.gbeatty.arxivexplorer.models.Category;
import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.settings.SettingsActivity;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView, BaseFragment.ActivityListener, SharedPreferencesView {

    @BindView(R.id.navigation)
    BottomNavigationView bottomBarView;
    @BindView(R.id.search_view) MaterialSearchView searchView;
    private MainPresenter presenter;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        presenter = new MainPresenter(this, this);
        bottomBarView.setOnNavigationItemSelectedListener(item -> presenter.onNavigationItemSelected(item.getItemId()));
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if(savedInstanceState == null)
            presenter.switchToCategoriesFragment();
        MaterialSearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.onQueryTextSubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        goToRatingAuto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);


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

//    @Override
//    public void switchToSearchFragment(String tag) {
//        showFragment(R.id.content, SearchFragment.newInstance(), tag);
//    }

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

    // Method used to show a fragment in a Container View inside the Activity
    public void showFragment(int fragmentContainerId, BaseFragment fragment, String backStateName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            transaction.replace(fragmentContainerId, fragment, backStateName);
            transaction.addToBackStack(backStateName);
            transaction.commit();
            fragmentManager.executePendingTransactions();
        }
    }

    public void showLoading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setTitle("Loading Papers");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismissLoading(){
        progressDialog.dismiss();
    }

    public void errorLoading() {
        runOnUiThread(() -> {
            dismissLoading();
            Toast.makeText(this, "Error Loading Papers", Toast.LENGTH_SHORT).show();
        });
    }


    public String getSortOrder() {
        return preferences.getString("sort_order_list", ArxivAPI.SORT_ORDER_DESCENDING);
    }

    public String getSortBy() {
        return preferences.getString("sort_by_list", ArxivAPI.SORT_BY_LAST_UPDATED_DATE);
    }

    public int getMaxResult() {
        return Integer.parseInt(preferences.getString("max_results", getString(R.string.maxResultDefault)));
    }

    public void switchToPapersFragment(ArrayList<Paper> papers, String tag, String query, int maxResult) {
        runOnUiThread(() -> showFragment(R.id.content, PapersFragment.newInstance(papers, query, maxResult), tag));
    }


}

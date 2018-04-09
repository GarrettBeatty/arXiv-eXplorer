package com.gbeatty.arxivexplorer.main;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.crashlytics.android.Crashlytics;
import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.base.BaseFragment;
import com.gbeatty.arxivexplorer.category.CategoriesFragment;
import com.gbeatty.arxivexplorer.dashboard.DashboardFragment;
import com.gbeatty.arxivexplorer.downloaded.DownloadedFragment;
import com.gbeatty.arxivexplorer.favorites.FavoritesFragment;
import com.gbeatty.arxivexplorer.models.Category;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.search.SearchFragment;
import com.gbeatty.arxivexplorer.settings.SettingsActivity;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements MainView, BaseFragment.ActivityListener, SharedPreferencesView {

    @BindView(R.id.navigation)
    AHBottomNavigation bottomBarView;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    private MainPresenter presenter;
    private SharedPreferences preferences;
    private Toolbar myToolbar;


    @Override
    protected void onResume() {
        preferences = getSharedPreferences();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferences = getSharedPreferences();
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        UiModeManager uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);

        if(isDarkMode()) {
            uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
        }
        else
            uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainPresenter(this, this);

        MaterialSearchView searchView = findViewById(R.id.search_view);
        searchView.setHint("Search for papers");
        searchView.setBackIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_black_24dp, null));
        searchView.setCloseIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_close_black_24dp, null));
        searchView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        searchView.setHintTextColor(ResourcesCompat.getColor(getResources(), R.color.grey_500, null));

        myToolbar = findViewById(R.id.toolbar);

        setNighModeThemes();

        setSupportActionBar(myToolbar);

        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.navigation);
        navigationAdapter.setupWithBottomNavigation(bottomBarView);

        bottomBarView.setOnTabSelectedListener((position, wasSelected) -> presenter.onNavigationItemSelected(position));

        if(savedInstanceState == null)
            presenter.switchToDashboardFragment();


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

    private void setNighModeThemes(){
        if (isDarkMode()) {
            bottomBarView.setDefaultBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.toolbarDark, null));
            bottomBarView.setAccentColor(ResourcesCompat.getColor(getResources(), R.color.tabDark, null));
            searchView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.toolbarDark, null));
            myToolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.toolbarDark, null));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.toolbarDark)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
                getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.toolbarDark)); //status bar or the time bar at the top
            }
        } else {
            bottomBarView.setDefaultBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
            bottomBarView.setAccentColor(ResourcesCompat.getColor(getResources(), R.color.tabLight, null));
            searchView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.primary, null));
            myToolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.toolbarLight, null));
        }
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
        int id = item.getItemId();

        switch (id){
            case R.id.menu_settings:
                presenter.navigationSettingsClicked();
                return true;
            case R.id.menu_rating:
                presenter.navigationRatingClicked();
                return true;
        }
        return false;
    }

    @Override
    public void switchToCategoriesFragment(Category[] categories, String tag) {
        showFragment(R.id.content, CategoriesFragment.newInstance(categories), tag);
    }


    @Override
    public void switchToFavoritesFragment(String tag) {
        showFragment(R.id.content, FavoritesFragment.newInstance(), tag);
    }

    @Override
    public void switchToDashboardFragment(String tag) {
        showFragment(R.id.content, DashboardFragment.newInstance(), tag);
    }

    @Override
    public void switchToSearchFragment(String searchQuery, String tag) {
        showFragment(R.id.content, SearchFragment.newInstance(searchQuery), tag);
    }



    @Override
    public void switchToDownloadedFragment(String tag) {
        showFragment(R.id.content, DownloadedFragment.newInstance(), tag);
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
                .ratingBarColor(R.color.grey_500)
                .threshold(3)
                .onRatingBarFormSumbit(this::sendFeedbackEmail).build();

        ratingDialog.show();
    }

    private void goToRatingAuto() {
        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .ratingBarColor(R.color.grey_500)
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
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }else{
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

    // Method used to show a fragment in a Container View inside the Activity
    public void showFragment(int fragmentContainerId, BaseFragment fragment, String backStateName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

//            if(!backStateName.equals(Tags.MAIN_CATEGORIES_TAG)){
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
//            }else{
//                transaction.setCustomAnimations(R.anim.enter_two, R.anim.exit_two, R.anim.pop_enter_two, R.anim.pop_exit_two);
//            }
            transaction.replace(fragmentContainerId, fragment, backStateName);
            transaction.addToBackStack(backStateName);
            transaction.commit();
            fragmentManager.executePendingTransactions();
        }
    }

    public String getSortOrder() {
        return preferences.getString("sort_order_list", ArxivAPI.SORT_ORDER_DESCENDING);
    }

    public String getSortBy() {
        return preferences.getString("sort_by_list", ArxivAPI.SORT_BY_SUBMITTED_DATE);
    }

    public int getMaxResult() {
        return Integer.parseInt(preferences.getString("max_results", getString(R.string.maxResultDefault)));
    }

    public boolean isShowAbstract() {
        return preferences.getBoolean("show_abstract", false);
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean isDashboardCategoryChecked(String categoryName) {
        return preferences.getBoolean(categoryName, true);
    }

    @Override
    public boolean isLastUpdatedDate() {
        return preferences.getString("sort_by_list", ArxivAPI.SORT_BY_SUBMITTED_DATE).equals(ArxivAPI.SORT_BY_LAST_UPDATED_DATE);
    }

    @Override
    public boolean isRelevanceDate() {
        return preferences.getString("sort_by_list", ArxivAPI.SORT_BY_SUBMITTED_DATE).equals(ArxivAPI.SORT_BY_RELEVANCE);
    }

    @Override
    public boolean isRenderLatex() {
        return preferences.getBoolean("latex", false);
    }

    @Override
    public boolean isPublishedDate() {
        return preferences.getString("sort_by_list", ArxivAPI.SORT_BY_SUBMITTED_DATE).equals(ArxivAPI.SORT_BY_SUBMITTED_DATE);
    }

    private boolean isDarkMode(){
        return preferences.getBoolean("dark_mode", false);
    }
}

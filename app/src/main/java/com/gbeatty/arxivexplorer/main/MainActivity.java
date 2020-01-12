package com.gbeatty.arxivexplorer.main;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.gbeatty.arxivexplorer.BuildConfig;
import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.base.BaseFragment;
import com.gbeatty.arxivexplorer.category.CategoriesFragment;
import com.gbeatty.arxivexplorer.dashboard.DashboardFragment;
import com.gbeatty.arxivexplorer.downloaded.DownloadedFragment;
import com.gbeatty.arxivexplorer.favorites.FavoritesFragment;
import com.gbeatty.arxivexplorer.models.Category;
import com.gbeatty.arxivexplorer.paper.list.PapersFragment;
import com.gbeatty.arxivexplorer.search.SearchFragment;
import com.gbeatty.arxivexplorer.settings.SettingsActivity;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;
import com.google.android.gms.ads.MobileAds;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.sufficientlysecure.donations.DonationsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView, BaseFragment.ActivityListener, SharedPreferencesView {

    @BindView(R.id.navigation)
    AHBottomNavigation bottomBarView;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    private MainPresenter presenter;
    private SharedPreferences preferences;
    private Toolbar myToolbar;

    private static final String GOOGLE_PUBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA26PDdUKgq2kMQsOVxnJ0elEquDXue03TOCuJPyOHAbuArGH5+9XMWGXBkLOxJW3yKVQB7P7u2rFtOMyPMSObm8Ja8yHWT5o+fELttwPW+QDOtYspURcvf9QCJPHU0IyEwNWBJLfWqrLaqFlrdmJKnmsihcwsKot53jHGv+FxehGNrb00sBy9k5oau1DlqlrNMsCDzHdp0GZGXuJ5RF2x+vh3AXHVLmvNZes1LRgYRRppVLNcFwdiLTqqRFJ9+m3OTRRC18x7QfM8HY+d2mj4IcMhbeP/8IsUf2CxeGe+1Ot1N4UXQ/xrSx91W74HJDhWoQERpN+v/Ea8/zDxNlWrUwIDAQAB";
    private static final String[] GOOGLE_CATALOG = new String[]{"donate_1",
            "donate_2", "donate_3", "donate_5", "donate_8",
            "donate_13"};

    private static final String BITCOIN_ADDRESS = "1BmqSrEkvqBLZ1AGaaCJb73bzRkijKDgPN";

    @Override
    protected void onResume() {
        preferences = getSharedPreferences();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferences = getSharedPreferences();
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        if (isDarkMode()) {
            if (Build.VERSION.SDK_INT >= 23) {
                UiModeManager uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
                uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
            } else {
                getDelegate().setLocalNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                UiModeManager uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
                uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
            } else {
                getDelegate().setLocalNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
            }

        }

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
        bottomBarView.setTranslucentNavigationEnabled(false);


        if (savedInstanceState == null)
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
        donateDialog();
    }

    private void setNighModeThemes() {
        if (isDarkMode()) {
            bottomBarView.setDefaultBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.toolbarDark, null));
            bottomBarView.setAccentColor(ResourcesCompat.getColor(getResources(), R.color.tabDark, null));
            searchView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.toolbarDark, null));
            myToolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.toolbarDark, null));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.toolbarDark)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.toolbarDark)); //status bar or the time bar at the top
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

        switch (id) {
            case R.id.menu_settings:
                presenter.navigationSettingsClicked();
                return true;
            case R.id.menu_rating:
                presenter.navigationRatingClicked();
                return true;
            case R.id.menu_donate:
                presenter.navigationDonateClicked();
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

    @Override
    public void refreshPaperList() {
        PapersFragment fragment = (PapersFragment) getCurrentFragment();
        fragment.getPresenter().navigationRefreshClicked();
    }


    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content);
    }

    @Override
    public void goToSettings() {
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    public void donateDialog() {
        int num = preferences.getInt("donate", 0);
        boolean askAgain = preferences.getBoolean("askagain", true);

        preferences
                .edit()
                .putInt("donate", num + 1)
                .apply();

        Log.d("number", String.valueOf(num));

        if (num % 10 != 0) return;

        if(!askAgain) return;
        // Place your dialog code here to display the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Donate");
        builder.setMessage("Consider donating to support the developer.");
        builder.setPositiveButton("Yes, Donate",
                (dialog, id) -> {
                    preferences
                            .edit()
                            .putBoolean("askagain", false)
                            .apply();
                    goToDonate();
                });

        builder.setNegativeButton("Not now",
                (dialog, id) -> dialog.cancel());

        builder.setNeutralButton("do not ask again",
                (dialog, id) -> {
                    preferences
                            .edit()
                            .putBoolean("askagain", false)
                            .apply();
                    dialog.cancel();
                });
        builder.create().show();
    }

    @Override
    public void goToRating() {
        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .ratingBarColor(R.color.grey_500)
                .threshold(3)
                .onRatingBarFormSumbit(this::sendFeedbackEmail).build();

        ratingDialog.show();
    }

    @Override
    public void goToDonate() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DonationsFragment donationsFragment;
        if (BuildConfig.DONATIONS_GOOGLE) {
            donationsFragment = DonationsFragment.newInstance(BuildConfig.DEBUG, true, GOOGLE_PUBKEY, GOOGLE_CATALOG,
                    getResources().getStringArray(R.array.donation_google_catalog_values), false, null, null,
                    null, false, null, null, false, null);
        } else {
            donationsFragment = DonationsFragment.newInstance(BuildConfig.DEBUG, false, null, null, null, false, null,
                    null, null, false, null, null, true, BITCOIN_ADDRESS);
        }

        ft.replace(R.id.content, donationsFragment, "donationsFragment");
        ft.addToBackStack(null);
        ft.commit();
    }

    private void goToRatingAuto() {
        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .ratingBarColor(R.color.grey_500)
                .threshold(3)
                .session(7)
                .onRatingBarFormSumbit(this::sendFeedbackEmail).build();
        ratingDialog.show();
    }

    private void sendFeedbackEmail(String feedback) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"garrettbdev@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "arXiv eXplorer Feedback");
        i.putExtra(Intent.EXTRA_TEXT, feedback);
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
        } else {
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
        return getSortBy().equals(getResources().getString(R.string.lastUpdatedDate));
    }

    @Override
    public boolean isRelevanceDate() {
        return getSortBy().equals(getResources().getString(R.string.relevance));
    }

    @Override
    public boolean isPublishedDate() {
        return getSortBy().equals(getResources().getString(R.string.submittedDate));
    }

    @Override
    public boolean isRenderLatex() {
        return preferences.getBoolean(getString(R.string.render_latex_key), getResources().getBoolean(R.bool.render_latex_default));
    }

    @Override
    public boolean isReadIndicator() {
        return preferences.getBoolean(getString(R.string.read_key), getResources().getBoolean(R.bool.read_default));
    }

    private boolean isDarkMode() {
        return preferences.getBoolean(getString(R.string.dark_mode_key), getResources().getBoolean(R.bool.dark_mode_default));
    }

    public boolean isShowAbstract() {
        return preferences.getBoolean(getString(R.string.show_abstract_key), getResources().getBoolean(R.bool.show_abstract_default));
    }

    public String getSortOrder() {
        return preferences.getString(getString(R.string.sort_order_list_key), getResources().getString(R.string.sort_order_default));
    }

    public String getSortBy() {
        return preferences.getString(getString(R.string.sort_by_list_key), getResources().getString(R.string.sort_by_default));
    }

    public int getMaxResult() {
        return Integer.parseInt(preferences.getString(getString(R.string.max_results_key), String.valueOf(getResources().getInteger(R.integer.max_results_default))));
    }

    /**
     * Needed for Google Play In-app Billing. It uses startIntentSenderForResult(). The result is not propagated to
     * the Fragment like in startActivityForResult(). Thus we need to propagate manually to our Fragment.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("donationsFragment");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}

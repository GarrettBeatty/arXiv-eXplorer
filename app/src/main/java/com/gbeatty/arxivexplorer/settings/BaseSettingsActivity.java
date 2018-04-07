package com.gbeatty.arxivexplorer.settings;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.dashboard.DashboardFragment;

public class BaseSettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    protected static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, value) -> {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);

        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);

        }
        return true;
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    protected static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    protected static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    protected void setupActionBar() {
        ViewGroup rootView = findViewById(R.id.action_bar_root); //id from appcompat

        if (rootView != null) {
            View view = getLayoutInflater().inflate(R.layout.toolbar, rootView, false);
            rootView.addView(view, 0);

            Toolbar myToolbar = findViewById(R.id.toolbar);

            if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("dark_mode", false)) {
                myToolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.toolbarLight, null));
            } else {
                myToolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.toolbarDark, null));
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.toolbarDark)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
                    getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.toolbarDark)); //status bar or the time bar at the top
                }
            }

            setSupportActionBar(myToolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || DashboardFragment.class.getName().equals(fragmentName)
                || SettingsActivity.PrefsFragment.class.getName().equals(fragmentName);
    }
}

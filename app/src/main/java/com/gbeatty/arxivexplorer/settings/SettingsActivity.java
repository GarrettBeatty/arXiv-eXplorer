package com.gbeatty.arxivexplorer.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.helpers.Helper;
import com.gbeatty.arxivexplorer.network.ArxivAPI;

import java.io.File;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends BaseSettingsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsActivity.PrefsFragment()).commit();
        setupActionBar();
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(getActivity());

            PreferenceCategory generalCategory = new PreferenceCategory(getActivity());
            generalCategory.setTitle("General Preferences");

            screen.addPreference(generalCategory);

            ListPreference sortBy = new ListPreference(getActivity());
            sortBy.setDefaultValue(ArxivAPI.SORT_BY_SUBMITTED_DATE);
            sortBy.setEntries(R.array.pref_sort_by_list_titles);
            sortBy.setEntryValues(R.array.pref_sort_by_list_values);
            sortBy.setKey("sort_by_list");
            sortBy.setPositiveButtonText(null);
            sortBy.setNegativeButtonText(null);
            sortBy.setTitle(R.string.pref_title_sort_by);

            ListPreference sortOrder = new ListPreference(getActivity());
            sortOrder.setDefaultValue(ArxivAPI.SORT_ORDER_DESCENDING);
            sortOrder.setEntries(R.array.pref_sort_order_list_titles);
            sortOrder.setEntryValues(R.array.pref_sort_order_list_values);
            sortOrder.setKey("sort_order_list");
            sortOrder.setPositiveButtonText(null);
            sortOrder.setNegativeButtonText(null);
            sortOrder.setTitle(R.string.pref_title_sort_order);

            EditTextPreference maxResults = new EditTextPreference(getActivity());
            maxResults.setDefaultValue("10");
            maxResults.setKey("max_results");
            maxResults.setTitle(R.string.pref_title_max_results);
            maxResults.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

            SwitchPreference showAbstract = new SwitchPreference(getActivity());
            showAbstract.setDefaultValue(false);
            showAbstract.setKey("show_abstract");
            showAbstract.setTitle(R.string.pref_title_show_abstract);

            Preference deleteDownloadedPapers = new Preference(getActivity());
            deleteDownloadedPapers.setKey("delete_downloaded_papers");
            deleteDownloadedPapers.setTitle(R.string.pref_title_clear_downloads);

            SwitchPreference darkMode = new SwitchPreference(getActivity());
            darkMode.setDefaultValue(false);
            darkMode.setKey("dark_mode");
            darkMode.setTitle(R.string.pref_title_dark_mode);

            generalCategory.addPreference(sortBy);
            generalCategory.addPreference(sortOrder);
            generalCategory.addPreference(maxResults);
            generalCategory.addPreference(showAbstract);
            generalCategory.addPreference(deleteDownloadedPapers);
            generalCategory.addPreference(darkMode);

            PreferenceCategory dashboardCategory = new PreferenceCategory(getActivity());
            dashboardCategory.setTitle("Dashboard Preferences");
            screen.addPreference(dashboardCategory);

            Preference dashboardPreferences = new Preference(getActivity());
            dashboardPreferences.setKey("dashboard_preferences");
            dashboardPreferences.setTitle(R.string.pref_title_dashboard_preferences);

            dashboardCategory.addPreference(dashboardPreferences);

            setPreferenceScreen(screen);

            bindPreferenceSummaryToValue(sortOrder);
            bindPreferenceSummaryToValue(sortBy);
            bindPreferenceSummaryToValue(maxResults);

            deleteDownloadedPapers.setOnPreferenceClickListener(preference -> {
                Helper.deleteFilesDir(new File(getActivity().getFilesDir(), "papers"));
                Toast.makeText(preference.getContext(), "Deleted Downloaded Papers", Toast.LENGTH_SHORT).show();
                return true;
            });

            dashboardPreferences.setOnPreferenceClickListener(preference -> {
                final Intent preferencesActivity = new Intent(getActivity(), DashboardSettingsActivity.class);
                preferencesActivity.putExtra("Dashboard Settings", "dashboard_settings");
                startActivity(preferencesActivity);
                return true;
            });

            darkMode.setOnPreferenceClickListener(preference -> {
                Intent i = getActivity().getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().finish();
                startActivity(i);
                return true;
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}

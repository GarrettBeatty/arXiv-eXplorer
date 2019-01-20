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
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.helpers.Helper;

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

    public static class PrefsFragment extends PreferenceFragment implements SettingsView {

        SettingsPresenter presenter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            presenter = new SettingsPresenter(this);

            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(getActivity());

            //DASHBOARD PREFERENCES
            PreferenceCategory dashboardCategory = new PreferenceCategory(getActivity());
            dashboardCategory.setTitle(R.string.dashboard_preferences);
            screen.addPreference(dashboardCategory);

            Preference dashboardPreferences = new Preference(getActivity());
            dashboardPreferences.setKey(getString(R.string.dashboard_preferences_key));
            dashboardPreferences.setTitle(R.string.pref_title_dashboard_preferences);

            dashboardCategory.addPreference(dashboardPreferences);

            setPreferenceScreen(screen);
            //END DASHBOARD PREFERENCES


            PreferenceCategory generalCategory = new PreferenceCategory(getActivity());
            generalCategory.setTitle(R.string.general_preferences);

            screen.addPreference(generalCategory);

            ListPreference sortBy = new ListPreference(getActivity());
            sortBy.setDefaultValue(getResources().getString(R.string.sort_by_default));
            sortBy.setEntries(R.array.pref_sort_by_list_titles);
            sortBy.setEntryValues(R.array.pref_sort_by_list_values);
            sortBy.setKey(getString(R.string.sort_by_list_key));
            sortBy.setPositiveButtonText(null);
            sortBy.setNegativeButtonText(null);
            sortBy.setTitle(R.string.pref_title_sort_by);

            ListPreference sortOrder = new ListPreference(getActivity());
            sortOrder.setDefaultValue(getResources().getString(R.string.sort_order_default));
            sortOrder.setEntries(R.array.pref_sort_order_list_titles);
            sortOrder.setEntryValues(R.array.pref_sort_order_list_values);
            sortOrder.setKey(getString(R.string.sort_order_list_key));
            sortOrder.setPositiveButtonText(null);
            sortOrder.setNegativeButtonText(null);
            sortOrder.setTitle(R.string.pref_title_sort_order);

            EditTextPreference maxResults = new EditTextPreference(getActivity());
            maxResults.setDefaultValue(getResources().getInteger(R.integer.max_results_default));
            maxResults.setKey(getString(R.string.max_results_key));
            maxResults.setTitle(R.string.pref_title_max_results);
            maxResults.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

            Preference deleteDownloadedPapers = new Preference(getActivity());
            deleteDownloadedPapers.setKey(getString(R.string.delete_downloaded_papers_key));
            deleteDownloadedPapers.setTitle(R.string.pref_title_clear_downloads);

            SwitchPreference showAbstract = new SwitchPreference(getActivity());
            showAbstract.setDefaultValue(getResources().getBoolean(R.bool.show_abstract_default));
            showAbstract.setKey(getString(R.string.show_abstract_key));
            showAbstract.setTitle(R.string.pref_title_show_abstract);

            SwitchPreference darkMode = new SwitchPreference(getActivity());
            darkMode.setDefaultValue(getResources().getBoolean(R.bool.dark_mode_default));
            darkMode.setKey(getString(R.string.dark_mode_key));
            darkMode.setTitle(R.string.pref_title_dark_mode);

            SwitchPreference latex = new SwitchPreference(getActivity());
            latex.setDefaultValue(getResources().getBoolean(R.bool.render_latex_default));
            latex.setKey(getString(R.string.render_latex_key));
            latex.setTitle(R.string.pref_title_latex);

            generalCategory.addPreference(sortBy);
            generalCategory.addPreference(sortOrder);
            generalCategory.addPreference(maxResults);
            generalCategory.addPreference(showAbstract);
            generalCategory.addPreference(darkMode);
            generalCategory.addPreference(latex);
            generalCategory.addPreference(deleteDownloadedPapers);

            bindPreferenceSummaryToValue(sortOrder);
            bindPreferenceSummaryToValue(sortBy);
            bindPreferenceSummaryToValue(maxResults);

            deleteDownloadedPapers.setOnPreferenceClickListener(preference -> {
                presenter.deleteDownloadedPapersClicked();
                return true;
            });

            dashboardPreferences.setOnPreferenceClickListener(preference -> {
                presenter.dashboardPreferencesClicked();
                return true;
            });

            darkMode.setOnPreferenceClickListener(preference -> {
                presenter.darkModeClicked();
                return true;
            });

            latex.setOnPreferenceChangeListener((preference, o) -> {
                presenter.latexClicked(((SwitchPreference) preference).isChecked());
                return true;
            });

        }

        @Override
        public void deleteDownloadedPapers() {
            Helper.deleteFilesDir(new File(getActivity().getFilesDir(), "papers"));
            Toast.makeText(getActivity(), "Deleted Downloaded Papers", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void goToDashboardPreferences() {
            final Intent preferencesActivity = new Intent(getActivity(), DashboardSettingsActivity.class);
            preferencesActivity.putExtra("Dashboard Settings", "dashboard_settings");
            startActivity(preferencesActivity);
        }

        @Override
        public void goToDarkMode() {
            Intent i = getActivity().getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().finish();
            startActivity(i);
        }

        @Override
        public void showLatexWarning() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Rendering Latex may affect performance.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        //do things
                    });
            AlertDialog alert = builder.create();
            alert.show();
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

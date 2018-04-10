package com.gbeatty.arxivexplorer.settings;

class SettingsPresenter {

    private SettingsView view;

    public SettingsPresenter(SettingsView view) {
        this.view = view;
    }

    public void deleteDownloadedPapersClicked() {
        view.deleteDownloadedPapers();
    }

    public void dashboardPreferencesClicked() {
        view.goToDashboardPreferences();
    }

    public void darkModeClicked() {
        view.goToDarkMode();
    }

    public void latexClicked(boolean checked) {
        if(!checked) view.showLatexWarning();
    }
}

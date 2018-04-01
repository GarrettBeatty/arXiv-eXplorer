package com.gbeatty.arxivexplorer.base;

import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

public abstract class BasePresenter {

    private final SharedPreferencesView sharedPreferencesView;

    protected BasePresenter(SharedPreferencesView sharedPreferencesView){
        this.sharedPreferencesView = sharedPreferencesView;
    }

    protected SharedPreferencesView getSharedPreferenceView(){
        return sharedPreferencesView;
    }

}

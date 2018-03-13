package com.gbeatty.arxivexplorer.base;

import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

public abstract class BasePresenter {

    private SharedPreferencesView sharedPreferencesView;

    public BasePresenter(SharedPreferencesView sharedPreferencesView){
        this.sharedPreferencesView = sharedPreferencesView;
    }

    public SharedPreferencesView getSharedPreferenceView(){
        return sharedPreferencesView;
    }

}

package com.gbeatty.arxivexplorer.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.gbeatty.arxivexplorer.models.Paper;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

import java.util.ArrayList;

public abstract class BaseFragment extends Fragment implements SharedPreferencesView {

    private ActivityListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ActivityListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    protected void showFragment(int fragmentContainerId, BaseFragment fragment, String backStateName) {
        listener.showFragment(fragmentContainerId, fragment, backStateName);
    }

    public interface ActivityListener {
        void showFragment(int fragmentContainerId, BaseFragment fragment, String backStateName);

        String getSortOrder();

        String getSortBy();

        int getMaxResult();

        void showLoading();

        void errorLoading();

        void dismissLoading();

        void switchToPapersFragment(ArrayList<Paper> papers, String tag, String query, int maxResult);

        boolean isShowAbstract();

        SharedPreferences getSharedPreferences();
    }

    public void showLoading(){
        listener.showLoading();
    }

    public void switchToPapersFragment(ArrayList<Paper> papers, String tag, String query, int maxResult){
        listener.switchToPapersFragment(papers, tag, query, maxResult);
    }

    public void dismissLoading(){
        listener.dismissLoading();
    }

    public void errorLoading() {
        listener.errorLoading();
    }

    @Override
    public String getSortOrder() {
        return listener.getSortOrder();
    }

    @Override
    public String getSortBy() {
        return listener.getSortBy();
    }

    @Override
    public int getMaxResult() {
        return listener.getMaxResult();
    }
    
    public boolean isShowAbstract(){return  listener.isShowAbstract();}

    public SharedPreferences getSharedPreferences(){return listener.getSharedPreferences();}
}

package com.gbeatty.arxivexplorer.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

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

    @Override
    public boolean isShowAbstract() {
        return listener.isShowAbstract();
    }

    @Override
    public boolean isDashboardCategoryChecked(String categoryName) {
        return listener.isDashboardCategoryChecked(categoryName);
    }

    @Override
    public boolean isLastUpdatedDate() {
        return listener.isLastUpdatedDate();
    }

    @Override
    public boolean isPublishedDate() {
        return listener.isPublishedDate();
    }

    @Override
    public boolean isRelevanceDate() {
        return listener.isRelevanceDate();
    }

    @Override
    public boolean isRenderLatex() {
        return listener.isRenderLatex();
    }

    public void showError() {
        if (getActivity() == null) return;
        if(getContext() == null) return;
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), R.string.error_loading, Toast.LENGTH_SHORT).show());
    }

    public interface ActivityListener {
        void showFragment(int fragmentContainerId, BaseFragment fragment, String backStateName);

        String getSortOrder();

        String getSortBy();

        int getMaxResult();

        boolean isShowAbstract();

        SharedPreferences getSharedPreferences();

        boolean isDashboardCategoryChecked(String categoryName);

        boolean isPublishedDate();

        boolean isLastUpdatedDate();

        boolean isRelevanceDate();

        boolean isRenderLatex();
    }

}

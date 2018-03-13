package com.gbeatty.arxivexplorer.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.gbeatty.arxivexplorer.R;
import com.gbeatty.arxivexplorer.network.ArxivAPI;
import com.gbeatty.arxivexplorer.settings.SharedPreferencesView;

public abstract class BaseFragment extends Fragment implements SharedPreferencesView{

    private ActivityListener listener;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
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
    }

    public void showLoading(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading..");
        progressDialog.setTitle("Loading Papers");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismissLoading(){
        progressDialog.dismiss();
    }

    public void errorLoading() {
        getActivity().runOnUiThread(() -> {
            dismissLoading();
            Toast.makeText(getContext(), "Error Loading Papers", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public String getSortOrder() {
        return preferences.getString("sort_order_list", ArxivAPI.SORT_ORDER_DESCENDING);
    }

    @Override
    public String getSortBy() {
        return preferences.getString("sort_by_list", ArxivAPI.SORT_BY_LAST_UPDATED_DATE);
    }

    @Override
    public int getMaxResult() {
        return Integer.parseInt(preferences.getString("max_results", getString(R.string.maxResultDefault)));
    }
}

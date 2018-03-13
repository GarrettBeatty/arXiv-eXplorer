package com.gbeatty.arxivexplorer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.gbeatty.arxivexplorer.R;

public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.ActivityListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Method used to show a fragment in a Container View inside the Activity
    public void showFragment(int fragmentContainerId, BaseFragment fragment, String backStateName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            transaction.replace(fragmentContainerId, fragment, backStateName);
            transaction.addToBackStack(backStateName);
            transaction.commit();
            fragmentManager.executePendingTransactions();
        }
    }
}

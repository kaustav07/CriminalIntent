package com.android.bignerdranch.criminalintent;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentlayout);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentlayout,fragment).commit();
        }
    }

    @LayoutRes
    public int getResId(){
        return R.layout.activity_fragment;
    }

    public abstract Fragment createFragment();
}

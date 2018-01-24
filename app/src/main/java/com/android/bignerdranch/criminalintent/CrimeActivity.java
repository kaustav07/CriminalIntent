package com.android.bignerdranch.criminalintent;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.bignerdranch.criminalintent.DBHelpers.DBschema;
import com.android.bignerdranch.criminalintent.Fragments.CrimeFragment;
import com.android.bignerdranch.criminalintent.Model.Crime;
import com.android.bignerdranch.criminalintent.Model.CrimeLab;

import java.util.List;
import java.util.UUID;

public class CrimeActivity extends AppCompatActivity implements CrimeFragment.CrimeCallback{

    public static String CRIME_UUID = "crimeuuid";
    CrimeLab mCrimeLab;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crime_detail);
        viewPager = (ViewPager) findViewById(R.id.crime_viewpager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mCrimeLab = CrimeLab.newInstance(getApplicationContext());
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.newInstance(mCrimeLab.getCrimes().get(position).getId());
            }

            @Override
            public int getCount() {
                return CrimeLab.newInstance(getApplicationContext()).getCrimes().size();
            }
        });
        List<Crime> crimes = mCrimeLab.getCrimes();
        UUID id = (UUID)getIntent().getSerializableExtra(CRIME_UUID);
        int index = 0;
        while(true){
            if(crimes.get(index).getId().equals(id))
                break;
            else
                index++;
        }
        viewPager.setCurrentItem(index);
    }

    public void gofirst(View v){
        viewPager.setCurrentItem(0);
    }

    public void golast(View v){
        viewPager.setCurrentItem(mCrimeLab.getCrimes().size() - 1);
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeLab mCrimeLab = CrimeLab.newInstance(this);
        mCrimeLab.updateCrime(mCrimeLab.getContentValues(crime), DBschema.CrimeTable.Cols.UUID + "= ?",new String[] {crime.getId().toString()});
    }
}

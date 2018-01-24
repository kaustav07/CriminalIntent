package com.android.bignerdranch.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.bignerdranch.criminalintent.DBHelpers.DBschema;
import com.android.bignerdranch.criminalintent.Fragments.CrimeFragment;
import com.android.bignerdranch.criminalintent.Fragments.CrimeListFragment;
import com.android.bignerdranch.criminalintent.Model.Crime;
import com.android.bignerdranch.criminalintent.Model.CrimeLab;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.CrimeCallBack,CrimeFragment.CrimeCallback {

    @Override
    public Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    public int getResId(){
        return R.layout.activitymasterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
       if(findViewById(R.id.fragmentdetaillayout) == null){
           Intent intent = new Intent(this, CrimeActivity.class);
           intent.putExtra(CrimeActivity.CRIME_UUID,crime.getId());
           startActivity(intent);
       }
       else {
           Fragment crimefragment = CrimeFragment.newInstance(crime.getId());
           getSupportFragmentManager().beginTransaction().replace(R.id.fragmentdetaillayout,crimefragment).commit();
           updateList();
       }
    }

    @Override
    public void onCrmeDeleted(Crime crime) {
        CrimeLab mCrimeLab = CrimeLab.newInstance(this);
        mCrimeLab.deleteCrime(DBschema.CrimeTable.Cols.UUID + "= ?",new String[] {crime.getId().toString()});
        updateList();
        if(findViewById(R.id.fragmentdetaillayout) == null) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragmentdetaillayout));
        }

    }


    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeLab mCrimeLab = CrimeLab.newInstance(this);
        mCrimeLab.updateCrime(mCrimeLab.getContentValues(crime), DBschema.CrimeTable.Cols.UUID + "= ?",new String[] {crime.getId().toString()});
        if(findViewById(R.id.fragmentdetaillayout) != null)
            updateList();
    }

    private void updateList() {
        CrimeListFragment fm = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentlayout);
        if(fm != null)
            fm.updateUI();
    }
}

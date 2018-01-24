package com.android.bignerdranch.criminalintent.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bignerdranch.criminalintent.CrimeActivity;
import com.android.bignerdranch.criminalintent.DBHelpers.DBschema;
import com.android.bignerdranch.criminalintent.Model.Crime;
import com.android.bignerdranch.criminalintent.Model.CrimeLab;
import com.android.bignerdranch.criminalintent.R;
import com.android.bignerdranch.criminalintent.Utils.SwipeHelper;

import java.util.List;
import java.util.UUID;


public class CrimeListFragment extends Fragment {

    CrimeLab mCrimeLab;
    RecyclerView recyclerView;
    CrimeAdapter crimeAdapter;
    CrimeCallBack mCrimeCallBack;

    public CrimeListFragment() {
        // Required empty public constructor
    }

    public interface CrimeCallBack{
        void onCrimeSelected(Crime crime);
        void onCrmeDeleted(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCrimeCallBack = (CrimeCallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCrimeCallBack = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrimeLab = CrimeLab.newInstance(getActivity().getApplicationContext());
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mytoolmenu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addcrime:
                addCrime();
                break;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.crimelistview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        ItemTouchHelper.Callback swipeHelperRight = new SwipeHelper(0,ItemTouchHelper.END,crimeAdapter);
        ItemTouchHelper.Callback swipeHelperLeft = new SwipeHelper(0,ItemTouchHelper.START,crimeAdapter);
        ItemTouchHelper helperRight = new ItemTouchHelper(swipeHelperRight);
        ItemTouchHelper helperLeft = new ItemTouchHelper(swipeHelperLeft);
        helperRight.attachToRecyclerView(recyclerView);
        helperLeft.attachToRecyclerView(recyclerView);
        return v;
    }


    public void updateUI() {
        mCrimeLab = CrimeLab.newInstance(getActivity().getApplicationContext());
        List<Crime> crimes = mCrimeLab.getCrimes();
        if(crimeAdapter == null) {
            crimeAdapter = new CrimeAdapter(crimes);
            recyclerView.setAdapter(crimeAdapter);

        }
        else {
            crimeAdapter.setCrimes(crimes);
            crimeAdapter.notifyDataSetChanged();
        }

    }

    private void addCrime(){
        Crime newcrime = new Crime();
        mCrimeLab.insertCrime(mCrimeLab.getContentValues(newcrime),newcrime);
        mCrimeCallBack.onCrimeSelected(newcrime);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTitle;
        TextView mDate;
        Button mPolice;
        Crime mCrime;

        public void setCrime(Crime crime) {
            mCrime = crime;
        }


        public CrimeHolder(View v,int viewType) {
                super(v);

            mTitle = (TextView) itemView.findViewById(R.id.crimeitemtitle);
            mDate = (TextView) itemView.findViewById(R.id.crimeitemdate);
            if(viewType == 1){
                mPolice = (Button) itemView.findViewById(R.id.callpolice);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCrimeCallBack.onCrimeSelected(mCrime);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeListFragment.CrimeHolder> implements SwipeHelper.SwipeHelperAdapter{

        List<Crime> mCrimes;

        CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeListFragment.CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            CrimeHolder crimeHolder;
            if(viewType == 1)
                crimeHolder = new CrimeHolder(inflater.inflate(R.layout.crime_list_item_police,parent,false),viewType);
            else
                crimeHolder = new CrimeHolder(inflater.inflate(R.layout.crime_list_item,parent,false),viewType);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(CrimeListFragment.CrimeHolder holder, final int position) {
            holder.setCrime(mCrimes.get(position));
            holder.mTitle.setText(mCrimes.get(position).getTitle());
            holder.mDate.setText(mCrimes.get(position).getDate().toString());
            if(holder.mPolice != null) {
                holder.mPolice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Required - " + !mCrimes.get(position).getSolved(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(!mCrimes.get(position).getSolved())
                return 1;
            else
                return 0;
        }

        @Override
        public void onCrimeDeleted(int position) {
            mCrimeCallBack.onCrmeDeleted(mCrimes.get(position));
        }
    }
}



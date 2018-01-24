package com.android.bignerdranch.criminalintent.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.android.bignerdranch.criminalintent.Fragments.CrimeListFragment;
import com.android.bignerdranch.criminalintent.Model.Crime;

/**
 * Created by Kaustav on 21-11-2017.
 */

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    private SwipeHelperAdapter mSwipeHelperAdapter;

    /**
     * Creates a Callback for the given drag and swipe allowance. These values serve as
     * defaults
     * and if you want to customize behavior per ViewHolder, you can override
     * {@link #getSwipeDirs(RecyclerView, ViewHolder)}
     * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
     *
     * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     */
    public SwipeHelper(int dragDirs, int swipeDirs,SwipeHelperAdapter adapter) {
        super(dragDirs, swipeDirs);
        mSwipeHelperAdapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mSwipeHelperAdapter.onCrimeDeleted(viewHolder.getAdapterPosition());
    }

    public void setAdapter(SwipeHelperAdapter adapter){
        mSwipeHelperAdapter = adapter;
    }


    public interface SwipeHelperAdapter{
        public void onCrimeDeleted(int position);
    }

}

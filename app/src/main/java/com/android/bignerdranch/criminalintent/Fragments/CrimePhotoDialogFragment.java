package com.android.bignerdranch.criminalintent.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;

import com.android.bignerdranch.criminalintent.R;
import com.android.bignerdranch.criminalintent.Utils.PicUtils;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimePhotoDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimePhotoDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static File mPhoto = null;
    private static String PHITO_ID = "photo_id";
    ImageView mImageView = null;


    public CrimePhotoDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrimePhotoDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrimePhotoDialogFragment newInstance(File param2) {
        CrimePhotoDialogFragment fragment = new CrimePhotoDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(CrimePhotoDialogFragment.PHITO_ID,param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhoto = (File) getArguments().getSerializable(CrimePhotoDialogFragment.PHITO_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_crime_photo_dialog, container, false);
        mImageView = (ImageView) view.findViewById(R.id.zommimage);
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(mImageView != null && mPhoto != null)
                    mImageView.setImageBitmap(PicUtils.getScaledBitmap(mPhoto.getPath(),mImageView.getHeight(),mImageView.getWidth()));
                else
                    mImageView.setImageDrawable(null);
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        return dialog;
    }
}

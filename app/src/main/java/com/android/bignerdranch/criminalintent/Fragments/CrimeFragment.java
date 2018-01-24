package com.android.bignerdranch.criminalintent.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.bignerdranch.criminalintent.DBHelpers.DBschema;
import com.android.bignerdranch.criminalintent.Model.Crime;
import com.android.bignerdranch.criminalintent.Model.CrimeLab;
import com.android.bignerdranch.criminalintent.R;
import com.android.bignerdranch.criminalintent.Utils.PicUtils;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeFragment extends Fragment {

    Crime mCrime;
    CrimeLab mCrimeLab;
    private static String CRIME_ID = "crimeid";
    private static int REQUEST_CONTACT = 100;
    private static int READ_CONTACTS = 101;
    private static int CAPTURE_IMAGE = 102;
    SQLiteDatabase mDatabase;
    Context mContext;
    Button sendReport;
    Button choosesuspect;
    Button callsuspect;
    ImageView imageCrime;
    ImageButton btnImageCrime;
    File mPhotoFile;
    CrimeCallback mCrimeCallback;


    public CrimeFragment() {
        // Required empty public constructor
    }

    public static CrimeFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(CrimeFragment.CRIME_ID,id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCrimeCallback = (CrimeCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCrimeCallback = null;
    }

    public interface CrimeCallback{
        public void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrimeLab = CrimeLab.newInstance(getActivity().getApplicationContext());
        mCrime = mCrimeLab.getCrime((UUID) getArguments().get(CrimeFragment.CRIME_ID));
        mPhotoFile = mCrimeLab.getPhotoFile(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_crime, container, false);
        sendReport = (Button) v.findViewById(R.id.sendreport);
        choosesuspect = (Button) v.findViewById(R.id.choosesuspect);
        callsuspect = (Button) v.findViewById(R.id.callsuspect);
        imageCrime = (ImageView) v.findViewById(R.id.crimeimage);
        btnImageCrime = (ImageButton) v.findViewById(R.id.capturebtn);
        EditText mTitle = (EditText) v.findViewById(R.id.crimetitle);
        mTitle.setText(mCrime.getTitle());
        CheckBox mSolved = (CheckBox) v.findViewById(R.id.crimesolved);
        mSolved.setChecked(mCrime.getSolved());
        Button mDate = (Button) v.findViewById(R.id.crimedate);
        mDate.setText(mCrime.getDate().toString());
        mDate.setEnabled(false);
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                //CrimeLab.newInstance(getActivity().getApplicationContext()).getCrime((UUID) getArguments().get(CrimeFragment.CRIME_ID)).setTitle(s.toString());
                mCrimeCallback.onCrimeUpdated(mCrime);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(mCrime.getSuspect() != null)
            choosesuspect.setText(mCrime.getSuspect());
        if(mCrime.getSuspectNumber() == null || mCrime.getSuspectNumber().equals(""))
            callsuspect.setEnabled(false);

        ViewTreeObserver observer = imageCrime.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(mPhotoFile == null || !mPhotoFile.exists())
                    imageCrime.setImageDrawable(null);
                else {
                    Bitmap bitmap = PicUtils.getScaledBitmap(mPhotoFile.getPath(), imageCrime.getHeight(),imageCrime.getWidth());
                    imageCrime.setImageBitmap(bitmap);
                }
            }
        });

        imageCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri fileuri = FileProvider.getUriForFile(getActivity(),"com.android.bignerdranch.criminalintent.fileprovider",mCrimeLab.getPhotoFile(mCrime));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String mimeType = getActivity().getApplicationContext().getContentResolver().getType(fileuri);
                intent.setDataAndType(fileuri,mimeType);
                List<ResolveInfo> activities = getActivity().getPackageManager().queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);

                for(ResolveInfo cameraact:activities){
                    getActivity().grantUriPermission(cameraact.activityInfo.packageName,fileuri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                startActivity(intent);
                /*android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CrimePhotoDialogFragment crimePhotoDialogFragment = CrimePhotoDialogFragment.newInstance(mPhotoFile);
                crimePhotoDialogFragment.show(fm,"photo");*/
            }
        });


        mSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
               // CrimeLab.newInstance(getActivity().getApplicationContext()).getCrime((UUID) getArguments().get(CrimeFragment.CRIME_ID)).setSolved(isChecked);
                mCrimeCallback.onCrimeUpdated(mCrime);
            }
        });



        choosesuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    checkforpermissions();
                }else {
                    chooseSuspect();
                }
            }
        });

        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder.from(getActivity()).setSubject(getString(R.string.crime_report_subject))
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .startChooser();
            }
        });

        callsuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCrime.getSuspectNumber() != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mCrime.getSuspectNumber()));
                    startActivity(intent);
                }
            }
        });


        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Boolean cantakephoto = mPhotoFile != null && captureImage.resolveActivity(getActivity().getPackageManager()) != null;
        btnImageCrime.setEnabled(cantakephoto);

        btnImageCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri fileuri = FileProvider.getUriForFile(getActivity(),"com.android.bignerdranch.criminalintent.fileprovider",mCrimeLab.getPhotoFile(mCrime));
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,fileuri);

                List<ResolveInfo> activities = getActivity().getPackageManager().queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY);

                for(ResolveInfo cameraact:activities){
                    getActivity().grantUriPermission(cameraact.activityInfo.packageName,fileuri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage,CAPTURE_IMAGE);

            }
        });

       // updatePhotoView();
        return v;
    }



    public void checkforpermissions(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS);
        }

    }

    public void chooseSuspect(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,REQUEST_CONTACT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode == READ_CONTACTS)
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseSuspect();

                }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            String contactid = null;
            if(requestCode == REQUEST_CONTACT){
                Uri contact = data.getData();

                    Cursor c = getActivity().getContentResolver().query(contact, null, null, null, null);
                    Cursor phones = null;
                try {
                    if (c != null && c.getCount() != 0) {
                        c.moveToFirst();
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        contactid = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        mCrime.setSuspect(name);
                        choosesuspect.setText(name);
                        phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactid, null, null);
                    }


                    if (phones != null && phones.getCount() != 0) {
                        phones.moveToFirst();
                        String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        mCrime.setSuspectNumber(number);
                    }
                    else {
                        mCrime.setSuspectNumber("");
                        callsuspect.setEnabled(false);
                    }

                    //mCrimeLab.updateCrime(mCrimeLab.getContentValues(mCrime), DBschema.CrimeTable.Cols.UUID + "= ?",new String[] {mCrime.getId().toString()});
                    mCrimeCallback.onCrimeUpdated(mCrime);
                }
                finally {
                    if (c != null)
                        c.close();
                    if (phones != null)
                        phones.close();
                }
            }
            else if(requestCode == CAPTURE_IMAGE){
                Uri fileuri = FileProvider.getUriForFile(getActivity(),"com.android.bignerdranch.criminalintent.fileprovider",mCrimeLab.getPhotoFile(mCrime));

                getActivity().revokeUriPermission(fileuri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

               // updatePhotoView();
            }
        }
    }

    public void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists())
            imageCrime.setImageDrawable(null);
        else {
            Bitmap bitmap = PicUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            imageCrime.setImageBitmap(bitmap);
        }
    }

    public String getCrimeReport(){
        String report = null;
        String suspect = null;
        String title = null;
        String date = null;
        String isSolved = null;

        if(mCrime.getSuspect() == null){
            suspect = "no suspect";
        }
        else
            suspect = mCrime.getSuspect();
        if(mCrime.getTitle() == null)
            title = "no title";
        else
            title = mCrime.getTitle();

        String dateformat = "EEE, MMM dd";
        date = DateFormat.format(dateformat,mCrime.getDate()).toString();
        if (mCrime.getSolved())
            isSolved = "yes";
        else
            isSolved = "no";

        report = getString(R.string.crime_send_report_content,title,suspect,date,isSolved);

        return report;
    }
}

package com.android.bignerdranch.criminalintent.Model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;

import com.android.bignerdranch.criminalintent.DBHelpers.CrimeCursorWrapper;
import com.android.bignerdranch.criminalintent.DBHelpers.DBschema;
import com.android.bignerdranch.criminalintent.DBHelpers.MyDBHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Kaustav on 09-09-2017.
 */

public class CrimeLab {

    private static CrimeLab mCrimeLab;
    private Map<UUID,Crime> mCrimes = new LinkedHashMap<>();
    private SQLiteDatabase mDatabase;
    private Context mContext;
    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursorWrapper = querryCrimes(null,null);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast())
            {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }
        finally {
            cursorWrapper.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursorWrapper = querryCrimes(DBschema.CrimeTable.Cols.UUID + "=?",new String[] {id.toString()});
        Crime crime;
        try {
            cursorWrapper.moveToFirst();
            crime = cursorWrapper.getCrime();
        }
        finally {
            cursorWrapper.close();
        }
        return crime;
    }




    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new MyDBHelper(context).getWritableDatabase();
       /* for(int i=0;i<100;i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i % 2 == 0);
            crime.setPoliceRequired(i % 2 == 0);
            mCrimes.put(crime.getId(),crime);
        }*/
    }

    private CrimeCursorWrapper querryCrimes(String whereclaus, String[] whereargs){
        @SuppressLint("Recycle") Cursor cursor = mDatabase.query(DBschema.CrimeTable.NAME,null,whereclaus,whereargs,null,null,null);
        return new CrimeCursorWrapper(cursor);
    }


    public static CrimeLab newInstance(Context context){
        if(mCrimeLab == null)
            mCrimeLab = new CrimeLab(context);
        return mCrimeLab;
    }

    public void updateCrime(ContentValues values, String whereclause, String[] args){
        try {
            mDatabase.update(DBschema.CrimeTable.NAME, values, whereclause, args);
        }
        catch (Exception e){
            Log.d("DBdmg",e.getMessage());
        }
    }

    public void deleteCrime(String whereclause, String[] args){
        mDatabase.delete(DBschema.CrimeTable.NAME,whereclause,args);
    }

    public void insertCrime(ContentValues values,Crime crime){
        mCrimes.put(crime.getId(),crime);
        mDatabase.insert(DBschema.CrimeTable.NAME,null,values);
    }

    public File getPhotoFile(Crime crime){
        File fileDir = mContext.getFilesDir();
        return new File(fileDir,crime.getPhotoName());
    }

    public ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(DBschema.CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(DBschema.CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(DBschema.CrimeTable.Cols.ISSOLVED,crime.getSolved());
        values.put(DBschema.CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(DBschema.CrimeTable.Cols.SUSPECT,crime.getSuspect());
        values.put(DBschema.CrimeTable.Cols.NUMBER,crime.getSuspectNumber());
        return values;
    }

}

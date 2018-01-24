package com.android.bignerdranch.criminalintent.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kaustav on 08-10-2017.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "crimes";
    public static final int VERSION = 1;

    public MyDBHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DBschema.CrimeTable.NAME + "(" +
        " _id integer primary key autoincrement, "
        + DBschema.CrimeTable.Cols.UUID + " , "
        + DBschema.CrimeTable.Cols.TITLE + " , "
        + DBschema.CrimeTable.Cols.DATE + " , "
        + DBschema.CrimeTable.Cols.SUSPECT + " , "
        + DBschema.CrimeTable.Cols.NUMBER + " , "
        + DBschema.CrimeTable.Cols.ISSOLVED + " ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

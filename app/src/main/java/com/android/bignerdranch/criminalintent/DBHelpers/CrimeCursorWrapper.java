package com.android.bignerdranch.criminalintent.DBHelpers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.android.bignerdranch.criminalintent.Model.Crime;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Kaustav on 13-11-2017.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime(){
        String uuidstring = getString(getColumnIndex(DBschema.CrimeTable.Cols.UUID));
        String titlestring = getString(getColumnIndex(DBschema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(DBschema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(DBschema.CrimeTable.Cols.ISSOLVED));

        Crime crime = new Crime(UUID.fromString(uuidstring));
        crime.setTitle(titlestring);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);

        return crime;
    }

}

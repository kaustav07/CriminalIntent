package com.android.bignerdranch.criminalintent.DBHelpers;

/**
 * Created by Kaustav on 08-10-2017.
 */

public final class DBschema {
    public static final class CrimeTable{
        public static final String NAME = "crimes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String ISSOLVED = "issolved";
            public static final String SUSPECT = "suspect";
            public static final String NUMBER = "number";
        }
    }
}

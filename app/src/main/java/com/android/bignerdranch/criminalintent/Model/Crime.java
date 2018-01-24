package com.android.bignerdranch.criminalintent.Model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Kaustav on 09-09-2017.
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private Boolean mSolved;


    private String mSuspectNumber;
    private String mSuspect;

//    public Boolean getPoliceRequired() {
//        return mPoliceRequired;
//    }
//
//    public void setPoliceRequired(Boolean policeRequired) {
//        mPoliceRequired = policeRequired;
//    }
//
//    private Boolean mPoliceRequired;

    public String getSuspectNumber() {
        return mSuspectNumber;
    }

    public void setSuspectNumber(String suspectNumber) {
        mSuspectNumber = suspectNumber;
    }

    public String getPhotoName(){
        return "IMG_"+ getId().toString()+".jpg";
    }

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(UUID id){
        mId = id;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        if(mTitle != null)
            return mTitle;
        else
            return "";
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Boolean getSolved() {
        if(mSolved != null)
            return mSolved;
        else
            return false;
    }

    public void setSolved(Boolean solved) {
        mSolved = solved;
    }
}

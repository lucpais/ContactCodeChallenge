package com.lucas.concept.contactcodechallenge.model;

import com.volcaniccoder.volxfastscroll.ValueArea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contact implements Serializable, Comparable<Contact> {

    @ValueArea
    private String mFirstName;
    private String mLastName;
    private String mUid;
    private String mBirthDate;
    private List<PhoneInfo> mPhoneList;
    private String mHomeAddress;
    private String mWorkAddress;
    private String mThumbUrl;

    public String getHomeAddress() {
        return mHomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.mHomeAddress = homeAddress;
    }

    public String getWorkAddress() {
        return mWorkAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.mWorkAddress = workAddress;
    }

    public Contact() {
        mPhoneList = new ArrayList<>();
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        this.mUid = uid;
    }

    public String getBirthDate() {
        return mBirthDate;
    }

    public void setBirthDate(String birthDate) {
        this.mBirthDate = birthDate;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public List<PhoneInfo> getPhoneList() {
        return mPhoneList;
    }

    public void setPhoneList(List<PhoneInfo> phoneList) {
        this.mPhoneList = phoneList;
    }

    public String getThumbUrl() {
        return mThumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.mThumbUrl = thumbUrl;
    }

    @Override
    public int compareTo(Contact o) {
        return this.getFirstName().compareTo(o.getFirstName());
    }
}

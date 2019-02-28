package com.lucas.concept.contactcodechallenge.model;

import java.io.Serializable;

public class PhoneInfo implements Serializable {

    private String mPhoneType;
    private String mPhoneNumber;

    public String getPhoneType() {
        return mPhoneType;
    }

    public void setPhoneType(String phoneType) {
        this.mPhoneType = phoneType;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }
}

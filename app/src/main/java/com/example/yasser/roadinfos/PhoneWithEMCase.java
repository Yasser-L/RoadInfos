package com.example.yasser.roadinfos;

import java.util.List;

/**
 * Created by Yasser on 29/04/2017.
 */

public class PhoneWithEMCase {

    String PhoneNumber;
    List<Boolean> EmCases;

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public List<Boolean> getEmCases() {
        return EmCases;
    }

    public void setEmCases(List<Boolean> emCases) {
        EmCases = emCases;
    }

    public PhoneWithEMCase(String phoneNumber, List<Boolean> emCases) {
        PhoneNumber = phoneNumber;
        EmCases = emCases;
    }
}

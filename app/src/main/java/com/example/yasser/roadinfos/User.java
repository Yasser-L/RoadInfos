package com.example.yasser.roadinfos;

/**
 * Created by Yasser on 16/05/2017.
 */

public class User {

    private String Email, Password, FirstName, LastName, DoB, Sex, PhoneNumber, UserType;
    private boolean Validated;

    public User(String email, String password, String firstName, String lastName, String doB, String sex, String phoneNumber, String userType, boolean validated) {
        Email = email;
        Password = password;
        FirstName = firstName;
        LastName = lastName;
        DoB = doB;
        Sex = sex;
        PhoneNumber = phoneNumber;
        UserType = userType;
        Validated = validated;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        DoB = doB;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public boolean isValidated() {
        return Validated;
    }

    public void setValidated(boolean validated) {
        Validated = validated;
    }
}

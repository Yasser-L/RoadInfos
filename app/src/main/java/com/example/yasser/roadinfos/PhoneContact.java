package com.example.yasser.roadinfos;

import android.text.Layout;
import android.widget.CheckBox;
import android.widget.ImageButton;

/**
 * Created by Yasser on 12/03/2017.
 */

public class PhoneContact {
    private String name, number;

    public ImageButton getRemoveButton() {
        return removeButton;
    }

    public void setRemoveButton(ImageButton removeButton) {
        this.removeButton = removeButton;
    }

    public Layout getEmergencyCases() {
        return emergencyCases;
    }

    public void setEmergencyCases(Layout emergencyCases) {
        this.emergencyCases = emergencyCases;
    }

    private ImageButton removeButton;
    private CheckBox emAccident;
    private CheckBox emMedical;

    public CheckBox getEmAccident() {
        return emAccident;
    }

    public void setEmAccident(CheckBox emAccident) {
        this.emAccident = emAccident;
    }

    public CheckBox getEmMedical() {
        return emMedical;
    }

    public void setEmMedical(CheckBox emMedical) {
        this.emMedical = emMedical;
    }

    public CheckBox getEmAssault() {
        return emAssault;
    }

    public void setEmAssault(CheckBox emAssault) {
        this.emAssault = emAssault;
    }

    private CheckBox emAssault;
    private Layout emergencyCases;

    public PhoneContact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}

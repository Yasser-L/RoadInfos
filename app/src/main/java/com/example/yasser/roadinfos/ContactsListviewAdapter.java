package com.example.yasser.roadinfos;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Yasser on 02/05/2017.
 */

public class ContactsListviewAdapter extends BaseExpandableListAdapter {

    /*
    Private variables
       */
    private Context context;
    private List<PhoneContact> contactList;
    private List<CheckBox> cbs = new ArrayList<>(Arrays.asList(new CheckBox(context), new CheckBox(context), new CheckBox(context)));
    private HashMap<PhoneContact, List<CheckBox>> cases;
    TextView contact_name, contact_number;
    ImageButton remove_contact;
    CheckBox accident_cb, medical_cb, assault_cb;

    /*
    Constructor
    */

    public ContactsListviewAdapter(List<PhoneContact> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return contactList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return cases.get(contactList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return contactList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return cases.get(contactList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        PhoneContact contact = (PhoneContact) getGroup(groupPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_list_parent, null);
        }

        contact_name = (TextView) convertView.findViewById(R.id.name);
        contact_number = (TextView) convertView.findViewById(R.id.number);
        remove_contact = (ImageButton) convertView.findViewById(R.id.removeContact);

        contact_name.setTypeface(null, Typeface.BOLD);
        contact_name.setText(contact.getName());
        contact_number.setText(contact.getNumber());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PhoneContact contact = (PhoneContact) getChild(groupPosition, childPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_list_child, null);
        }

        accident_cb = (CheckBox) convertView.findViewById(R.id.emCaseAccident);
        medical_cb = (CheckBox) convertView.findViewById(R.id.emCaseMedical);
        assault_cb = (CheckBox) convertView.findViewById(R.id.emCaseAssault);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

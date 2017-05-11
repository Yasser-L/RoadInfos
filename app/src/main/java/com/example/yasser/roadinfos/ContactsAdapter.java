package com.example.yasser.roadinfos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yasser on 13/03/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private List<PhoneContact> contactList;
    private Context context;
    RecyclerView mRecyclerView;
    int mExpandedPosition = -1;
    int currentPosition;
    List< Boolean> checkedCases = new ArrayList<>(Arrays.asList(false, false, false));
    List<PhoneWithEMCase> phoneWithEMCases = new ArrayList<>(3);

    interface OnItemCheckListener {
        void onItemCheck(PhoneContact contact);
        void onItemUncheck(PhoneContact contact);
    }

    @NonNull
    private OnItemCheckListener onItemCheckListener;


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name, number;
        public ImageButton removeContact;
        public View emergencyCases;
        public CheckBox emAccident;
        public CheckBox emMedical;
        public CheckBox emAssault;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            number = (TextView) itemView.findViewById(R.id.number);
            removeContact = (ImageButton) itemView.findViewById(R.id.removeContact) ;
            emergencyCases = itemView.findViewById(R.id.emergencyCases);
            emAccident = (CheckBox) itemView.findViewById(R.id.emCaseAccident);
            emMedical = (CheckBox) itemView.findViewById(R.id.emCaseMedical);
            emAssault = (CheckBox) itemView.findViewById(R.id.emCaseAssault);
            context = name.getContext();

//            checkedCases = new ArrayList<>(Arrays.asList(false, false, false));

//            emAccident.setTag(this);
//            emMedical.setTag(this);
//            emAssault.setTag(this);

            emAccident.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        checkedCases.set(0, true);
                    else
                        checkedCases.set(0, false);
                }
            });

            emMedical.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        checkedCases.set(1, true);
                    else
                        checkedCases.set(1, false);
                }
            });

            emAssault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        checkedCases.set(2, true);
                    else
                        checkedCases.set(2, false);
                }
            });

        }


//        public List<Boolean> CheckedCases(){
//            List<Boolean> checkedCases = new ArrayList<>();
//
//            checkedCases.add(emAccident.isChecked());
//            checkedCases.add(emMedical.isChecked());
//            checkedCases.add(emAssault.isChecked());
//
//            return checkedCases;
//        }

    }

    public ContactsAdapter(List<PhoneContact> contacts){
        this.contactList = contacts;
    }



    public List<PhoneContact> ContactsList(){
        return contactList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }



    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.MyViewHolder holder, final int position) {
        final PhoneContact contact = contactList.get(position);
        holder.name.setText(contact.getName());
        holder.number.setText(contact.getNumber());

        for (PhoneWithEMCase phoneWithEMCase : phoneWithEMCases) {
            phoneWithEMCase.setPhoneNumber(contact.getNumber());
            phoneWithEMCase.setEmCases(checkedCases);
        }


        currentPosition = position;
        final PhoneContact contact1 = contactList.get(position);

        final Boolean acc, med, ass;

        holder.emAccident.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                    checkedCases.set(0, true);
                else
                    checkedCases.set(0, false);
            }
        });

        holder.emMedical.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                    checkedCases.set(1, true);
                else
                    checkedCases.set(1, false);
            }
        });

        holder.emAssault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                    checkedCases.set(2, true);
                else
                    checkedCases.set(2, false);
            }
        });

        phoneWithEMCases.add(currentPosition ,new PhoneWithEMCase(contact.getNumber(), checkedCases));


        holder.removeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                removeItem(contact1);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Toast.makeText(context, "Phew!", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });


        final boolean isExpanded = position==mExpandedPosition;
        holder.emergencyCases.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(mRecyclerView);
                notifyDataSetChanged();
            }
        });
    }






    private void removeItem(PhoneContact contact) {
        int currentPosition = contactList.indexOf(contact);
        contactList.remove(currentPosition);
        notifyItemRemoved(currentPosition);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


}

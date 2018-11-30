package com.example.qtfison.h2gapp.members;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.qtfison.h2gapp.Classes.TextViewDatePicker;
import com.example.qtfison.h2gapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getConvertedDate;

public class Registration extends AppCompatActivity {
    private RadioGroup radioSexGroup, radioStatusGroup;
    private RadioButton radioSexButton, radioStatusButton;
    private CheckBox checkAgree;
    private Button btnAddNew;
    private EditText edtId, edtFname, edtLname, edtEmail, edtPhone, edtAddress, edtRank, edtDeployment, edtJobLocation, edtNID, edtMarriedWith, edtMarriedPhone, edtDOB, edtEntryDate, edtMarriageDate, edtRole;
    private String fName, lName, gender, status, email, phone, address, rank, deployment, jobLocation, NID, marriedWith, marriedPhone, role;
    private String agree;
    private String entryDate, marriageDate, DOB;
    private Member member;
    private FirebaseDatabase database;
    private DatabaseReference myDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Member");
        findViews();
        setDatePick();
        addListenerOnButtons();
    }

    public void addListenerOnButtons() {
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsInitializedandValitetedFromFields()) {
                    database = FirebaseDatabase.getInstance();
                    myDatabaseRef = database.getReference("members");
                    addNewMember();
                }else{

                }
            }
        });
    }

    public void findViews() {
        edtFname =  findViewById(R.id.fname);
        edtLname =  findViewById(R.id.lname);
        edtEmail =  findViewById(R.id.userEmailId);
        edtPhone =  findViewById(R.id.mobileNumber);
        edtAddress =  findViewById(R.id.address);
        edtRank =  findViewById(R.id.rank);
        edtDeployment =  findViewById(R.id.deployment);
        edtJobLocation =  findViewById(R.id.job_location);
        edtNID =  findViewById(R.id.nid);
        edtMarriedWith =  findViewById(R.id.married_with);
        edtMarriedPhone =  findViewById(R.id.married_nbr);
        edtEntryDate =  findViewById(R.id.h2gentrydate);
        edtDOB =  findViewById(R.id.DOB);
        edtMarriageDate =  findViewById(R.id.marriage_date);
        edtRole =  findViewById(R.id.edtRole);


        radioSexGroup = findViewById(R.id.radioSex);
        radioStatusGroup =  findViewById(R.id.radioStatus);

        checkAgree =  findViewById(R.id.terms_conditions);
        btnAddNew =  findViewById(R.id.addNewBtn);
    }

    public void setDatePick() {
        TextViewDatePicker editTextDatePicker = new TextViewDatePicker(Registration.this, edtEntryDate,getConvertedDate("01/09/2015").getTime(),new Date().getTime());//without min date, max date
        TextViewDatePicker editTextDatePicker1 = new TextViewDatePicker(Registration.this, edtDOB,getConvertedDate("01/01/1970").getTime(),new Date().getTime());
        TextViewDatePicker editTextDatePicker3 = new TextViewDatePicker(Registration.this, edtMarriageDate,getConvertedDate("01/01/2015").getTime(),new Date().getTime());
        //TextViewDatePicker editTextDatePicker = new TextViewDatePicker(context, myEditText); // no limitation
    }

    public String getSelectedGender() {
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioSexButton = radioSexGroup.findViewById(selectedId);
        return radioSexButton.getText().toString();
    }

    public String getSelectedStatus() {
        int selectedId = radioStatusGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioStatusButton = radioStatusGroup.findViewById(selectedId);
        return radioStatusButton.getText().toString();
    }

    public boolean IsInitializedandValitetedFromFields() {
        boolean test;
        fName = edtFname.getText().toString().trim();
        if (TextUtils.isEmpty(fName)||fName.length()>25) {
            edtFname.setError("Please enter a valid name(25 chars)");
            Toast.makeText(getBaseContext(),"Please enter a valid name(25 chars)",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            test= true;
        }
        lName = edtLname.getText().toString().trim();
        if (TextUtils.isEmpty(lName.trim())||lName.length()>25) {
            edtLname.setError("Please enter a valid name(25 chars)");
            Toast.makeText(getBaseContext(),"Please enter a valid Last name(25 chars)",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            test= true;
        }
        gender = getSelectedGender();
        DOB = edtDOB.getText().toString();
        if (TextUtils.isEmpty(DOB)) {
            edtDOB.setError("Select a valid Date Of Birth");
            Toast.makeText(getBaseContext(),"Please enter a valid Date Of Birth",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            test= true;
        }
        status = getSelectedStatus();
        email = edtEmail.getText().toString();
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()||TextUtils.getTrimmedLength(edtLname.getText())>30){
            edtEmail.setError("Please enter a valid email(30 chars");
            Toast.makeText(getBaseContext(),"Please enter a valid email(30 chars)",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            test= true;
        }
        phone = edtPhone.getText().toString().trim();
        if (!phone.matches("^\\+[0-9]{10,13}$")) {
            edtPhone.setError("Please enter a valid phone(+250785545)(10-13 chars)");
            Toast.makeText(getBaseContext(),"Please enter a valid phone(+250785545)(10-13 chars)",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            test= true;
        }
        address = edtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)||address.length()>30) {
            edtAddress.setError("Please enter a valid Address(30 chars)");
            Toast.makeText(getBaseContext(),"Please enter a valid Address(30 chars)",Toast.LENGTH_SHORT).show();

            return false;
        } else {
            test= true;
        }
        rank = edtRank.getText().toString();
        if (TextUtils.isEmpty(rank)||rank.length()>5) {
            edtRank.setError("Please enter a valid Rank(max 5 chars)");
            Toast.makeText(getBaseContext(),"Please enter a valid Rank(5 chars)",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            test= true;
        }
        deployment = edtDeployment.getText().toString();
        if (TextUtils.isEmpty(deployment)||deployment.length()>25) {
            edtDeployment.setError("Please enter a valid deployment(25 chars)");
            Toast.makeText(getBaseContext(),"Please enter a valid deployment(25 chars)",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            test= true;
        }
        jobLocation = edtJobLocation.getText().toString();
        if (TextUtils.isEmpty(jobLocation)||jobLocation.length()>25) {
            edtJobLocation.setError("Please enter a valid job Location(25 chars)");
            Toast.makeText(getBaseContext(),"Please enter a valid job Location(25 chars)",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            test= true;
        }
        NID = edtNID.getText().toString();
        if (NID.length()!=16) {
            edtNID.setError("Please enter a valid NID only 16 Digits");
            Toast.makeText(getBaseContext(),"Please enter a valid NID only 16 Digits",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            test= true;
        }
        if(!status.equalsIgnoreCase("single")) {
            marriedWith = edtMarriedWith.getText().toString();
            if (TextUtils.isEmpty(marriedWith) || marriedWith.length() > 30) {
                edtMarriedWith.setError("Please enter a valid Names(30 chars)");
                Toast.makeText(getBaseContext(),"Please enter a valid Names(30 chars)",Toast.LENGTH_SHORT).show();
                return false;
            } else {
                test = true;
            }
            marriedPhone = edtMarriedPhone.getText().toString();
            if (marriedPhone.matches("^\\+[0-9]{10,13}$")) {
                edtMarriedPhone.setError("Please enter a valid phone(+250785545..)(10-13 chars)");
                Toast.makeText(getBaseContext(),"Please enter a valid phone(+250785545..)(10-13 chars)",Toast.LENGTH_SHORT).show();
                return false;
            } else {
                test = true;
            }
            marriageDate = edtMarriageDate.getText().toString();
            if (TextUtils.isEmpty(marriageDate)) {
                edtMarriageDate.setError("Please enter a valid Date(30 chars)");
                Toast.makeText(getBaseContext(),"Please enter a valid Date(30 chars)",Toast.LENGTH_SHORT).show();
                return false;
            } else {
                test= true;
            }
        }else {
            edtMarriedWith.setEnabled(false);
            edtMarriedPhone.setEnabled(false);
            edtMarriageDate.setEnabled(false);
            marriedWith = "-";
            marriedPhone= "-";
            marriageDate="-";
            test = true;
        }
        role = edtRole.getText().toString();
        if (TextUtils.isEmpty(role)||role.length()>15) {
            edtRole.setError("Please enter a valid Role in Commity(15 chars)");
            Toast.makeText(getBaseContext(),"Please enter a valid Role in Commity(15 chars)",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            test= true;
        }
        entryDate = edtEntryDate.getText().toString();
        if (TextUtils.isEmpty(entryDate)) {
            edtEntryDate.setError("Please enter a valid Entry Date");
            Toast.makeText(getBaseContext(),"Please enter a valid Entry Date",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            test= true;
        }
        if(test) {
            member = new Member(fName, lName, gender, status, email, phone, address, rank, deployment, jobLocation, NID, marriedWith, marriedPhone, role, entryDate, marriageDate, DOB);
        }
        return true;
    }
    public void AddDataToDataBase() {
        myDatabaseRef.push().setValue(member);
        Toast.makeText(getBaseContext(), "New Member Added", Toast.LENGTH_SHORT).show();
    }
    public void addNewMember() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        AddDataToDataBase();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
        builder.setTitle("Are you sure? to saved")
                .setMessage("Saved Data Won't be deleted").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return false;
    }

}
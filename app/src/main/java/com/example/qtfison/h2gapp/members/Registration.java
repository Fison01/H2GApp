package com.example.qtfison.h2gapp.members;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.qtfison.h2gapp.Classes.TextViewDatePicker;
import com.example.qtfison.h2gapp.Configs;
import com.example.qtfison.h2gapp.MainActivity;
import com.example.qtfison.h2gapp.MyProgressiveDialog;
import com.example.qtfison.h2gapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getConvertedDateformat1;
import static com.example.qtfison.h2gapp.Configs.USERLOGINROLE;
import static com.example.qtfison.h2gapp.Configs.userRole;


public class Registration extends AppCompatActivity {
    private RadioGroup radioSexGroup, radioStatusGroup;
    private RadioButton radioSexButton, radioStatusButton;
    private CheckBox checkAgree;
    private Button btnAddNew;
    private EditText  edtFname, edtLname, edtEmail, edtPhone, edtAddress, edtRank, edtDeployment, edtJobLocation, edtMarriedWith, edtMarriedPhone, edtDOB, edtEntryDate;
    private String fName, lName, gender, status, email, phone, address, rank, deployment, jobLocation, marriedWith, marriedPhone;
    private String entryDate, marriageDate, dob;
    private Member member;
    private FirebaseDatabase database;
    private DatabaseReference myDatabaseRef;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register Yourself(fill the form and save)");

        findViews();
        setDatePick();

        //displayMemberInfo();
        addListenerOnButtons();

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        checkIfAlreadyExist();
    }

    public void addListenerOnButtons() {
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsInitializedandValitetedFromFields()) {
                    database = FirebaseDatabase.getInstance();
                    myDatabaseRef = database.getReference("members");
                    addNewMember();
                    SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences( getBaseContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(userRole, Configs.MEMBER);
                    editor.apply();
                    editor.commit();
                }
            }
          }
        );
    }
            public void findViews() {
                edtFname = findViewById(R.id.fname);
                edtLname = findViewById(R.id.lname);
                edtEmail = findViewById(R.id.userEmailId);
                edtPhone = findViewById(R.id.mobileNumber);
                edtAddress = findViewById(R.id.address);
                edtRank = findViewById(R.id.rank);
                edtDeployment = findViewById(R.id.deployment);
                edtJobLocation = findViewById(R.id.job_location);

                edtMarriedWith = findViewById(R.id.married_with);
                edtMarriedPhone = findViewById(R.id.married_nbr);
                edtEntryDate = findViewById(R.id.h2gentrydate);
                edtDOB = findViewById(R.id.DOB);

                radioSexGroup = findViewById(R.id.radioSex);
                radioStatusGroup = findViewById(R.id.radioStatus);

                checkAgree = findViewById(R.id.terms_conditions);
                btnAddNew = findViewById(R.id.addNewBtn);
                currentUser=FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser!=null)
                edtEmail.setText(currentUser.getEmail());
            }

            public void setDatePick() {
                TextViewDatePicker editTextDatePicker = new TextViewDatePicker(Registration.this, edtEntryDate, getConvertedDateformat1("01/09/2015").getTime(), new Date().getTime());//without min date, max date
                TextViewDatePicker editTextDatePicker1 = new TextViewDatePicker(Registration.this, edtDOB, getConvertedDateformat1("01/01/1970").getTime(), new Date().getTime());

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
                boolean test=true;
                fName = edtFname.getText().toString().trim();
                if (TextUtils.isEmpty(fName) || fName.length() > 25) {
                    edtFname.setError("Please enter a valid name(25 chars)");
                    Toast.makeText(getBaseContext(), "Please enter a valid name(25 chars)", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    test = true;
                }
                lName = edtLname.getText().toString().trim();
                if (TextUtils.isEmpty(lName.trim()) || lName.length() > 25) {
                    edtLname.setError("Please enter a valid name(25 chars)");
                    Toast.makeText(getBaseContext(), "Please enter a valid Last name(25 chars)", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    test = true;
                }
                gender = getSelectedGender();
                dob = edtDOB.getText().toString();
                if (TextUtils.isEmpty(dob)) {
                    edtDOB.setError("Select a valid Date Of Birth");
                    Toast.makeText(getBaseContext(), "Please enter a valid Date Of Birth", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    test = true;
                }
                status = getSelectedStatus();
                email = edtEmail.getText().toString();
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || TextUtils.getTrimmedLength(edtLname.getText()) > 30) {
                    edtEmail.setError("Please enter a valid email(30 chars");
                    Toast.makeText(getBaseContext(), "Please enter a valid email(30 chars)", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    test = true;
                }
                phone = edtPhone.getText().toString().trim();
                if (!phone.matches("^\\+[0-9]{10,13}$")) {
                    edtPhone.setError("Please enter a valid phone(+250785545)(10-13 chars)");
                    Toast.makeText(getBaseContext(), "Please enter a valid phone(+250785545)(10-13 chars)", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    test = true;
                }
                address = edtAddress.getText().toString().trim();
                if (TextUtils.isEmpty(address) || address.length() > 30) {
                    edtAddress.setError("Please enter a valid Address(30 chars)");
                    Toast.makeText(getBaseContext(), "Please enter a valid Address(30 chars)", Toast.LENGTH_SHORT).show();

                    return false;
                } else {
                    test = true;
                }
                rank = edtRank.getText().toString();
                if (TextUtils.isEmpty(rank) || rank.length() > 5) {
                    edtRank.setError("Please enter a valid Rank(max 5 chars)");
                    Toast.makeText(getBaseContext(), "Please enter a valid Rank(5 chars)", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    test = true;
                }
                deployment = edtDeployment.getText().toString();
                if (TextUtils.isEmpty(deployment) || deployment.length() > 25) {
                    edtDeployment.setError("Please enter a valid deployment(25 chars)");
                    Toast.makeText(getBaseContext(), "Please enter a valid deployment(25 chars)", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    test = true;
                }
                jobLocation = edtJobLocation.getText().toString();
                if (TextUtils.isEmpty(jobLocation) || jobLocation.length() > 25) {
                    edtJobLocation.setError("Please enter a valid job Location(25 chars)");
                    Toast.makeText(getBaseContext(), "Please enter a valid job Location(25 chars)", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    test = true;
                }

                if (!status.equalsIgnoreCase("single")) {
                    marriedWith = edtMarriedWith.getText().toString();
                    if (TextUtils.isEmpty(marriedWith) || marriedWith.length() > 30) {
                        edtMarriedWith.setError("Please enter a valid Names(30 chars)");
                        Toast.makeText(getBaseContext(), "Please enter a valid Names(30 chars)", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        test = true;
                    }
                    marriedPhone = edtMarriedPhone.getText().toString();
                    if (marriedPhone.matches("^\\+[0-9]{10,13}$")) {
                        edtMarriedPhone.setError("Please enter a valid phone(+250785545..)(10-13 chars)");
                        Toast.makeText(getBaseContext(), "Please enter a valid phone(+250785545..)(10-13 chars)", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        test = true;
                    }

                } else {
                    edtMarriedWith.setEnabled(false);
                    edtMarriedPhone.setEnabled(false);
                    marriedWith = "-";
                    marriedPhone = "-";
                    marriageDate = "-";
                    test = true;
                }
                entryDate = edtEntryDate.getText().toString();
                if (TextUtils.isEmpty(entryDate)) {
                    edtEntryDate.setError("Please enter a valid Entry Date");
                    Toast.makeText(getBaseContext(), "Please enter a valid Entry Date", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    test = true;
                }
                if(!checkAgree.isChecked()){
                    checkAgree.setError("You must be agree");
                    Toast.makeText(getBaseContext(),"agreement must be agree",Toast.LENGTH_SHORT).show();
                    return false;
                }
                    member = new Member(fName, lName, gender, status, email, phone, address, rank, deployment, jobLocation, marriedWith, marriedPhone, "Member", entryDate, dob);
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

            public void displayMemberInfo(){
                FirebaseDatabase.getInstance().getReference("members").orderByChild("email").equalTo(currentUser.getEmail()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        System.out.println("111111"+currentUser.getEmail());
                        edtFname.setText(dataSnapshot.child("fName").getValue().toString());
                        edtLname.setText(dataSnapshot.child("lName").getValue().toString());
                        if(edtFname.getText().toString().trim().length()>0 && edtLname.getText().toString().trim().length()>0) {
                            edtFname.setText(dataSnapshot.child("address").getValue().toString());
                            edtEmail.setText(dataSnapshot.child("email").getValue().toString());
                            edtPhone.setText(dataSnapshot.child("phone").getValue().toString());
                            edtAddress.setText(dataSnapshot.child("address").getValue().toString());
                            edtRank.setText(dataSnapshot.child("rank").getValue().toString());
                            edtDeployment.setText(dataSnapshot.child("deployment").getValue().toString());
                            edtJobLocation.setText(dataSnapshot.child("jobLocation").getValue().toString());

                            edtMarriedWith.setText(dataSnapshot.child("marriedWith").getValue().toString());
                            edtMarriedPhone.setText(dataSnapshot.child("marriedPhone").getValue().toString());
                            edtEntryDate.setText(dataSnapshot.child("entryDate").getValue().toString());
                            edtDOB.setText(dataSnapshot.child("dob").getValue().toString());

                            // radioSexGroup.sets( dataSnapshot.child("gender:").getValue().toString());
                            // radioStatusGroup = findViewById(R.id.radioStatus);

                            checkAgree.setChecked(true);
                            // btnAddNew = findViewById(R.id.addNewBtn);
                            Intent i=new Intent(getBaseContext(),MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
    public void checkIfAlreadyExist(){
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            return;
        }
        FirebaseDatabase.getInstance().getReference("members").orderByChild("email").equalTo(currentUser.getEmail()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child("fName").getValue().toString().length()>0 && dataSnapshot.child("lName").getValue().toString().length()>0) {
                    Intent i=new Intent(getBaseContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
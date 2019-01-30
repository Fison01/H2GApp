package com.example.qtfison.h2gapp.loans;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.qtfison.h2gapp.Classes.TextViewDatePicker;
import com.example.qtfison.h2gapp.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Date;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getInterestPercentege;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getLoginEmail;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getUniqueId;
public class ApplyLoanActivity extends AppCompatActivity {
    AutoCompleteTextView AVTMember1;
    AutoCompleteTextView AVTMember2;
    AutoCompleteTextView AVTMember3;
    EditText edt_cr_amt,edt_loan_purpose,edt_to_day,edt_pay_date;
    TextView txt_interest;
    Button btn_check_interest;
    CheckBox chk_agree;
    Button btnSave;
    private FirebaseDatabase database;
    private DatabaseReference myDatabaseRef;
    Loan loan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_loan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Loan Application");
        getSupportActionBar().setLogo(R.drawable.ic_payment);
        AVTMember1= findViewById(R.id.txt_member1);
        AVTMember2= findViewById(R.id.txt_member2);
        AVTMember3= findViewById(R.id.txt_member3);
        edt_cr_amt = findViewById(R.id.edt_cr_amt);
        edt_loan_purpose =findViewById(R.id.edt_loan_purpose);
        edt_to_day =findViewById(R.id.ext_to_day);
        edt_pay_date = findViewById(R.id.edt_pay_date);
        setDatePick();
        txt_interest =findViewById(R.id.txt_interest);
        btn_check_interest =findViewById(R.id.btn_check_interest);
        chk_agree = findViewById(R.id.chk_agree);
        btnSave=findViewById(R.id.btn_request);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidatedAndInialised()) {
                    database = FirebaseDatabase.getInstance();
                    myDatabaseRef = database.getReference("loans");
                    confirmBeforeAndSave();
                }else{

                }
            }
        });
        autoCompletions();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return false;
    }
    public void autoCompletions(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        database.child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){
                    String suggestion = suggestionSnapshot.child("fName").getValue(String.class)+" "+suggestionSnapshot.child("lName").getValue(String.class)+"\n"+suggestionSnapshot.child("email").getValue(String.class);
                    autoComplete.add(suggestion);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        AVTMember1.setAdapter(autoComplete);
        AVTMember2.setAdapter(autoComplete);
        AVTMember3.setAdapter(autoComplete);
    }
    public boolean isValidatedAndInialised(){
            String loanPurpose,takenDate,paymentDuedate,member1,member2,member3,myEmail,loanId;
            long crAmount,interest;
            boolean test;
            try {
                crAmount = Long.parseLong(edt_cr_amt.getText().toString().trim());
            }catch (Exception e){
                edt_cr_amt.setError("Please enter a valid Amount(15 chars)");
                Toast.makeText(getBaseContext(),"Please enter a valid Amount(15 chars)",Toast.LENGTH_SHORT).show();
                return false;
            }
            loanPurpose = edt_loan_purpose.getText().toString().trim();
            if (TextUtils.isEmpty(loanPurpose.trim())||loanPurpose.length()>25) {
                edt_loan_purpose.setError("Please enter a valid name(25 chars)");
                Toast.makeText(getBaseContext(),"Please enter a valid Last name(25 chars)",Toast.LENGTH_SHORT).show();
                return false;
            } else {
                test= true;
            }
            loanPurpose = edt_loan_purpose.getText().toString().trim();
            if (TextUtils.isEmpty(loanPurpose.trim())||loanPurpose.length()>25) {
                edt_loan_purpose.setError("Please enter a valid name(25 chars)");
                Toast.makeText(getBaseContext(),"Please enter a valid Last name(25 chars)",Toast.LENGTH_SHORT).show();
                return false;
            } else {
                test= true;
            }

            if (TextUtils.isEmpty(edt_to_day.getText().toString())) {

                edt_to_day.setError("Please enter a valid Date");
                Toast.makeText(getBaseContext(),"Please enter a valid Date",Toast.LENGTH_SHORT).show();
                return false;
            } else {
                String dt[]=edt_to_day.getText().toString().split("/");
                takenDate = dt[0]+"/"+dt[1]+"/"+dt[2];
                test= true;
            }

            if (TextUtils.isEmpty(edt_to_day.getText().toString())) {
                edt_pay_date.setError("Please enter a valid Date");
                Toast.makeText(getBaseContext(),"Please enter a valid Date",Toast.LENGTH_SHORT).show();
                return false;
            } else {
                String dt1[]=edt_to_day.getText().toString().split("/");
                paymentDuedate =  dt1[0]+"/"+dt1[1]+"/"+dt1[2];
                test= true;
            }
            member1 =AVTMember1.getText().toString().split("\n")[1];
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(member1.trim()).matches()||TextUtils.getTrimmedLength(member1)>30){
                AVTMember1.setError("Please enter a valid email(30 chars");
                Toast.makeText(getBaseContext(),"Please enter a valid email(30 chars)"+member1,Toast.LENGTH_SHORT).show();
                return false;
            }else{
                test= true;
            }
            member2 =AVTMember2.getText().toString().split("\n")[1];
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(member1).matches()||TextUtils.getTrimmedLength(member2)>30){
                AVTMember2.setError("Please enter a valid email(30 chars");
                Toast.makeText(getBaseContext(),"Please enter a valid email(30 chars)",Toast.LENGTH_SHORT).show();
                return false;
            }else{
                test= true;
            }
            member3 =AVTMember3.getText().toString().split("\n")[1];
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(member3).matches()||TextUtils.getTrimmedLength(member3)>30){
                AVTMember3.setError("Please enter a valid email(30 chars");
                Toast.makeText(getBaseContext(),"Please enter a valid email(30 chars)",Toast.LENGTH_SHORT).show();
                return false;
            }else{
                test= true;
            }
            if(member1==member2 || member1==member3 || member3==member2){
                Toast.makeText(getBaseContext(),"Sorry you can select one member twise",Toast.LENGTH_SHORT).show();
                AVTMember1.setError("Sorry");
                AVTMember2.setError("Sorry");
                AVTMember3.setError("Sorry");
                return false;
            }else{
                test= true;
            }
            if(!chk_agree.isChecked()){
                chk_agree.setError("Please Sign the Agreement by checked the box");
                Toast.makeText(getBaseContext(),"Please Sign the Agreement by checked the box",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(test) {
                interest=(crAmount*getInterestPercentege())/100;
                loan=new Loan(getLoginEmail(getBaseContext()),getUniqueId(), takenDate, paymentDuedate,interest,loanPurpose,member1, member2, member3, crAmount, 0,crAmount+interest, 0);
            }
                return true;
    }
    public void confirmBeforeAndSave(){
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure? to save and request")
                .setMessage("Saved data Won't be deleted").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    public void AddDataToDataBase() {
        myDatabaseRef.push().setValue(loan);
       // increaseIssues(loan.getMyEmail(),"members","loanIssues");
        Toast.makeText(getBaseContext(), "Transaction saved", Toast.LENGTH_SHORT).show();
    }
    public void setDatePick() {
        TextViewDatePicker editTextDatePicker = new TextViewDatePicker(ApplyLoanActivity.this, edt_to_day,new Date().getTime(),new Date().getTime());//without min date, max date
        TextViewDatePicker editTextDatePicker1 = new TextViewDatePicker(ApplyLoanActivity.this, edt_pay_date,new Date().getTime(),0);
        //TextViewDatePicker editTextDatePicker = new TextViewDatePicker(context, myEditText); // no limitation
    }


}

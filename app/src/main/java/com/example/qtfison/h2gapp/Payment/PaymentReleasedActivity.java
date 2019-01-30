package com.example.qtfison.h2gapp.Payment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qtfison.h2gapp.Classes.MyNotification;
import com.example.qtfison.h2gapp.Classes.TextViewDatePicker;
import com.example.qtfison.h2gapp.Configs;
import com.example.qtfison.h2gapp.MyProgressiveDialog;
import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.members.Registration;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getUniqueId;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isValidDate;


public class PaymentReleasedActivity extends AppCompatActivity {
    EditText edtPayType,edtAmount,edtStart,edtend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_released);
        edtPayType = findViewById(R.id.Pay_type);
         edtAmount =findViewById(R.id.edt_pay_amt);
         edtStart = findViewById(R.id.pay_start_date);
         edtend = findViewById(R.id.pay_end_date);
        setDatePick();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDate();
            }
        });
    }
    public void saveDate(){

        TextViewDatePicker editTextDatePicker =  new TextViewDatePicker(PaymentReleasedActivity.this, edtStart,new Date().getTime(),0);//without min date, max date
        TextViewDatePicker editTextDatePicker1 = new TextViewDatePicker(getBaseContext(), edtend,new Date().getTime(),0);
        String paymentNeededId;
        String paymentType, startDate, endingDate;
        long amount;
        paymentNeededId = getUniqueId();
        boolean isValid=true;
        paymentType = edtPayType.getText().toString();
        if (paymentType.length() <= 0) {
            Toast.makeText(getBaseContext(), "Invalid Payment Type", Toast.LENGTH_SHORT).show();
            edtPayType.setError("error");
            isValid=false;
        }
        amount=0;
        try {
            amount = Long.parseLong(edtAmount.getText().toString());
        }catch (Exception e) {
            Toast.makeText(getBaseContext(), "Amount required", Toast.LENGTH_SHORT).show();
            edtAmount.setError("error");
            isValid = false;
        }

        startDate = edtStart.getText().toString();
        if (!isValidDate(startDate)) {
            Toast.makeText(getBaseContext(), "Opening date required", Toast.LENGTH_SHORT).show();
            edtStart.setError("error");
            isValid=false;
        }
        endingDate = edtend.getText().toString();
        if (!isValidDate(endingDate)) {
            Toast.makeText(getBaseContext(), "Deadline date required", Toast.LENGTH_SHORT).show();
            edtend.setError("error");
            isValid=false;
        }

        if(isValid) {
            PaymentNeeded paymentNeeded = new PaymentNeeded(paymentNeededId, paymentType, startDate, endingDate, amount,0,0,0);
            addNewPayment(paymentNeeded);
        }
    }
    String lastKey,payKey; //lastkey: for detemine last member in calculation of tot, paykey :for update payment needed by tot
    public void addNewPayment(final PaymentNeeded paymentNeeded) {
        MyProgressiveDialog progress=new MyProgressiveDialog(PaymentReleasedActivity.this);
        progress.showPregress();
        FirebaseDatabase database;
        DatabaseReference myDatabaseRef;
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference("payment_needed");
        myDatabaseRef.push().setValue(paymentNeeded);
        Toast.makeText(getBaseContext(), "New Payment_needed Saved", Toast.LENGTH_SHORT).show();
        myDatabaseRef = database.getReference("members");
        database.getReference("members").limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                lastKey=dataSnapshot.getKey(); // key of last entry in member
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
        database.getReference("payment_needed").limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                payKey=dataSnapshot.getKey(); //key of last payment needs
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

        myDatabaseRef.addChildEventListener(new ChildEventListener() {
            int count = 0;
            long amount = 0;

            @NonNull
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                FirebaseDatabase database;
                DatabaseReference myDatabaseRef;
                database = FirebaseDatabase.getInstance();
                PaymentReleased paymentReleased = new PaymentReleased(paymentNeeded.getPaymentNeededid(), dataSnapshot.child("email").getValue().toString(), 0, "Not yet");
                myDatabaseRef = database.getReference("payment_released");
                myDatabaseRef.push().setValue(paymentReleased);
                //increaseIssues(dataSnapshot.child("email").getValue().toString(),"members","paymentIssues");
                count++;
                amount = amount + paymentNeeded.getAmount();
                if (lastKey.equalsIgnoreCase(dataSnapshot.getKey())){
                    database.getReference("payment_needed").child(payKey).child("total").setValue(amount);
                    database.getReference("payment_needed").child(payKey).child("nbrMembers").setValue(count);
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
                Toast.makeText(getBaseContext(), "Error contact Fison" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        progress.hideprogress();
        finish();
    }
    public void setDatePick() {
        TextViewDatePicker editTextDatePicker = new TextViewDatePicker(PaymentReleasedActivity.this, edtStart,new Date().getTime(),0);//without min date, max date
        TextViewDatePicker editTextDatePicker1 = new TextViewDatePicker(PaymentReleasedActivity.this, edtend,new Date().getTime(),0);
        //TextViewDatePicker editTextDatePicker = new TextViewDatePicker(context, myEditText); // no limitation
    }

}

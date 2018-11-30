package com.example.qtfison.h2gapp.Classes;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyFireBaseFuctions {
   static FirebaseDatabase database;
    DatabaseReference myRef;
    public MyFireBaseFuctions() {
    }
    public static void updateData(final String selectedKey, final int newV, final String PaidDate,final Context context) {
        database = FirebaseDatabase.getInstance();
        final DatabaseReference myref = database.getReference();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myref.child("payment_released").child(selectedKey).child("isPaid").setValue(newV);
                myref.child("payment_released").child(selectedKey).child("paidOn").setValue(PaidDate);
                Toast.makeText(context,"Thanks, recorded",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();

            }
        });
    }
}

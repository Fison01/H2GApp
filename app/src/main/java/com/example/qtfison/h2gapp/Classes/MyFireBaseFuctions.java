package com.example.qtfison.h2gapp.Classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.qtfison.h2gapp.Configs;
import com.example.qtfison.h2gapp.MyProgressiveDialog;
import com.example.qtfison.h2gapp.Users.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFireBaseFuctions {
   static FirebaseDatabase database;
    DatabaseReference myRef;
    public MyFireBaseFuctions() {
    }
    public static void updateData(final String selectedKey, final int newV, final String PaidDate, final Context context, final String paymentId, final String email, final String noticeId) {
        database = FirebaseDatabase.getInstance();
        final DatabaseReference myref = database.getReference();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myref.child("payment_released").child(selectedKey).child("isPaid").setValue(newV);
                myref.child("payment_released").child(selectedKey).child("paidOn").setValue(PaidDate);
                updateDataPayment(paymentId,context);
                if(newV==1)
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Approval");
                Toast.makeText(context,"Thanks, recorded",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();

            }
        });
    }
    public static void updateDataPayment(final String paymentNeededid,final Context context) {
        database = FirebaseDatabase.getInstance();
        final DatabaseReference myref = database.getReference();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               myref.child("payment_needed").orderByChild("paymentNeededid").equalTo(paymentNeededid).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        int a=Integer.parseInt(dataSnapshot.child("nbrPaid").getValue().toString())+1;
                        myref.child("payment_needed").child(dataSnapshot.getKey()).child("nbrPaid").setValue(a);
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
                     //.setValue(newV);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();

            }
        });
    }


    public static void EmergencyDoneRemoveNotice(final String notifId){
        System.out.println("11111k1:"+notifId);
        FirebaseDatabase.getInstance().getReference("notifications").orderByChild("notifId").equalTo(notifId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("11111k2:"+notifId);
                if (dataSnapshot.child("notifId").getValue().toString().equalsIgnoreCase(notifId)) {
                    dataSnapshot.getRef().removeValue();
                    System.out.println("11111k2");
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
        } );
    }
    public static void sendPasswordReset(final String emailAddress, final Context context) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(context,"Password reset send to "+emailAddress,Toast.LENGTH_SHORT).show();
                        }else{

                            Toast.makeText(context,"Not found or invalid email: "+emailAddress,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}

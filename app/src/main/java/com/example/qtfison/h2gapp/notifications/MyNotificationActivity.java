package com.example.qtfison.h2gapp.notifications;

import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.qtfison.h2gapp.Classes.MyNotification;
import com.example.qtfison.h2gapp.Classes.MyNotificationViewHolder;
import com.example.qtfison.h2gapp.Configs;
import com.example.qtfison.h2gapp.Payment.MemberPaymentActivity;
import com.example.qtfison.h2gapp.Payment.PaymentReleasedActivity;
import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.loans.LoanActivity;
import com.example.qtfison.h2gapp.members.ItemClickListener;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.qtfison.h2gapp.Configs.userRole;


public class MyNotificationActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<MyNotification> options;
    FirebaseRecyclerAdapter<MyNotification, MyNotificationViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notifications);
        getSupportActionBar().setTitle("H2G Emergency");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.recycleVNotice);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("notifications");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        Configs.USERLOGINROLE=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(userRole,null);
        Configs.LOGINUSEREMAIL=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(Configs.USERLOGINROLE.equalsIgnoreCase(Configs.MEMBER)){
            options = new FirebaseRecyclerOptions.Builder<MyNotification>()
                    .setQuery(myRef.orderByChild("toEmail").equalTo(Configs.LOGINUSEREMAIL), MyNotification.class)
                     .build();
        }else {
            options = new FirebaseRecyclerOptions.Builder<MyNotification>()
                    .setQuery(myRef.orderByChild("toEmail").equalTo(Configs.TREASURER), MyNotification.class)
                    .build();
        }
        adapter= new FirebaseRecyclerAdapter<MyNotification, MyNotificationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyNotificationViewHolder holder, int position, @NonNull final MyNotification model) {

                holder.txt_email.setText(model.getFromEmail());
                holder.txt_Descr.setText(model.getNotifDesc());
                holder.txt_amount.setText(""+model.getAmount()+ "Rwf");
                holder.txt_date.setText(model.getDate());
                if(model.getNoticeType().equalsIgnoreCase("p")){
                    TextDrawable drawable = TextDrawable.builder() .buildRound("P", Color.parseColor("#6eb4ad"));
                    holder.img_notice.setImageDrawable(drawable);
                }else if(model.getNoticeType().equalsIgnoreCase("L")) {
                    TextDrawable drawable = TextDrawable.builder() .buildRound("P", Color.parseColor("#6eb4ad"));
                    holder.img_notice.setImageDrawable(drawable);
                }else{
                    TextDrawable drawable = TextDrawable.builder() .buildRound("R", Color.parseColor("#6eb4ad"));
                    holder.img_notice.setImageDrawable(drawable);
                }
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(getBaseContext(),"ok",Toast.LENGTH_SHORT).show();
                }
                });
                holder.img_notice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity1(MemberPaymentActivity.class,model);
                    }
                });
                holder.txt_amount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity1(MemberPaymentActivity.class,model);
                    }
                });
                holder.txt_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity1(MemberPaymentActivity.class,model);
                    }
                });
            }
            @NonNull
            @Override
            public MyNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView=LayoutInflater.from(getBaseContext()).inflate(R.layout.notification_items,viewGroup,false);
                return new MyNotificationViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void startActivity1(final Class c,final MyNotification model){
        DatabaseReference  myref = FirebaseDatabase.getInstance().getReference("members");
        myref.orderByChild("email").equalTo(model.getFromEmail()).addChildEventListener(new ChildEventListener() {
            String fname1=null,lname1=null,key1=null;
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                fname1 = dataSnapshot.child("fName").getValue().toString();
                lname1 = dataSnapshot.child("lName").getValue().toString();
                key1=dataSnapshot.getKey();
                Intent i=new Intent(getBaseContext(),c);
                i.putExtra("email",model.getFromEmail());
                i.putExtra("fname",fname1+" "+lname1);
                i.putExtra("loanId",model.getTransactionId());
                i.putExtra("notifId",model.getNotifId());
                startActivity(i);
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

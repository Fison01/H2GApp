package com.example.qtfison.h2gapp.loans;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.qtfison.h2gapp.Configs;
import com.example.qtfison.h2gapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isUserAllowed;
import static com.example.qtfison.h2gapp.Configs.TREASURER;

public class LoanPaymentsHistoryActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    long amountLeft, daysLeft;
    String loanId,fname,notifId;
    FirebaseRecyclerOptions<LoanPayment> options;
    FirebaseRecyclerAdapter<LoanPayment, LoanPaymentViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_payments_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycleView);
        amountLeft = getIntent().getLongExtra("amountLeft", 0);
        daysLeft = getIntent().getLongExtra("daysLeft", 0);
        loanId = getIntent().getStringExtra("loanId");
        fname=getIntent().getStringExtra("fname");
        notifId=getIntent().getStringExtra("notifId");
        getSupportActionBar().setTitle("Payment: "+fname);
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("loanPayment");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        options = new FirebaseRecyclerOptions.Builder<LoanPayment>()
                .setQuery(myRef.orderByChild("loanId").equalTo(loanId), LoanPayment.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<LoanPayment, LoanPaymentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final LoanPaymentViewHolder holder, int position, @NonNull final LoanPayment model) {
                holder.txt_amount_paid.setText("Amount Paid: " + model.getAmountPay() + "Rwf");
                holder.txt_amount_left.setText("Amount left : " + (amountLeft - model.getAmountPay())+ " Rwf,  Remaining Days" + daysLeft);
                holder.txt_date.setText(model.getDate());
                if (model.getApproved() == 1) {
                    TextDrawable drawable = TextDrawable.builder().buildRound("√", Color.parseColor("#6eb4ad"));
                    holder.img_stutus.setImageDrawable(drawable);
                } else if (model.getApproved() == 0) {
                    TextDrawable drawable = TextDrawable.builder().buildRound("X", Color.RED);
                    holder.img_stutus.setImageDrawable(drawable);
                } else {
                    TextDrawable drawable = TextDrawable.builder().buildRound("D", Color.parseColor("#6eb4ad"));
                    holder.img_stutus.setImageDrawable(drawable);
                }
                if(!isUserAllowed(TREASURER,getBaseContext())){
                    holder.img_menu.setVisibility(View.INVISIBLE);
                    return;
                }
                holder.img_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popup = new PopupMenu(v.getContext(), v);
                        MenuInflater inflater = popup.getMenuInflater();
                       inflater.inflate(R.menu.loan_payment_item_menu, popup.getMenu());
                         popup.dismiss();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu_denial:
                                        if (isUserAllowed( TREASURER,getBaseContext())) {
                                            deny(model);
                                            return true;
                                        } else {
                                            Toast.makeText(getBaseContext(), "Contact Treasurer", Toast.LENGTH_SHORT).show();
                                            return true;
                                        }
                                    case R.id.menu_amount_paid:
                                        if (isUserAllowed( TREASURER,getBaseContext())) {
                                            approveAndUpdateCalculations(model);
                                        }
                                    default:
                                        return false;
                                }
                            }
                        });
                        popup.show();
                    }
                });
            }

            @NonNull
            @Override
            public LoanPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.loan_payment_items_post, viewGroup, false);
                return new LoanPaymentViewHolder(itemView);
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
    public void approveAndUpdateCalculations(final LoanPayment model) {
        if (model.getApproved() == -1) {
            Toast.makeText(getBaseContext(), "Transaction is denied By treasurer!!", Toast.LENGTH_SHORT).show();
            return;
        } else if (model.getApproved() == 1) {
            Toast.makeText(getBaseContext(), "Already Done", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase.getInstance().getReference("loans").orderByChild("loanId").equalTo(loanId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.child("loanId").getValue().toString().equalsIgnoreCase(loanId)) {
                    long amountPaid = Long.parseLong(dataSnapshot.child("amountPaid").getValue().toString());
                    long amountRest = Long.parseLong(dataSnapshot.child("amountRest").getValue().toString());
                    int isPaidFull = 0;
                    if (model.getAmountPay() <= amountRest) {
                        FirebaseDatabase database;
                        database = FirebaseDatabase.getInstance();
                        amountPaid = amountPaid + model.getAmountPay();
                        amountRest = amountRest - model.getAmountPay();
                        if (amountRest > 0) {
                            isPaidFull = 2;
                        } else if (amountRest == 0) {
                            isPaidFull = 1;
                        }
                        database.getReference("loans").child(dataSnapshot.getKey()).child("isFullpaid").setValue(isPaidFull);
                        database.getReference("loans").child(dataSnapshot.getKey()).child("amountPaid").setValue(amountPaid);
                        database.getReference("loans").child(dataSnapshot.getKey()).child("amountRest").setValue(amountRest);
                        FirebaseDatabase.getInstance().getReference("loanPayment").orderByChild("paymentId").equalTo(model.getPaymentId()).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if (dataSnapshot.child("paymentId").getValue().toString().equalsIgnoreCase(model.getPaymentId())) {
                                    FirebaseDatabase.getInstance().getReference("loanPayment").child(dataSnapshot.getKey()).child("approved").setValue(1);
                                    EmergencyDoneRemoveNotice(model.getLoanId());
                                    Toast.makeText(getBaseContext(), "Done Successful!!", Toast.LENGTH_SHORT).show();
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

                    } else {
                        Toast.makeText(getBaseContext(), "Invalid Amount !! great than Credit Amount", Toast.LENGTH_SHORT).show();
                    }
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

    public void deny(final LoanPayment model) {
        if (model.getApproved() == -1) {
            Toast.makeText(getBaseContext(), "Transaction is denied By treasurer!!", Toast.LENGTH_SHORT).show();
            return;
        } else if (model.getApproved() == 1) {
            Toast.makeText(getBaseContext(), "Already Done", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase.getInstance().getReference("loanPayment").orderByChild("paymentId").equalTo(model.getPaymentId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("paymentId").getValue().toString().equalsIgnoreCase(model.getPaymentId())) {
                    FirebaseDatabase.getInstance().getReference("loanPayment").child(dataSnapshot.getKey()).child("approved").setValue(-1);
                    EmergencyDoneRemoveNotice(model.getPaymentId());
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
    public void EmergencyDoneRemoveNotice(final String transactionId){
        FirebaseDatabase.getInstance().getReference("notifications").orderByChild("notifId").equalTo(notifId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("notifId").getValue().toString().equalsIgnoreCase(notifId)) {
                    dataSnapshot.getRef().removeValue();
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
}

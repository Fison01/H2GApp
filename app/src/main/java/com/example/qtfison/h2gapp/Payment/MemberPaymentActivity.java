package com.example.qtfison.h2gapp.Payment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qtfison.h2gapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static com.example.qtfison.h2gapp.Classes.MyFireBaseFuctions.updateData;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getFormatedDate;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isForYourEmailId;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isUserAllowed;

public class MemberPaymentActivity extends AppCompatActivity {
String  fname,email;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseRecyclerAdapter<PaymentReleased,PaymentRecyclerViewHolder> adapter;
    FirebaseRecyclerOptions<PaymentReleased> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fname=getIntent().getStringExtra("fname");
        email=getIntent().getStringExtra("email");
        getSupportActionBar().setTitle("Payments : "+fname);
        recyclerView=findViewById(R.id.recycleViewP);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getBaseContext(), 3, GridLayoutManager.VERTICAL, false);
        mGridLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mGridLayoutManager);
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("payment_released");
        }
        displayContent();
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
    private void displayContent() {
                    options = new FirebaseRecyclerOptions.Builder<PaymentReleased>()
                    .setQuery(myRef.orderByChild("email").equalTo(email)
                            , PaymentReleased.class)
                    .build();
        adapter= new FirebaseRecyclerAdapter<PaymentReleased, PaymentRecyclerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PaymentRecyclerViewHolder holder, int position, @NonNull final PaymentReleased model) {

                    final  String selectedKey;
                    final int newv;
                    if(isUserAllowed(getBaseContext(),"Treasurer")){
                        newv=1;
                    }else{
                        newv=2;
                    }
                    selectedKey=getSnapshots().getSnapshot(position).getKey();
                    holder.paid_on.setText(model.getPaidOn());
                    if (model.getIsPaid() == 0) {
                        holder.txt_paid_status.setText("X");
                    } else if(model.getIsPaid() == 1) {
                        holder.txt_paid_status.setText("√");
                    }else {
                        holder.txt_paid_status.setText("P");
                    }
                    holder.txt_pid.setText(""+ model.getEmail());
                    System.out.println("Tessss: "+ model.getEmail());
                    FirebaseDatabase database;
                    DatabaseReference myDatabaseRef;
                    database = FirebaseDatabase.getInstance();
                    myDatabaseRef = database.getReference("payment_needed");
                    myDatabaseRef.orderByChild("paymentNeededid").equalTo(model.getReleasedOnId())
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    holder.txt_Amount.setText(dataSnapshot.child("amount").getValue().toString());
                                    holder.txt_start_date.setText(dataSnapshot.child("startDate").getValue().toString());
                                    holder.txt_end_date.setText(dataSnapshot.child("endingDate").getValue().toString());
                                    holder.txt_paymentType.setText(dataSnapshot.child("paymentType").getValue().toString());
                                    holder.img_menu_popup.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            PopupMenu popup = new PopupMenu(v.getContext(), v);
                                            MenuInflater inflater = popup.getMenuInflater();
                                            inflater.inflate(R.menu.payment_released_menu, popup.getMenu());
                                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch (item.getItemId()) {
                                                        case R.id.menu_amount_paid:
                                                            if(isUserAllowed(getBaseContext(),"Treasurer")){
                                                                updateData(selectedKey,newv,getFormatedDate(new Date()),getBaseContext());
                                                                return true;
                                                            }else{
                                                                Toast.makeText(getBaseContext(),"Contact Treasurer",Toast.LENGTH_SHORT).show();
                                                                return true;
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
                                    Toast.makeText(getBaseContext(),"App Error contact Fison"+databaseError.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                }
                            });

            }
            @NonNull
            @Override
            public PaymentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView=LayoutInflater.from(getBaseContext()).inflate(R.layout.post_payment_items,viewGroup,false);
                return new PaymentRecyclerViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}

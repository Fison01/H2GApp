package com.example.qtfison.h2gapp.Payment;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.qtfison.h2gapp.Classes.MyNotification;
import com.example.qtfison.h2gapp.Configs;
import com.example.qtfison.h2gapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Date;

import static com.example.qtfison.h2gapp.Classes.MyFireBaseFuctions.updateData;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getFormatedDate;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getUniqueId;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isUserAllowed;
import static com.example.qtfison.h2gapp.Configs.*;

public class MemberPaymentActivity extends AppCompatActivity {
    String  fname,email,noticeId;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseRecyclerAdapter<PaymentReleased,PaymentRecyclerViewHolder> adapter;
    FirebaseRecyclerOptions<PaymentReleased> options;
    FirebaseAuth mAuth;
    ProgressBar progressBar,progressBar2;
    Handler handler,handler2;
    TextView txtUser,txtRole,txtTotP,txtCountP,txtDoneP,txtUnDoneP;
    long myTotalP,countPaid,sumPaid,countUnpaid,sumUnpaid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fname=getIntent().getStringExtra("fname");
        email=getIntent().getStringExtra("email");
        noticeId=getIntent().getStringExtra("notifId");
        getSupportActionBar().setTitle("Payments : "+fname);
        progressBar = findViewById(R.id.progress_bar);
        progressBar2 = findViewById(R.id.progressBar2);
        recyclerView=findViewById(R.id.recycleViewP);
        mAuth=FirebaseAuth.getInstance();
        txtUser=findViewById(R.id.txt_user);
        txtRole=findViewById(R.id.txt_role);
        if(LOGINUSEREMAIL!=null)
            txtUser.setText(LOGINUSEREMAIL.split("@")[0]);
        txtRole.setText("@"+USERLOGINROLE);
        txtTotP=findViewById(R.id.txt_total_p);
        txtCountP=findViewById(R.id.txt_payment_count);
        txtDoneP=findViewById(R.id.txt_total_pdonep);
        txtUnDoneP=findViewById(R.id.txt_total_unpaidP);
        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(getBaseContext(), GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mGridLayoutManager);
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("payment_released");
        }
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        };
        handler2=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        progressBar2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        progressBar2.setVisibility(View.GONE);
                        handler2.removeCallbacksAndMessages(null);
                        break;
                }
            }
        };
        displayContent();
        getMemberTotalPayment();
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
        Message processStart = handler.obtainMessage(1);
        processStart.sendToTarget();
                    options = new FirebaseRecyclerOptions.Builder<PaymentReleased>()
                    .setQuery(myRef.orderByChild("email").equalTo(email)
                            , PaymentReleased.class)
                    .build();
        adapter= new FirebaseRecyclerAdapter<PaymentReleased, PaymentRecyclerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PaymentRecyclerViewHolder holder, int position, @NonNull final PaymentReleased model) {
                    final  String selectedKey;
                    final int newv;
                    if(isUserAllowed(TREASURER,getBaseContext())){
                        newv=1;
                    }else{
                        newv=2;
                    }
                    selectedKey=getSnapshots().getSnapshot(position).getKey();
                    holder.paid_on.setText(model.getPaidOn());
                    if (model.getIsPaid() == 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.paid_status.setImageDrawable(getDrawable(R.drawable.ic_stat_name));
                        }
                        FirebaseMessaging.getInstance().subscribeToTopic("unpaidContrition");
                    } else if(model.getIsPaid() == 1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.paid_status.setImageDrawable(getDrawable(R.drawable.ic_done_all_black_24dp));
                            holder.txtRounded1.setBackground(getDrawable(R.drawable.rounded_corn0));
                            holder.txtRounded2.setBackground(getDrawable(R.drawable.rounded_corn0));
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("unpaidContrition");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("WaitingApproval");
                        }
                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.paid_status.setImageDrawable(getDrawable(R.drawable.ic_waiting));
                            holder.txtRounded1.setBackground(getDrawable(R.drawable.rounded_corn0));
                            holder.txtRounded2.setBackground(getDrawable(R.drawable.rounded_corn0));
                        }
                        FirebaseMessaging.getInstance().subscribeToTopic("WaitingContrition");
                    }
                    holder.txt_pid.setText(""+ model.getEmail());
                    FirebaseDatabase database;
                    DatabaseReference myDatabaseRef;
                    database = FirebaseDatabase.getInstance();
                    myDatabaseRef = database.getReference("payment_needed");
                    myDatabaseRef.orderByChild("paymentNeededid").equalTo(model.getReleasedOnId())
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                                    final long amountPaid=Long.parseLong(dataSnapshot.child("amount").getValue().toString());
                                    holder.txt_Amount.setText(""+amountPaid);
                                    holder.txt_start_date.setText(dataSnapshot.child("startDate").getValue().toString());
                                    holder.txt_end_date.setText(dataSnapshot.child("endingDate").getValue().toString());
                                    holder.txt_paymentType.setText(dataSnapshot.child("paymentType").getValue().toString());
                                    holder.img_menu_popup.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            PopupMenu popup = new PopupMenu(v.getContext(), v);
                                            MenuInflater inflater = popup.getMenuInflater();
                                            inflater.inflate(R.menu.payment_item_menu, popup.getMenu());
                                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch (item.getItemId()) {
                                                        case R.id.menu_amount_paid:
                                                            if(isUserAllowed(TREASURER,getBaseContext())||email.equalsIgnoreCase(LOGINUSEREMAIL)){
                                                                if(model.getIsPaid()==NOT_YET || (isUserAllowed(TREASURER,getBaseContext())&& model.getIsPaid()==PANDING)) {
                                                                    updateData(selectedKey, newv, getFormatedDate(new Date()), getBaseContext(), model.getReleasedOnId(), model.getEmail(),noticeId);
                                                                   // if (!USERLOGINROLE.equalsIgnoreCase(ADMIN) && !USERLOGINROLE.equalsIgnoreCase(TREASURER)) {
                                                                      //  MyNotification notice = new MyNotification(LOGINUSEREMAIL,TREASURER, "Please Approve Or Deny", Configs.NOTICE_TYPE_CONTRP, amountPaid, getFormatedDate(new Date()), model.getReleasedOnId(), getUniqueId());
                                                                        //FirebaseDatabase.getInstance().getReference("notifications").push().setValue(notice);
                                                                   // }
                                                                    return true;
                                                                }else {
                                                                    Toast.makeText(getBaseContext(),"Sorry!! Already Done Previously",Toast.LENGTH_SHORT).show();
                                                                    return true;
                                                                }
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
                Message processStart1 = handler.obtainMessage(2);
                processStart1.sendToTarget();
            }
            @NonNull
            @Override
            public PaymentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView=LayoutInflater.from(getBaseContext()).inflate(R.layout.post_payment_items,viewGroup,false);
                progressBar.setVisibility(View.INVISIBLE);
                return new PaymentRecyclerViewHolder(itemView);
            }

        };
        adapter.startListening();
       recyclerView. addItemDecoration(new DividerItemDecoration(this ,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

    }
    public void getMemberTotalPayment(){
        Message processStart2 = handler2.obtainMessage(1);
        processStart2.sendToTarget();
        FirebaseDatabase.getInstance().getReference("payment_released").orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final PaymentReleased model=dataSnapshot.getValue(PaymentReleased.class);
                FirebaseDatabase.getInstance().getReference("payment_needed").orderByChild("paymentNeededid").equalTo(model.getReleasedOnId()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        PaymentNeeded paymentNeeded = dataSnapshot.getValue(PaymentNeeded.class);
                        if (model.getIsPaid() == 0) {
                            sumUnpaid = sumUnpaid + paymentNeeded.getAmount();
                            countUnpaid = countUnpaid + 1;
                        } else if (model.getIsPaid() == 1) {
                            sumPaid = sumPaid + paymentNeeded.getAmount();
                            countPaid = countPaid + 1;
                        } else {
                            sumUnpaid = sumUnpaid + paymentNeeded.getAmount();
                            countUnpaid = countUnpaid + 1;
                        }

                        txtTotP.setText(sumPaid+sumUnpaid+" Rwf");
                        txtCountP.setText(countPaid+" out "+(countUnpaid+countPaid)+"  Your payment(s) done, "+countUnpaid+" remaining");
                        txtDoneP.setText(sumPaid+" Rwf ");
                        txtUnDoneP.setText(sumUnpaid+" Rwf ");
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
                Message processStart2 = handler2.obtainMessage(2);
                processStart2.sendToTarget();
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

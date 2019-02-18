package com.example.qtfison.h2gapp.account;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qtfison.h2gapp.Classes.MyNotification;
import com.example.qtfison.h2gapp.Configs;
import com.example.qtfison.h2gapp.Payment.PaymentRecyclerViewHolder;
import com.example.qtfison.h2gapp.Payment.PaymentReleased;
import com.example.qtfison.h2gapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static com.example.qtfison.h2gapp.Classes.MyFireBaseFuctions.updateData;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getFormatedDate;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getUniqueId;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isUserAllowed;
import static com.example.qtfison.h2gapp.Configs.ADMIN;
import static com.example.qtfison.h2gapp.Configs.LOGINUSEREMAIL;
import static com.example.qtfison.h2gapp.Configs.NOT_YET;
import static com.example.qtfison.h2gapp.Configs.PANDING;
import static com.example.qtfison.h2gapp.Configs.TREASURER;
import static com.example.qtfison.h2gapp.Configs.USERLOGINROLE;


public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseRecyclerAdapter<PaymentReleased,PaymentRecyclerViewHolder> adapter;
    FirebaseRecyclerOptions<PaymentReleased> options;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    Handler handler;
    String email;
    TextView txtUser,txtRole,tvTotMembers;
    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.activity_member_payment, container, false);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView=view.findViewById(R.id.recycleViewP);
        mAuth= FirebaseAuth.getInstance();
        LinearLayoutManager mGridLayoutManager = new LinearLayoutManager(getContext(), GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mGridLayoutManager);
        LOGINUSEREMAIL=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        email=LOGINUSEREMAIL;
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("payment_released");
        }
        txtUser=view.findViewById(R.id.txt_user);
        txtRole=view.findViewById(R.id.txt_role);
        if(LOGINUSEREMAIL!=null)
            txtUser.setText(LOGINUSEREMAIL.split("@")[0]);
        txtRole.setText("@"+USERLOGINROLE);
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
        ImageView img=view.findViewById(R.id.imageView6);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentLayout = getView().findViewById(R.id.recycleViewP);
                Snackbar.make(parentLayout, "That is the list of payments needed for you. 'No' Means not yet paid, Double '√' Means already paid and ✍ Means waiting approve", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }
        });

        displayContent();
        return  view;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    void displayContent() {
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
                if(isUserAllowed(TREASURER,getContext())){
                    newv=1;
                }else{
                    newv=2;
                }
                selectedKey=getSnapshots().getSnapshot(position).getKey();
                holder.paid_on.setText(model.getPaidOn());
                if (model.getIsPaid() == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.paid_status.setImageDrawable(getActivity().getDrawable(R.drawable.ic_stat_name));
                    }
                    //holder.txt_paid_status.setText("X");
                } else if(model.getIsPaid() == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.paid_status.setImageDrawable(getActivity().getDrawable(R.drawable.ic_done_all_black_24dp));
                    }
                    //holder.txt_paid_status.setText("√");
                }else {
                    //holder.txt_paid_status.setText("P");
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
                                                        if(isUserAllowed(TREASURER,getContext())||email.equalsIgnoreCase(LOGINUSEREMAIL)){
                                                            if(model.getIsPaid()==NOT_YET || (isUserAllowed(TREASURER,getContext())&& model.getIsPaid()==PANDING)) {
                                                                updateData(selectedKey, newv, getFormatedDate(new Date()), getContext(), model.getReleasedOnId(), model.getEmail(),"1"/*noticeId*/);
                                                                if (!USERLOGINROLE.equalsIgnoreCase(ADMIN) && !USERLOGINROLE.equalsIgnoreCase(TREASURER)) {
                                                                    MyNotification notice = new MyNotification(LOGINUSEREMAIL,TREASURER, "Please Approve Or Deny", Configs.NOTICE_TYPE_CONTRP, amountPaid, getFormatedDate(new Date()), model.getReleasedOnId(), getUniqueId());
                                                                    FirebaseDatabase.getInstance().getReference("notifications").push().setValue(notice);
                                                                }
                                                                return true;
                                                            }else {
                                                                Toast.makeText(getContext(),"Sorry!! Already Done Previously",Toast.LENGTH_SHORT).show();
                                                                return true;
                                                            }
                                                        }else{
                                                            Toast.makeText(getContext(),"Contact Treasurer",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(),"App Error contact Fison"+databaseError.getMessage().toString(),Toast.LENGTH_SHORT).show();
                            }
                        });
                Message processStart1 = handler.obtainMessage(2);
                processStart1.sendToTarget();
            }
            @NonNull
            @Override
            public PaymentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView=LayoutInflater.from(getContext()).inflate(R.layout.post_payment_items,viewGroup,false);
                progressBar.setVisibility(View.INVISIBLE);
                return new PaymentRecyclerViewHolder(itemView);
            }

        };
        adapter.startListening();
        recyclerView. addItemDecoration(new DividerItemDecoration(getContext() ,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

    }

}

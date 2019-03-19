package com.example.qtfison.h2gapp.members;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.example.qtfison.h2gapp.MyProgressiveDialog;
import com.example.qtfison.h2gapp.Payment.MemberPaymentActivity;
import com.example.qtfison.h2gapp.Payment.PaymentNeeded;
import com.example.qtfison.h2gapp.Payment.PaymentReleased;
import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.loans.LoanActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getRandomColor;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isUserAllowed;
import static com.example.qtfison.h2gapp.Configs.LOGINUSEREMAIL;
import static com.example.qtfison.h2gapp.Configs.TREASURER;
import static com.example.qtfison.h2gapp.Configs.USERLOGINROLE;


public class MemberFragment extends Fragment {

    TextView txtUser,txtRole,tvTotMembers;
    RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseRecyclerOptions<Member> options;
    FirebaseRecyclerAdapter<Member,MemberRecycleViewHolder> adapter;
    public MemberFragment() {
    }
    public static MemberFragment newInstance(String param1, String param2) {
        MemberFragment fragment = new MemberFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
@NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_member, container, false);
        recyclerView=view.findViewById(R.id.recycleView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvTotMembers=view.findViewById(R.id.tvTotMember);
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("members");
        }
    txtUser=view.findViewById(R.id.txt_user);
    txtRole=view.findViewById(R.id.txt_role);
    if(LOGINUSEREMAIL!=null)
        txtUser.setText(LOGINUSEREMAIL.split("@")[0]);
        txtRole.setText(USERLOGINROLE);

        displayComment();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    FirebaseDatabase.getInstance().getReference("members").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            tvTotMembers.setText("@"+dataSnapshot.getChildrenCount()+" Members");
            return;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

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
    private void displayComment() {
        options=new FirebaseRecyclerOptions.Builder<Member>()
                .setQuery(myRef,Member.class)
                .build();
        adapter= new FirebaseRecyclerAdapter<Member, MemberRecycleViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final MemberRecycleViewHolder holder, int position, @NonNull final Member model) {
                        holder.txt_fname.setText(model.getfName());
                        holder.txt_lname.setText(model.getlName());
                        holder.txt_address.setText(model.getAddress());
                        holder.txt_phone.setText(model.getPhone());
                        TextDrawable drawable = TextDrawable.builder() .buildRound(""+model.getfName().charAt(0),getRandomColor() );
                        holder.img_Id.setImageDrawable(drawable);
                        FirebaseDatabase.getInstance().getReference("payment_released").orderByChild("email").equalTo(model.getEmail()).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                               if( !dataSnapshot.child("isPaid").getValue().toString().equalsIgnoreCase("1")){
                                   return;
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
                        FirebaseDatabase.getInstance().getReference("loans").orderByChild("myEmail").equalTo(model.getEmail()).addChildEventListener(new ChildEventListener() {
                            int a=0;
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if(!dataSnapshot.child("isFullpaid").getValue().toString().equalsIgnoreCase("1")){
                                    a =a+1;
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
                        holder.txt_fname.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        holder.img_menu_popup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopupMenu popup = new PopupMenu(v.getContext(), v);
                                MenuInflater inflater = popup.getMenuInflater();
                                inflater.inflate(R.menu.popup_menu_member, popup.getMenu());
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.menu_charged_amount:
                                                if(isUserAllowed(TREASURER,getContext())) {
                                                    showChargeAmountDialog(model.getEmail(),model.getfName());
                                                    return true;
                                                }else{
                                                    Toast.makeText(getContext(),"Contact Cordinator Or Secretary",Toast.LENGTH_SHORT).show();
                                                    return true;
                                                }
                                            case R.id.menu_check_payment:
                                                startActivity1(MemberPaymentActivity.class,model);
                                                return true;
                                            case R.id.menu_check_loan:
                                                Toast.makeText(getContext(),"Under Developement",Toast.LENGTH_SHORT).show();
                                                return true;
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
                    public MemberRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View itemView=LayoutInflater.from(getContext()).inflate(R.layout.post_member_items,viewGroup,false);
                        return new MemberRecycleViewHolder(itemView);
                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(getActivity() != null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() != null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        adapter.notifyDataSetChanged();

    }
    public void showChargeAmountDialog(final String email,final String fName){
        LayoutInflater li = LayoutInflater.from(getContext());
      final   View promptsView = li.inflate(R.layout.payment_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(  getContext());
        alertDialogBuilder.setView(promptsView);
               alertDialogBuilder
                .setCancelable(false)
                .setTitle("Add New payment")
                .setIcon(R.drawable.ic_payment)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                EditText edtPayType =  promptsView .findViewById(R.id.Pay_type);
                                EditText edtAmount = promptsView .findViewById(R.id.edt_pay_amt);
                                EditText edtStart = promptsView .findViewById(R.id.pay_start_date);
                                EditText edtend =  promptsView .findViewById(R.id.pay_end_date);
                                final String paymentNeededId;
                                final String paymentType,startDate,endingDate;
                                final long amount;
                                paymentNeededId = getUniqueId();

                                if(edtPayType.getText().length()>0 && edtPayType.getText().length()<25) {
                                    paymentType = edtPayType.getText().toString();
                                }else{
                                   Toast.makeText(getContext(),"Payment Type required(max 25 chars)",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(edtStart.getText().length()==0){
                                    Toast.makeText(getContext(),"Starting date required",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                startDate = edtStart.getText().toString();
                                if(edtend.getText().length()==0){
                                    Toast.makeText(getContext(),"Deadline date required",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                endingDate = edtend.getText().toString();
                                try {
                                    amount = Long.parseLong(edtAmount.getText().toString());
                                    PaymentNeeded paymentNeeded= new PaymentNeeded(paymentNeededId,paymentType,startDate,endingDate,amount,amount,1,0);
                                    addNewPayment(paymentNeeded,email,fName);
                                }catch (Exception e){
                                    Toast.makeText(getContext(),"Invalid Amount",Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void addNewPayment(final PaymentNeeded paymentNeeded,String email,String fName) {

     //   progress.showPregress();
        FirebaseDatabase database;
        DatabaseReference myDatabaseRef, ref;
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference("payment_needed");
        myDatabaseRef.push().setValue(paymentNeeded);
        Toast.makeText(getContext(), "Amount charged to " + fName, Toast.LENGTH_SHORT).show();
        PaymentReleased paymentReleased = new PaymentReleased(paymentNeeded.getPaymentNeededid(), email, 0, "Not yet","");
        myDatabaseRef = database.getReference("payment_released");
        myDatabaseRef.push().setValue(paymentReleased);
        //increaseIssues(email,"members","paymentIssues");
    }
    public String getUniqueId() {
        return  UUID.randomUUID().toString().replace("-", "");
    }
    public void startActivity1(Class c,Member model){
        Intent i=new Intent(getContext(),c);
        i.putExtra("email",model.getEmail());
        i.putExtra("fname",model.getfName());
        i.putExtra("loanId","0");
        startActivity(i);
    }

}

package com.example.qtfison.h2gapp.account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qtfison.h2gapp.R;

import com.example.qtfison.h2gapp.loans.LoanActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    FirebaseDatabase database;
    DatabaseReference myRef;

    TextView txtTotColl,txtTotUncoll,txtTotExpected,txt_tot_expenses;
    Button btnApplyLoan,btnPayloan,btnMyLoan;
    private OnFragmentInteractionListener mListener;

    public AccountFragment() {

    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_account, container, false);
        txtTotColl=view.findViewById(R.id.txt_tot_coll);
        txtTotUncoll=view.findViewById(R.id.txt_tot_uncoll);
        txtTotExpected=view.findViewById(R.id.txt_tot_expected);
        txt_tot_expenses=view.findViewById(R.id.txt_tot_exp);
        btnApplyLoan=view.findViewById(R.id.btn_appy_loan);
        btnMyLoan=view.findViewById(R.id.btn_myloan);
        btnPayloan=view.findViewById(R.id.btn_pay_loan);
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference();
            myRef.child("payment_needed").addChildEventListener(new ChildEventListener() {
                int nbrPaid, nbrMember,nbrRest;
                long amount;
                long collected=0;
                long unCollected=0;
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    amount=Long.parseLong(dataSnapshot.child("amount").getValue().toString());
                    nbrPaid=Integer.parseInt(dataSnapshot.child("nbrPaid").getValue().toString());
                    nbrMember=Integer.parseInt(dataSnapshot.child("nbrMembers").getValue().toString());
                    nbrRest=nbrMember-nbrPaid;
                    collected=collected+(amount*nbrPaid);
                    unCollected=unCollected+(amount*nbrRest);
                    txtTotColl.setText(""+collected);
                    txtTotUncoll.setText(""+unCollected);
                    txtTotExpected.setText(""+(collected+unCollected));
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

            }});
            myRef.child("expenses").addChildEventListener(new ChildEventListener() {
                long expenses=0;
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    expenses=expenses+Long.parseLong(dataSnapshot.child("expeseAmount").getValue().toString());
                    txt_tot_expenses.setText(expenses+"");
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

        btnApplyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),LoanActivity.class);
                startActivity(i);
            }
        });
        btnPayloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnPayloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}

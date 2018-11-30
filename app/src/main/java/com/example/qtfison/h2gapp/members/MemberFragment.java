package com.example.qtfison.h2gapp.members;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.qtfison.h2gapp.Payment.MemberPaymentActivity;
import com.example.qtfison.h2gapp.Payment.PaymentNeeded;
import com.example.qtfison.h2gapp.Payment.PaymentReleased;
import com.example.qtfison.h2gapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.UUID;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isUserAllowed;


public class MemberFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseRecyclerOptions<Member> options;
    Member selectedPost;
    String selectedKey;

    FirebaseRecyclerAdapter<Member,MemberRecycleViewHolder> adapter;
    public MemberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemberFragment newInstance(String param1, String param2) {
        MemberFragment fragment = new MemberFragment();
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
@NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
       View view=inflater.inflate(R.layout.fragment_member, container, false);
        recyclerView=view.findViewById(R.id.recycleView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("members");
        }
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
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.menu_member, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_member:
                if(isUserAllowed(getActivity(),"Cordinator","Secretary")) {
                    Intent intent = new Intent(getContext(), Registration.class);
                    startActivity(intent);
                    return true;
                }else{
                    Toast.makeText(getContext(),"Contact Cordinator Or Secretary",Toast.LENGTH_SHORT).show();
                    return true;
                }
            case R.id.logout:
                    //signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                        TextDrawable drawable = TextDrawable.builder() .buildRound(""+model.getfName().charAt(0), getRandomColor());
                        holder.img_Id.setImageDrawable(drawable);
                        FirebaseDatabase database;
                        DatabaseReference myDatabaseRef;
                        database = FirebaseDatabase.getInstance();
                        myDatabaseRef = database.getReference("payment_released");
                        myDatabaseRef.orderByChild("email").equalTo(model.getEmail())
                                .addChildEventListener(new ChildEventListener() {
                                                            int count_issues=0;
                                                           @Override
                                                           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                               if(!dataSnapshot.child("isPaid").getValue().toString().equalsIgnoreCase("1")) {
                                                                   count_issues++;
                                                                   TextDrawable drawableIssues = TextDrawable.builder().buildRound("" + count_issues, Color.RED);
                                                                   holder.img_issues.setImageDrawable(drawableIssues);
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
                                Toast.makeText(getContext(),"clicked on "+model.getfName(),Toast.LENGTH_SHORT).show();
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
                                        //do your things in each of the following cases
                                        switch (item.getItemId()) {
                                            case R.id.menu_charged_amount:
                                                if(isUserAllowed(getContext(),"Treasurer")) {
                                                    showChargeAmountDialog(model.getEmail(),model.getfName());
                                                    return true;
                                                }else{
                                                    Toast.makeText(getContext(),"Contact Cordinator Or Secretary",Toast.LENGTH_SHORT).show();
                                                    return true;
                                                }
                                            case R.id.menu_check_payment:
                                                Intent intent=new Intent(getContext(),MemberPaymentActivity.class);
                                                intent.putExtra("email",model.getEmail());
                                                intent.putExtra("fname",model.getfName());
                                                startActivity(intent);
                                            //  return true;
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
    public void onResume() {
        super.onResume();
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
                                EditText edtPayType = (EditText) promptsView .findViewById(R.id.Pay_type);
                                EditText edtAmount = (EditText) promptsView .findViewById(R.id.edt_pay_amt);
                                EditText edtStart = (EditText) promptsView .findViewById(R.id.pay_start_date);
                                EditText edtend = (EditText) promptsView .findViewById(R.id.pay_end_date);
                                final String paymentNeededId;
                                final String paymentType,startDate,endingDate;
                                final long amount;
                                paymentNeededId = getUniqueId();
                                paymentType = edtPayType.getText().toString();
                                startDate = edtStart.getText().toString();
                                endingDate = edtend.getText().toString();
                                amount = Long.parseLong(edtAmount.getText().toString());
                                PaymentNeeded paymentNeeded= new PaymentNeeded(paymentNeededId,paymentType,startDate,endingDate,amount);
                                addNewPayment(paymentNeeded,email,fName);
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
        FirebaseDatabase database;
        DatabaseReference myDatabaseRef, ref;
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference("payment_needed");
        myDatabaseRef.push().setValue(paymentNeeded);
        Toast.makeText(getContext(), "Amount charged to " + fName, Toast.LENGTH_SHORT).show();
        PaymentReleased paymentReleased = new PaymentReleased(paymentNeeded.getPaymentNeededid(), email, 0, "Not yet");
        myDatabaseRef = database.getReference("payment_released");
        myDatabaseRef.push().setValue(paymentReleased);
    }
    public String getUniqueId() {
        return  UUID.randomUUID().toString().replace("-", "");
    }
}

package com.example.qtfison.h2gapp.Payment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qtfison.h2gapp.Classes.TextViewDatePicker;
import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.members.Registration;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Date;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getConvertedDate;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getFormatedDate;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getUniqueId;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isForYourEmailId;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isUserAllowed;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isValidDate;

public class PaymentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseRecyclerAdapter<PaymentReleased, PaymentRecyclerViewHolder> adapter;
    FirebaseRecyclerOptions<PaymentReleased> options;
    private OnFragmentInteractionListener mListener;

    public PaymentFragment() {

    }

    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();
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
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        recyclerView = view.findViewById(R.id.recycleViewP);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        mGridLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("payment_released");
        }
        displayContent();
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_payment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_payment_needed:
                showDialog();
                return true;
            case R.id.logout:
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void displayContent() {
        final SharedPreferences sharedPref = getActivity().getSharedPreferences("preference_file_key", Context.MODE_PRIVATE);
        options = new FirebaseRecyclerOptions.Builder<PaymentReleased>()
                .setQuery(myRef.orderByChild("email").equalTo(sharedPref.getString(getString(R.string.pref_login_user_email), null))
                        , PaymentReleased.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<PaymentReleased, PaymentRecyclerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PaymentRecyclerViewHolder holder, int position, @NonNull final PaymentReleased model) {
                if (isForYourEmailId(getActivity(), model.getEmail())) {
                    final String selectedKey;
                    final int newv;
                    if (isUserAllowed(getActivity(), "Treasurer")) {
                        newv = 1;
                    } else {
                        newv = 2;
                    }
                    selectedKey = getSnapshots().getSnapshot(position).getKey();
                    holder.paid_on.setText(model.getPaidOn());
                    if (model.getIsPaid() == 0) {
                        holder.txt_paid_status.setText("X");
                    } else if (model.getIsPaid() == 1) {
                        holder.txt_paid_status.setText("√");
                    } else {
                        holder.txt_paid_status.setText("P");
                    }
                    holder.txt_pid.setText("" + model.getEmail());
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
                                                            updateData(selectedKey, newv, getFormatedDate(new Date()));
                                                            return true;
                                                        //case R.id.menu_remove_from_itinerary:
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
                                    Toast.makeText(getContext(), "Error contact Fison" + databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @NonNull
            @Override
            public PaymentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.post_payment_items, viewGroup, false);
                return new PaymentRecyclerViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void updateData(final String selectedKey, final int newV, final String PaidDate) {
        database = FirebaseDatabase.getInstance();
        final DatabaseReference myref = database.getReference();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myref.child("payment_released").child(selectedKey).child("isPaid").setValue(newV);
                myref.child("payment_released").child(selectedKey).child("paidOn").setValue(PaidDate);
                Toast.makeText(getContext(), "Thanks, recorded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showDialog() {
        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.payment_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Add New payment")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText edtPayType = promptsView.findViewById(R.id.Pay_type);
                                EditText edtAmount = promptsView.findViewById(R.id.edt_pay_amt);
                                EditText edtStart = promptsView.findViewById(R.id.pay_start_date);
                                EditText edtend = promptsView.findViewById(R.id.pay_end_date);
                                TextViewDatePicker editTextDatePicker =  new TextViewDatePicker(edtStart.getContext(), edtStart,new Date().getTime(),getConvertedDate("12/12/2200").getTime());//without min date, max date
                                TextViewDatePicker editTextDatePicker1 = new TextViewDatePicker(edtend.getContext(), edtend,new Date().getTime(),getConvertedDate("12/12/2200").getTime());
                                 String paymentNeededId;
                                 String paymentType, startDate, endingDate;
                                 long amount;
                                paymentNeededId = getUniqueId();
                                boolean isValid=true;
                                paymentType = edtPayType.getText().toString();
                                if (paymentType.length() <= 0) {
                                    Toast.makeText(getContext(), "Invalid Payment Type", Toast.LENGTH_SHORT).show();
                                    edtPayType.setError("error");
                                    isValid=false;
                                }
                                startDate = edtStart.getText().toString();
                                if (isValidDate(startDate)) {
                                    Toast.makeText(getContext(), "Opening date required", Toast.LENGTH_SHORT).show();
                                    edtStart.setError("error");
                                    isValid=false;
                                }
                                endingDate = edtend.getText().toString();
                                if (isValidDate(endingDate)) {
                                    Toast.makeText(getContext(), "Deadline date required", Toast.LENGTH_SHORT).show();
                                    edtend.setError("error");
                                    isValid=false;
                                }
                                amount=0;
                                try {
                                    amount = Long.parseLong(edtAmount.getText().toString());
                                }catch (Exception e) {
                                    Toast.makeText(getContext(), "Amount required", Toast.LENGTH_SHORT).show();
                                    edtAmount.setError("error");
                                    isValid = false;
                                }

                                if(isValid) {
                                    PaymentNeeded paymentNeeded = new PaymentNeeded(paymentNeededId, paymentType, startDate, endingDate, amount);
                                    addNewPayment(paymentNeeded);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void addNewPayment(final PaymentNeeded paymentNeeded) {
        FirebaseDatabase database;
        DatabaseReference myDatabaseRef, ref;
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference("payment_needed");
        myDatabaseRef.push().setValue(paymentNeeded);
        Toast.makeText(getContext(), "New Payment_needed Saved", Toast.LENGTH_SHORT).show();
        myDatabaseRef = database.getReference("members");
        myDatabaseRef.addChildEventListener(new ChildEventListener() {
            @NonNull
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                FirebaseDatabase database;
                DatabaseReference myDatabaseRef;
                database = FirebaseDatabase.getInstance();
                PaymentReleased paymentReleased = new PaymentReleased(paymentNeeded.getPaymentNeededid(), dataSnapshot.child("email").getValue().toString(), 0, "Not yet");
                myDatabaseRef = database.getReference("payment_released");
                myDatabaseRef.push().setValue(paymentReleased);
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
                Toast.makeText(getContext(), "Error contact Fison" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

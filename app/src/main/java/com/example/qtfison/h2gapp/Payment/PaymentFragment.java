package com.example.qtfison.h2gapp.Payment;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.qtfison.h2gapp.Classes.TextViewDatePicker;
import com.example.qtfison.h2gapp.MyProgressiveDialog;
import com.example.qtfison.h2gapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Date;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getUniqueId;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isValidDate;

public class PaymentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseRecyclerAdapter<PaymentNeeded, PaymentSummaryRecycleViewHolder> adapter;
    FirebaseRecyclerOptions<PaymentNeeded> options;
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
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, true);
        mGridLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("payment_needed");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_payment_needed:
                Intent i=new Intent(getContext(),PaymentReleasedActivity.class);
                startActivity(i);
                //showDialog();
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
        void onFragmentInteraction(Uri uri);
    }

    private void displayContent() {
        final MyProgressiveDialog progress=new MyProgressiveDialog(getActivity());
        progress.showPregress();
        options = new FirebaseRecyclerOptions.Builder<PaymentNeeded>()
                .setQuery(myRef , PaymentNeeded.class)
                .build();
        progress.hideprogress();
        adapter = new FirebaseRecyclerAdapter<PaymentNeeded, PaymentSummaryRecycleViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PaymentSummaryRecycleViewHolder holder, int position, @NonNull final PaymentNeeded model) {
              //  final String selectedKey=getSnapshots().getSnapshot(position).getKey();
                holder.txt_Amount.setText(""+model.getAmount());
                holder.txt_start_date.setText(model.getStartDate());
                holder.txt_end_date.setText(model.getEndingDate());
                holder.txt_paymentType.setText(model.getPaymentType());
                int paidnbr=model.getNbrPaid();
                int unpaidnbr=model.getNbrMembers()-paidnbr;
                TextDrawable drawable = TextDrawable.builder() .buildRound(""+unpaidnbr, Color.parseColor("#6eb4ad"));
                TextDrawable drawable2 = TextDrawable.builder() .buildRound(""+paidnbr, Color.parseColor("#6eb4ad"));
                long paidDone=model.getAmount()*paidnbr;
                long paidNotDone=model.getAmount()*unpaidnbr;
                holder.img_unpaid.setImageDrawable(drawable);
                holder.img_paid.setImageDrawable(drawable2);
                holder.txt_paid.setText(""+paidDone);
                holder.txt_unpaid.setText(""+paidNotDone);
                holder.txt_total.setText(""+model.getTotal());
                if(unpaidnbr==0){
                    holder.txt_paid_status.setText("√");
                }else {
                    holder.txt_paid_status.setText("X");
                }
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
                                                            return true;
                                                        default:
                                                            return false;
                                                    }
                                                }
                                            });
                                            popup.show();
                                        }
                                    });
               // progress.hideprogress();
            }

            @NonNull
            @Override
            public PaymentSummaryRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.payment_historical_post_item, viewGroup, false);
                return new PaymentSummaryRecycleViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}

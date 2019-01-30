package com.example.qtfison.h2gapp.expenses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.example.qtfison.h2gapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getUniqueId;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isValidDate;

public class ExpensesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseRecyclerAdapter<Expense, ExpenseRecycleViewHolder> adapter;
    FirebaseRecyclerOptions<Expense> options;
    private OnFragmentInteractionListener mListener;

    public ExpensesFragment() {
    }
    public static ExpensesFragment newInstance(String param1, String param2) {
        ExpensesFragment fragment = new ExpensesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
         View view=inflater.inflate(R.layout.fragment_expenses, container, false);
        recyclerView = view.findViewById(R.id.recycleViewP);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mGridLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("expenses");
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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.expense_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_expense:
                Intent i=new Intent(getContext(),NewExpenseActivity.class);
                startActivity(i);
                //showDialog();
                return true;
            case R.id.logout:
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    private void displayContent() {
        options = new FirebaseRecyclerOptions.Builder<Expense>()
                .setQuery(myRef, Expense.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Expense, ExpenseRecycleViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ExpenseRecycleViewHolder holder, int position, @NonNull final Expense model) {
                if (model.getIsReleased() == 0) {
                    holder.txt_isreleased.setText("X");
                } else if (model.getIsReleased() == 1) {
                    holder.txt_isreleased.setText("√");
                }
                holder.txt_expense.setText(model.getExpenseName());
                                    holder.txt_amount.setText(""+model.getExpeseAmount());
                                    holder.txt_to.setText(model.getExpenseTo());
                                    holder.txt_date.setText(model.getExpenseReleaseddate());
                                    holder.txt_description.setText(model.getExpenseDescription());
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
                                                            //updateData(selectedKey, newv, getFormatedDate(new Date()));
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
            public ExpenseRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.expenses_post_items, viewGroup, false);
                return new ExpenseRecycleViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    public void showDialog() {
        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.expense_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Add New Expenses")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText edtexpenseName = promptsView.findViewById(R.id.edt_expensename);
                                EditText edtexpenseAmount = promptsView.findViewById(R.id.edt_expense_amt);
                                EditText edtReleaseddate = promptsView.findViewById(R.id.edt_exp_date);
                                EditText edtExpenseTo = promptsView.findViewById(R.id.edt_exp_pay_to);
                                EditText edtExpenseDescription = promptsView.findViewById(R.id.edt_exp_full_desc);

                                String expenseName, expenseReleaseddate, expenseTo,expenseId,expenseDescription;
                                long expeseAmount;
                                expenseId = getUniqueId();
                                boolean isValid=true;
                                expenseName = edtexpenseName.getText().toString();
                                if (expenseName.length() <= 0) {
                                    Toast.makeText(getContext(), "Invalid Expense name", Toast.LENGTH_SHORT).show();
                                    edtexpenseName.setError("error");
                                    isValid=false;
                                }
                                expeseAmount=0;
                                try {
                                    expeseAmount = Long.parseLong(edtexpenseAmount.getText().toString());
                                }catch (Exception e) {
                                    Toast.makeText(getContext(), "Amount required", Toast.LENGTH_SHORT).show();
                                    edtexpenseAmount.setError("error");
                                    isValid = false;
                                }

                                expenseReleaseddate = edtReleaseddate.getText().toString();
                                if (!isValidDate(expenseReleaseddate)) {
                                    Toast.makeText(getContext(), "invalid date", Toast.LENGTH_SHORT).show();
                                    edtReleaseddate.setError("error");
                                    isValid=false;
                                }
                                expenseTo = edtExpenseTo.getText().toString();
                                if (expenseTo.length() <= 0) {
                                    Toast.makeText(getContext(), "Invalid input(paid to..)", Toast.LENGTH_SHORT).show();
                                    edtExpenseTo.setError("error");
                                    isValid=false;
                                }
                                expenseDescription = edtExpenseDescription.getText().toString();
                                if (expenseDescription.length() <= 0) {
                                    Toast.makeText(getContext(), "Invalid Description", Toast.LENGTH_SHORT).show();
                                    edtExpenseDescription.setError("error");
                                    isValid=false;
                                }
                                if(isValid) {
                                    Expense expense = new Expense(expenseName,expenseReleaseddate, expenseTo,expenseId,expenseDescription, 0,expeseAmount);
                                    addNewExpense(expense);
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
    public void addNewExpense(final Expense expense) {
        FirebaseDatabase database;
        DatabaseReference myDatabaseRef, ref;
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference("expenses");
        myDatabaseRef.push().setValue(expense);
        Toast.makeText(getContext(), "New Expense Saved", Toast.LENGTH_SHORT).show();
    }
}

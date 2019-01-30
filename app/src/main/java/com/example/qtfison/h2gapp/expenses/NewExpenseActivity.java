package com.example.qtfison.h2gapp.expenses;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qtfison.h2gapp.Classes.TextViewDatePicker;
import com.example.qtfison.h2gapp.Payment.PaymentReleasedActivity;
import com.example.qtfison.h2gapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getUniqueId;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isValidDate;

public class NewExpenseActivity extends AppCompatActivity {
    EditText edtexpenseName,edtexpenseAmount,edtReleaseddate,edtExpenseTo,edtExpenseDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ne_expense);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
         edtexpenseName = findViewById(R.id.edt_expensename);
        edtexpenseAmount = findViewById(R.id.edt_expense_amt);
        edtReleaseddate = findViewById(R.id.edt_exp_date);
         edtExpenseTo = findViewById(R.id.edt_exp_pay_to);
         edtExpenseDescription = findViewById(R.id.edt_exp_full_desc);
        setDatePick();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExpense();
            }
        });
    }
    public void saveExpense(){
        String expenseName, expenseReleaseddate, expenseTo,expenseId,expenseDescription;
        long expeseAmount;
        expenseId = getUniqueId();
        boolean isValid=true;
        expenseName = edtexpenseName.getText().toString();
        if (expenseName.length() <= 0) {
            Toast.makeText(getBaseContext(), "Invalid Expense name", Toast.LENGTH_SHORT).show();
            edtexpenseName.setError("error");
            isValid=false;
        }
        expeseAmount=0;
        try {
            expeseAmount = Long.parseLong(edtexpenseAmount.getText().toString());
        }catch (Exception e) {
            Toast.makeText(getBaseContext(), "Amount required", Toast.LENGTH_SHORT).show();
            edtexpenseAmount.setError("error");
            isValid = false;
        }

        expenseReleaseddate = edtReleaseddate.getText().toString();
        if (!isValidDate(expenseReleaseddate)) {
            Toast.makeText(getBaseContext(), "invalid date", Toast.LENGTH_SHORT).show();
            edtReleaseddate.setError("error");
            isValid=false;
        }
        expenseTo = edtExpenseTo.getText().toString();
        if (expenseTo.length() <= 0) {
            Toast.makeText(getBaseContext(), "Invalid input(paid to..)", Toast.LENGTH_SHORT).show();
            edtExpenseTo.setError("error");
            isValid=false;
        }
        expenseDescription = edtExpenseDescription.getText().toString();
        if (expenseDescription.length() <= 0) {
            Toast.makeText(getBaseContext(), "Invalid Description", Toast.LENGTH_SHORT).show();
            edtExpenseDescription.setError("error");
            isValid=false;
        }
        if(isValid) {
            Expense expense = new Expense(expenseName,expenseReleaseddate, expenseTo,expenseId,expenseDescription, 0,expeseAmount);
            addNewExpense(expense);
        }
    }
    public void addNewExpense(final Expense expense) {
        FirebaseDatabase database;
        DatabaseReference myDatabaseRef, ref;
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference("expenses");
        myDatabaseRef.push().setValue(expense);
        Toast.makeText(getBaseContext(), "New Expense Saved", Toast.LENGTH_SHORT).show();
    }
    public void setDatePick() {
        TextViewDatePicker editTextDatePicker = new TextViewDatePicker(NewExpenseActivity.this, edtReleaseddate,new Date().getTime(),0);//without min date, max date
        //TextViewDatePicker editTextDatePicker1 = new TextViewDatePicker(NewExpenseActivity.this, edtend,new Date().getTime(),0);
        //TextViewDatePicker editTextDatePicker = new TextViewDatePicker(context, myEditText); // no limitation
    }
}

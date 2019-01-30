package com.example.qtfison.h2gapp.loans;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qtfison.h2gapp.Classes.MyNotification;
import com.example.qtfison.h2gapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;


import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getConvertedDateformat1;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getDifferenceBetweenDate;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getFormatedDate;
import static com.example.qtfison.h2gapp.Classes.UtilFunctions.getUniqueId;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isUserAllowed;
import static com.example.qtfison.h2gapp.Configs.LOGINUSEREMAIL;
import static com.example.qtfison.h2gapp.Configs.NOTICE_TYPE_LOANP;
import static com.example.qtfison.h2gapp.Configs.TREASURER;

public class LoanActivity extends AppCompatActivity {
    String  fname,email;
    String loanId,notifId;
    RecyclerView recyclerView;

    FirebaseRecyclerAdapter<Loan,LoanRecyclerViewHolder> adapter;
    FirebaseRecyclerOptions<Loan> options;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fname=getIntent().getStringExtra("fname");
        email=getIntent().getStringExtra("email");
        loanId=getIntent().getStringExtra("loanId");
        notifId=getIntent().getStringExtra("notifId");
        getSupportActionBar().setTitle("Loan: "+fname);
        recyclerView=findViewById(R.id.recycleViewP);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getBaseContext(), 2, GridLayoutManager.VERTICAL, false);
        mGridLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mGridLayoutManager);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("loans");
        displayContent();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu ){
        getMenuInflater().inflate(R.menu.loan_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_apply_loan:
                    applyLoan(getBaseContext(),new ApplyLoanActivity().getClass());
            default:
        }
        return false;
    }

    private void displayContent() {
        if(loanId.equalsIgnoreCase("0")) {
            options = new FirebaseRecyclerOptions.Builder<Loan>()
                    .setQuery(myRef.orderByChild("myEmail").equalTo(email), Loan.class)
                    .build();
        }else{
            options = new FirebaseRecyclerOptions.Builder<Loan>()
                    .setQuery(myRef.orderByChild("loanId").equalTo(loanId), Loan.class)
                    .build();
        }
            adapter= new FirebaseRecyclerAdapter<Loan, LoanRecyclerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final LoanRecyclerViewHolder holder, int position, @NonNull final Loan model) {


                final String selectedKey=getSnapshots().getSnapshot(position).getKey();
                String  paymentDuedate=model.getPaymentDuedate();
                Date payDate=getConvertedDateformat1(paymentDuedate);
                long daysRest=getDifferenceBetweenDate(payDate,new Date());
                if (model.getIsFullpaid() == 0) {
                    holder.txt_isFullpaid.setText("X");
                } else if(model.getIsFullpaid() == 1) {
                    holder.txt_isFullpaid.setText("√");
                }else {
                    holder.txt_isFullpaid.setText("S");
                }
                                holder.txt_amountTake.setText(""+model.getAmountTake());
                                holder.txt_takenDate.setText(model.getTakenDate());
                                holder.txt_interest.setText(""+model.getInterest());
                                holder.txt_paymentDuedate.setText(model.getPaymentDuedate());
                                holder.txt_loan_paid.setText(""+model.getAmountPaid());
                                holder.txt_reasons.setText(model.getReasons());
                                holder.txt_amount2Paid.setText(""+(model.getAmountTake()+model.getInterest()));
                                holder.txt_amountRest.setText(""+model.getAmountRest());
                                holder.txt_perc_paid.setText((model.getAmountPaid()*100)/(model.getAmountTake()+model.getInterest())+"%");
                                holder.txt_perc_unpaid.setText((model.getAmountRest()*100)/(model.getAmountTake()+model.getInterest())+"%");
                                holder.txt_rest_days.setText("Remaining Days "+daysRest+". Approximately "+daysRest/30+"Months");
                                holder.img_menu_popup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PopupMenu popup = new PopupMenu(v.getContext(), v);
                                        MenuInflater inflater = popup.getMenuInflater();
                                        inflater.inflate(R.menu.menu_loan_item, popup.getMenu());
                                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            @Override
                                            public boolean onMenuItemClick(MenuItem item) {
                                                switch (item.getItemId()) {
                                                    case R.id.menu_amount_paid:
                                                        getAmountAndUpDatePayment(model.getLoanId(),model.getMyEmail());
                                                        break;
                                                    case R.id.menu_view:
                                                        Intent i=new Intent(getBaseContext(),LoanPaymentsHistoryActivity.class);
                                                        i.putExtra("amountLeft",model.getAmountRest());
                                                        i.putExtra("daysLeft",getDifferenceBetweenDate(getConvertedDateformat1(model.getTakenDate()),getConvertedDateformat1(model.getPaymentDuedate())));
                                                        i.putExtra("loanId",model.getLoanId());
                                                        i.putExtra("fname",fname);
                                                        i.putExtra("notifId",notifId);
                                                        startActivity(i);
                                                       break;
                                                    default:
                                                        return false;
                                                }
                                                return false;
                                            }

                                        });
                                        popup.show();
                                    }
                                });

            }
            @NonNull
            @Override
            public LoanRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView=LayoutInflater.from(getBaseContext()).inflate(R.layout.loan_historical_post_item,viewGroup,false);
                return new LoanRecyclerViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
   public  void applyLoan(Context context,Class a){
       Intent i=new Intent(context,a);
       startActivity(i);
       finish();
   }

   public void getAmountAndUpDatePayment(final String loanId,final String email2){
       if(isUserAllowed(TREASURER,getBaseContext())||email.equalsIgnoreCase(LOGINUSEREMAIL)) {
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Amount you paid to this Loan");
           final EditText input = new EditText(this);
           input.setInputType(InputType.TYPE_CLASS_NUMBER);
           builder.setView(input);
           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   try {
                       long amountYouPay= Long.parseLong(input.getText().toString().trim());
                       LoanPayment loanPayment=new LoanPayment(getUniqueId(), loanId, getFormatedDate(new Date()), amountYouPay,0);
                       FirebaseDatabase database;
                       database = FirebaseDatabase.getInstance();
                       database.getReference("loanPayment").push().setValue(loanPayment);
                       MyNotification notice=new MyNotification(email2,TREASURER,"Loan Payment Need To Be approved", NOTICE_TYPE_LOANP,amountYouPay,getFormatedDate(new Date()),loanId, getUniqueId());
                       database.getReference("notifications").push().setValue(notice);
                   }catch (Exception e){
                       Toast.makeText(getBaseContext(),"Invalid Amount"+e.getMessage(),Toast.LENGTH_SHORT).show();
                       return;
                   }
               }
           });
           builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
               }
           }).show();
       } else {
           Toast.makeText(getBaseContext(), "Contact Treasurer", Toast.LENGTH_SHORT).show();
       }

   }

}

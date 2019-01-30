package com.example.qtfison.h2gapp.loans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.members.ItemClickListener;

public class LoanRecyclerViewHolder extends RecyclerView.ViewHolder{

    TextView txt_email, txt_rest_days, txt_loan_paid,txt_perc_paid,txt_perc_unpaid,txt_amountTake,txt_takenDate,txt_paymentDuedate,txt_interest,txt_isFullpaid,txt_reasons,txt_amount2Paid,txt_amountRest;
    ImageView img_menu_popup;
    ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public LoanRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
       // txt_email=itemView.findViewById(R.id.txt_expense);
       // txt_loanId=itemView.findViewById(R.id.txt_payment_type);
        txt_amountTake=itemView.findViewById(R.id.txt_loan_amount);
        txt_takenDate= itemView.findViewById(R.id.txt_get_date);
        txt_paymentDuedate=itemView.findViewById(R.id.txt_due_date);
        txt_interest=itemView.findViewById(R.id.txt_amount_interest);
        txt_isFullpaid=itemView.findViewById(R.id.txt_paid_ok);
        txt_reasons=itemView.findViewById(R.id.txt_loan_reason);
        txt_amount2Paid=itemView.findViewById(R.id.txt_total_paid);
        txt_amountRest=itemView.findViewById(R.id.txt_loan_unpaid);
        img_menu_popup=itemView.findViewById(R.id.id_popup_menu);
        txt_perc_paid=itemView.findViewById(R.id.txt_perc_paid);
        txt_perc_unpaid=itemView.findViewById(R.id.txt_perc_unpaid);
        txt_loan_paid=itemView.findViewById(R.id.txt_loan_paid);
        txt_rest_days=itemView.findViewById(R.id.txt_rest_days);

    }
}

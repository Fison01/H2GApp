package com.example.qtfison.h2gapp.Payment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.members.ItemClickListener;

public class PaymentSummaryRecycleViewHolder extends RecyclerView.ViewHolder {
    TextView txt_paymentType,txt_start_date,txt_end_date,paid_on,txt_paid_status,txt_Amount,txt_pid,txt_unpaid,txt_paid,txt_total;
    ImageView img_menu_popup,img_paid,img_unpaid;
    ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public PaymentSummaryRecycleViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_Amount=itemView.findViewById(R.id.txt_expense);
        txt_paymentType=itemView.findViewById(R.id.txt_payment_type);
        txt_start_date=itemView.findViewById(R.id.txt_start_date);
        txt_end_date= itemView.findViewById(R.id.txt_end_date);
        paid_on=itemView.findViewById(R.id.txt_payment_date);
        txt_paid_status=itemView.findViewById(R.id.txt_paid_ok);
        txt_pid=itemView.findViewById(R.id.txt_pid);
        img_paid=itemView.findViewById(R.id.img_paid);
        img_unpaid=itemView.findViewById(R.id.img_unpaid);
        txt_paid=itemView.findViewById(R.id.txt_paid);
        txt_unpaid=itemView.findViewById(R.id.txt_unpaid);
        txt_total=itemView.findViewById(R.id.txt_total);
        img_menu_popup=itemView.findViewById(R.id.id_popup_menu);
    }
}

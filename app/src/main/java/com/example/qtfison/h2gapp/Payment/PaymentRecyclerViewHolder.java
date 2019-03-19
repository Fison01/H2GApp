package com.example.qtfison.h2gapp.Payment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.members.ItemClickListener;

public class PaymentRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_paymentType,txt_start_date,txt_end_date,paid_on,txt_paid_status,txt_Amount,txt_pid,txtRounded1,txtRounded2;
    public ImageView img_menu_popup,paid_status,img_receipt;
    public ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public PaymentRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_Amount=(TextView) itemView.findViewById(R.id.txt_expense);
        txt_paymentType=(TextView) itemView.findViewById(R.id.txt_payment_type);
        txt_start_date=(TextView)itemView.findViewById(R.id.txt_start_date);
        txt_end_date=(TextView) itemView.findViewById(R.id.txt_end_date);
        paid_on=(TextView)itemView.findViewById(R.id.txt_payment_date);
        txt_paid_status=(TextView)itemView.findViewById(R.id.txt_paid_ok);
        txt_pid=(TextView)itemView.findViewById(R.id.txt_pid);
        img_menu_popup=(ImageView)itemView.findViewById(R.id.id_popup_menu);
        paid_status=(ImageView)itemView.findViewById(R.id.imageView11);
        txtRounded1=itemView.findViewById(R.id.txt_Rounded1);
        txtRounded2=itemView.findViewById(R.id.txtRound2);
        img_receipt=itemView.findViewById(R.id.img_receipt);
    }
}

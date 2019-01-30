package com.example.qtfison.h2gapp.loans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.members.ItemClickListener;

public class LoanPaymentViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_amount_paid,txt_date,txt_amount_left;
    public ImageView img_stutus,img_menu;
    public ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public LoanPaymentViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_amount_paid=itemView.findViewById(R.id.txt_amount_paid);
        txt_date=itemView.findViewById(R.id.txt_date);
        txt_amount_left=itemView.findViewById(R.id.txt_amount_left);
        img_menu=itemView.findViewById(R.id.img_menu);
        img_stutus=itemView.findViewById(R.id.img_stutus);
    }
}

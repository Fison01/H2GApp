package com.example.qtfison.h2gapp.Classes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.members.ItemClickListener;

public class MyNotificationViewHolder extends RecyclerView.ViewHolder {
   public TextView txt_email,txt_Descr,txt_amount,txt_date;
    public ImageView img_notice;
    public  ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public MyNotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_email=itemView.findViewById(R.id.txt_email);
        txt_Descr=itemView.findViewById(R.id.txt_Descr);
        txt_amount=itemView.findViewById(R.id.txt_amount);
        txt_date= itemView.findViewById(R.id.txt_date);
        img_notice=itemView.findViewById(R.id.img_notice);
    }
}

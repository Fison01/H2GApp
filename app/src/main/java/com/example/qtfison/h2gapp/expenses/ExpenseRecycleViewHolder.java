package com.example.qtfison.h2gapp.expenses;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.members.ItemClickListener;

public class ExpenseRecycleViewHolder extends RecyclerView.ViewHolder {
    TextView txt_expense,txt_amount,txt_date,txt_to,txt_description,txt_isreleased;
    ImageView img_menu_popup;
    ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public ExpenseRecycleViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_amount=itemView.findViewById(R.id.txt_exp_amount);
        txt_expense=itemView.findViewById(R.id.txt_expense);
        txt_date=itemView.findViewById(R.id.txt_date);
        txt_to=itemView.findViewById(R.id.txt_to);
        txt_description=itemView.findViewById(R.id.txt_description);
        txt_isreleased=itemView.findViewById(R.id.txt_isreleased);
        img_menu_popup=itemView.findViewById(R.id.id_popup_menu);
    }
}

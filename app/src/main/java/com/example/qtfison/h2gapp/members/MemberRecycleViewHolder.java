package com.example.qtfison.h2gapp.members;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qtfison.h2gapp.R;

public class MemberRecycleViewHolder extends RecyclerView.ViewHolder  {
    TextView txt_fname,txt_lname,txt_address,txt_phone;
    ImageView img_Id,img_issues,img_menu_popup;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MemberRecycleViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_fname= itemView.findViewById(R.id.txt_fname);
        txt_lname=itemView.findViewById(R.id.txt_lname);
        txt_address= itemView.findViewById(R.id.txt_address);
        txt_phone=itemView.findViewById(R.id.txt_phone);
        img_Id= itemView.findViewById(R.id.img_ID);
        img_issues= itemView.findViewById(R.id.img_issues);
        img_menu_popup=itemView.findViewById(R.id.id_popup_menu);
    }
}

package com.example.qtfison.h2gapp.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.amulyakhare.textdrawable.TextDrawable;
import com.example.qtfison.h2gapp.Configs;
import com.example.qtfison.h2gapp.R;
import com.example.qtfison.h2gapp.members.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static com.example.qtfison.h2gapp.Configs.ADMIN;
import static com.example.qtfison.h2gapp.Configs.userRole;

public class UtilFunctions {
    public UtilFunctions() {
    }
    public static String getUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    public static String getFormatedDate(Date date) {
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return  df.format(date);
        }catch (Exception e){
            return null;
        }
    }
    public static boolean isUserAllowed(String authorizedRole,Context context){
        Configs.USERLOGINROLE=PreferenceManager.getDefaultSharedPreferences(context).getString(userRole,"null");
         String role=Configs.USERLOGINROLE;
        if(role.equalsIgnoreCase(authorizedRole)||role.equalsIgnoreCase(ADMIN)){ return true; }else { return false; }
    }
    public static boolean isUserAllowed(String authorizedRole1,String authorizedRole2,Context context){
        Configs.USERLOGINROLE=PreferenceManager.getDefaultSharedPreferences(context).getString(userRole,"null");
        String role=Configs.USERLOGINROLE;
        if(role.equalsIgnoreCase(authorizedRole1)|| role.equalsIgnoreCase(authorizedRole2)||role.equalsIgnoreCase(ADMIN)){ return true; }else { return false; }
    }
    public static Date getConvertedDateformat1(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date mDate = sdf.parse(date);
            return mDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getConvertedDateformat2(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            String mDate = sdf.format(date);
            return mDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static long getDifferenceBetweenDate(Date date1,Date date2){
        long diff = date1.getTime() - date2.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return days;
    }
    public static boolean isValidDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        }catch (ParseException e){
            return false;
        }
        if (!sdf.format(testDate).equals(date))
        {
            return false;
        }
        return true;
    }
    public static String getLoginEmail(Context context){
        final SharedPreferences sharedPref = context.getSharedPreferences( "preference_file_key", Context.MODE_PRIVATE);
        return sharedPref.getString("login_user_email", null);
    }
    public static int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    public static int getInterestPercentege(){
        return 5;
    }

    public static void displayNotification(View v, final Context context) {
        RecyclerView recyclerView=v.findViewById(R.id.recycleVNotice);
        FirebaseRecyclerAdapter<MyNotification, MyNotificationViewHolder> adapter;
        FirebaseRecyclerOptions<MyNotification> options;
        options = new FirebaseRecyclerOptions.Builder<MyNotification>()
                .setQuery(FirebaseDatabase.getInstance().getReference("notifications") , MyNotification.class)
                .build();
        adapter= new FirebaseRecyclerAdapter<MyNotification, MyNotificationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyNotificationViewHolder holder, int position, @NonNull final MyNotification model) {
                holder.txt_email.setText(model.getFromEmail());
                holder.txt_Descr.setText(model.getNotifDesc());
                holder.txt_amount.setText(""+model.getAmount()+ "Rwf");
                holder.txt_date.setText(model.getDate());
                if(model.getNoticeType().equalsIgnoreCase("p")){
                     TextDrawable drawable = TextDrawable.builder() .buildRound("P", Color.parseColor("#6eb4ad"));
                     holder.img_notice.setImageDrawable(drawable);
                }else if(model.getNoticeType().equalsIgnoreCase("L")) {
                    TextDrawable drawable = TextDrawable.builder() .buildRound("P", Color.parseColor("#6eb4ad"));
                    holder.img_notice.setImageDrawable(drawable);
                }else{
                    TextDrawable drawable = TextDrawable.builder() .buildRound("R", Color.parseColor("#6eb4ad"));
                    holder.img_notice.setImageDrawable(drawable);
                }
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                });
            }
            @NonNull
            @Override
            public MyNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView=LayoutInflater.from(context).inflate(R.layout.notification_items,viewGroup,false);
                return new MyNotificationViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}

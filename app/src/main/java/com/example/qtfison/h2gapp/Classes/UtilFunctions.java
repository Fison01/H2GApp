package com.example.qtfison.h2gapp.Classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qtfison.h2gapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
    public static boolean isUserAllowed(Context context,String authorizedRole){
        final SharedPreferences sharedPref = context.getSharedPreferences( "preference_file_key", Context.MODE_PRIVATE);
        final String role=sharedPref.getString("login_user_role", null);
        System.out.println("Rooolee: "+role);
        if(role.equalsIgnoreCase(authorizedRole)||role.equalsIgnoreCase("Admin")){ return true; }else { return false; }
    }
    public static boolean isUserAllowed(Context context,String authorizedRole1,String authorizedRole2){
        final SharedPreferences sharedPref = context.getSharedPreferences( "preference_file_key", Context.MODE_PRIVATE);
        final String role=sharedPref.getString("login_user_role", null);
            System.out.println("Rooolee: "+role);
        if(role.equalsIgnoreCase(authorizedRole1)|| role.equalsIgnoreCase(authorizedRole2)||role.equalsIgnoreCase("Admin")){ return true; }else { return false; }
    }
    public static boolean isForYourEmailId(Context context,String email){
        final SharedPreferences sharedPref = context.getSharedPreferences( "preference_file_key", Context.MODE_PRIVATE);
        final String SharedPrefEmail=sharedPref.getString("login_user_email", null);
        if(SharedPrefEmail.equalsIgnoreCase(email)){
            return true;
        }else {
            return false;
        }
    }
    public static Date getConvertedDate(String data){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        try {
            Date mDate = sdf.parse(data);
            long timeInMilliseconds = mDate.getTime();
            return mDate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
}

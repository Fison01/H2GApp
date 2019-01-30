package com.example.qtfison.h2gapp;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;

public class Configs {
    public static final int PANDING=2;
    public static final int APPROVED=1;
    public static final int DENIED=-1;
    public static final int NOT_YET=0;
    public static final String userRole="userRole";
    public static final String preferenceFile="preference_file_key";
    public static final String TREASURER="Treasurer";
    public static final String ADMIN="Admin";
    public static final String cordinator="Cordinator";
    public static final String NOTICE_TYPE_CONTRP="1";// contribition paid
    public static final String NOTICE_TYPE_RELEASEDP="2";// released payment
    public static final String NOTICE_TYPE_LOANP="3";// loan Payment
    public static final String MEMBER = "Member";
    public static  Context context=FirebaseAuth.getInstance().getApp().getApplicationContext();
    public static String USERLOGINROLE=PreferenceManager.getDefaultSharedPreferences(context).getString(userRole,null);
    public static  String LOGINUSEREMAIL=FirebaseAuth.getInstance().getCurrentUser().getEmail();
}

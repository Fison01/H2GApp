package com.example.qtfison.h2gapp;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.qtfison.h2gapp.Classes.MyFireBaseFuctions;
import com.example.qtfison.h2gapp.Classes.MyNotification;
import com.example.qtfison.h2gapp.Payment.PaymentFragment;
import com.example.qtfison.h2gapp.Payment.PaymentReleasedActivity;
import com.example.qtfison.h2gapp.Users.LoginActivity;
import com.example.qtfison.h2gapp.account.AccountFragment;
import com.example.qtfison.h2gapp.expenses.ExpensesFragment;
import com.example.qtfison.h2gapp.members.MemberFragment;
import com.example.qtfison.h2gapp.members.Registration;
import com.example.qtfison.h2gapp.notifications.MyNotificationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.qtfison.h2gapp.Classes.UtilFunctions.isUserAllowed;
import static com.example.qtfison.h2gapp.Configs.LOGINUSEREMAIL;
import static com.example.qtfison.h2gapp.Configs.NOTICE_TYPE_CONTRP;
import static com.example.qtfison.h2gapp.Configs.NOTICE_TYPE_LOANP;
import static com.example.qtfison.h2gapp.Configs.TREASURER;
import static com.example.qtfison.h2gapp.Configs.USERLOGINROLE;
import static com.example.qtfison.h2gapp.Configs.userRole;


public class MainActivity extends AppCompatActivity implements MemberFragment.OnFragmentInteractionListener,
                                                                PaymentFragment.OnFragmentInteractionListener,
                                                                ExpensesFragment.OnFragmentInteractionListener,
                                                                AccountFragment.OnFragmentInteractionListener {
    Fragment fragment;
    FirebaseAuth mAuth;
    FragmentManager manager;
    static String a;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_member:
                    fragment = new MemberFragment();
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
                    return true;
                case R.id.navigation_payment:
                    fragment = new PaymentFragment();
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
                    return true;
                case R.id.navigation_meeting:
                    Toast.makeText(getBaseContext(),"Under Developement",Toast.LENGTH_SHORT).show();
                    //fragment = new ExpensesFragment();
                    //manager = getSupportFragmentManager();
                    // manager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
                    //return true;
                case R.id.navigation_account:
                    Toast.makeText(getBaseContext(),"Under Developement",Toast.LENGTH_SHORT).show();
                    // fragment = new AccountFragment();
                    // manager = getSupportFragmentManager();
                    //  manager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
                    // return true;
            }

            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        displayNotification();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragment = new MemberFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
        String s = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(userRole, null);
        if (s == null) {
            Intent intent = new Intent(getBaseContext(), Registration.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final ImageView img = new ImageView(getBaseContext());

        FirebaseDatabase.getInstance().getReference("notifications").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Object data= dataSnapshot.child("fromEmail").getValue();
                if(data!=null) {
                    if (data.toString().equalsIgnoreCase(LOGINUSEREMAIL) || isUserAllowed(TREASURER, getBaseContext())) {
                        a = "P";
                        TextDrawable drawable = TextDrawable.builder().buildRound("" + a, Color.RED);
                        img.setImageDrawable(drawable);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        img.setMinimumHeight(45);
        img.setMinimumWidth(45);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MyNotificationActivity.class);
                startActivity(i);
            }
        });
        menu.add(0, 0, 1, "notify").setActionView(img).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                signOut();
                return true;
            case R.id.menu_apply_loan:
                // Intent i = new Intent(getBaseContext(), ApplyLoanActivity.class);
                //startActivity(i);
                Toast.makeText(getBaseContext(),"Under Developement",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_add_payment_needed:
                if(isUserAllowed(TREASURER,getBaseContext())) {
                    Intent a = new Intent(getBaseContext(), PaymentReleasedActivity.class);
                    startActivity(a);
                    return true;
                }else{
                    Toast.makeText(getBaseContext(),"Contact Cordinator Or Secretary",Toast.LENGTH_SHORT).show();
                    return true;
                }
            case R.id.menu_psw_reset:
                LOGINUSEREMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                MyFireBaseFuctions.sendPasswordReset(LOGINUSEREMAIL, getBaseContext());
                return true;
            case  R.id.menu_expese_new:
                Toast.makeText(getBaseContext(),"Under Developement",Toast.LENGTH_SHORT).show();
            default:
        }
        return false;
    }

    private void signOut() {
        try {
            mAuth.signOut();
            updateUI();
            startActivity(new Intent(getBaseContext(),LoginActivity.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateUI() {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final SharedPreferences.Editor editor = sharedPref.edit();
        // mAuth.signOut();
        editor.putString(userRole, null);
        editor.apply();
        editor.commit();
    }

    public void displayNotification() {
        if(isUserAllowed(TREASURER,getBaseContext())) {
            FirebaseDatabase.getInstance().getReference("notifications").orderByChild("toEmail").equalTo(TREASURER).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    MyNotification notification = dataSnapshot.getValue(MyNotification.class);
                    if(notification.getNoticeType().equalsIgnoreCase(NOTICE_TYPE_CONTRP)||notification.getNoticeType().equalsIgnoreCase(NOTICE_TYPE_LOANP))
                    {
                        final NotificationManager NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent i;
                        i = new Intent(getBaseContext(), MyNotificationActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        final PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, i, 0);

                        Notification notify = new Notification.Builder(getBaseContext())
                                .setContentTitle("Please approve or deny")
                                .setContentText("Some payments need your Approval")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentIntent(pendingIntent)
                                .build();
                        NM.notify(0, notify);
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else  {
            LOGINUSEREMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            FirebaseDatabase.getInstance().getReference("notifications").orderByChild("toEmail").equalTo(LOGINUSEREMAIL).addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    MyNotification notification = dataSnapshot.getValue(MyNotification.class);
                    final NotificationManager NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    LOGINUSEREMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    Intent i;
                    i = new Intent(getBaseContext(), MyNotificationActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    final PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, i, 0);
                    Notification notify = new Notification.Builder(getBaseContext())
                            .setContentTitle("Please approve or deny")
                            .setContentText("Some Members need your approval")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .build();
                    NM.notify(0, notify);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}

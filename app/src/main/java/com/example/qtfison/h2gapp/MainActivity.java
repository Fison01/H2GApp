package com.example.qtfison.h2gapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.qtfison.h2gapp.Payment.PaymentFragment;
import com.example.qtfison.h2gapp.account.AccountFragment;
import com.example.qtfison.h2gapp.meeting.MeetingFragment;
import com.example.qtfison.h2gapp.members.MemberFragment;
import com.example.qtfison.h2gapp.members.Registration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements MemberFragment.OnFragmentInteractionListener,
                                                                PaymentFragment.OnFragmentInteractionListener,
                                                                MeetingFragment.OnFragmentInteractionListener,
                                                                AccountFragment.OnFragmentInteractionListener{
    Fragment fragment;
    FirebaseAuth mAuth;
    FragmentManager manager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_member:
                    fragment=new MemberFragment();
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.frame_layout,fragment).commit();
                    return true;
                case R.id.navigation_payment:
                    fragment=new PaymentFragment();
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.frame_layout,fragment).commit();
                    return true;
                case R.id.navigation_meeting:
                    fragment=new MeetingFragment();
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.frame_layout,fragment).commit();
                    return true;
                case R.id.navigation_account:
                    fragment=new AccountFragment();
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.frame_layout,fragment).commit();
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragment=new MemberFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout,fragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu ){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                signOut();
                return true;
            default:
        }
        return false;
    }
    private void signOut() {
        mAuth.signOut();
        updateUI(null);
        finish();
    }
    private void updateUI(FirebaseUser user) {
        //final SharedPreferences sharedPref = getBaseContext().getSharedPreferences( getString(R.string.preference_file_key), Context.MODE_PRIVATE);
       // final SharedPreferences.Editor editor = sharedPref.edit();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUser=user;
    }
}

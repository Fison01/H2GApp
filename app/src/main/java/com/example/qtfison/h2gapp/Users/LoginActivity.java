package com.example.qtfison.h2gapp.Users;


import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qtfison.h2gapp.Classes.MyFireBaseFuctions;
import com.example.qtfison.h2gapp.Configs;
import com.example.qtfison.h2gapp.MainActivity;
import com.example.qtfison.h2gapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.example.qtfison.h2gapp.Configs.userRole;

public class LoginActivity extends AppCompatActivity   {
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mForgetPw;
    private static Class activityClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailField = findViewById(R.id.txt_Email);
        mPasswordField = findViewById(R.id.txt_Password);
        mForgetPw = findViewById(R.id.txt_forgetpw);

        findViewById(R.id.emailSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mEmailField.getText().toString().trim(),mPasswordField.getText().toString());
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(LoginActivity.this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mForgetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Enter your H2G registered email");
                final EditText input = new EditText(LoginActivity.this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
                builder.setView(input);
                builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
                    String m_Text;
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if (TextUtils.isEmpty(m_Text)||!android.util.Patterns.EMAIL_ADDRESS.matcher(m_Text).matches()) {
                          Toast.makeText(getBaseContext(),"Invalid email",Toast.LENGTH_SHORT).show();
                        } else {
                            MyFireBaseFuctions.sendPasswordReset(m_Text,getBaseContext());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent=new Intent(getBaseContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
       // updateUI(currentUser);
    }
    private void updateUI(final FirebaseUser user) {
       // System.out.println("kkkkk :"+user.getEmail());
        if(user==null){return;}
        FirebaseDatabase database;
        DatabaseReference myDatabaseRef;
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference("members");
        myDatabaseRef.orderByChild("email").equalTo(user.getEmail())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences( getBaseContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(userRole, dataSnapshot.child("role").getValue().toString());
                        editor.apply();
                        editor.commit();
                        Configs.LOGINUSEREMAIL=user.getEmail();
                        Configs.USERLOGINROLE=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(userRole,null);
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

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
        mProgress.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUI(mAuth.getCurrentUser());
                            mProgress.dismiss();
                            activityClass=MainActivity.class;
                            Intent intent=new Intent(getBaseContext(),activityClass);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mProgress.dismiss();
                    Toast.makeText(getBaseContext(),("Invalid password"),Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    mProgress.dismiss();
                    Toast.makeText(getBaseContext(),"Incorrect email address",Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.dismiss();
                    Toast.makeText(getBaseContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }});
    }
    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)||!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailField.setError("Invalid Email.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        return valid;
    }


}

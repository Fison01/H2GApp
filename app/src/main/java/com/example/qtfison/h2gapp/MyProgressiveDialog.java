package com.example.qtfison.h2gapp;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressiveDialog {
        private ProgressDialog mProgress;
        Context context;
       public MyProgressiveDialog(Context context) {
            this.context=context;
            mProgress = new ProgressDialog(context);
            mProgress.setTitle("Processing...");
            mProgress.setMessage("Please wait...");
            mProgress.setCancelable(false);
            mProgress.setIndeterminate(true);
        }
        public void showPregress(){
           mProgress.show();
        }
        public void hideprogress(){
           mProgress.dismiss();
        }
}

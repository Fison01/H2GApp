package com.example.qtfison.h2gapp.uploading;

public class Upload {
    private String mName;
    private String mImageUri;

    public Upload() {
    }

    public Upload(String mName, String mImageUri) {
        if(mName.trim().equals(""))
            mName="no name";
        this.mName = mName;
        this.mImageUri = mImageUri;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }
}

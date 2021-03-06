package com.example.qtfison.h2gapp.Payment;

public class PaymentReleased {

    private String releasedOnId;
    private String email;
    private int isPaid;
    private String paidOn;
    private String imageReciept;
    public PaymentReleased() {
    }

    public PaymentReleased( String releasedOnId, String email, int isPaid, String paidOn,String imageReciept) {
        this.releasedOnId = releasedOnId;
        this.email = email;
        this.isPaid = isPaid;
        this.paidOn = paidOn;
        this.imageReciept=imageReciept;
    }


    public String getReleasedOnId() {
        return releasedOnId;
    }

    public void setReleasedOnId(String releasedOnId) {
        this.releasedOnId = releasedOnId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public String getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(String paidOn) {
        this.paidOn = paidOn;
    }

    public String getImageReciept() {
        return imageReciept;
    }

    public void setImageReciept(String imageReciept) {
        this.imageReciept = imageReciept;
    }
}
package com.example.qtfison.h2gapp.Payment;

public class PaymentNeeded {
   private String paymentNeededid;
   private String paymentType,startDate,endingDate;
   private long amount;

    public PaymentNeeded() {
    }

    public PaymentNeeded(String paymentNeededid, String paymentType, String startDate, String endingDate, long amount) {
        this.paymentNeededid = paymentNeededid;
        this.paymentType = paymentType;
        this.startDate = startDate;
        this.endingDate = endingDate;
        this.amount = amount;
    }

    public String getPaymentNeededid() {
        return paymentNeededid;
    }

    public void setPaymentNeededid(String paymentNeededid) {
        this.paymentNeededid = paymentNeededid;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}

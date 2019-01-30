package com.example.qtfison.h2gapp.loans;

public class LoanPayment {
   private String paymentId;
   private String loanId;
   private String date;
   private long amountPay;
   private  int approved;

    public LoanPayment() {
    }

    public LoanPayment(String paymentId, String loanId, String date, long amountPay,int approved) {
        this.paymentId = paymentId;
        this.loanId = loanId;
        this.date = date;
        this.amountPay = amountPay;
        this.approved=approved;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getAmountPay() {
        return amountPay;
    }

    public void setAmountPay(long amountPay) {
        this.amountPay = amountPay;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }
}

package com.example.qtfison.h2gapp.Payment;

public class PaymentNeeded {
   private String paymentNeededid;
   private String paymentType,startDate,endingDate;
   private long amount,total;
   int nbrMembers,nbrPaid;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getNbrMembers() {
        return nbrMembers;
    }

    public void setNbrMembers(int nbrMembers) {
        this.nbrMembers = nbrMembers;
    }

    public PaymentNeeded() {


    }

    public PaymentNeeded(String paymentNeededid, String paymentType, String startDate, String endingDate, long amount,long total,int nbrMembers,int nbrPaid) {
        this.paymentNeededid = paymentNeededid;
        this.paymentType = paymentType;
        this.startDate = startDate;
        this.endingDate = endingDate;
        this.amount = amount;
        this.total=total;
        this.nbrMembers=nbrMembers;
        this.nbrPaid=nbrPaid;
    }

    public String getPaymentNeededid() {
        return paymentNeededid;
    }

    public int getNbrPaid() {
        return nbrPaid;
    }

    public void setNbrPaid(int nbrPaid) {
        this.nbrPaid = nbrPaid;
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

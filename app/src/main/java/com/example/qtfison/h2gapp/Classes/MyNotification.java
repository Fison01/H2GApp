package com.example.qtfison.h2gapp.Classes;

public class MyNotification {

    String fromEmail;
    String toEmail;
    String notifDesc;
    String noticeType;
    long   amount;
    String date; //
    String transactionId;
    String notifId;

    public MyNotification() {
    }
    public MyNotification(String fromEmail,String toEmail, String notifDesc, String noticeType, long amount, String date, String transactionId, String notifId) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.notifDesc = notifDesc;
        this.noticeType = noticeType;
        this.amount = amount;
        this.date = date;
        this.transactionId = transactionId;
        this.notifId = notifId;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getNotifDesc() {
        return notifDesc;
    }

    public void setNotifDesc(String notifDesc) {
        this.notifDesc = notifDesc;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getNotifId() {
        return notifId;
    }

    public void setNotifId(String notifId) {
        this.notifId = notifId;
    }
}

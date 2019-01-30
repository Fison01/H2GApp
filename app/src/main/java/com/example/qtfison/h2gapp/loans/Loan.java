package com.example.qtfison.h2gapp.loans;

public class Loan {
    private String myEmail,loanId,takenDate,paymentDuedate,reasons,member1,member2,member3;
    private long amountTake,amountPaid,amountRest,interest;
    int isFullpaid;

    public Loan() {

    }

    public Loan(String myEmail, String loanId, String takenDate, String paymentDuedate, long interest, String reasons, String member1, String member2, String member3, long amountTake, long amountPaid, long amountRest, int isFullpaid) {
        this.myEmail = myEmail;
        this.loanId = loanId;
        this.takenDate = takenDate;
        this.paymentDuedate = paymentDuedate;
        this.interest = interest;
        this.reasons = reasons;
        this.member1 = member1;
        this.member2 = member2;
        this.member3 = member3;
        this.amountTake = amountTake;
        this.amountPaid = amountPaid;
        this.amountRest = amountRest;
        this.isFullpaid = isFullpaid;
    }

    public String getMyEmail() {
        return myEmail;
    }

    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(String takenDate) {
        this.takenDate = takenDate;
    }

    public String getPaymentDuedate() {
        return paymentDuedate;
    }

    public void setPaymentDuedate(String paymentDuedate) {
        this.paymentDuedate = paymentDuedate;
    }

    public long getInterest() {
        return interest;
    }

    public void setInterest(long interest) {
        this.interest = interest;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public String getMember1() {
        return member1;
    }

    public void setMember1(String member1) {
        this.member1 = member1;
    }

    public String getMember2() {
        return member2;
    }

    public void setMember2(String member2) {
        this.member2 = member2;
    }

    public String getMember3() {
        return member3;
    }

    public void setMember3(String member3) {
        this.member3 = member3;
    }

    public long getAmountTake() {
        return amountTake;
    }

    public void setAmountTake(long amountTake) {
        this.amountTake = amountTake;
    }

    public long getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(long amountPaid) {
        this.amountPaid = amountPaid;
    }

    public long getAmountRest() {
        return amountRest;
    }

    public void setAmountRest(long amountRest) {
        this.amountRest = amountRest;
    }

    public int getIsFullpaid() {
        return isFullpaid;
    }

    public void setIsFullpaid(int isFullpaid) {
        this.isFullpaid = isFullpaid;
    }
}

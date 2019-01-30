package com.example.qtfison.h2gapp.expenses;

public class Expense {
  private   String expenseName,expenseReleaseddate,expenseTo,expenseId,expenseDescription;
  private  int isReleased;
  private   Long expeseAmount;
    public Expense() {
    }

    public Expense(String expenseName, String expenseReleaseddate, String expenseTo, String expenseId, String expenseDescription, int isReleased, Long expeseAmount) {
        this.expenseName = expenseName;
        this.expenseReleaseddate = expenseReleaseddate;
        this.expenseTo = expenseTo;
        this.expenseId = expenseId;
        this.expenseDescription = expenseDescription;
        this.isReleased = isReleased;
        this.expeseAmount = expeseAmount;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getExpenseReleaseddate() {
        return expenseReleaseddate;
    }

    public void setExpenseReleaseddate(String expenseReleaseddate) {
        this.expenseReleaseddate = expenseReleaseddate;
    }

    public String getExpenseTo() {
        return expenseTo;
    }

    public void setExpenseTo(String expenseTo) {
        this.expenseTo = expenseTo;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public int getIsReleased() {
        return isReleased;
    }

    public void setIsReleased(int isReleased) {
        this.isReleased = isReleased;
    }

    public Long getExpeseAmount() {
        return expeseAmount;
    }

    public void setExpeseAmount(Long expeseAmount) {
        this.expeseAmount = expeseAmount;
    }
}

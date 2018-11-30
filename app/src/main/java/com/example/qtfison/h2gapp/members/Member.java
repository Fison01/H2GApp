package com.example.qtfison.h2gapp.members;

import java.util.Date;

public class Member {
    private String fName, lName, gender, status, email, phone;
   private String address,rank, deployment, jobLocation,nID,marriedWith,marriedPhone,role;
   private String entryDate,marriageDate,DOB;
    public Member() {
    }
    public Member(String fName, String lName, String gender,  String status, String email, String phone, String address, String rank, String deployment, String jobLocation, String nID, String marriedWith, String marriedPhone, String role, String entryDate, String marriageDate,String DOB) {

        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.status = status;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.rank = rank;
        this.deployment = deployment;
        this.jobLocation = jobLocation;
        this.nID = nID;
        this.marriedWith = marriedWith;
        this.marriedPhone = marriedPhone;
        this.role =role;
        this.entryDate = entryDate;
        this.marriageDate = marriageDate;
        this.DOB=DOB;
    }


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

      public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getDeployment() {
        return deployment;
    }

    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getnID() {
        return nID;
    }

    public void setnID(String nID) {
        this.nID = nID;
    }

    public String getMarriedWith() {
        return marriedWith;
    }

    public void setMarriedWith(String marriedWith) {
        this.marriedWith = marriedWith;
    }

    public String getMarriedPhone() {
        return marriedPhone;
    }

    public void setMarriedPhone(String marriedPhone) {
        this.marriedPhone = marriedPhone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(String marriageDate) {
        this.marriageDate = marriageDate;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
}

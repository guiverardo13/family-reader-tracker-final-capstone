package com.techelevator.model;

public class Family {
    private int familyId;
    private String familyName;

    public Family(int familyId, String familyName) {
        this.familyId = familyId;
        this.familyName = familyName;
    }

    public int getFamilyId() {
        return familyId;
    }


    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}

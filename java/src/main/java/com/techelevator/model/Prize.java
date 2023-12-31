package com.techelevator.model;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.util.Date;

public class Prize {
    private int prizeId;
    private int familyId;
    private String name;
    private String description;
    private boolean milestone;
    private String userGroup;
    private LocalDate startDate;
    private LocalDate endDate;
    private int goal;
    private String goalType;
    public Prize() {

    }

    public Prize(int prizeId, int familyId, String name, String description, String userGroup, LocalDate startDate, LocalDate endDate, int goal, String goalType) {
        this.prizeId = prizeId;
        this.familyId = familyId;
        this.name = name;
        this.description = description;
        this.milestone = false;
        this.userGroup = userGroup;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goal = goal;
        this.goalType = goalType;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMilestone() {
        return milestone;
    }

    public void setMilestone(boolean milestone) {
        this.milestone = milestone;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getGoal(){
        return goal;
    }

    public void setGoal(int goal){
        this.goal = goal;
    }
}

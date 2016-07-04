package com.maudit.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohankumar on 1/20/2016.
 */
public class SectionModel {
    Context context;
    int sectionID,subsectionID,SectionResult,isSectionAssigned;

    public int getSectionResult() {
        return SectionResult;
    }

    public void setSectionResult(int sectionResult) {
        SectionResult = sectionResult;
    }

    String  userType,sectionTitle,resultStatus,scheduleAnswerID,scheduleAnswerTypeID,qAnswerValue,
            sectionAbbr;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getSubsectionID() {
        return subsectionID;
    }

    public void setSubsectionID(int subsectionID) {
        this.subsectionID = subsectionID;
    }


    public SubSectionModel subSectionModel;
    public List<SubSectionModel> subSectionlist=new ArrayList<>();
    public SubSectionModel getSubSectionModel() {
        return subSectionModel;
    }

    public void setSubSectionModel(SubSectionModel subSectionModel) {
        subSectionlist.add(subSectionModel);
        this.subSectionModel = subSectionModel;
    }



    public Integer getIsSectionAssigned() {
        return isSectionAssigned;
    }

    public void setIsSectionAssigned(Integer isSectionAssigned) {
        this.isSectionAssigned = isSectionAssigned;
    }



    public String getqAnswerValue() {
        return qAnswerValue;
    }

    public void setqAnswerValue(String qAnswerValue) {
        this.qAnswerValue = qAnswerValue;
    }



    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getScheduleAnswerID() {
        return scheduleAnswerID;
    }

    public void setScheduleAnswerID(String scheduleAnswerID) {
        this.scheduleAnswerID = scheduleAnswerID;
    }

    public String getScheduleAnswerTypeID() {
        return scheduleAnswerTypeID;
    }

    public void setScheduleAnswerTypeID(String scheduleAnswerTypeID) {
        this.scheduleAnswerTypeID = scheduleAnswerTypeID;
    }



    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }


    public String getSectionAbbr() {
        return sectionAbbr;
    }

    public void setSectionAbbr(String sectionAbbr) {
        this.sectionAbbr = sectionAbbr;
    }


}

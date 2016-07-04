package com.maudit.model;

/**
 * Created by Mohankumar on 2/3/2016.
 */
public class AnswersModel {
    int scheduleAnswerID,isSectionAssigned,scheduleAnswerSectionID,scheduleAnswerTypeID,scheduleQuestionID,isChecked;
    String qAnswerValue,answerColor;
    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    public int getScheduleAnswerSectionID() {
        return scheduleAnswerSectionID;
    }

    public void setScheduleAnswerSectionID(int scheduleAnswerSectionID) {
        this.scheduleAnswerSectionID = scheduleAnswerSectionID;
    }

    public String getAnswerColor() {
        return answerColor;
    }

    public void setAnswerColor(String answerColor) {
        this.answerColor = answerColor;
    }



    public int getScheduleQuestionID() {
        return scheduleQuestionID;
    }

    public void setScheduleQuestionID(int scheduleQuestionID) {
        this.scheduleQuestionID = scheduleQuestionID;
    }


    public int getIsSectionAssigned() {
        return isSectionAssigned;
    }

    public void setIsSectionAssigned(int isSectionAssigned) {
        this.isSectionAssigned = isSectionAssigned;
    }

    public String getqAnswerValue() {
        return qAnswerValue;
    }

    public void setqAnswerValue(String qAnswerValue) {
        this.qAnswerValue = qAnswerValue;
    }

    public int getScheduleAnswerID() {
        return scheduleAnswerID;
    }

    public void setScheduleAnswerID(int scheduleAnswerID) {
        this.scheduleAnswerID = scheduleAnswerID;
    }

    public int getScheduleAnswerTypeID() {
        return scheduleAnswerTypeID;
    }

    public void setScheduleAnswerTypeID(int scheduleAnswerTypeID) {
        this.scheduleAnswerTypeID = scheduleAnswerTypeID;
    }



}

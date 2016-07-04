package com.maudit.model;

/**
 *
 */
public class AnswerColumnsModel {
    String scheduleAnsOptions,scheduleAnsOptionValue,answerColor;
    int scheduleAnswerTypeID,scheduleChecklistID,answerColumnCount,scheduleQuestionID,isChecked,setScheduleAnswerSectionID,setIsSectionAssigned,
            scheduleAnswerID,sectionID, subSectionID;

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public int getSubSectionID() {
        return subSectionID;
    }

    public void setSubSectionID(int subSectionID) {
        this.subSectionID = subSectionID;
    }

    public int getSetIsSectionAssigned() {
        return setIsSectionAssigned;
    }

    public void setSetIsSectionAssigned(int setIsSectionAssigned) {
        this.setIsSectionAssigned = setIsSectionAssigned;
    }

    public int getScheduleAnswerID() {
        return scheduleAnswerID;
    }

    public void setScheduleAnswerID(int scheduleAnswerID) {
        this.scheduleAnswerID = scheduleAnswerID;
    }

    public int getSetScheduleAnswerSectionID() {
        return setScheduleAnswerSectionID;
    }

    public void setSetScheduleAnswerSectionID(int setScheduleAnswerSectionID) {
        this.setScheduleAnswerSectionID = setScheduleAnswerSectionID;
    }


    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }
    public int getScheduleQuestionID() {
        return scheduleQuestionID;
    }

    public void setScheduleQuestionID(int scheduleQuestionID) {
        this.scheduleQuestionID = scheduleQuestionID;
    }

    public int getAnswerColumnCount() {
        return answerColumnCount;
    }

    public void setAnswerColumnCount(int answerColumnCount) {
        this.answerColumnCount = answerColumnCount;
    }


    public String getAnswerColor() {
        return answerColor;
    }

    public void setAnswerColor(String answerColor) {
        this.answerColor = answerColor;
    }

    public String getScheduleAnsOptions() {
        return scheduleAnsOptions;
    }

    public void setScheduleAnsOptions(String scheduleAnsOptions) {
        this.scheduleAnsOptions = scheduleAnsOptions;
    }

    public String getScheduleAnsOptionValue() {
        return scheduleAnsOptionValue;
    }

    public void setScheduleAnsOptionValue(String scheduleAnsOptionValue) {
        this.scheduleAnsOptionValue = scheduleAnsOptionValue;
    }

    public int getScheduleAnswerTypeID() {
        return scheduleAnswerTypeID;
    }

    public void setScheduleAnswerTypeID(int scheduleAnswerTypeID) {
        this.scheduleAnswerTypeID = scheduleAnswerTypeID;
    }

    public int getScheduleChecklistID() {
        return scheduleChecklistID;
    }

    public void setScheduleChecklistID(int scheduleChecklistID) {
        this.scheduleChecklistID = scheduleChecklistID;
    }


}

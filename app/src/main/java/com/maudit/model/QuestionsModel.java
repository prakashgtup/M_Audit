package com.maudit.model;

import java.io.Serializable;

/**
 *
 */
public class QuestionsModel  implements Serializable {
    int scheduleQuestionID, subSectionID, isMandatory, sectionID, isChecked;
    String qAbbrv, questionText, hint, standardORClauses, sequence,remark,sectionAbbr,subSectionAbbr;

    public int getQuestionHeight() {
        return questionHeight;
    }

    public void setQuestionHeight(int questionHeight) {
        this.questionHeight = questionHeight;
    }

    int questionHeight = 0;
    AnswersModel answersModel;

    public String getSubSectionAbbr() {
        return subSectionAbbr;
    }

    public void setSubSectionAbbr(String subSectionAbbr) {
        this.subSectionAbbr = subSectionAbbr;
    }

    public String getSectionAbbr() {
        return sectionAbbr;
    }

    public void setSectionAbbr(String sectionAbbr) {
        this.sectionAbbr = sectionAbbr;
    }

    public AnswersModel getAnswersModel() {
        return answersModel;
    }

    public void setAnswersModel(AnswersModel answersModel) {
        this.answersModel = answersModel;
    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }


    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(int isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getqAbbrv() {
        return qAbbrv;
    }

    public void setqAbbrv(String qAbbrv) {
        this.qAbbrv = qAbbrv;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getScheduleQuestionID() {
        return scheduleQuestionID;
    }

    public void setScheduleQuestionID(int scheduleQuestionID) {
        this.scheduleQuestionID = scheduleQuestionID;
    }


    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getStandardORClauses() {
        return standardORClauses;
    }

    public void setStandardORClauses(String standardORClauses) {
        this.standardORClauses = standardORClauses;
    }

    public int getSubSectionID() {
        return subSectionID;
    }

    public void setSubSectionID(int subSectionID) {
        this.subSectionID = subSectionID;
    }
}

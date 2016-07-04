package com.maudit.model;

/**
 *
 */
public class AnswerSectionsModel {
    String scheduleAnsSection,repeatSectionTitle,sectionValue;
    int scheduleAnswerSectionID,scheduleChecklistID,sectionColumnType;


    public String getSectionValue() {
        return sectionValue;
    }

    public void setSectionValue(String sectionValue) {
        this.sectionValue = sectionValue;
    }


    public String getRepeatSectionTitle() {
        return repeatSectionTitle;
    }

    public void setRepeatSectionTitle(String repeatSectionTitle) {
        this.repeatSectionTitle = repeatSectionTitle;
    }

    public int getSectionColumnType() {
        return sectionColumnType;
    }

    public void setSectionColumnType(int sectionColumnType) {
        this.sectionColumnType = sectionColumnType;
    }

    public String getScheduleAnsSection() {
        return scheduleAnsSection;
    }

    public void setScheduleAnsSection(String scheduleAnsSection) {
        this.scheduleAnsSection = scheduleAnsSection;
    }

    public int getScheduleAnswerSectionID() {
        return scheduleAnswerSectionID;
    }

    public void setScheduleAnswerSectionID(int scheduleAnswerSectionID) {
        this.scheduleAnswerSectionID = scheduleAnswerSectionID;
    }

    public int getScheduleChecklistID() {
        return scheduleChecklistID;
    }

    public void setScheduleChecklistID(int scheduleChecklistID) {
        this.scheduleChecklistID = scheduleChecklistID;
    }
}

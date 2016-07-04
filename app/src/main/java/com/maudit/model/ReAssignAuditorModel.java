package com.maudit.model;

/**
 *
 */
public class ReAssignAuditorModel {
    Integer UserID, ScheduleSectionID, AuditorScheduleID;
    String Abbrv, Username;
    boolean IsLeadAuditor;

    public boolean isLeadAuditor() {
        return IsLeadAuditor;
    }

    public void setIsLeadAuditor(boolean isLeadAuditor) {
        IsLeadAuditor = isLeadAuditor;
    }

    public String getAbbrv() {
        return Abbrv;
    }

    public void setAbbrv(String abbrv) {
        Abbrv = abbrv;
    }

    public Integer getAuditorScheduleID() {
        return AuditorScheduleID;
    }

    public void setAuditorScheduleID(Integer auditorScheduleID) {
        AuditorScheduleID = auditorScheduleID;
    }

    public Integer getScheduleSectionID() {
        return ScheduleSectionID;
    }

    public void setScheduleSectionID(Integer scheduleSectionID) {
        ScheduleSectionID = scheduleSectionID;
    }

    public Integer getUserID() {
        return UserID;
    }

    public void setUserID(Integer userID) {
        UserID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}

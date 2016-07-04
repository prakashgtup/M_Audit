package com.maudit.model;

/**
 *
 */
public class AdhocListModel {
    Integer LocationID, AuditTypeID, ChecklistTypeID, StatusID;
    String LocationName;
    String AuditType;
    String IconImagePath;
    String StatusType;
    String ChecklistName;

    public String getChecklistName() {
        return ChecklistName;
    }

    public void setChecklistName(String checklistName) {
        ChecklistName = checklistName;
    }

    public String getAuditType() {
        return AuditType;
    }

    public void setAuditType(String auditType) {
        AuditType = auditType;
    }

    public Integer getAuditTypeID() {
        return AuditTypeID;
    }

    public void setAuditTypeID(Integer auditTypeID) {
        AuditTypeID = auditTypeID;
    }

    public Integer getChecklistTypeID() {
        return ChecklistTypeID;
    }

    public void setChecklistTypeID(Integer checklistTypeID) {
        ChecklistTypeID = checklistTypeID;
    }

    public String getIconImagePath() {
        return IconImagePath;
    }

    public void setIconImagePath(String iconImagePath) {
        IconImagePath = iconImagePath;
    }


    public Integer getLocationID() {
        return LocationID;
    }

    public void setLocationID(Integer locationID) {
        LocationID = locationID;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public Integer getStatusID() {
        return StatusID;
    }

    public void setStatusID(Integer statusID) {
        StatusID = statusID;
    }

    public String getStatusType() {
        return StatusType;
    }

    public void setStatusType(String statusType) {
        StatusType = statusType;
    }
}

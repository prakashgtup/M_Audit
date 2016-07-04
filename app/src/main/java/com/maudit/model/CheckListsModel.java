package com.maudit.model;

import android.content.Context;

/**
 * Created by Mohankumar on 1/19/2016.
 */
public class CheckListsModel {
    Context context;
    int scheduleStatus,auditorStatus;

    public int getAuditorStatus() {
        return auditorStatus;
    }

    public void setAuditorStatus(int auditorStatus) {
        this.auditorStatus = auditorStatus;
    }
    public CheckListsModel(Context context){
        this.context=context;
    }
    String auditScheduleID;
    String locationID;
    String locationName;
    String startDate;
    String sDate;
    String endDate;
    String auditTime;
    String username;
    String scheduleStatus1;
    String statusType;
    String isLeadAuditor;
    Integer IsForSave,AuditTypeID,ChecklistTypeID;

    public Integer getChecklistTypeID() {
        return ChecklistTypeID;
    }

    public void setChecklistTypeID(Integer checklistTypeID) {
        ChecklistTypeID = checklistTypeID;
    }

    public Integer getAuditTypeID() {
        return AuditTypeID;
    }

    public void setAuditTypeID(Integer auditTypeID) {
        AuditTypeID = auditTypeID;
    }

    public Integer getIsForSave() {
        return IsForSave;
    }

    public void setIsForSave(Integer isForSave) {
        IsForSave = isForSave;
    }

    public String getAuditorsAssigned() {
        return AuditorsAssigned;
    }

    public void setAuditorsAssigned(String auditorsAssigned) {
        AuditorsAssigned = auditorsAssigned;
    }

    public String getAssignedSections() {
        return AssignedSections;
    }

    public void setAssignedSections(String assignedSections) {
        AssignedSections = assignedSections;
    }

    String AuditorsAssigned;
    String AssignedSections;

    public String getAuditScheduleID() {
        return auditScheduleID;
    }

    public void setAuditScheduleID(String auditScheduleID) {
        this.auditScheduleID = auditScheduleID;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIsLeadAuditor() {
        return isLeadAuditor;
    }

    public void setIsLeadAuditor(String isLeadAuditor) {
        this.isLeadAuditor = isLeadAuditor;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getScheduleStatus1() {
        return scheduleStatus1;
    }

    public void setScheduleStatus1(String scheduleStatus1) {
        this.scheduleStatus1 = scheduleStatus1;
    }

    public int getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(int scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String createdBy;
    String checklistName;
    String lastModifiedDate;

}

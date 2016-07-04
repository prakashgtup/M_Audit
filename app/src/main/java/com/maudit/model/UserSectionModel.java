package com.maudit.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class UserSectionModel implements Serializable {
    Integer UserID, SectionID;
    String Username;
    List<GetAuditorsAndSection> getAuditorsLists = null;
    List<Boolean> mSelectedMain = null;


    public List<Boolean> getmSelectedMain() {
        return mSelectedMain;
    }

    public void setmSelectedMain(List<Boolean> mSelectedMain) {
        this.mSelectedMain = mSelectedMain;
    }

    public List<GetAuditorsAndSection> getGetAuditorsLists() {
        return getAuditorsLists;
    }

    public void setGetAuditorsLists(List<GetAuditorsAndSection> getAuditorsLists) {
        this.getAuditorsLists = getAuditorsLists;
    }

    public Integer getSectionID() {
        return SectionID;
    }

    public void setSectionID(Integer sectionID) {
        SectionID = sectionID;
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

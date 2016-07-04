package com.maudit.model;

/**
 *
 */
public class GetAuditorsAndSection {
    String Username, Email, Abbrv;
    Integer SectionID,UserID;

    public String getAbbrv() {
        return Abbrv;
    }

    public void setAbbrv(String abbrv) {
        Abbrv = abbrv;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

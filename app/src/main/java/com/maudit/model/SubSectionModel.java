package com.maudit.model;

import android.util.Log;

/**
 * Created by Mohankumar on 1/21/2016.
 */
public class SubSectionModel {
    int subSectionID,sectionID;
    String subSectionAbbr,subSectionTitle;

    public String getSubSectionAbbr() {
        return subSectionAbbr;
    }

    public void setSubSectionAbbr(String subSectionAbbr) {
        this.subSectionAbbr = subSectionAbbr;
    }

    public int getSubSectionID() {
        return subSectionID;
    }

    public void setSubSectionID(int subSectionID) {
        this.subSectionID = subSectionID;
    }

    public String getSubSectionTitle() {
        return subSectionTitle;
    }

    public void setSubSectionTitle(String subSectionTitle) {
        this.subSectionTitle = subSectionTitle;
    }
    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

}

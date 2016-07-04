package com.maudit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

/**
 * Created by Mohankumar on 11/30/2015.
 */
public class Session {
    private Context mContext;
    public static final String M_AUDIT="m-audit", USER_ID="user_id",USER_NAME="user_name",
            USER_TYPE="user_type",SCHEDULE_ID="schedule_id",QUESTION_ID="question_id",
            EDITABLE="editable",ASSIGNED_SECTIONS="assigned_section",
    INSTALLED_DATE = "installed_date", ISIT_FIRST_TIME = "isitfirsttime",
    SHOW_SCORE = "show_score",SHOW_STATUS = "show_status",SHOW_REJECT_BUTTON = "show_reject";
    SharedPreferences pref;
    SharedPreferences.Editor editor;




    private boolean isNetworkConnected() {
        ConnectivityManager cm=(ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!=null;
    }
    public Session(Context context){
        mContext=context;
        pref = context.getSharedPreferences(M_AUDIT, 0);
        editor = pref.edit();
        editor.commit();
    }
    //store the userid
    public void setUserId(String userId){
        editor.putString(USER_ID, userId);
        editor.commit();
    }
    public  String getUserId(){
        return  pref.getString(USER_ID,"");
    }
    //store the userName
    public void setUserName(String username){
        editor.putString(USER_NAME, username);
        editor.commit();
    }
    public  String getUserName(){
        return  pref.getString(USER_NAME,"");
    }
    //store the userType
    public void setUserType(String userType){
        editor.putString(USER_TYPE, userType);
        editor.commit();
    }
    public  String getUserType(){
        return  pref.getString(USER_TYPE,"");
    }
    //store the ScheduleId
    public void setScheduleId(int scheduleId){
        editor.putInt(SCHEDULE_ID, scheduleId);
        editor.commit();
    }
    public  Integer getScheduleId(){
        return  pref.getInt(SCHEDULE_ID, 0);
    }
    //store the ScheduleId
    public void setQuestionId(int questionId){
        editor.putInt(QUESTION_ID, questionId);
        editor.commit();
    }
    public  Integer getQuestionId(){
        return  pref.getInt(QUESTION_ID, 0);
    }

    //store the section editable or not
    public void setEditable(boolean isEditable){
        editor.putBoolean(EDITABLE, isEditable);
        editor.commit();
    }
    public boolean getEditable(){
        return  pref.getBoolean(EDITABLE, false);
    }

    //store the section editable or not
    public void setIsScoreAvil(boolean isScoreAvil){
        editor.putBoolean(SHOW_SCORE, isScoreAvil);
        editor.commit();
    }
    public void setIsStatusAvail(boolean isStatusAvail){
        editor.putBoolean(SHOW_STATUS, isStatusAvail);
        editor.commit();
    }
    public void setIsRejectAvail(boolean isRejectAvail){
        editor.putBoolean(SHOW_REJECT_BUTTON, isRejectAvail);
        editor.commit();
    }

    public boolean getIsScoreAvil(){
        return  pref.getBoolean(SHOW_SCORE, false);
    }
    public boolean getIsStatusAvail(){
        return  pref.getBoolean(SHOW_STATUS, false);
    }
    public boolean getIsRejectAvail(){
        return  pref.getBoolean(SHOW_REJECT_BUTTON, false);
    }

    //store the section AssignedSection or not
    public void setAssignedSection(boolean isEditable){
        editor.putBoolean(ASSIGNED_SECTIONS,isEditable);
        editor.commit();
    }
    public boolean getAssignedSection(){
        return  pref.getBoolean(ASSIGNED_SECTIONS,false);
    }

    //store the section AssignedSection or not
    public void setInstalledDate(String date){
        editor.putString(INSTALLED_DATE,date);
        editor.putBoolean(ISIT_FIRST_TIME,true);
        editor.commit();
    }
    public String getInstalledDate(){
        return pref.getString(INSTALLED_DATE, "");
    }
    public boolean iSFirstTime(){
        return  pref.getBoolean(ISIT_FIRST_TIME, false);
    }
}

package com.maudit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.maudit.model.AnswerColumnsModel;
import com.maudit.model.AnswerSectionsModel;
import com.maudit.model.AnswersModel;
import com.maudit.model.MediaModel;
import com.maudit.model.QuestionsModel;
import com.maudit.model.SectionModel;
import com.maudit.model.SubSectionModel;
import com.maudit.utils.Session;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DataBaseAdpter extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "MAudit.db";
    public static final int DATABASE_VERSION = 1;
    private Session mSession;
    private String mUserId,mUserType,mUserName;
    private int mScheduleId=0;
    SQLiteDatabase sqliteDB;

    public DataBaseAdpter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mSession=new Session(context);
        mUserId=mSession.getUserId();
        mUserType=mSession.getUserType();
        mUserName=mSession.getUserName();
        mScheduleId=mSession.getScheduleId();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db, TableCreation.ScheduleSection.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.ScheduleSection.columns));
        createTable(db, TableCreation.ScheduleSubSection.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.ScheduleSubSection.columns));
        createTable(db, TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME,TableCreation.getColumnsStringFromArray(TableCreation.ScheduleSectionQuestionnarie.columns));
        createTable(db, TableCreation.ScheduleAnswerSection.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.ScheduleAnswerSection.columns));
        createTable(db, TableCreation.ScheduleAnswerMainSection.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.ScheduleAnswerMainSection.columns));
        createTable(db, TableCreation.ScheduleQuestionMedia.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.ScheduleQuestionMedia.columns));
        createTable(db, TableCreation.ScheduleAnswerCheckedState.TABLE_NAME, TableCreation.getColumnsStringFromArray(TableCreation.ScheduleAnswerCheckedState.columns));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void createTable(SQLiteDatabase db,String tableName, String columns) {
        String query = "CREATE TABLE " + tableName + " (" + columns + ");";
        db.execSQL(query);
    }

    public void dropTable(SQLiteDatabase db,String tableName) {
        String query = "DROP TABLE IF EXISTS " + tableName + ";";
        db.execSQL(query);
    }
    // Open the Database in write mode
    public void open(){
        sqliteDB=this.getWritableDatabase();
    }//Open the database in read mode
    public void read(){
        sqliteDB = this.getReadableDatabase();
    }
    // Close the Database
    public void Close(){
        sqliteDB.close();
    }

   //insert the section details
    public void insertScheduleSectionDetails(List<SectionModel> sectionList){
        open();
        ContentValues contentValues = new ContentValues();
        for(SectionModel sectionModel:sectionList){
            contentValues.put(TableCreation.ScheduleSection.USER_ID,mUserId);
            contentValues.put(TableCreation.ScheduleSection.SCHEDULE_ID,mScheduleId);
            contentValues.put(TableCreation.ScheduleSection.SECTION_ID,sectionModel.getSectionID());
            contentValues.put(TableCreation.ScheduleSection.SECTION_ABBR,sectionModel.getSectionAbbr());
            contentValues.put(TableCreation.ScheduleSection.SECTION_TITLE,sectionModel.getSectionTitle());
            contentValues.put(TableCreation.ScheduleSection.RESULT_STATUS,0);
            contentValues.put(TableCreation.ScheduleSection.SECTION_RESULT,sectionModel.getResultStatus());
            contentValues.put(TableCreation.ScheduleSection.IS_SECTION_ASSIGNED,sectionModel.getIsSectionAssigned());
            sqliteDB.insert(TableCreation.ScheduleSection.TABLE_NAME, null, contentValues);
        }
        close();
    }

    public  void updateSectionResult(int sectionId, int SectionResult){
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCreation.ScheduleSection.SECTION_RESULT, SectionResult);
        sqliteDB.update(TableCreation.ScheduleSection.TABLE_NAME, contentValues, TableCreation.ScheduleSection.SCHEDULE_ID + "=? AND " +TableCreation.ScheduleSection.USER_ID + "=? AND "+ TableCreation.ScheduleSection.SECTION_ID+ "=?", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId), String.valueOf(sectionId)});
        close();
    }

    //get the store value from table
    public Integer getSectionResult(int sectionId){
        open();
        Integer SectionResult=0;
        Cursor cursorAnswerMainSection =null;
        try {
            cursorAnswerMainSection = sqliteDB.query(TableCreation.ScheduleSection.TABLE_NAME, null, TableCreation.ScheduleSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSection.USER_ID + "=? AND " + TableCreation.ScheduleSection.SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(sectionId)}, null, null, null);
            if (cursorAnswerMainSection != null && cursorAnswerMainSection.getCount() > 0) {
                if (cursorAnswerMainSection.moveToLast()) {
                    SectionResult = cursorAnswerMainSection.getInt(cursorAnswerMainSection.getColumnIndex("SectionResult"));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorAnswerMainSection != null)
                cursorAnswerMainSection.close();
        }
        close();
        return SectionResult;
    }

    //insert the subsection details
    public void insertScheduleSubSectionDetails(List<SubSectionModel> subSectionList){
        open();
        ContentValues contentValues = new ContentValues();
        for(SubSectionModel subSectionModel:subSectionList){
            contentValues.put(TableCreation.ScheduleSubSection.USER_ID, mUserId);
            contentValues.put(TableCreation.ScheduleSubSection.SCHEDULE_ID,mScheduleId);
            contentValues.put(TableCreation.ScheduleSubSection.SECTION_ID,subSectionModel.getSectionID());
            contentValues.put(TableCreation.ScheduleSubSection.SUB_SECTION_ID,subSectionModel.getSubSectionID());
            contentValues.put(TableCreation.ScheduleSubSection.SUB_SECTION_TITLE,subSectionModel.getSubSectionTitle());
            contentValues.put(TableCreation.ScheduleSubSection.SUB_SECTION_ABBR,subSectionModel.getSubSectionAbbr());
            sqliteDB.insert(TableCreation.ScheduleSubSection.TABLE_NAME, null, contentValues);
        }
        close();
    }

    //insert the Questions details
    public void insertScheduleQuestionnarie(List<QuestionsModel> questionsModelList,List<SubSectionModel> subSectionlist){
        open();
        ContentValues contentValues = new ContentValues();
        for(QuestionsModel questionsModel:questionsModelList){
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID,mScheduleId);
            if(questionsModel.getSubSectionID()!=0){
                contentValues.put(TableCreation.ScheduleSectionQuestionnarie.SUB_SECTION_ID,questionsModel.getSubSectionID());
                for(SubSectionModel subSectionModel:subSectionlist){
                    if(questionsModel.getSubSectionID()==subSectionModel.getSubSectionID()){
                        contentValues.put(TableCreation.ScheduleSectionQuestionnarie.SECTION_ID,subSectionModel.getSectionID());
                    }
                }
            }else{
                contentValues.put(TableCreation.ScheduleSectionQuestionnarie.SECTION_ID,questionsModel.getSectionID());
                contentValues.put(TableCreation.ScheduleSectionQuestionnarie.SUB_SECTION_ID,0);
            }
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.USER_ID, mUserId);
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.QUESTION_ID,questionsModel.getScheduleQuestionID());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.QABBR,questionsModel.getqAbbrv());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.QUESTION_TEXT,questionsModel.getQuestionText());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.HINT,questionsModel.getHint());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.SEQUENCE,questionsModel.getSequence());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.IS_MANDATORY,questionsModel.getIsMandatory());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.STANDARD_OR_ClAUSES,questionsModel.getStandardORClauses());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.REMARK, questionsModel.getRemark());
            sqliteDB.insert(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, null, contentValues);
        }
        close();
    }

    //insert the answer main section
    public void insertScheduleAnswerMainSection(List<AnswerSectionsModel> answerColumnsModelList){
        open();
        ContentValues contentValues = new ContentValues();
        for(AnswerSectionsModel answerSectionsModel:answerColumnsModelList){
            contentValues.put(TableCreation.ScheduleAnswerMainSection.SCHEDULE_ID, mScheduleId);
            contentValues.put(TableCreation.ScheduleAnswerMainSection.USER_ID, mUserId);
            contentValues.put(TableCreation.ScheduleAnswerMainSection.SECTION_ID,0);
            contentValues.put(TableCreation.ScheduleAnswerMainSection.SUB_SECTION_ID,0);
            contentValues.put(TableCreation.ScheduleAnswerMainSection.SCHEDULE_ANSWER_SECTIONS_ID,answerSectionsModel.getScheduleAnswerSectionID());
            contentValues.put(TableCreation.ScheduleAnswerMainSection.SCHEDULE_ANSWER_SECTION,answerSectionsModel.getScheduleAnsSection());
            contentValues.put(TableCreation.ScheduleAnswerMainSection.SCHEDULE_CHECKLIST_ID,answerSectionsModel.getScheduleChecklistID());
            contentValues.put(TableCreation.ScheduleAnswerMainSection.SECTION_COLUMN_TYPE,answerSectionsModel.getSectionColumnType());
            contentValues.put(TableCreation.ScheduleAnswerMainSection.REPEAT_SECTION_TITLE,answerSectionsModel.getRepeatSectionTitle());
            sqliteDB.insert(TableCreation.ScheduleAnswerMainSection.TABLE_NAME, null, contentValues);
        }
        close();
    }
    //insert the Questions details
    public void insertScheduleQuestionMedia(MediaModel mediaModel){
        open();
        Log.i("mediaModel",mediaModel.getIsLocalImage()+"");
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCreation.ScheduleQuestionMedia.SCHEDULE_ID,mScheduleId);
        contentValues.put(TableCreation.ScheduleQuestionMedia.USER_ID,mUserId);
        //if(mediaModel.getSectionId()!=0){
            contentValues.put(TableCreation.ScheduleQuestionMedia.SECTION_ID,mediaModel.getSectionId());
        //}else{
            contentValues.put(TableCreation.ScheduleQuestionMedia.SUB_SECTION_ID,mediaModel.getSubSectionId());
        //}
        contentValues.put(TableCreation.ScheduleQuestionMedia.QUESTION_ID,mediaModel.getScheduleQuestionID());
        contentValues.put(TableCreation.ScheduleQuestionMedia.IS_LOCAL_IMAGE,mediaModel.getIsLocalImage());
        contentValues.put(TableCreation.ScheduleQuestionMedia.CAPTURE_ID,mediaModel.getCaptureId());
        if(mediaModel.getIsLocalImage()==0) {
            Integer getAvailImgSequence = getAvailImgSequence(mediaModel.getSectionId(), mediaModel.getSubSectionId(), mediaModel.getScheduleQuestionID());
            Log.i("Is Image",""+getAvailImgSequence);
            Integer imgSequenceNo = getAvailImgSequence+1;
            contentValues.put(TableCreation.ScheduleQuestionMedia.LOCAL_SEQUENCE_NO, imgSequenceNo);
            contentValues.put(TableCreation.ScheduleQuestionMedia.IMAGE_NAME, "IMG"+imgSequenceNo);
            contentValues.put(TableCreation.ScheduleQuestionMedia.QUESTION_MEDIA_ID, mediaModel.getQuestionMediaID());
            contentValues.put(TableCreation.ScheduleQuestionMedia.IS_FROM_SERVER, mediaModel.getIsFromServer());
        }else {
            contentValues.put(TableCreation.ScheduleQuestionMedia.LOCAL_SEQUENCE_NO, mediaModel.getImageId());
            contentValues.put(TableCreation.ScheduleQuestionMedia.IMAGE_NAME, mediaModel.getImageName());
        }
        contentValues.put(TableCreation.ScheduleQuestionMedia.LOCAL_MEDIA_PATH, mediaModel.getImagePath());
        if(mediaModel.getIsLocalImage()==1) {
            contentValues.put(TableCreation.ScheduleQuestionMedia.LOCAL_MEDIA_VALUE, mediaModel.getImageBase64Value());
        }
        if(mediaModel.getIsLocalImage()==1) {
            Integer getAvailImgSequence = getAvailImgSequence(mediaModel.getSectionId(), mediaModel.getSubSectionId(), mediaModel.getScheduleQuestionID(),mediaModel.getImageId());
            Log.i("MediaId",""+getAvailImgSequence);
            Log.i("ImageId",""+mediaModel.getImageId());
            if(getAvailImgSequence!=0){
                sqliteDB.update(TableCreation.ScheduleQuestionMedia.TABLE_NAME, contentValues,
                        TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " +
                                TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " +
                                TableCreation.ScheduleQuestionMedia.SECTION_ID + "=? AND " +
                                TableCreation.ScheduleQuestionMedia.SUB_SECTION_ID + "=? AND " +
                                TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=? AND " +
                                TableCreation.ScheduleQuestionMedia.QUESTION_MEDIA_ID + "=? AND " +
                                TableCreation.ScheduleQuestionMedia.LOCAL_SEQUENCE_NO + "=?", new String[]{String.valueOf(mScheduleId),
                                String.valueOf(mUserId),String.valueOf(mediaModel.getSectionId()),
                                String.valueOf(mediaModel.getSubSectionId()),String.valueOf(mediaModel.getScheduleQuestionID()),
                                String.valueOf(getAvailImgSequence),String.valueOf(mediaModel.getImageId())});
            }else {
                sqliteDB.insert(TableCreation.ScheduleQuestionMedia.TABLE_NAME, null, contentValues);
            }
        }else {
            Log.i("Server",""+mediaModel.getIsFromServer());
            sqliteDB.insert(TableCreation.ScheduleQuestionMedia.TABLE_NAME, null, contentValues);
        }
       // getScheduleQuestionMedia(mediaModel.getSectionId(), mediaModel.getSubSectionId(), mediaModel.getScheduleQuestionID(), mediaModel.getCaptureId());
        close();
        }

    //get the LocalsequenceNo based on selected sectionId
    public Integer getAvailImgSequence(int sectionId, int subsectinId,int questionid,int ImageId){
        Integer result=0;
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.query(TableCreation.ScheduleQuestionMedia.TABLE_NAME, null,
                    TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " +
                            TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " +
                            TableCreation.ScheduleQuestionMedia.SECTION_ID + "=? AND " +
                            TableCreation.ScheduleQuestionMedia.SUB_SECTION_ID + "=? AND " +
                            TableCreation.ScheduleQuestionMedia.LOCAL_SEQUENCE_NO + "=? AND " +
                            TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(sectionId),String.valueOf(subsectinId),String.valueOf(ImageId),String.valueOf(questionid)}, null, null, null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToLast()) {
                    result = cursorSection.getInt(cursorSection.getColumnIndex("QuestionMediaID"));;
                    cursorSection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        //close();
        return result;
    }

    //get the LocalsequenceNo based on selected sectionId
    public Integer getAvailImgSequence(int sectionId, int subsectinId,int questionid){
        Integer result=0;
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.query(TableCreation.ScheduleQuestionMedia.TABLE_NAME, null,
                    TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " +
                            TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " +
                            TableCreation.ScheduleQuestionMedia.SECTION_ID + "=? AND " +
                            TableCreation.ScheduleQuestionMedia.SUB_SECTION_ID + "=? AND " +
                            TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(sectionId),String.valueOf(subsectinId),String.valueOf(questionid)}, null, null, null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToLast()) {
                    result = cursorSection.getInt(cursorSection.getColumnIndex("LocalSequenceNo"));;
                    cursorSection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        //close();
        return result;
    }

    public  void updateScheduleAnswerType(AnswersModel answersModel,int questionId,int sectionId,int subSectionId){
        open();
        if(subSectionId!=0){
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.QANSWER_VALUE,answersModel.getqAnswerValue());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ANSWER_TYPE_ID,answersModel.getScheduleAnswerTypeID());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.IS_CHECKED, answersModel.getIsChecked());
            // contentValues.put(TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ANS_OPTIONS,answerColumnsModel.getScheduleAnsOptions());
            sqliteDB.update(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, contentValues, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND "+TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.SUB_SECTION_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.QUESTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId),String.valueOf(subSectionId), String.valueOf(questionId)});
            getScheduleQuestionDetails(sectionId,subSectionId);
        }else{
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.QANSWER_VALUE,answersModel.getqAnswerValue());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ANSWER_TYPE_ID,answersModel.getScheduleAnswerTypeID());
            contentValues.put(TableCreation.ScheduleSectionQuestionnarie.IS_CHECKED, answersModel.getIsChecked());
            // contentValues.put(TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ANS_OPTIONS,answerColumnsModel.getScheduleAnsOptions());
            sqliteDB.update(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, contentValues, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND " +TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.SECTION_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.QUESTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId),String.valueOf(sectionId), String.valueOf(questionId)});
        }
        close();
    }

    public  void updateRemarks(String remarks,int questionId){
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCreation.ScheduleSectionQuestionnarie.REMARK, remarks);
        sqliteDB.update(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, contentValues, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.QUESTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(questionId)});
        close();
    }


    //get the all sectionvalue based on the scheduleid
    public List<SectionModel> getScheduleSectionDetails(int scheduleId){
        List<SectionModel> listSection=new ArrayList<>();
        SectionModel sectionModel=null;
        read();
       // Cursor cursorAnswer=db.query(TableCreation.ScheduleSection.TABLE_NAME,null,TableCreation.ScheduleAnswerSection.SCHEDULE_ID+"=? AND "+TableCreation.ScheduleAnswerSection.SECTION_ID+"=? AND "+TableCreation.ScheduleAnswerSection.IS_CHECKED+"=?",new String[]{String.valueOf(mScheduleId)},null,null,null);
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.query(TableCreation.ScheduleSection.TABLE_NAME, null, TableCreation.ScheduleAnswerSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerSection.USER_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)}, null, null, null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToFirst()) {
                    do {
                        sectionModel = new SectionModel();
                        sectionModel.setSectionID(cursorSection.getInt(cursorSection.getColumnIndex("ScheduleSectionID")));
                        sectionModel.setSectionAbbr(cursorSection.getString(cursorSection.getColumnIndex("SectionAbbr")));
                        sectionModel.setSectionTitle(cursorSection.getString(cursorSection.getColumnIndex("SectionTitle")));
                        sectionModel.setResultStatus(cursorSection.getString(cursorSection.getColumnIndex("ResultStatus")));
                        sectionModel.setIsSectionAssigned(cursorSection.getInt(cursorSection.getColumnIndex("IsSectionAssigned")));
                        sectionModel.setSectionResult(cursorSection.getInt(cursorSection.getColumnIndex("SectionResult")));
                        listSection.add(sectionModel);
                    } while (cursorSection.moveToNext());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        close();
        return listSection;
    }

    //get the all Main sectionvalue
    public List<AnswerSectionsModel> getAllMainSectionDetails(){
        List<AnswerSectionsModel> listSection=new ArrayList<>();
        AnswerSectionsModel sectionModel=null;
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.query(TableCreation.ScheduleAnswerMainSection.TABLE_NAME, null, TableCreation.ScheduleAnswerMainSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerMainSection.USER_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)}, null, null, null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToFirst()) {
                    do {
                        sectionModel = new AnswerSectionsModel();
                        sectionModel.setScheduleAnswerSectionID(cursorSection.getInt(cursorSection.getColumnIndex("ScheduleAnswerSectionID")));
                        sectionModel.setScheduleAnsSection(cursorSection.getString(cursorSection.getColumnIndex("ScheduleAnsSection")));
                        sectionModel.setScheduleChecklistID(cursorSection.getInt(cursorSection.getColumnIndex("ScheduleChecklistID")));
                        sectionModel.setSectionColumnType(cursorSection.getInt(cursorSection.getColumnIndex("SectionColumnType")));
                        sectionModel.setRepeatSectionTitle(cursorSection.getString(cursorSection.getColumnIndex("RepeatSectionTitle")));
                        listSection.add(sectionModel);
                    } while (cursorSection.moveToNext());
                }
                cursorSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        close();
        return listSection;
    }

    //get section and Subsection based on ScheduleQuestion ID{
    public QuestionsModel getSectionSubSection(int ScheduleQuestionID){
        QuestionsModel questionsModel=null;
        read();
        Cursor cursorQuestionSection = null;
        try {
            cursorQuestionSection = sqliteDB.query(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, null, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.QUESTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(ScheduleQuestionID)}, null, null, null);
            if (cursorQuestionSection != null && cursorQuestionSection.getCount() > 0) {
                cursorQuestionSection.moveToFirst();
                questionsModel = new QuestionsModel();
                questionsModel.setSectionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleSectionID")));
                questionsModel.setSubSectionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleSubSectionID")));
                cursorQuestionSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // this gets called even if there is an exception somewhere above
            if (cursorQuestionSection != null)
                cursorQuestionSection.close();
        }
        close();
        return questionsModel;
    }
    //get the all subsectionvalue based on the scheduleid
    public List<SubSectionModel> getScheduleSubSectionList(int sectionId){
        List<SubSectionModel> listSubSection=new ArrayList<>();
        SubSectionModel subSectionModel=null;
        read();
        Cursor cursorSubSection = null;
        try {
            cursorSubSection = sqliteDB.query(TableCreation.ScheduleSubSection.TABLE_NAME, null, TableCreation.ScheduleSubSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSubSection.USER_ID + "=? AND " + TableCreation.ScheduleSubSection.SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(sectionId)}, null, null, null);
            if (cursorSubSection != null && cursorSubSection.getCount() > 0) {
                if (cursorSubSection.moveToFirst()) {
                    do {
                        subSectionModel = new SubSectionModel();
                        subSectionModel.setSectionID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSectionID")));
                        subSectionModel.setSubSectionID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSubSectionID")));
                        subSectionModel.setSubSectionAbbr(cursorSubSection.getString(cursorSubSection.getColumnIndex("ScheduleSubSectionAbbr")));
                        subSectionModel.setSubSectionTitle(cursorSubSection.getString(cursorSubSection.getColumnIndex("ScheduleSubSectionTitle")));
                        // subSectionModel.setResultStatus(cursorSubSection.getString(cursorSubSection.getColumnIndex("ResultStatus")));
                        listSubSection.add(subSectionModel);
                    } while (cursorSubSection.moveToNext());
                }
                cursorSubSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSubSection != null)
                cursorSubSection.close();
        }
        close();
        return listSubSection;
    }

    //Reset the Subsection or section
    public void resetSectionValues(int sectionId,int subSectionId){
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCreation.ScheduleSectionQuestionnarie.REMARK, "");
        ContentValues checkedContentValues = new ContentValues();
        checkedContentValues.put(TableCreation.ScheduleAnswerSection.IS_CHECKED, 0);
        if(subSectionId!=0){
            sqliteDB.update(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, contentValues, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND " +TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND "+ TableCreation.ScheduleSectionQuestionnarie.SUB_SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId), String.valueOf(subSectionId)});
            sqliteDB.delete(TableCreation.ScheduleQuestionMedia.TABLE_NAME, TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND "+TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.SUB_SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId), String.valueOf(subSectionId)});
            sqliteDB.update(TableCreation.ScheduleAnswerSection.TABLE_NAME, checkedContentValues, TableCreation.ScheduleAnswerSection.SCHEDULE_ID + "=? AND " +TableCreation.ScheduleAnswerSection.USER_ID + "=? AND "+ TableCreation.ScheduleAnswerSection.SUB_SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId), String.valueOf(subSectionId)});

        }else{
            sqliteDB.update(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, contentValues, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND " +TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND "+ TableCreation.ScheduleSectionQuestionnarie.SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId), String.valueOf(sectionId)});
            sqliteDB.delete(TableCreation.ScheduleQuestionMedia.TABLE_NAME, TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " +TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId), String.valueOf(sectionId)});
            sqliteDB.update(TableCreation.ScheduleAnswerSection.TABLE_NAME, checkedContentValues, TableCreation.ScheduleAnswerSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerSection.USER_ID + "=? AND " + TableCreation.ScheduleAnswerSection.SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(sectionId)});
        }
        close();
    }
    //get the all subsectionvalue based on the scheduleid
    public List<QuestionsModel> getScheduleQuestionDetails(int sectionId,int subSectionId){
        List<QuestionsModel> listQuestions=new ArrayList<>();
        QuestionsModel questionsModel=null;
        AnswersModel answersModel=null;
       read();
        Cursor cursorQuestionSection=null;
        try {
        if(subSectionId!=0){
            cursorQuestionSection =sqliteDB.query(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, null, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND "+TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND "+ TableCreation.ScheduleSectionQuestionnarie.SUB_SECTION_ID + "=?", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId), String.valueOf(subSectionId)}, null, null, null);
            if(cursorQuestionSection!=null&&cursorQuestionSection.getCount()>0){
                if(cursorQuestionSection.moveToFirst()){
                    do{
                        questionsModel=new QuestionsModel();
                        questionsModel.setSectionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleSectionID")));
                        questionsModel.setSubSectionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleSubSectionID")));
                        questionsModel.setScheduleQuestionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleQuestionID")));
                        questionsModel.setqAbbrv(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QAbbrv")));
                        questionsModel.setQuestionText(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QuestionText")));
                        questionsModel.setHint(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("Hint")));
                        questionsModel.setStandardORClauses(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("StandardORClauses")));
                        questionsModel.setIsMandatory(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("IsMandatory")));
                        questionsModel.setSequence(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("Sequence")));
                        questionsModel.setRemark(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("Remark")));
                        if(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleAnswerTypeID"))!=0){
                            answersModel=new AnswersModel();
                            answersModel.setIsChecked(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("IsChecked")));
                            answersModel.setqAnswerValue(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QAnswerValue")));
                            answersModel.setScheduleAnswerTypeID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleAnswerTypeID")));
                            answersModel.setScheduleAnswerSectionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleAnswerSectionID")));
                            questionsModel.setAnswersModel(answersModel);
                        }else{
                            answersModel=new AnswersModel();
                            answersModel.setIsChecked(0);
                            answersModel.setqAnswerValue("");
                            answersModel.setScheduleAnswerTypeID(0);
                            answersModel.setScheduleAnswerSectionID(0);
                            questionsModel.setAnswersModel(answersModel);
                        }
                        listQuestions.add(questionsModel);
                    }while(cursorQuestionSection.moveToNext());
                }cursorQuestionSection.close();
            }
        }
        else{
            cursorQuestionSection =sqliteDB.query(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, null, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND " +TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND "+ TableCreation.ScheduleSectionQuestionnarie.SECTION_ID + "=?", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId), String.valueOf(sectionId)}, null, null, null);
            if(cursorQuestionSection!=null&&cursorQuestionSection.getCount()>0){
                if(cursorQuestionSection.moveToFirst()){
                    do{
                        questionsModel=new QuestionsModel();
                        questionsModel.setSectionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleSectionID")));
                        questionsModel.setSubSectionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleSubSectionID")));
                        questionsModel.setScheduleQuestionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleQuestionID")));
                        questionsModel.setqAbbrv(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QAbbrv")));
                        questionsModel.setQuestionText(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QuestionText")));
                        questionsModel.setHint(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("Hint")));
                        questionsModel.setStandardORClauses(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("StandardORClauses")));
                        questionsModel.setIsMandatory(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("IsMandatory")));
                        questionsModel.setSequence(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("Sequence")));
                        questionsModel.setRemark(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("Remark")));
                        if(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QAnswerValue"))!=null&&!cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QAnswerValue")).equals("")){
                            answersModel=new AnswersModel();
                            answersModel.setqAnswerValue(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QAnswerValue")));
                            answersModel.setScheduleAnswerTypeID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleAnswerTypeID")));
                            answersModel.setScheduleAnswerSectionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleAnswerSectionID")));
                            questionsModel.setAnswersModel(answersModel);
                        }
                        listQuestions.add(questionsModel);
                    }while(cursorQuestionSection.moveToNext());
                }cursorQuestionSection.close();
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // this gets called even if there is an exception somewhere above
            if(cursorQuestionSection != null)
                cursorQuestionSection.close();
        }
        close();
        return listQuestions;
    }

    //get the all subsectionvalue based on the scheduleid
    public List<QuestionsModel> getAllScheduleQuestionDetails(){
        List<QuestionsModel> listQuestions=new ArrayList<>();
        QuestionsModel questionsModel=null;
        AnswersModel answersModel=null;
        read();
        Cursor cursorQuestionSection=null;
        try {
            cursorQuestionSection = sqliteDB.query(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, null, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)}, null, null, null);
            if (cursorQuestionSection != null && cursorQuestionSection.getCount() > 0) {
                if (cursorQuestionSection.moveToFirst()) {
                    do {
                        questionsModel = new QuestionsModel();
                        questionsModel.setScheduleQuestionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleQuestionID")));
                        questionsModel.setSequence(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("Sequence")));
                        questionsModel.setRemark(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("Remark")));
                        answersModel = new AnswersModel();
                        answersModel.setqAnswerValue(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QAnswerValue")));
                        answersModel.setScheduleAnswerTypeID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleAnswerTypeID")));
                        answersModel.setScheduleAnswerSectionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleAnswerSectionID")));
                        answersModel.setIsSectionAssigned(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("IsSectionAssigned")));
                        answersModel.setIsChecked(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("IsChecked")));
                        questionsModel.setAnswersModel(answersModel);

                        listQuestions.add(questionsModel);
                    } while (cursorQuestionSection.moveToNext());
                }
                cursorQuestionSection.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorQuestionSection != null)
                cursorQuestionSection.close();
        }
        close();
        return listQuestions;
    }

    //get the all subsectionvalue based on the scheduleid
    public List<QuestionsModel> getAllScheduleQuestionDetails(int sectionID){
        List<QuestionsModel> listQuestions=new ArrayList<>();
        QuestionsModel questionsModel=null;
        AnswersModel answersModel=null;
        read();
        Cursor cursorQuestionSection=null;
        try {
            int subSectionId = 0;
            cursorQuestionSection = sqliteDB.query(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, null, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(sectionID)}, null, null, null);
            if (cursorQuestionSection != null && cursorQuestionSection.getCount() > 0) {
                if (cursorQuestionSection.moveToFirst()) {
                    do {
                        questionsModel = new QuestionsModel();
                        questionsModel.setScheduleQuestionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleQuestionID")));
                        questionsModel.setSequence(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("Sequence")));
                        questionsModel.setRemark(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("Remark")));
                        questionsModel.setIsMandatory(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("IsMandatory")));
                        answersModel = new AnswersModel();
                        answersModel.setqAnswerValue(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QAnswerValue")));
                        answersModel.setScheduleAnswerTypeID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleAnswerTypeID")));
                        answersModel.setScheduleAnswerSectionID(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleAnswerSectionID")));
                        answersModel.setIsSectionAssigned(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("IsSectionAssigned")));
                        answersModel.setIsChecked(cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("IsChecked")));
                        questionsModel.setAnswersModel(answersModel);
                        subSectionId = cursorQuestionSection.getInt(cursorQuestionSection.getColumnIndex("ScheduleSubSectionID"));
                        questionsModel.setSectionAbbr(sectionAbbr(sectionID));
                        questionsModel.setSubSectionAbbr(subSectionAbbr(sectionID, subSectionId));
                        questionsModel.setQuestionText(cursorQuestionSection.getString(cursorQuestionSection.getColumnIndex("QuestionText")));


                        listQuestions.add(questionsModel);
                    } while (cursorQuestionSection.moveToNext());
                }
                cursorQuestionSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // this gets called even if there is an exception somewhere above
            if (cursorQuestionSection != null)
                cursorQuestionSection.close();
        }
        close();
        return listQuestions;
    }


    //update questionId to ScheduleAnswerMainSection table
    public void updateSectionId(int sectionId, int subSectionId, int answerSectionId, String sectionValue) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCreation.ScheduleAnswerMainSection.SCHEDULE_ANSWER_SECTION, sectionValue);
        sqliteDB.update(TableCreation.ScheduleAnswerMainSection.TABLE_NAME, contentValues, TableCreation.ScheduleAnswerMainSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerMainSection.USER_ID + "=? AND " + TableCreation.ScheduleAnswerMainSection.SCHEDULE_ANSWER_SECTIONS_ID + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(answerSectionId)});
        //close();
    }

    //get the store value from table
    public String getStoredSectionValue(int sectionId,int subSectionId,int answerSectionId){
        open();
        String storedSectionValue="";
        Cursor cursorAnswerMainSection =null;
        try {
            cursorAnswerMainSection=sqliteDB.query(TableCreation.ScheduleAnswerMainSection.TABLE_NAME,null,TableCreation.ScheduleAnswerMainSection.SCHEDULE_ID+"=? AND "+TableCreation.ScheduleAnswerMainSection.USER_ID+"=? AND "+TableCreation.ScheduleAnswerMainSection.SCHEDULE_ANSWER_SECTIONS_ID+"=? ",new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(answerSectionId)},null,null,null);
            if(cursorAnswerMainSection!=null&&cursorAnswerMainSection.getCount()>0){
                if(cursorAnswerMainSection.moveToLast()){
                    storedSectionValue=cursorAnswerMainSection.getString(cursorAnswerMainSection.getColumnIndex("ScheduleAnsSection"));
                }
                cursorAnswerMainSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // this gets called even if there is an exception somewhere above
            if(cursorAnswerMainSection != null)
                cursorAnswerMainSection.close();
        }
       //close();
        return storedSectionValue;
    }

    //insert the Questions details
    public void insertScheduleAnswerSection(List<AnswerColumnsModel> answerColumnsModelList,int count){
        open();
        for(int j=0;j<count;j++){
            ContentValues contentValues = new ContentValues();
            for(AnswerColumnsModel answerColumnsModel:answerColumnsModelList){
                contentValues.put(TableCreation.ScheduleAnswerSection.SCHEDULE_ID,mScheduleId);
                contentValues.put(TableCreation.ScheduleAnswerSection.USER_ID,mUserId);
                contentValues.put(TableCreation.ScheduleAnswerSection.SCHEDULE_ANSWER_TYPE_ID, answerColumnsModel.getScheduleAnswerTypeID());
                contentValues.put(TableCreation.ScheduleAnswerSection.ANSWER_COLOR,answerColumnsModel.getAnswerColor());
                contentValues.put(TableCreation.ScheduleAnswerSection.IS_CHECKED,0);
                contentValues.put(TableCreation.ScheduleAnswerSection.QUESTION_ID,0);
                contentValues.put(TableCreation.ScheduleAnswerSection.SCHEDULE_ANS_OPTIONS,answerColumnsModel.getScheduleAnsOptions());
                contentValues.put(TableCreation.ScheduleAnswerSection.SCHEDULE_ANS_OPTION_VALUE,answerColumnsModel.getScheduleAnsOptionValue());
                contentValues.put(TableCreation.ScheduleAnswerSection.ANSWER_COLUMN_INDEX,j);
                sqliteDB.insert(TableCreation.ScheduleAnswerSection.TABLE_NAME, null, contentValues);
            }
        }

        close();
    }

    //---------------------------------------------------
    //           Update value to table
    //---------------------------------------------------
    //update questionId to ScheduleAnswerSection table
    public void updateQuestionID(int columnIndex,int answerSectionID,QuestionsModel questionsModel,List<AnswerColumnsModel> answerColumnsModelList){
        open();
        Cursor cursorAnswerSectionSelected = null;
        AnswerColumnsModel answerColumnsModelCheckedState=null;
        try {
            cursorAnswerSectionSelected = sqliteDB.query(TableCreation.ScheduleAnswerSection.TABLE_NAME, null, TableCreation.ScheduleAnswerSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerSection.USER_ID + "=? AND " + TableCreation.ScheduleAnswerSection.QUESTION_ID + "=? AND " + TableCreation.ScheduleAnswerSection.ANSWER_COLUMN_INDEX + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(questionsModel.getScheduleQuestionID()), String.valueOf(columnIndex)}, null, null, null);
            if (cursorAnswerSectionSelected != null && cursorAnswerSectionSelected.getCount() > 0) {
                Log.i("NOT Avail", "Not avail");
            } else {
                for (AnswerColumnsModel answerColumnsModel : answerColumnsModelList) {
                    answerColumnsModelCheckedState = new AnswerColumnsModel();
                    ContentValues contentValues = new ContentValues();
                    answerColumnsModelCheckedState = getIsCheckedValue(questionsModel.getScheduleQuestionID(), answerColumnsModel.getScheduleAnswerTypeID(), columnIndex);
                    contentValues.put(TableCreation.ScheduleAnswerSection.SCHEDULE_ID, mScheduleId);
                    contentValues.put(TableCreation.ScheduleAnswerSection.USER_ID, mUserId);
                    contentValues.put(TableCreation.ScheduleAnswerSection.ANSWER_COLUMN_INDEX, columnIndex);
                    Integer getScheduleAnswerId = getScheduleAnswerId(questionsModel.getScheduleQuestionID(),answerSectionID);
                    contentValues.put(TableCreation.ScheduleAnswerSection.SCHEDULE_ANSWER_ID, getScheduleAnswerId);
                   contentValues.put(TableCreation.ScheduleAnswerSection.SCHEDULE_ANSWER_SECTIONS_ID, answerSectionID);
                    contentValues.put(TableCreation.ScheduleAnswerSection.SECTION_ID, questionsModel.getSectionID());
                    contentValues.put(TableCreation.ScheduleAnswerSection.SUB_SECTION_ID, questionsModel.getSubSectionID());
                    contentValues.put(TableCreation.ScheduleAnswerSection.QUESTION_ID, questionsModel.getScheduleQuestionID());
                    contentValues.put(TableCreation.ScheduleAnswerSection.ANSWER_COLOR, answerColumnsModel.getAnswerColor());
                    contentValues.put(TableCreation.ScheduleAnswerSection.SCHEDULE_ANSWER_TYPE_ID, answerColumnsModel.getScheduleAnswerTypeID());
                    contentValues.put(TableCreation.ScheduleAnswerSection.SCHEDULE_ANS_OPTION_VALUE, answerColumnsModel.getScheduleAnsOptionValue());
                    contentValues.put(TableCreation.ScheduleAnswerSection.IS_CHECKED, answerColumnsModelCheckedState.getIsChecked());
                    contentValues.put(TableCreation.ScheduleAnswerSection.SCHEDULE_ANS_OPTIONS, answerColumnsModel.getScheduleAnsOptions());
                    sqliteDB.insert(TableCreation.ScheduleAnswerSection.TABLE_NAME, null, contentValues);
                }
            }
            cursorAnswerSectionSelected.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // this gets called even if there is an exception somewhere above
            if (cursorAnswerSectionSelected != null)
                cursorAnswerSectionSelected.close();
        }
        close();
    }

    //get the SectionAbbr based on selected sectionId
    public Integer getScheduleAnswerId(int questionid,int scheduleAnswersectionId){
        Integer result=0;
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.query(TableCreation.ScheduleAnswerCheckedState.TABLE_NAME, null,
                    TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ID + "=? AND " +
                            TableCreation.ScheduleAnswerCheckedState.USER_ID + "=? AND " +
                            TableCreation.ScheduleAnswerCheckedState.QUESTION_ID + "=? AND " +
                            TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ANSWER_SECTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(questionid),String.valueOf(scheduleAnswersectionId)}, null, null, null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToLast()) {
                    result = cursorSection.getInt(cursorSection.getColumnIndex("ScheduleAnswerID"));;
                    cursorSection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        //close();
        return result;
    }

    //get the all subsectionvalue based on the scheduleid
    public List<MediaModel> getAllScheduleQuestionMedia(){
        List<MediaModel> listMediaModel=new ArrayList<>();
        MediaModel mediaModel=null;
        read();
        Cursor cursorSubSection = null;
        try {
            cursorSubSection = sqliteDB.query(TableCreation.ScheduleQuestionMedia.TABLE_NAME, null,
                    TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " +
                            TableCreation.ScheduleQuestionMedia.USER_ID + "=?",
                    new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)}, null, null, null);
            if (cursorSubSection != null && cursorSubSection.getCount() > 0) {
                if (cursorSubSection.moveToFirst()) {
                    do {
                        mediaModel = new MediaModel();
                        mediaModel.setSectionId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSectionID")));
                        mediaModel.setQuestionMediaID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("QuestionMediaID")));
                        mediaModel.setSubSectionId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSubSectionID")));
                        mediaModel.setScheduleQuestionID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleQuestionID")));
                        mediaModel.setImageName(cursorSubSection.getString(cursorSubSection.getColumnIndex("ImageName")));
                        mediaModel.setImagePath(cursorSubSection.getString(cursorSubSection.getColumnIndex("LocalMediaPath")));
                        mediaModel.setImageBase64Value(cursorSubSection.getString(cursorSubSection.getColumnIndex("LocalMediaValue")));
                        mediaModel.setImageId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("LocalSequenceNo")));
                        mediaModel.setIsLocalImage(cursorSubSection.getInt(cursorSubSection.getColumnIndex("IsLocalImage")));
                        mediaModel.setLocalSequenceNo(cursorSubSection.getInt(cursorSubSection.getColumnIndex(TableCreation.ScheduleQuestionMedia.LOCAL_SEQUENCE_NO)));
                        mediaModel.setIsFromServer(cursorSubSection.getInt(cursorSubSection.getColumnIndex(TableCreation.ScheduleQuestionMedia.IS_FROM_SERVER)));
                        listMediaModel.add(mediaModel);
                    } while (cursorSubSection.moveToNext());
                }
                cursorSubSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSubSection != null)
                cursorSubSection.close();
        }
        close();
        return listMediaModel;
    }
    //store the radiobutton selected position based on the questionId, columnIndex
    public void scheduleAnswerSectionSelected(AnswerColumnsModel answerColumnsModel){
        open();
        Cursor cursorAnswerSectionSelected =null;
        try {
        cursorAnswerSectionSelected=sqliteDB.query(TableCreation.ScheduleAnswerSection.TABLE_NAME,null,TableCreation.ScheduleAnswerSection.SCHEDULE_ID+"=? AND "+TableCreation.ScheduleAnswerSection.USER_ID+"=? AND "+TableCreation.ScheduleAnswerSection.QUESTION_ID+"=? AND "+TableCreation.ScheduleAnswerSection.ANSWER_COLUMN_INDEX+"=? ",new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(answerColumnsModel.getScheduleQuestionID()),String.valueOf(answerColumnsModel.getAnswerColumnCount())},null,null,null);
        if (cursorAnswerSectionSelected != null && cursorAnswerSectionSelected.getCount() > 0) {
            ContentValues removeContentValues = new ContentValues();
            removeContentValues.put(TableCreation.ScheduleAnswerSection.IS_CHECKED, 0);
            sqliteDB.update(TableCreation.ScheduleAnswerSection.TABLE_NAME, removeContentValues, TableCreation.ScheduleAnswerSection.SCHEDULE_ID + "=? AND " +TableCreation.ScheduleAnswerSection.USER_ID+"=? AND "+ TableCreation.ScheduleAnswerSection.QUESTION_ID + "=? AND " + TableCreation.ScheduleAnswerSection.ANSWER_COLUMN_INDEX + "=? ", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId), String.valueOf(answerColumnsModel.getScheduleQuestionID()), String.valueOf(answerColumnsModel.getAnswerColumnCount())});

            ContentValues contentValues = new ContentValues();
            contentValues.put(TableCreation.ScheduleAnswerSection.IS_CHECKED, answerColumnsModel.getIsChecked());
            sqliteDB.update(TableCreation.ScheduleAnswerSection.TABLE_NAME, contentValues, TableCreation.ScheduleAnswerSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerSection.USER_ID + "=? AND " + TableCreation.ScheduleAnswerSection.QUESTION_ID + "=? AND " + TableCreation.ScheduleAnswerSection.SCHEDULE_ANSWER_TYPE_ID + "=? AND " + TableCreation.ScheduleAnswerSection.ANSWER_COLUMN_INDEX + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(answerColumnsModel.getScheduleQuestionID()), String.valueOf(answerColumnsModel.getScheduleAnswerTypeID()), String.valueOf(answerColumnsModel.getAnswerColumnCount())});
            cursorAnswerSectionSelected.close();
        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorAnswerSectionSelected != null)
                cursorAnswerSectionSelected.close();
        }
        //close();
    }
    //get the all subsectionvalue based on the scheduleid
    public List<MediaModel> getScheduleQuestionMediaCaptureId(int sectionId,int subSectionId,int questionId){
        mScheduleId=mSession.getScheduleId();
        List<MediaModel> listMediaModel=new ArrayList<>();
        MediaModel mediaModel=null;
        read();
        Cursor cursorSubSection=null;
        try {
            if (subSectionId != 0) {
                cursorSubSection = sqliteDB.query(TableCreation.ScheduleQuestionMedia.TABLE_NAME, null, TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.SUB_SECTION_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(subSectionId), String.valueOf(questionId)}, null, null, null);
                if (cursorSubSection != null && cursorSubSection.getCount() > 0) {
                    if (cursorSubSection.moveToFirst()) {
                        do {
                            mediaModel = new MediaModel();
                            if(!cursorSubSection.getString(cursorSubSection.getColumnIndex("LocalMediaPath")).equalsIgnoreCase("")) {
                                mediaModel.setSectionId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSectionID")));
                                mediaModel.setSubSectionId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSubSectionID")));
                                mediaModel.setScheduleQuestionID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleQuestionID")));
                                mediaModel.setImageName(cursorSubSection.getString(cursorSubSection.getColumnIndex("ImageName")));
                                mediaModel.setImagePath(cursorSubSection.getString(cursorSubSection.getColumnIndex("LocalMediaPath")));
                                mediaModel.setCaptureId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("Capture_Id")));
                                mediaModel.setIsLocalImage(cursorSubSection.getInt(cursorSubSection.getColumnIndex("IsLocalImage")));
                                mediaModel.setIsFromServer(cursorSubSection.getInt(cursorSubSection.getColumnIndex("IsFromServer")));
                                mediaModel.setImageId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("LocalSequenceNo")));
                                mediaModel.setQuestionMediaID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("QuestionMediaID")));
                                listMediaModel.add(mediaModel);
                            }
                        } while (cursorSubSection.moveToNext());
                    }
                }
                cursorSubSection.close();
            } else {
                cursorSubSection = sqliteDB.query(TableCreation.ScheduleQuestionMedia.TABLE_NAME, null, TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.SECTION_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(sectionId), String.valueOf(questionId)}, null, null, null);
                if (cursorSubSection != null && cursorSubSection.getCount() > 0) {
                    if (cursorSubSection.moveToFirst()) {
                        do {
                            mediaModel = new MediaModel();
                            if(!cursorSubSection.getString(cursorSubSection.getColumnIndex("LocalMediaPath")).equalsIgnoreCase("")) {
                                mediaModel.setSectionId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSectionID")));
                                mediaModel.setSubSectionId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSubSectionID")));
                                mediaModel.setScheduleQuestionID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleQuestionID")));
                                mediaModel.setImageName(cursorSubSection.getString(cursorSubSection.getColumnIndex("ImageName")));
                                mediaModel.setImagePath(cursorSubSection.getString(cursorSubSection.getColumnIndex("LocalMediaPath")));
                                mediaModel.setCaptureId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("Capture_Id")));
                                mediaModel.setIsLocalImage(cursorSubSection.getInt(cursorSubSection.getColumnIndex("IsLocalImage")));
                                mediaModel.setIsFromServer(cursorSubSection.getInt(cursorSubSection.getColumnIndex("IsFromServer")));
                                mediaModel.setImageId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("LocalSequenceNo")));
                                mediaModel.setQuestionMediaID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("QuestionMediaID")));
                                listMediaModel.add(mediaModel);
                            }
                        } while (cursorSubSection.moveToNext());
                    }
                    cursorSubSection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSubSection != null)
                cursorSubSection.close();
        }
        close();
        return listMediaModel;
    }

    //get the all subsectionvalue based on the scheduleid
    public List<MediaModel> getScheduleQuestionMedia(int sectionId, int subSectionId, int questionId) {
        mScheduleId = mSession.getScheduleId();
        List<MediaModel> listMediaModel = new ArrayList<>();
        MediaModel mediaModel = null;
        read();
        Cursor cursorSubSection = null;
        try {
            if (subSectionId != 0) {
                cursorSubSection = sqliteDB.query(TableCreation.ScheduleQuestionMedia.TABLE_NAME, null, TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.SUB_SECTION_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(subSectionId), String.valueOf(questionId)}, null, null, null);
                if (cursorSubSection != null && cursorSubSection.getCount() > 0) {
                    if (cursorSubSection.moveToFirst()) {
                        do {
                            mediaModel = new MediaModel();
                            mediaModel.setSectionId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSectionID")));
                            mediaModel.setSubSectionId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSubSectionID")));
                            mediaModel.setScheduleQuestionID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleQuestionID")));
                            mediaModel.setImageName(cursorSubSection.getString(cursorSubSection.getColumnIndex("ImageName")));
                            mediaModel.setImagePath(cursorSubSection.getString(cursorSubSection.getColumnIndex("LocalMediaPath")));
                            mediaModel.setCaptureId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("Capture_Id")));
                            mediaModel.setIsLocalImage(cursorSubSection.getInt(cursorSubSection.getColumnIndex("IsLocalImage")));
                            mediaModel.setIsFromServer(cursorSubSection.getInt(cursorSubSection.getColumnIndex("IsFromServer")));
                            mediaModel.setImageId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("LocalSequenceNo")));
                            mediaModel.setQuestionMediaID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("QuestionMediaID")));
                            listMediaModel.add(mediaModel);
                        } while (cursorSubSection.moveToNext());
                    }
                }
                cursorSubSection.close();
            } else {
                cursorSubSection = sqliteDB.query(TableCreation.ScheduleQuestionMedia.TABLE_NAME, null, TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.SECTION_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(sectionId), String.valueOf(questionId)}, null, null, null);
                if (cursorSubSection != null && cursorSubSection.getCount() > 0) {
                    if (cursorSubSection.moveToFirst()) {
                        do {
                            mediaModel = new MediaModel();
                            mediaModel.setSectionId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSectionID")));
                            mediaModel.setSubSectionId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleSubSectionID")));
                            mediaModel.setScheduleQuestionID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("ScheduleQuestionID")));
                            mediaModel.setImageName(cursorSubSection.getString(cursorSubSection.getColumnIndex("ImageName")));
                            mediaModel.setImagePath(cursorSubSection.getString(cursorSubSection.getColumnIndex("LocalMediaPath")));
                            mediaModel.setCaptureId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("Capture_Id")));
                            mediaModel.setIsFromServer(cursorSubSection.getInt(cursorSubSection.getColumnIndex("IsFromServer")));
                            mediaModel.setIsLocalImage(cursorSubSection.getInt(cursorSubSection.getColumnIndex("IsLocalImage")));
                            mediaModel.setImageId(cursorSubSection.getInt(cursorSubSection.getColumnIndex("LocalSequenceNo")));
                            mediaModel.setQuestionMediaID(cursorSubSection.getInt(cursorSubSection.getColumnIndex("QuestionMediaID")));
                            listMediaModel.add(mediaModel);
                        } while (cursorSubSection.moveToNext());
                    }
                    cursorSubSection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSubSection != null)
                cursorSubSection.close();
        }
        close();
        return listMediaModel;
    }
    public  void deleteImagepath(QuestionsModel questionsModel,MediaModel mediaModel){
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCreation.ScheduleQuestionMedia.LOCAL_MEDIA_PATH, "");
        contentValues.put(TableCreation.ScheduleQuestionMedia.LOCAL_MEDIA_VALUE, "");
        contentValues.put(TableCreation.ScheduleQuestionMedia.IS_LOCAL_IMAGE, 1);
        Integer getAvailImgSequence = getAvailImgSequence(mediaModel.getSectionId(), mediaModel.getSubSectionId(), mediaModel.getScheduleQuestionID(), mediaModel.getImageId());
        Log.i("getAvailImgSequence", getAvailImgSequence + "");
        if (getAvailImgSequence != 0) {
            sqliteDB.update(TableCreation.ScheduleQuestionMedia.TABLE_NAME, contentValues, TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND "
                    + TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " +
                    TableCreation.ScheduleQuestionMedia.SUB_SECTION_ID + "=? AND "
                    + TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=? AND "
                    + TableCreation.ScheduleQuestionMedia.LOCAL_SEQUENCE_NO + "=? AND "
                    + TableCreation.ScheduleQuestionMedia.IMAGE_NAME + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(questionsModel.getSubSectionID()), String.valueOf(questionsModel.getScheduleQuestionID()), String.valueOf(mediaModel.getImageId()), String.valueOf(mediaModel.getImageName())});
        } else {
            if (questionsModel.getSubSectionID() != 0) {
                sqliteDB.delete(TableCreation.ScheduleQuestionMedia.TABLE_NAME,
                        TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND "
                                + TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND "
                                + TableCreation.ScheduleQuestionMedia.SUB_SECTION_ID + "=? AND "
                                + TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=? AND "
                                + TableCreation.ScheduleQuestionMedia.LOCAL_SEQUENCE_NO + "=? AND "
                                + TableCreation.ScheduleQuestionMedia.IMAGE_NAME + "=? ",
                        new String[]{String.valueOf(mScheduleId),
                                String.valueOf(mUserId),
                                String.valueOf(questionsModel.getSubSectionID()),
                                String.valueOf(questionsModel.getScheduleQuestionID()),
                                String.valueOf(mediaModel.getImageId()),
                                String.valueOf(mediaModel.getImageName())});
                //sqliteDB.update(TableCreation.ScheduleQuestionMedia.TABLE_NAME, contentValues, TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.SUB_SECTION_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.LOCAL_SEQUENCE_NO + "=? AND " + TableCreation.ScheduleQuestionMedia.IMAGE_NAME + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(questionsModel.getSubSectionID()), String.valueOf(questionsModel.getScheduleQuestionID()), String.valueOf(mediaModel.getImageId()), String.valueOf(mediaModel.getImageName())});
            } else {
                sqliteDB.delete(TableCreation.ScheduleQuestionMedia.TABLE_NAME, TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.SECTION_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.LOCAL_SEQUENCE_NO + "=? AND " + TableCreation.ScheduleQuestionMedia.IMAGE_NAME + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(questionsModel.getSectionID()), String.valueOf(questionsModel.getScheduleQuestionID()), String.valueOf(mediaModel.getImageId()), String.valueOf(mediaModel.getImageName())});
                //sqliteDB.update(TableCreation.ScheduleQuestionMedia.TABLE_NAME, contentValues, TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.SECTION_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.QUESTION_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.LOCAL_SEQUENCE_NO + "=? AND " + TableCreation.ScheduleQuestionMedia.IMAGE_NAME + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(questionsModel.getSectionID()), String.valueOf(questionsModel.getScheduleQuestionID()), String.valueOf(mediaModel.getImageId()), String.valueOf(mediaModel.getImageName())});
            }
        }
        //close();
    }

    public AnswerColumnsModel getIsCheckedValue(int questionId,int answerTypeId, int columnIndex){
        open();
        AnswerColumnsModel answerColumnsModel=new AnswerColumnsModel();
        Cursor cursorAnswerCheckedState = null;
        try {
            cursorAnswerCheckedState = sqliteDB.query(TableCreation.ScheduleAnswerCheckedState.TABLE_NAME, null, TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerCheckedState.USER_ID + "=? AND " + TableCreation.ScheduleAnswerCheckedState.QUESTION_ID + "=? AND " + TableCreation.ScheduleAnswerCheckedState.COLUMN_INDEX + "=? AND " + TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ANSWER_TYPE_ID + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(questionId), String.valueOf(columnIndex), String.valueOf(answerTypeId)}, null, null, null);
            if (cursorAnswerCheckedState != null && cursorAnswerCheckedState.getCount() > 0) {
                if (cursorAnswerCheckedState.moveToLast()) {
                    answerColumnsModel.setIsChecked(cursorAnswerCheckedState.getInt(cursorAnswerCheckedState.getColumnIndex("IsChecked")));
                    answerColumnsModel.setScheduleAnsOptionValue(cursorAnswerCheckedState.getString(cursorAnswerCheckedState.getColumnIndex("QAnswerValue")));
                    answerColumnsModel.setScheduleAnswerID(cursorAnswerCheckedState.getInt(cursorAnswerCheckedState.getColumnIndex("ScheduleAnswerID")));
                    answerColumnsModel.setSetScheduleAnswerSectionID(cursorAnswerCheckedState.getInt(cursorAnswerCheckedState.getColumnIndex("ScheduleAnswerSectionID")));
                    cursorAnswerCheckedState.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorAnswerCheckedState != null)
                cursorAnswerCheckedState.close();
        }
        //close();
        return answerColumnsModel;
    }


    //insert the AnswerCheckedState details
    public void insertScheduleAnswerCheckedState(List<AnswerColumnsModel> answerCheckedStateList, List<AnswerSectionsModel> answerSectionsList){
        open();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < answerCheckedStateList.size(); i++) {
            AnswerColumnsModel answerColumnsModel = answerCheckedStateList.get(i);
            if (answerColumnsModel.getScheduleAnswerTypeID() != 0) {
                contentValues.put(TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ID, mScheduleId);
                contentValues.put(TableCreation.ScheduleAnswerCheckedState.USER_ID, mUserId);
                contentValues.put(TableCreation.ScheduleAnswerCheckedState.QUESTION_ID, answerColumnsModel.getScheduleQuestionID());
                contentValues.put(TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ANSWER_SECTION_ID, answerColumnsModel.getSetScheduleAnswerSectionID());
                contentValues.put(TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ANSWER_TYPE_ID, answerColumnsModel.getScheduleAnswerTypeID());
                contentValues.put(TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ANSWER_ID, answerColumnsModel.getScheduleAnswerID());
                contentValues.put(TableCreation.ScheduleAnswerCheckedState.ANSWER_COLOR, answerColumnsModel.getAnswerColor());
                contentValues.put(TableCreation.ScheduleAnswerCheckedState.QANSWER_VALUE, answerColumnsModel.getScheduleAnsOptionValue());
                int columnIndex = getColumnIndex(answerSectionsList, answerColumnsModel.getSetScheduleAnswerSectionID());
                contentValues.put(TableCreation.ScheduleAnswerCheckedState.COLUMN_INDEX, columnIndex);
                if (answerColumnsModel.getIsChecked() == 1) {
                    contentValues.put(TableCreation.ScheduleAnswerCheckedState.IS_CHECKED, answerColumnsModel.getIsChecked());
                } else {
                    contentValues.put(TableCreation.ScheduleAnswerCheckedState.IS_CHECKED, 0);
                }
                Integer isAvail = isAnswerAvail(answerColumnsModel.getScheduleQuestionID(),answerColumnsModel.getSetScheduleAnswerSectionID());
                if(isAvail==0) {
                    sqliteDB.insert(TableCreation.ScheduleAnswerCheckedState.TABLE_NAME, null, contentValues);
                }
            }
        }
        close();
    }

    //get the SectionAbbr based on selected sectionId
    public Integer isAnswerAvail(int questionid,int scheduleAnswersectionId){
        Integer result=0;
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.query(TableCreation.ScheduleAnswerCheckedState.TABLE_NAME, null,
                    TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ID + "=? AND " +
                            TableCreation.ScheduleAnswerCheckedState.USER_ID + "=? AND " +
                            TableCreation.ScheduleAnswerCheckedState.QUESTION_ID + "=? AND " +
                            TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ANSWER_SECTION_ID + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(questionid),String.valueOf(scheduleAnswersectionId)}, null, null, null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToLast()) {
                    result = 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        //close();
        return result;
    }

    public int getColumnIndex(List<AnswerSectionsModel> answerSectionsList, int SectionId) {
        if(answerSectionsList!=null) {
            for (int i = 0; i < answerSectionsList.size(); i++) {
                AnswerSectionsModel answerSectionsModel = answerSectionsList.get(i);
                if (answerSectionsModel.getScheduleAnswerSectionID()==SectionId){
                    return i;
                }
            }
        }
        return 0;
    }

    public List<AnswerColumnsModel> getScheduleAnswerType(int columnIndex,int questionId) {
        List<AnswerColumnsModel> listAnswerCheckedState = new ArrayList<>();
        AnswerColumnsModel answerColumnsModel = null;
        read();
        Cursor cursorAnswerSection=null;
        try {
            if (questionId != 0) {
                cursorAnswerSection = sqliteDB.query(TableCreation.ScheduleAnswerSection.TABLE_NAME, null, TableCreation.ScheduleAnswerSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerSection.USER_ID + "=? AND " + TableCreation.ScheduleAnswerSection.QUESTION_ID + "=? AND " + TableCreation.ScheduleAnswerSection.ANSWER_COLUMN_INDEX + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(questionId), String.valueOf(columnIndex)}, null, null, null);
                if (cursorAnswerSection != null && cursorAnswerSection.getCount() > 0) {
                    if (cursorAnswerSection.moveToFirst()) {
                        do {
                            answerColumnsModel = new AnswerColumnsModel();
                            answerColumnsModel.setAnswerColumnCount(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("AnswerColumnIndex")));
                            answerColumnsModel.setScheduleQuestionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleQuestionID")));
                            answerColumnsModel.setIsChecked(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("IsChecked")));
                            answerColumnsModel.setScheduleAnswerID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerID")));
                            answerColumnsModel.setScheduleAnswerTypeID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerTypeID")));
                            answerColumnsModel.setAnswerColor(cursorAnswerSection.getString(cursorAnswerSection.getColumnIndex("AnswerColor")));
                            String ScheduleAnsOptionValue = String.valueOf(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnsOptionValue")));
                            answerColumnsModel.setScheduleAnsOptionValue(ScheduleAnsOptionValue);
                            answerColumnsModel.setScheduleAnsOptions(cursorAnswerSection.getString(cursorAnswerSection.getColumnIndex("ScheduleAnsOptions")));
                            listAnswerCheckedState.add(answerColumnsModel);
                        } while (cursorAnswerSection.moveToNext());
                    }
                    cursorAnswerSection.close();
                }
            } else {
                cursorAnswerSection = sqliteDB.query(TableCreation.ScheduleAnswerSection.TABLE_NAME, null, TableCreation.ScheduleAnswerSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerSection.USER_ID + "=? AND " + TableCreation.ScheduleAnswerSection.QUESTION_ID + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(0)}, null, null, null);
                if (cursorAnswerSection != null && cursorAnswerSection.getCount() > 0) {
                    if (cursorAnswerSection.moveToFirst()) {
                        do {
                            answerColumnsModel = new AnswerColumnsModel();
                            answerColumnsModel.setAnswerColumnCount(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("AnswerColumnIndex")));
                            answerColumnsModel.setScheduleQuestionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleQuestionID")));
                            answerColumnsModel.setIsChecked(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("IsChecked")));
                            answerColumnsModel.setScheduleAnswerID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerID")));
                            answerColumnsModel.setScheduleAnswerTypeID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerTypeID")));
                            answerColumnsModel.setAnswerColor(cursorAnswerSection.getString(cursorAnswerSection.getColumnIndex("AnswerColor")));
                            String ScheduleAnsOptionValue = String.valueOf(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnsOptionValue")));
                            answerColumnsModel.setScheduleAnsOptionValue(ScheduleAnsOptionValue);
                            answerColumnsModel.setScheduleAnsOptions(cursorAnswerSection.getString(cursorAnswerSection.getColumnIndex("ScheduleAnsOptions")));
                            listAnswerCheckedState.add(answerColumnsModel);
                        } while (cursorAnswerSection.moveToNext());
                    }
                    cursorAnswerSection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorAnswerSection != null)
                cursorAnswerSection.close();
        }
        //close();
        return listAnswerCheckedState;
    }

    public List<AnswerColumnsModel> getScheduleAnswerType(int questionId) {
        List<AnswerColumnsModel> listAnswerCheckedState = new ArrayList<>();
        AnswerColumnsModel answerColumnsModel = null;
        read();
        Cursor cursorAnswerSection =null;
        try {
        cursorAnswerSection=sqliteDB.query(TableCreation.ScheduleAnswerSection.TABLE_NAME,null,TableCreation.ScheduleAnswerSection.SCHEDULE_ID+"=? AND "+TableCreation.ScheduleAnswerSection.USER_ID+"=? AND "+TableCreation.ScheduleAnswerSection.QUESTION_ID+"=?",new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(questionId)},null,null,null);

            if(cursorAnswerSection!=null&&cursorAnswerSection.getCount()>0) {
                if (cursorAnswerSection.moveToFirst()) {
                    do {
                        answerColumnsModel=new AnswerColumnsModel();
                        answerColumnsModel.setAnswerColumnCount(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("AnswerColumnIndex")));
                        answerColumnsModel.setScheduleQuestionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleQuestionID")));
                        answerColumnsModel.setIsChecked(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("IsChecked")));
                        answerColumnsModel.setScheduleAnswerTypeID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerTypeID")));
                        answerColumnsModel.setAnswerColor(cursorAnswerSection.getString(cursorAnswerSection.getColumnIndex("AnswerColor")));
                        answerColumnsModel.setScheduleAnsOptions(cursorAnswerSection.getString(cursorAnswerSection.getColumnIndex("ScheduleAnsOptions")));
                        listAnswerCheckedState.add(answerColumnsModel);
                    } while (cursorAnswerSection.moveToNext());
                }cursorAnswerSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorAnswerSection != null)
                cursorAnswerSection.close();
        }
        close();
        return listAnswerCheckedState;
    }

    public void deleteSection(Integer IsForSave){
       read();
        Cursor cursorSection=sqliteDB.query(TableCreation.ScheduleSection.TABLE_NAME, null, null, null, null, null, null);
        if(cursorSection!=null&&cursorSection.getCount()>0){
            sqliteDB.delete(TableCreation.ScheduleSection.TABLE_NAME, TableCreation.ScheduleSection.SCHEDULE_ID + "=? AND "+TableCreation.ScheduleSection.USER_ID + "=? AND " + TableCreation.ScheduleSection.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(IsForSave)});
            Cursor cursorSubSection=sqliteDB.query(TableCreation.ScheduleSubSection.TABLE_NAME,null,null,null,null,null,null);
            if(cursorSubSection!=null&&cursorSubSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleSubSection.TABLE_NAME, TableCreation.ScheduleSubSection.SCHEDULE_ID + "=? AND "+TableCreation.ScheduleSubSection.USER_ID + "=? AND " + TableCreation.ScheduleSubSection.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
            Cursor cursorQuestionSection=sqliteDB.query(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME,null,null,null,null,null,null);
            if(cursorQuestionSection!=null&&cursorQuestionSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND "+TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
            Cursor cursorMediaSection=sqliteDB.query(TableCreation.ScheduleQuestionMedia.TABLE_NAME,null,null,null,null,null,null);
            if(cursorMediaSection!=null&&cursorMediaSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleQuestionMedia.TABLE_NAME,TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND "+TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
            Cursor cursorAnswerMainSection=sqliteDB.query(TableCreation.ScheduleAnswerMainSection.TABLE_NAME,null,null,null,null,null,null);
            if(cursorAnswerMainSection!=null&&cursorAnswerMainSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleAnswerMainSection.TABLE_NAME,TableCreation.ScheduleAnswerMainSection.SCHEDULE_ID + "=? AND "+TableCreation.ScheduleAnswerMainSection.USER_ID + "=? AND " + TableCreation.ScheduleAnswerMainSection.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
            Cursor cursorAnswerSection=sqliteDB.query(TableCreation.ScheduleAnswerSection.TABLE_NAME,null,null,null,null,null,null);
            if(cursorAnswerSection!=null&&cursorAnswerSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleAnswerSection.TABLE_NAME,TableCreation.ScheduleAnswerSection.SCHEDULE_ID + "=? AND "+TableCreation.ScheduleAnswerSection.USER_ID + "=? AND " + TableCreation.ScheduleAnswerSection.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
            Cursor cursorCheckedStateSection=sqliteDB.query(TableCreation.ScheduleAnswerCheckedState.TABLE_NAME,null,null,null,null,null,null);
            if(cursorCheckedStateSection!=null&&cursorCheckedStateSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleAnswerCheckedState.TABLE_NAME,TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ID + "=? AND "+TableCreation.ScheduleAnswerCheckedState.USER_ID + "=? AND " + TableCreation.ScheduleAnswerCheckedState.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
        }
        close();
    }

    public void deleteAllUnsavedSection(Integer IsForSave){
        read();
        Cursor cursorSection=sqliteDB.query(TableCreation.ScheduleSection.TABLE_NAME, null, null, null, null, null, null);
        if(cursorSection!=null&&cursorSection.getCount()>0){
            sqliteDB.delete(TableCreation.ScheduleSection.TABLE_NAME, TableCreation.ScheduleSection.USER_ID + "=? AND " + TableCreation.ScheduleSection.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mUserId),String.valueOf(IsForSave)});
            Cursor cursorSubSection=sqliteDB.query(TableCreation.ScheduleSubSection.TABLE_NAME,null,null,null,null,null,null);
            if(cursorSubSection!=null&&cursorSubSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleSubSection.TABLE_NAME, TableCreation.ScheduleSubSection.USER_ID + "=? AND " + TableCreation.ScheduleSubSection.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
            Cursor cursorQuestionSection=sqliteDB.query(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME,null,null,null,null,null,null);
            if(cursorQuestionSection!=null&&cursorQuestionSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, TableCreation.ScheduleSectionQuestionnarie.USER_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
            Cursor cursorMediaSection=sqliteDB.query(TableCreation.ScheduleQuestionMedia.TABLE_NAME,null,null,null,null,null,null);
            if(cursorMediaSection!=null&&cursorMediaSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleQuestionMedia.TABLE_NAME,TableCreation.ScheduleQuestionMedia.USER_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
            Cursor cursorAnswerMainSection=sqliteDB.query(TableCreation.ScheduleAnswerMainSection.TABLE_NAME,null,null,null,null,null,null);
            if(cursorAnswerMainSection!=null&&cursorAnswerMainSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleAnswerMainSection.TABLE_NAME,TableCreation.ScheduleAnswerMainSection.USER_ID + "=? AND " + TableCreation.ScheduleAnswerMainSection.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
            Cursor cursorAnswerSection=sqliteDB.query(TableCreation.ScheduleAnswerSection.TABLE_NAME,null,null,null,null,null,null);
            if(cursorAnswerSection!=null&&cursorAnswerSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleAnswerSection.TABLE_NAME,TableCreation.ScheduleAnswerSection.USER_ID + "=? AND " + TableCreation.ScheduleAnswerSection.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
            Cursor cursorCheckedStateSection=sqliteDB.query(TableCreation.ScheduleAnswerCheckedState.TABLE_NAME,null,null,null,null,null,null);
            if(cursorCheckedStateSection!=null&&cursorCheckedStateSection.getCount()>0){
                sqliteDB.delete(TableCreation.ScheduleAnswerCheckedState.TABLE_NAME,TableCreation.ScheduleAnswerCheckedState.USER_ID + "=? AND " + TableCreation.ScheduleAnswerCheckedState.IS_FOR_SAVE + "=?",new String[]{String.valueOf(mUserId),String.valueOf(IsForSave)});
            }
        }
        close();
    }


    //check value changed locally
    public Integer checkLocalHadValue(String myScheduleId){
        open();
        Integer SectionResult=0;
        Cursor cursorAnswerMainSection =null;
        try {
            cursorAnswerMainSection = sqliteDB.query(TableCreation.ScheduleSection.TABLE_NAME, null, TableCreation.ScheduleSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSection.USER_ID + "=?", new String[]{String.valueOf(myScheduleId), String.valueOf(mUserId)}, null, null, null);
            if (cursorAnswerMainSection != null && cursorAnswerMainSection.getCount() > 0) {
                if (cursorAnswerMainSection.moveToLast()) {
                    SectionResult = cursorAnswerMainSection.getInt(cursorAnswerMainSection.getColumnIndex("IsForSave"));
                }
                cursorAnswerMainSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorAnswerMainSection != null)
                cursorAnswerMainSection.close();
        }
        close();
        return SectionResult;
    }
    //Save the values during Offline
    public void saveDraft(Integer IsForSave){
        open();
        //Update ScheduleSection Values
        ContentValues ScheduleSectionCValues = new ContentValues();
        ScheduleSectionCValues.put(TableCreation.ScheduleSection.IS_FOR_SAVE, IsForSave);
        sqliteDB.update(TableCreation.ScheduleSection.TABLE_NAME, ScheduleSectionCValues,
                TableCreation.ScheduleSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSection.USER_ID
                        + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)});
        //Update ScheduleSubSection Values
        ContentValues ScheduleSubSectionCValues = new ContentValues();
        ScheduleSubSectionCValues.put(TableCreation.ScheduleSubSection.IS_FOR_SAVE, IsForSave);
        sqliteDB.update(TableCreation.ScheduleSubSection.TABLE_NAME, ScheduleSubSectionCValues,
                TableCreation.ScheduleSubSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSubSection.USER_ID
                        + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)});
        //Update ScheduleSectionQuestionnarie Values
        ContentValues ScheduleSectionQuestionnarieCValues = new ContentValues();
        ScheduleSectionQuestionnarieCValues.put(TableCreation.ScheduleSectionQuestionnarie.IS_FOR_SAVE, IsForSave);
        sqliteDB.update(TableCreation.ScheduleSectionQuestionnarie.TABLE_NAME, ScheduleSectionQuestionnarieCValues,
                TableCreation.ScheduleSectionQuestionnarie.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSectionQuestionnarie.USER_ID
                        + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)});
        //Update ScheduleQuestionMedia Values
        ContentValues ScheduleQuestionMediaCValues = new ContentValues();
        ScheduleQuestionMediaCValues.put(TableCreation.ScheduleQuestionMedia.IS_FOR_SAVE, IsForSave);
        sqliteDB.update(TableCreation.ScheduleQuestionMedia.TABLE_NAME, ScheduleQuestionMediaCValues,
                TableCreation.ScheduleQuestionMedia.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleQuestionMedia.USER_ID
                        + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)});
        //Update ScheduleAnswerMainSection Values
        ContentValues ScheduleAnswerMainSectionCValues = new ContentValues();
        ScheduleAnswerMainSectionCValues.put(TableCreation.ScheduleAnswerMainSection.IS_FOR_SAVE, IsForSave);
        sqliteDB.update(TableCreation.ScheduleAnswerMainSection.TABLE_NAME, ScheduleAnswerMainSectionCValues,
                TableCreation.ScheduleAnswerMainSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerMainSection.USER_ID
                        + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)});
        //Update ScheduleAnswerSection Values
        ContentValues ScheduleAnswerSectionCValues = new ContentValues();
        ScheduleAnswerSectionCValues.put(TableCreation.ScheduleAnswerSection.IS_FOR_SAVE, IsForSave);
        sqliteDB.update(TableCreation.ScheduleAnswerSection.TABLE_NAME, ScheduleAnswerSectionCValues,
                TableCreation.ScheduleAnswerSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerSection.USER_ID
                        + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)});
        //Update ScheduleAnswerCheckedState Values
        ContentValues ScheduleAnswerCheckedStateCValues = new ContentValues();
        ScheduleAnswerCheckedStateCValues.put(TableCreation.ScheduleAnswerCheckedState.IS_FOR_SAVE, IsForSave);
        sqliteDB.update(TableCreation.ScheduleAnswerCheckedState.TABLE_NAME, ScheduleAnswerCheckedStateCValues,
                TableCreation.ScheduleAnswerCheckedState.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleAnswerCheckedState.USER_ID
                        + "=?", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId)});
        close();
    }
    public List<AnswerColumnsModel> getQuestionAnswerDetails(int isChecked) {
        List<AnswerColumnsModel> listQuestionAnswers = new ArrayList<>();
        AnswerColumnsModel answerColumnsModel = null;
        read();
        Cursor cursorAnswerSection=null;
        try{
            cursorAnswerSection=sqliteDB.query(TableCreation.ScheduleAnswerSection.TABLE_NAME,null ,TableCreation.ScheduleAnswerSection.SCHEDULE_ID+"=? AND "+TableCreation.ScheduleAnswerSection.USER_ID+"=? AND "+TableCreation.ScheduleAnswerSection.IS_CHECKED+"=? ", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId),String.valueOf(isChecked)}, null, null, null);
            if(cursorAnswerSection!=null&&cursorAnswerSection.getCount()>0) {
                if (cursorAnswerSection.moveToFirst()) {
                    do {
                        answerColumnsModel=new AnswerColumnsModel();
                        answerColumnsModel.setSectionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleSectionID")));
                        answerColumnsModel.setSubSectionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleSubSectionID")));
                        answerColumnsModel.setScheduleQuestionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleQuestionID")));
                        answerColumnsModel.setIsChecked(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("IsChecked")));
                        answerColumnsModel.setScheduleAnswerID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerID")));
                        answerColumnsModel.setScheduleAnswerTypeID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerTypeID")));
                        answerColumnsModel.setSetScheduleAnswerSectionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerSectionID")));
                        answerColumnsModel.setScheduleAnsOptions(cursorAnswerSection.getString(cursorAnswerSection.getColumnIndex("ScheduleAnsOptions")));
                        String ScheduleAnsOptionValue = String.valueOf(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnsOptionValue")));
                        answerColumnsModel.setScheduleAnsOptionValue(ScheduleAnsOptionValue);
                        listQuestionAnswers.add(answerColumnsModel);
                    } while (cursorAnswerSection.moveToNext());
                }cursorAnswerSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorAnswerSection != null)
                cursorAnswerSection.close();
        }
        close();
        return listQuestionAnswers;
    }

    public List<AnswerColumnsModel> getQuestionAnswerDetails() {
        List<AnswerColumnsModel> listQuestionAnswers = new ArrayList<>();
        AnswerColumnsModel answerColumnsModel = null;
        read();
        Cursor cursorAnswerSection=null;
        try{
            cursorAnswerSection=sqliteDB.query(TableCreation.ScheduleAnswerSection.TABLE_NAME,null ,TableCreation.ScheduleAnswerSection.SCHEDULE_ID+"=? AND "+TableCreation.ScheduleAnswerSection.USER_ID+"=?", new String[]{String.valueOf(mScheduleId),String.valueOf(mUserId)}, null, null, null);
            if(cursorAnswerSection!=null&&cursorAnswerSection.getCount()>0) {
                if (cursorAnswerSection.moveToFirst()) {
                    do {
                        answerColumnsModel=new AnswerColumnsModel();
                        answerColumnsModel.setSectionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleSectionID")));
                        answerColumnsModel.setSubSectionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleSubSectionID")));
                        answerColumnsModel.setScheduleQuestionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleQuestionID")));
                        answerColumnsModel.setIsChecked(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("IsChecked")));
                        answerColumnsModel.setScheduleAnswerID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerID")));
                        answerColumnsModel.setScheduleAnswerTypeID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerTypeID")));
                        answerColumnsModel.setSetScheduleAnswerSectionID(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnswerSectionID")));
                        answerColumnsModel.setScheduleAnsOptions(cursorAnswerSection.getString(cursorAnswerSection.getColumnIndex("ScheduleAnsOptions")));
                        String ScheduleAnsOptionValue = String.valueOf(cursorAnswerSection.getInt(cursorAnswerSection.getColumnIndex("ScheduleAnsOptionValue")));
                        answerColumnsModel.setScheduleAnsOptionValue(ScheduleAnsOptionValue);
                        listQuestionAnswers.add(answerColumnsModel);
                    } while (cursorAnswerSection.moveToNext());
                }cursorAnswerSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorAnswerSection != null)
                cursorAnswerSection.close();
        }
        // close();
        return listQuestionAnswers;
    }


    //get the SectionAbbr based on selected sectionId
    public String sectionAbbr(int sectionId){
        String result="";
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.query(TableCreation.ScheduleSection.TABLE_NAME, null, TableCreation.ScheduleSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSection.USER_ID + "=? AND " + TableCreation.ScheduleSection.SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(sectionId)}, null, null, null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToLast()) {
                    result = cursorSection.getString(cursorSection.getColumnIndex("SectionAbbr"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        close();
        return result;
    }
    //get the SubSectionAbbr based on selected sectionId
    public String subSectionAbbr(int sectionId,int subSectionId){
        String result="";
        read();
        Cursor cursorSection = null;
        try {
            cursorSection = sqliteDB.query(TableCreation.ScheduleSubSection.TABLE_NAME, null, TableCreation.ScheduleSubSection.SCHEDULE_ID + "=? AND " + TableCreation.ScheduleSubSection.USER_ID + "=? AND " + TableCreation.ScheduleSubSection.SECTION_ID + "=? AND " + TableCreation.ScheduleSubSection.SUB_SECTION_ID + "=? ", new String[]{String.valueOf(mScheduleId), String.valueOf(mUserId), String.valueOf(sectionId), String.valueOf(subSectionId)}, null, null, null);
            if (cursorSection != null && cursorSection.getCount() > 0) {
                if (cursorSection.moveToLast()) {
                    result = cursorSection.getString(cursorSection.getColumnIndex("ScheduleSubSectionAbbr"));
                }
                cursorSection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursorSection != null)
                cursorSection.close();
        }
        close();
        return result;
    }

    @Override
    public synchronized void close() {
        if(sqliteDB != null){
            sqliteDB.close();
            super.close();
        }
    }
}

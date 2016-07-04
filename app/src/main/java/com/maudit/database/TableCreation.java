package com.maudit.database;

import android.provider.BaseColumns;

/**
 *
 */
public class TableCreation {
    private static final String PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String INTEGER_TYPE_NOT_NULL = " INTEGER not null";

    //---------------------------------------------------
    //               ScheduleChecklistSection table
    //---------------------------------------------------
    public static abstract class ScheduleChecklistSection implements BaseColumns{
        public static final String TABLE_NAME= "ScheduleChecklistSection";
        public static final String SCHDULE_CHECKLIST_ID ="ScheduleChecklistID";
        public static final String SCHEDULE_SECTION_ID="ScheduleSectionID";
        public static final String SCHEDULE_SUB_SECTION_ID="ScheduleSubSectionID";
        public static final String MODE="Mode";
        public static final String ABBR="Abbrv";
        public static final String TITLE="Title";
        public static final String RESULT_STATUS="ResultStatus";
        public static final String SEQUENCE="Sequence";
        public static final String USER_ID="UserId";

        public static final String LOCAL_SCHEDULE_ID="LocalScheduleId";
        public static final String USER_NAME="User_Name";
        public static final String USER_TYPE="User_Type";

        public static String[] columns = {
                LOCAL_SCHEDULE_ID + PRIMARY_KEY + ",",
                SCHEDULE_SECTION_ID + INTEGER_TYPE_NOT_NULL + ",",
                SCHEDULE_SUB_SECTION_ID + INTEGER_TYPE_NOT_NULL + ",",
                SCHDULE_CHECKLIST_ID + INTEGER_TYPE + ",",
                MODE + TEXT_TYPE+",",  ABBR + TEXT_TYPE + ",",
                TITLE + TEXT_TYPE + ",",
                RESULT_STATUS + INTEGER_TYPE + ",",
                SEQUENCE + INTEGER_TYPE + ",",
                USER_ID + INTEGER_TYPE_NOT_NULL + ",",

                USER_NAME + TEXT_TYPE + ",",
                USER_TYPE + TEXT_TYPE
        };
    }
    //---------------------------------------------------
    //              ScheduleSectionQuestionnarie table
    //---------------------------------------------------
    public static abstract class ScheduleSectionQuestionnarie implements BaseColumns{
        public static final String TABLE_NAME= "SectionQuestionnarie";
        public static final String SCHEDULE_QUESTION_ROW_ID="ID";
        public static final String USER_ID="UserId";
        public static final String IS_FOR_SAVE="IsForSave";
        public static final String QUESTION_ID ="ScheduleQuestionID";
        public static final String SCHEDULE_ID ="ScheduleID";
        public static final String SECTION_ID="ScheduleSectionID";
        public static final String SUB_SECTION_ID ="ScheduleSubSectionID";
        public static final String QABBR="QAbbrv";
        public static final String QUESTION_TEXT="QuestionText";
        public static final String HINT="Hint";
        public static final String SEQUENCE="Sequence";
        public static final String IS_MANDATORY="IsMandatory";
        public static final String STANDARD_OR_ClAUSES="StandardORClauses";
        public static final String REMARK="Remark";
        public static final String LOCAL_SCHEDULE_ID="LocalScheduleId";
        public static final String IS_SECTION_ASSIGNED="IsSectionAssigned";
        public static final String QANSWER_VALUE="QAnswerValue";
        public static final String SCHEDULE_ANSWER_TYPE_ID="ScheduleAnswerTypeID";
        public static final String SCHEDULE_ANSWER_OPTION_VALUE="ScheduleAnsOptionValue";
        public static final String SCHEDULE_ANSWER_ID="ScheduleAnswerID";
        public static final String SCHEDULE_ANSWER_SECTION_ID="ScheduleAnswerSectionID";
        public static final String IS_CHECKED="IsChecked";
        public static String[] columns = {
                SCHEDULE_QUESTION_ROW_ID + PRIMARY_KEY + ",",
                USER_ID + INTEGER_TYPE_NOT_NULL + ",",
                IS_FOR_SAVE + INTEGER_TYPE+" DEFAULT "+0+ ",",
                QUESTION_ID + INTEGER_TYPE + ",",
                SCHEDULE_ID + INTEGER_TYPE + ",",
                SECTION_ID + INTEGER_TYPE + ",",
                SUB_SECTION_ID + INTEGER_TYPE + ",",
                QABBR + TEXT_TYPE+",",  QUESTION_TEXT + TEXT_TYPE + ",",
                HINT + TEXT_TYPE + ",",
                STANDARD_OR_ClAUSES + TEXT_TYPE + ",",
                SEQUENCE + INTEGER_TYPE + ",",
                IS_MANDATORY + INTEGER_TYPE + ",",
                REMARK + TEXT_TYPE + ",",
                LOCAL_SCHEDULE_ID + INTEGER_TYPE + ",",
                IS_SECTION_ASSIGNED + INTEGER_TYPE+ ",",
                QANSWER_VALUE + INTEGER_TYPE + ",",
                SCHEDULE_ANSWER_TYPE_ID + INTEGER_TYPE+ ",",
                IS_CHECKED + INTEGER_TYPE+ ",",
                SCHEDULE_ANSWER_OPTION_VALUE + INTEGER_TYPE+ ",",
                SCHEDULE_ANSWER_ID + INTEGER_TYPE+ ",",
                SCHEDULE_ANSWER_SECTION_ID + INTEGER_TYPE
        };
    }

    //---------------------------------------------------
    //               ScheduleSection table
    //---------------------------------------------------
    public static abstract class ScheduleSection implements BaseColumns{
        public static final String TABLE_NAME= "ScheduleSection";
        public static final String SCHEDULE_ID ="ScheduleID";
        public static final String USER_ID="UserId";
        public static final String IS_FOR_SAVE="IsForSave";
        public static final String SECTION_ID ="ScheduleSectionID";
        public static final String SECTION_ABBR="SectionAbbr";
        public static final String SECTION_TITLE="SectionTitle";
        public static final String RESULT_STATUS="ResultStatus";
        public static final String SECTION_RESULT = "SectionResult";
        public static final String IS_SECTION_ASSIGNED = "IsSectionAssigned";

        public static String[] columns = {
                SCHEDULE_ID + INTEGER_TYPE_NOT_NULL + ",",
                USER_ID + INTEGER_TYPE_NOT_NULL + ",",
                IS_FOR_SAVE + INTEGER_TYPE+" DEFAULT "+0+ ",",
                SECTION_ID + INTEGER_TYPE_NOT_NULL + ",",
                SECTION_ABBR + TEXT_TYPE + ",",
                SECTION_TITLE + TEXT_TYPE + ",",
                RESULT_STATUS + INTEGER_TYPE+ ",",
                SECTION_RESULT + INTEGER_TYPE+ ",",
                IS_SECTION_ASSIGNED + INTEGER_TYPE
        };
    }
    //---------------------------------------------------
    //               ScheduleSubSection table
    //---------------------------------------------------
    public static abstract class ScheduleSubSection implements BaseColumns{
        public static final String TABLE_NAME= "ScheduleSubSection";
        public static final String SCHEDULE_ID ="ScheduleID";
        public static final String IS_FOR_SAVE="IsForSave";
        public static final String SECTION_ID="ScheduleSectionID";
        public static final String SUB_SECTION_ID ="ScheduleSubSectionID";
        public static final String USER_ID="UserId";
        public static final String SUB_SECTION_ABBR="ScheduleSubSectionAbbr";
        public static final String SUB_SECTION_TITLE="ScheduleSubSectionTitle";


        public static String[] columns = {
                SCHEDULE_ID + INTEGER_TYPE_NOT_NULL + ",",
                USER_ID + INTEGER_TYPE_NOT_NULL + ",",
                IS_FOR_SAVE + INTEGER_TYPE+" DEFAULT "+0+ ",",
                SECTION_ID + INTEGER_TYPE + ",",
                SUB_SECTION_ID + INTEGER_TYPE + ",",
                SUB_SECTION_ABBR + TEXT_TYPE + ",",
                SUB_SECTION_TITLE + TEXT_TYPE
        };
    }
    //---------------------------------------------------
    //              ScheduleAnswerMainSection table
    //---------------------------------------------------
    public static abstract class ScheduleAnswerMainSection implements BaseColumns{
        public static final String TABLE_NAME= "ScheduleAnswerMainSection";
        public static final String SCHEDULE_ID ="ScheduleID";
        public static final String USER_ID="UserId";
        public static final String IS_FOR_SAVE="IsForSave";
        public static final String SECTION_ID="ScheduleSectionID";
        public static final String SUB_SECTION_ID ="ScheduleSubSectionID";
        public static final String SCHEDULE_ANSWER_SECTIONS_ID ="ScheduleAnswerSectionID";
        public static final String SCHEDULE_CHECKLIST_ID ="ScheduleChecklistID";
        public static final String SCHEDULE_ANSWER_SECTION="ScheduleAnsSection";
        public static final String SECTION_COLUMN_TYPE="SectionColumnType";
        public static final String REPEAT_SECTION_TITLE="RepeatSectionTitle";
        public static final String SECTION_VALUE="SectionValue";

        public static String[] columns = {
                SCHEDULE_ID + INTEGER_TYPE + ",",
                USER_ID + INTEGER_TYPE_NOT_NULL + ",",
                IS_FOR_SAVE + INTEGER_TYPE+" DEFAULT "+0+ ",",
                SECTION_ID + INTEGER_TYPE + ",",
                SUB_SECTION_ID + INTEGER_TYPE + ",",
                SCHEDULE_ANSWER_SECTIONS_ID + INTEGER_TYPE + ",",
                SCHEDULE_ANSWER_SECTION + TEXT_TYPE+",",
                SCHEDULE_CHECKLIST_ID + INTEGER_TYPE+",",
                SECTION_COLUMN_TYPE + INTEGER_TYPE+",",
                REPEAT_SECTION_TITLE + TEXT_TYPE+",",
                SECTION_VALUE + TEXT_TYPE
        };
    }
    //---------------------------------------------------
    //              ScheduleAnswerSection table
    //---------------------------------------------------
    public static abstract class ScheduleAnswerSection implements BaseColumns{
        public static final String TABLE_NAME= "ScheduleAnswerSection";
        public static final String SCHEDULE_ID ="ScheduleID";
        public static final String USER_ID="UserId";
        public static final String IS_FOR_SAVE="IsForSave";
        public static final String SECTION_ID="ScheduleSectionID";
        public static final String SUB_SECTION_ID ="ScheduleSubSectionID";
        public static final String QUESTION_ID ="ScheduleQuestionID";
        public static final String SCHEDULE_ANSWER_SECTIONS_ID ="ScheduleAnswerSectionID";
        public static final String SCHEDULE_ANSWER_TYPE_ID="ScheduleAnswerTypeID";
        public static final String SCHEDULE_ANS_OPTIONS="ScheduleAnsOptions";
        public static final String SCHEDULE_ANS_OPTION_VALUE="ScheduleAnsOptionValue";
        public static final String SCHEDULE_ANSWER_ID="ScheduleAnswerID";
        public static final String ANSWER_COLOR="AnswerColor";
        public static final String IS_CHECKED="IsChecked";
        public static final String ANSWER_COLUMN_INDEX="AnswerColumnIndex";
        public static String[] columns = {
                SCHEDULE_ANSWER_TYPE_ID + INTEGER_TYPE + ",",
                SCHEDULE_ID + INTEGER_TYPE + ",",
                USER_ID + INTEGER_TYPE_NOT_NULL + ",",
                IS_FOR_SAVE + INTEGER_TYPE+" DEFAULT "+0+ ",",
                SECTION_ID + INTEGER_TYPE + ",",
                SUB_SECTION_ID + INTEGER_TYPE + ",",
                QUESTION_ID + INTEGER_TYPE+",",
                ANSWER_COLOR + TEXT_TYPE+",",
                SCHEDULE_ANS_OPTIONS + TEXT_TYPE+",",
                SCHEDULE_ANSWER_ID + INTEGER_TYPE+",",
                SCHEDULE_ANSWER_SECTIONS_ID + INTEGER_TYPE+",",
                SCHEDULE_ANS_OPTION_VALUE + INTEGER_TYPE+",",
                IS_CHECKED + INTEGER_TYPE+",",
                ANSWER_COLUMN_INDEX + INTEGER_TYPE ,
        };
    }
    //---------------------------------------------------
    //              ScheduleAnswerSection table
    //---------------------------------------------------
    public static abstract class ScheduleAnswerCheckedState implements BaseColumns{
        public static final String TABLE_NAME= "ScheduleAnswerCheckedState";
        public static final String SCHEDULE_ID ="ScheduleID";
        public static final String USER_ID="UserId";
        public static final String IS_FOR_SAVE="IsForSave";
        public static final String QUESTION_ID ="ScheduleQuestionID";
        public static final String SCHEDULE_ANSWER_TYPE_ID="ScheduleAnswerTypeID";
        public static final String SCHEDULE_ANSWER_SECTION_ID="ScheduleAnswerSectionID";
        public static final String SCHEDULE_ANSWER_ID="ScheduleAnswerID";
        public static final String SCHEDULE_ANS_OPTIONS_VALUE="ScheduleAnsOptionsValue";
        public static final String QANSWER_VALUE="QAnswerValue";
        public static final String ANSWER_COLOR="AnswerColor";
        public static final String IS_CHECKED="IsChecked";
        public static final String COLUMN_INDEX="ColumnIndex";
        public static String[] columns = {
                SCHEDULE_ID + INTEGER_TYPE + ",",
                USER_ID + INTEGER_TYPE_NOT_NULL + ",",
                IS_FOR_SAVE + INTEGER_TYPE+" DEFAULT "+0+ ",",
                QUESTION_ID + INTEGER_TYPE+",",
                ANSWER_COLOR + TEXT_TYPE+",",
                SCHEDULE_ANS_OPTIONS_VALUE + TEXT_TYPE+",",
                IS_CHECKED + INTEGER_TYPE+",",
                SCHEDULE_ANSWER_SECTION_ID + INTEGER_TYPE+",",
                SCHEDULE_ANSWER_TYPE_ID + INTEGER_TYPE+",",
                SCHEDULE_ANSWER_ID + INTEGER_TYPE+",",
                QANSWER_VALUE + INTEGER_TYPE+",",
                COLUMN_INDEX + INTEGER_TYPE
        };
    }
    //---------------------------------------------------
    //              ScheduleQuestionMedia table
    //---------------------------------------------------
    public static abstract class ScheduleQuestionMedia implements BaseColumns{
        public static final String TABLE_NAME= "ScheduleQuestionMedia";
        public static final String SCHEDULE_ID ="ScheduleID";
        public static final String SECTION_ID="ScheduleSectionID";
        public static final String SUB_SECTION_ID ="ScheduleSubSectionID";
        public static final String QUESTION_ID ="ScheduleQuestionID";
        public static final String USER_ID="UserId";
        public static final String IS_FOR_SAVE="IsForSave";
        public static final String IS_LOCAL_IMAGE="IsLocalImage";
        public static final String IS_FROM_SERVER="IsFromServer";
        public static final String LOCAL_MEDIA_PATH="LocalMediaPath";
        public static final String SERVER_IMAGE_PATH="ServerImagePath";
        public static final String LOCAL_SEQUENCE_NO="LocalSequenceNo";
        public static final String CAPTURE_ID="Capture_Id";
        public static final String IMAGE_NAME="ImageName";
        public static final String QUESTION_MEDIA_ID="QuestionMediaID";
        public static final String LOCAL_MEDIA_VALUE="LocalMediaValue";

        public static String[] columns = {
                SCHEDULE_ID + INTEGER_TYPE_NOT_NULL + ",",
                USER_ID + INTEGER_TYPE_NOT_NULL + ",",
                IS_FOR_SAVE + INTEGER_TYPE+" DEFAULT "+0+ ",",
                IS_LOCAL_IMAGE + INTEGER_TYPE+" DEFAULT "+0+ ",",
                IS_FROM_SERVER + INTEGER_TYPE+" DEFAULT "+0+ ",",
                SECTION_ID + INTEGER_TYPE + ",",
                SUB_SECTION_ID + INTEGER_TYPE + ",",
                QUESTION_ID + INTEGER_TYPE + ",",
                LOCAL_MEDIA_PATH + TEXT_TYPE+ ",",
                SERVER_IMAGE_PATH + TEXT_TYPE+ ",",
                LOCAL_SEQUENCE_NO + INTEGER_TYPE+ ",",
                IMAGE_NAME + TEXT_TYPE+ ",",
                CAPTURE_ID + INTEGER_TYPE+ ",",
                QUESTION_MEDIA_ID + INTEGER_TYPE+ ",",
                LOCAL_MEDIA_VALUE + TEXT_TYPE
        };
    }
    public static String getColumnsStringFromArray(String[] strings) {
        String columnString = "";
        for(int i = 0; i < strings.length; i++) {
            columnString += strings[i];
        }
        return columnString;
    }
}

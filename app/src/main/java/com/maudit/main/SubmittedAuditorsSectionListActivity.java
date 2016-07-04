package com.maudit.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maudit.database.DataBaseAdpter;
import com.maudit.model.AnswerColumnsModel;
import com.maudit.model.AnswerSectionsModel;
import com.maudit.model.AnswersModel;
import com.maudit.model.MediaModel;
import com.maudit.model.QuestionsModel;
import com.maudit.model.ScoreModel;
import com.maudit.model.SectionModel;
import com.maudit.model.SubSectionModel;
import com.maudit.utils.CommonMethod;
import com.maudit.utils.CustomTextView;
import com.maudit.utils.QuestionSectionView;
import com.maudit.utils.Session;
import com.maudit.utils.SubmittedQuestionSectionView;
import com.maudit.utils.Webservices;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SubmittedAuditorsSectionListActivity extends AppCompatActivity implements View.OnClickListener {
    public List<QuestionsModel> questionsList;
    private static SubmittedAuditorsSectionListActivity rootInstance;
    static List<SectionModel> sectionList;
    List<SubSectionModel> subSectionList;
    List<AnswerColumnsModel> answerTypeList;
    public static List<AnswerSectionsModel> answerSectionsList;
    List<AnswerColumnsModel> answersPreviousSelectionList;
    List<MediaModel> mediaList;
    Calendar c = Calendar.getInstance();
    private ProgressDialog mProgressDialog;
    private Webservices mWebservices;
    private CommonMethod mCommonMethod;
    private Session mSession;
    private TextView mTextviewUsername, mTextviewTitle, mTextviewDate, mTextviewSubsectionTitle;
    private ImageView mImagviewBack;
    private Button mBtnSection,mBtnPass,mBtnFail,mBtnNA;
    private CustomTextView mCustomTextView;
    private ImageView mImageviewSubsectionLeft, mImageviewSubsectionRight, mImageviewSectionLeft, mImageviewSectionRight;
    private LinearLayout mLinearlayout_header_btn_container, mLinearlayout_question_container, mLinearlayout_second_row_options_container, mLinearLayout_Evaluation,
    mlinearlayout_section_btn_container;
    private RelativeLayout mRelative_subsection_title_container, relativeLayoutView;
    public static SubmittedQuestionSectionView mQuestionSectionView;
    private int mSectionId = 0, currentsubSectionPosition = 0, subSectionCount = 0,questionItemColumnHeight, mRequestCamera = 100, mRequestGallery = 101,
            answerColumCount = 0, mSubSectionId = 0, mSubSectionPositionRight = 0, mSubSectionPositionLeft = 0, mShowStatus =0, mIsScoreBtn = 0,
            mIsAcceptOrReject = 0,mSectionColumnType=0,mScheduleStatus =0;
    public static DataBaseAdpter mDataBaseAdpter;
    private HorizontalScrollView mHorizontalScrollView_section;
    private Button mButton_score, mButton_accept, mButton_reject, mButton_other_remarks;
    private static String[] mAssignedSections;
    private String mRepeatSectionTitle="",mScheduleAnsSection ="", mQuestionMediaId = "",mSingleAssignedSection = "";
    private StringBuilder mStringBuilderQuestionAnswers, mStringBuilderSections, mRepeatSectionsBuilder;
    public static SubmittedAuditorsSectionListActivity defaultInstance() {
        return rootInstance;
    }
    boolean isGetFromLocal = false;
    int btnRightId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted_auditors_section_list);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        mProgressDialog = new ProgressDialog(SubmittedAuditorsSectionListActivity.this);
        mWebservices = new Webservices(this);
        mCommonMethod = new CommonMethod(this);
        mSession = new Session(this);
        mQuestionSectionView = new SubmittedQuestionSectionView(this, null);
        mLinearlayout_second_row_options_container = (LinearLayout) findViewById(R.id.linearlayout_second_row_options_container);
        // mLinearLayout_Evaluation =(LinearLayout)findViewById(R.id.linearlayout_evaluation);
        mLinearlayout_header_btn_container = (LinearLayout) findViewById(R.id.linearlayout_header_btn_container);
        mRelative_subsection_title_container = (RelativeLayout) findViewById(R.id.relative_subsection_title_container);
        mlinearlayout_section_btn_container = (LinearLayout) findViewById(R.id.linearlayout_section_btn_container);
        mLinearlayout_question_container = (LinearLayout) findViewById(R.id.linearlayout_question_container);
        mHorizontalScrollView_section = (HorizontalScrollView) findViewById(R.id.horizontalScrollView_section);
        mLinearlayout_question_container.setOnClickListener(this);
        mTextviewUsername = (TextView) findViewById(R.id.textview_username);
        mTextviewTitle = (TextView) findViewById(R.id.textview_title);
        mTextviewDate = (TextView) findViewById(R.id.textview_date);
        mTextviewDate.setVisibility(View.VISIBLE);
        mTextviewSubsectionTitle = (TextView) findViewById(R.id.textview_subsection_title);
        mImagviewBack = (ImageView) findViewById(R.id.imagview_back);
        mImagviewBack.setOnClickListener(this);
        mImageviewSubsectionLeft = (ImageView) findViewById(R.id.imageview_subsection_left);
        mImageviewSubsectionLeft.setOnClickListener(this);
        mImageviewSubsectionRight = (ImageView) findViewById(R.id.imageview_subsection_right);
        mImageviewSubsectionRight.setOnClickListener(this);
        mImageviewSectionLeft = (ImageView) findViewById(R.id.imageview_section_left);
        mImageviewSectionLeft.setOnClickListener(this);
        mImageviewSectionRight = (ImageView) findViewById(R.id.imageview_section_right);
        mImageviewSectionRight.setOnClickListener(this);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        mTextviewDate.setText(df.format(c.getTime()));
        mButton_score = (Button) findViewById(R.id.button_score);
        mButton_accept = (Button) findViewById(R.id.button_accept);
        mButton_accept.setOnClickListener(this);
        mButton_reject = (Button) findViewById(R.id.button_reject);
        mButton_reject.setOnClickListener(this);
        mButton_other_remarks = (Button) findViewById(R.id.button_other_remarks);
        mButton_other_remarks.setOnClickListener(this);
        mBtnPass = (Button) findViewById(R.id.button_pass);
        mBtnPass.setOnClickListener(this);
        mBtnFail = (Button) findViewById(R.id.button_fail);
        mBtnFail.setOnClickListener(this);
        mBtnNA = (Button) findViewById(R.id.button_na);
        mBtnNA.setOnClickListener(this);
        rootInstance = this;
        mDataBaseAdpter = new DataBaseAdpter(this);
        if (getIntent().getStringExtra("SCHEDULE_ID") != null) {
            mSession.setScheduleId(Integer.parseInt(getIntent().getStringExtra("SCHEDULE_ID")));
            if (mCommonMethod.isConnectingToInternet()) {
                new GetSectionListAsync().execute(getIntent().getStringExtra("SCHEDULE_ID"), "0");
            } else {
                mCommonMethod.alertDialog("Unable to connect", null);
            }
        }
        if (getIntent().getStringExtra("CHECKLIST_NAME") != null) {
            mTextviewTitle.setText(getIntent().getStringExtra("CHECKLIST_NAME")+" "+"Checklist");
        }
        mTextviewUsername.setText("Welcome: " + mSession.getUserName());

    }//oncreate end


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mCommonMethod.alertDialogMessage("Do you want to logout?", SubmittedAuditorsSectionListActivity.this, SubmittedAuditorsSectionListActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int childcount = mLinearlayout_header_btn_container.getChildCount();
        switch (v.getId()) {
            case R.id.button_accept:
                    mIsAcceptOrReject = 6;
                    alertDialogMessage("Accept Checklist ?", this, (Activity) this);
                break;
            case R.id.button_reject:
                if (mCommonMethod.isConnectingToInternet()) {
                    mIsAcceptOrReject = 5;
                    rejectReasonPopupInit();
                } else {
                    mCommonMethod.alertDialog("Unable to connect. Network not available", null);
                }
                break;
            case R.id.imagview_back:
                    finish();
                break;
            case R.id.imageview_section_left:
                mHorizontalScrollView_section.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                if(btnRightId!=0) {
                    btnRightId--;
                    View leftView = mLinearlayout_header_btn_container.getChildAt(btnRightId);
                    Button titleBtn = (Button) leftView;
                    titleBtn.setVisibility(View.VISIBLE);
                    mImageviewSectionRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                    mImageviewSectionLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                }
                if(btnRightId==0){
                    mImageviewSectionLeft.setBackgroundResource(R.drawable.arrow_big_left);
                    mImageviewSectionRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                }
                break;
            case R.id.imageview_section_right:
                mHorizontalScrollView_section.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                if (sectionList != null && sectionList.size() > 0) {
                    if (btnRightId < sectionList.size()-1) {
                        View rightView = mLinearlayout_header_btn_container.getChildAt(btnRightId);
                        Button btn = (Button) rightView;
                        btn.setVisibility(View.GONE);
                        btnRightId++;
                        mImageviewSectionRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                        mImageviewSectionLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                    }
                    if(btnRightId == (sectionList.size()-1)){
                        mImageviewSectionRight.setBackgroundResource(R.drawable.arrow_big_right);
                        mImageviewSectionLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                    }
                }
                break;
            case R.id.imageview_subsection_right:
                closeKeypad();
                // mSubSectionPositionRight=0;
                if (mTextviewSubsectionTitle.getTag() != null) {
                    if (mSubSectionPositionRight >= 0 && mSubSectionPositionRight < subSectionList.size() - 1) {
                        if (subSectionList != null && subSectionList.size() > 0) {
                            for (int i = 0; i < subSectionList.size(); i++) {
                                if (subSectionList.get(i).getSubSectionID() == Integer.parseInt(mTextviewSubsectionTitle.getTag().toString())) {
                                    mSubSectionPositionRight = i + 1;
                                    createSubSectionView(mSectionId, mSubSectionPositionRight);
                                    break;
                                }
                            }
                        }
                        if (mSubSectionPositionRight >= 0 && mSubSectionPositionRight < subSectionList.size() - 1) {
                            mSubSectionPositionLeft = 0;
                            createSubSectionView(mSectionId, mSubSectionPositionRight);
                            mImageviewSubsectionRight.setBackgroundResource(R.drawable.arrow_small_right_visible);
                            mImageviewSubsectionLeft.setBackgroundResource(R.drawable.arrow_small_left_visible);
                        }
                        if (mSubSectionPositionRight == subSectionList.size() - 1) {
                            mImageviewSubsectionRight.setBackgroundResource(R.drawable.arrow_small_right);
                            mImageviewSubsectionLeft.setBackgroundResource(R.drawable.arrow_small_left_visible);
                        }
                        currentsubSectionPosition++;
                    }
                }
                break;
            case R.id.imageview_subsection_left:
                closeKeypad();
                //mSubSectionPositionLeft=0;
                if (mTextviewSubsectionTitle.getTag() != null) {
                    if (mSubSectionPositionLeft >= 0 && mSubSectionPositionLeft < subSectionList.size() - 1) {
                        if (subSectionList != null && subSectionList.size() > 0) {
                            for (int i = 0; i < subSectionList.size(); i++) {
                                if (subSectionList.get(i).getSubSectionID() == Integer.parseInt(mTextviewSubsectionTitle.getTag().toString())) {
                                    mSubSectionPositionLeft = i - 1;
                                    break;
                                }
                            }
                        }
                        if (mSubSectionPositionLeft >= 0 && mSubSectionPositionLeft < subSectionList.size() - 1) {
                            mSubSectionPositionRight = 0;
                            createSubSectionView(mSectionId, mSubSectionPositionLeft);
                            mImageviewSubsectionLeft.setBackgroundResource(R.drawable.arrow_small_left_visible);
                            mImageviewSubsectionRight.setBackgroundResource(R.drawable.arrow_small_right_visible);
                        }
                        if (mSubSectionPositionLeft == 0) {
                            mImageviewSubsectionLeft.setBackgroundResource(R.drawable.arrow_small_left);
                            mImageviewSubsectionRight.setBackgroundResource(R.drawable.arrow_small_right_visible);
                        }
                        currentsubSectionPosition--;
                    }
                }
                break;
            case R.id.linearlayout_question_container:
                break;
            case R.id.button_other_remarks:
                if (mCommonMethod.isConnectingToInternet()) {
                    new GetOtherRemarksAsync().execute();
                } else {
                    mCommonMethod.alertDialog("Unable to add other remark. Network not available", null);
                }
                break;
            case R.id.button_pass:
                mBtnPass.setTextColor(Color.WHITE);
                mBtnFail.setTextColor(Color.BLACK);
                mBtnNA.setTextColor(Color.BLACK);
                Integer passSectionResult=mDataBaseAdpter.getSectionResult(mSectionId);
                if(passSectionResult==1) {
                    mBtnPass.setBackgroundResource(R.drawable.pass_btn_unselect_bg);
                    mDataBaseAdpter.updateSectionResult(mSectionId, 0);
                }else {
                    mBtnPass.setBackgroundResource(R.drawable.pass_btn_select_bg);
                    mDataBaseAdpter.updateSectionResult(mSectionId, 1);
                }
                mBtnFail.setBackgroundResource(R.drawable.fail_btn_unselect_bg);
                mBtnNA.setBackgroundResource(R.drawable.na_btn_unselect_bg);

                break;
            case R.id.button_fail:
                mBtnPass.setTextColor(Color.BLACK);
                mBtnFail.setTextColor(Color.WHITE);
                mBtnNA.setTextColor(Color.BLACK);
                mBtnPass.setBackgroundResource(R.drawable.pass_btn_unselect_bg);
                Integer failSectionResult=mDataBaseAdpter.getSectionResult(mSectionId);
                if(failSectionResult==2) {
                    mBtnFail.setBackgroundResource(R.drawable.fail_btn_unselect_bg);
                    mDataBaseAdpter.updateSectionResult(mSectionId, 0);
                }else {
                    mBtnFail.setBackgroundResource(R.drawable.fail_btn_select_bg);
                    mDataBaseAdpter.updateSectionResult(mSectionId, 2);
                }
                mBtnNA.setBackgroundResource(R.drawable.na_btn_unselect_bg);

                break;
            case R.id.button_na:
                mBtnPass.setTextColor(Color.BLACK);
                mBtnFail.setTextColor(Color.BLACK);
                mBtnNA.setTextColor(Color.WHITE);
                mBtnPass.setBackgroundResource(R.drawable.pass_btn_unselect_bg);
                mBtnFail.setBackgroundResource(R.drawable.fail_btn_unselect_bg);
                Integer naSectionResult=mDataBaseAdpter.getSectionResult(mSectionId);
                if(naSectionResult==3) {
                    mBtnNA.setBackgroundResource(R.drawable.na_btn_unselect_bg);
                    mDataBaseAdpter.updateSectionResult(mSectionId, 0);
                }else {
                    mBtnNA.setBackgroundResource(R.drawable.na_btn_select_bg);
                    mDataBaseAdpter.updateSectionResult(mSectionId, 3);
                }

                break;
        }
    }

    private void otherRemarksPopupInit(String message) {
        final Dialog mdialog = new Dialog(this);
        mdialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View mView = getLayoutInflater().inflate(R.layout.activity_other_remarks, null);

        mdialog.setContentView(mView);

        final EditText remarksEdtTxt = (EditText) mView.findViewById(R.id.remarks_Edt_txt);
        remarksEdtTxt.setText(message);
        if(!mSession.getEditable()){
            remarksEdtTxt.setEnabled(false);
            remarksEdtTxt.setFocusable(false);
        }else {
            remarksEdtTxt.setEnabled(true);
            remarksEdtTxt.setFocusable(true);
            remarksEdtTxt.setSelection(remarksEdtTxt.getText().length());
        }

        Button doneButton = (Button) mView.findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSession.getEditable()) {
                    new SaveOtherRemarksAsync().execute(remarksEdtTxt.getText().toString());
                }
                mdialog.dismiss();
            }
        });
        mdialog.setCanceledOnTouchOutside(false);
        mdialog.show();
        int width = (int)getResources().getDimension(R.dimen.other_remarks_popup_width);
        int height = (int)getResources().getDimension(R.dimen.other_remarks_popup_height);
        mdialog.getWindow().setLayout(width, height); //Controlling width and height.
    }

    private void rejectReasonPopupInit() {
        final Dialog mdialog = new Dialog(this);
        mdialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View mView = getLayoutInflater().inflate(R.layout.activity_reject_reason, null);

        mdialog.setContentView(mView);

        final EditText remarksEdtTxt = (EditText) mView.findViewById(R.id.remarks_Edt_txt);

        Button doneButton = (Button) mView.findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if(!remarksEdtTxt.getText().toString().equalsIgnoreCase("")) {
                      if (mCommonMethod.isConnectingToInternet()) {
                          //System.out.println("Submit Result--->"+ questionAnswer.toString());
                          new RejectChecklistAsync().execute(remarksEdtTxt.getText().toString().trim());
                      } else {
                          mCommonMethod.alertDialog("Unable to connect. Network not available", null);
                      }
                      mdialog.dismiss();
                  }else {
                      mCommonMethod.alertDialog("Please enter reason for rejection",null);
                  }

            }
        });
        Button cancelButton = (Button) mView.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mdialog.dismiss();
            }
        });
        mdialog.setCanceledOnTouchOutside(false);
        mdialog.show();
        int width = (int)getResources().getDimension(R.dimen.other_remarks_popup_width);
        int height = (int)getResources().getDimension(R.dimen.other_remarks_popup_height);
        mdialog.getWindow().setLayout(width, height); //Controlling width and height.
    }

    //xml parser for response value
    public List<SectionModel> xmlParser(String response) {
        SectionModel sectionModel = null;
        SubSectionModel subSectionModel = null;
        QuestionsModel questionsModel = null;
        AnswerColumnsModel answerColumnsModel = null,answerResponseColumnsModel=null;
        AnswerSectionsModel answerSectionsModel = null;
        AnswersModel answersModel = null;
        MediaModel mediaModel = null;
        String text = "", sectionId = "", subSectionId = "", hit = "", standardORClauses = "";
        Integer questionId = 0;
        boolean isAnswerSections = false,isAnswerSectionsAvail = false,
                isAnswerColumns = false, isAnswers = false, isUser = false, isConfiguration = false, isMedia = false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("Sections")) {
                            sectionModel = new SectionModel();
                        } else if (tagname.equalsIgnoreCase("SubSections")) {
                            subSectionModel = new SubSectionModel();
                        } else if (tagname.equalsIgnoreCase("Questions")) {
                            questionsModel = new QuestionsModel();
                            answersModel=new AnswersModel();
                            isConfiguration = false;
                            isMedia = false;
                        } else if (tagname.equalsIgnoreCase("StandardORClauses")) {
                            standardORClauses = parser.nextText();
                        } else if (tagname.equalsIgnoreCase("Hint")) {
                            hit = parser.nextText();
                        } else if (tagname.equalsIgnoreCase("AnswerSections")) {
                            isAnswerSections = true;
                            isAnswerSectionsAvail =true;
                            isAnswers = false;
                            answerSectionsModel = new AnswerSectionsModel();
                        } else if (tagname.equalsIgnoreCase("AnswerType")) {
                            isAnswerColumns = true;
                            answerColumnsModel = new AnswerColumnsModel();
                        } else if (tagname.equalsIgnoreCase("Answers")) {
                            isAnswerSections = false;
                            isAnswers = true;
                            answerResponseColumnsModel=new AnswerColumnsModel();
                        } else if (tagname.equalsIgnoreCase("Users")) {
                            isUser = true;
                        } else if (tagname.equalsIgnoreCase("Configuration")) {
                            isConfiguration = true;
                        } else if (tagname.equalsIgnoreCase("RepeatSectionTitle")) {
                            mRepeatSectionTitle = parser.nextText();
                        }else if (tagname.equalsIgnoreCase("ScheduleAnsSection")) {
                            mScheduleAnsSection = parser.nextText();
                        } else if(tagname.equalsIgnoreCase("QuestionMedia")){
                            isMedia = true;
                            mediaModel = new MediaModel();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("Sections")) {
                            // add Section object to list
                            sectionList.add(sectionModel);
                        } else if (isUser && tagname.equalsIgnoreCase("AssignedSections")) {
                            mAssignedSections = text.split(",");
                        } else if (tagname.equalsIgnoreCase("SectionID")) {
                            sectionId = text;
                            sectionModel.setSectionID(Integer.parseInt(text));
                        } else if (tagname.equalsIgnoreCase("SectionAbbr")) {
                            sectionModel.setSectionAbbr(text);
                        } else if (tagname.equalsIgnoreCase("SectionTitle")) {
                            sectionModel.setSectionTitle(text);
                        } else if (tagname.equalsIgnoreCase("ResultStatus")) {
                            sectionModel.setResultStatus(text);
                        } else if (tagname.equalsIgnoreCase("SubSectionID")) {
                            subSectionId = text;
                            if (!sectionId.equals("")) {
                                subSectionModel.setSectionID(Integer.parseInt(sectionId));
                            }
                            subSectionModel.setSubSectionID(Integer.parseInt(text));
                        } else if (tagname.equalsIgnoreCase("SubSectionAbbr")) {
                            subSectionModel.setSubSectionAbbr(text);
                        } else if (tagname.equalsIgnoreCase("SubSectionTitle")) {
                            subSectionModel.setSubSectionTitle(text);
                        } else if (tagname.equalsIgnoreCase("SubSections")) {
                            // add SubSection object to list
                            subSectionList.add(subSectionModel);
                        } else if (tagname.equalsIgnoreCase("Questions")) {
                            // add Questions object to list
                            questionsList.add(questionsModel);

                        } else if (!isMedia && tagname.equalsIgnoreCase("ScheduleQuestionID")) {
                            if (!subSectionId.equals("")) {
                                questionsModel.setSubSectionID(Integer.parseInt(subSectionId));
                            } else {
                                questionsModel.setSectionID(Integer.parseInt(sectionId));
                            }
                            questionId=Integer.parseInt(text);
                            questionsModel.setScheduleQuestionID(Integer.parseInt(text));
                        } else if (tagname.equalsIgnoreCase("QAbbrv")) {
                            questionsModel.setqAbbrv(text);
                        } else if (tagname.equalsIgnoreCase("QuestionText")) {
                            questionsModel.setQuestionText(text);
                        } else if (!isConfiguration && tagname.equalsIgnoreCase("IsMandatory")) {
                            questionsModel.setIsMandatory(Integer.parseInt(text));
                            if (!hit.equals("")) {
                                questionsModel.setHint(hit);
                            }
                            if (!standardORClauses.equals("")) {
                                questionsModel.setStandardORClauses(standardORClauses);
                            }
                        } else if (tagname.equalsIgnoreCase("Sequence")) {
                            questionsModel.setSequence(text);
                        } else if (tagname.equalsIgnoreCase("Remark")) {
                            questionsModel.setRemark(text);
                        }else if (tagname.equalsIgnoreCase("AnswerSections")) {
                            // add AnswerType object to list
                            answerSectionsList.add(answerSectionsModel);
                        } else if (!isAnswers && isAnswerSections && tagname.equalsIgnoreCase("ScheduleAnswerSectionID")) {
                            answerSectionsModel.setScheduleAnswerSectionID(Integer.parseInt(text));
                            if (!mRepeatSectionTitle.equals("")) {
                                answerSectionsModel.setRepeatSectionTitle(mRepeatSectionTitle);
                            }
                            answerSectionsModel.setSectionColumnType(mSectionColumnType);
                        }  else if (!isAnswerColumns&&isAnswerSections && tagname.equalsIgnoreCase("ScheduleChecklistID")) {
                            if (!mScheduleAnsSection.equals("")) {
                                answerSectionsModel.setScheduleAnsSection(mScheduleAnsSection);
                            }
                            answerSectionsModel.setScheduleChecklistID(Integer.parseInt(text));
                        }  else if (tagname.equalsIgnoreCase("AnswerType")) {
                            // add AnswerType object to list
                            answerTypeList.add(answerColumnsModel);
                        } else if (isAnswerColumns && tagname.equalsIgnoreCase("ScheduleAnswerTypeID")) {
                            answerColumnsModel.setScheduleAnswerTypeID(Integer.parseInt(text));
                        } else if (isAnswerColumns && tagname.equalsIgnoreCase("ScheduleChecklistID")) {
                            answerColumnsModel.setScheduleChecklistID(Integer.parseInt(text));
                        } else if (isAnswerColumns && tagname.equalsIgnoreCase("ScheduleAnsOptions")) {
                            answerColumnsModel.setScheduleAnsOptions(text);
                        } else if (isAnswerColumns && tagname.equalsIgnoreCase("ScheduleAnsOptionValue")) {
                            answerColumnsModel.setScheduleAnsOptionValue(text);
                        } else if (isAnswerColumns && tagname.equalsIgnoreCase("AnswerColor")) {
                            answerColumnsModel.setAnswerColor(text);
                        } else if (tagname.equalsIgnoreCase("AnswerColumns")) {
                            answerColumCount++;
                        }
                        /*if (isAnswers && tagname.equalsIgnoreCase("Answers")) {
                            // add Answer object to list
                            questionsModel.setAnswersModel(answersModel);
                        } else if (isAnswers && tagname.equalsIgnoreCase("IsSectionAssigned")) {
                            answersModel.setIsSectionAssigned(Integer.parseInt(text));
                        } else if (!isAnswerSections&&isAnswers && tagname.equalsIgnoreCase("ScheduleAnswerSectionID")) {
                            answersModel.setScheduleAnswerSectionID(Integer.parseInt(text));
                        }else if (isAnswers && tagname.equalsIgnoreCase("ScheduleAnswerTypeID")) {
                            answersModel.setScheduleAnswerTypeID(Integer.parseInt(text));
                        } else if (isAnswers && tagname.equalsIgnoreCase("QAnswerValue")) {
                            answersModel.setqAnswerValue(text);
                        }*/ if (isAnswers && tagname.equalsIgnoreCase("Answers")) {
                        if(answerResponseColumnsModel.getScheduleAnswerTypeID()!=0) {
                            answersPreviousSelectionList.add(answerResponseColumnsModel);
                        }

                    } else if (isAnswers && tagname.equalsIgnoreCase("IsSectionAssigned")) {
                        answerResponseColumnsModel.setScheduleQuestionID(questionId);
                        answerResponseColumnsModel.setSetIsSectionAssigned(Integer.parseInt(text));
                    } else if (!isAnswerSections&&isAnswers && tagname.equalsIgnoreCase("ScheduleAnswerSectionID")) {
                        answerResponseColumnsModel.setSetScheduleAnswerSectionID(Integer.parseInt(text));
                    } else if (isAnswers && tagname.equalsIgnoreCase("ScheduleAnswerID")) {
                        answerResponseColumnsModel.setScheduleAnswerID(Integer.parseInt(text));
                        answerResponseColumnsModel.setIsChecked(1);
                    }else if (isAnswers && tagname.equalsIgnoreCase("ScheduleAnswerTypeID")) {
                        answerResponseColumnsModel.setScheduleAnswerTypeID(Integer.parseInt(text));
                    } else if (isAnswers && tagname.equalsIgnoreCase("QAnswerValue")) {
                        answerResponseColumnsModel.setScheduleAnsOptionValue(text);
                    } else if (tagname.equalsIgnoreCase("ShowScore")) {
                        mIsScoreBtn = Integer.parseInt(text);
                    }else if (tagname.equalsIgnoreCase("ShowStatus")) {
                        mShowStatus = Integer.parseInt(text);
                    }else if (tagname.equalsIgnoreCase("SectionColumnType")) {
                        mSectionColumnType = Integer.parseInt(text);
                    }else if(tagname.equalsIgnoreCase("ScheduleStatus")){
                        mScheduleStatus=Integer.parseInt(text);
                    } else if (tagname.equalsIgnoreCase("QuestionMedia")) {
                        // add AnswerType object to list
                        mediaList.add(mediaModel);
                    } else if(isMedia && tagname.equalsIgnoreCase("QuestionMediaID")){
                        mediaModel.setQuestionMediaID(Integer.parseInt(text));
                    } else if(isMedia && tagname.equalsIgnoreCase("MediaPath")){
                        mediaModel.setImagePath(text);
                    } else if(isMedia && tagname.equalsIgnoreCase("ScheduleQuestionID")){
                        mediaModel.setScheduleQuestionID(Integer.parseInt(text));
                    }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sectionList;
    }

    //create the dynamic view for section
    public void createSubSectionView(int sectionId, int subSecPosition) {
        mSectionId = sectionId;
        List<QuestionsModel>  listQuestion=null;
        subSectionList = mDataBaseAdpter.getScheduleSubSectionList(mSectionId);
        if (subSectionList != null && subSectionList.size() > 0) {
            mImageviewSubsectionRight.setTag(subSectionList.size());
            mSubSectionId = subSectionList.get(subSecPosition).getSubSectionID();
            mTextviewSubsectionTitle.setTag(subSectionList.get(subSecPosition).getSubSectionID());
            mTextviewSubsectionTitle.setText(subSectionList.get(subSecPosition).getSubSectionAbbr() + "." + subSectionList.get(subSecPosition).getSubSectionTitle());
            mSubSectionId = subSectionList.get(subSecPosition).getSubSectionID();
            mSectionId = subSectionList.get(subSecPosition).getSectionID();
            listQuestion = mDataBaseAdpter.getScheduleQuestionDetails(mSectionId, mSubSectionId);
            mLinearlayout_question_container.removeAllViews();
            List<AnswerColumnsModel> answerType=mDataBaseAdpter.getScheduleAnswerType(0,0);
            mLinearlayout_question_container.addView(mQuestionSectionView.createQuestionSection(mSectionId, mSubSectionId, listQuestion, answerSectionsList, answerType, answerTypeList.size()));
            mImageviewSubsectionRight.setVisibility(View.VISIBLE);
            mImageviewSubsectionLeft.setVisibility(View.VISIBLE);
            if(subSectionList.size()==1){
                mImageviewSubsectionRight.setBackgroundResource(R.drawable.arrow_small_right);
            }else {
                mImageviewSubsectionRight.setBackgroundResource(R.drawable.arrow_small_right_visible);
            }
        } else {
            mImageviewSubsectionRight.setVisibility(View.INVISIBLE);
            mImageviewSubsectionLeft.setVisibility(View.INVISIBLE);
            if (sectionList.size() > 0) {
                for (int i = 0; i < sectionList.size(); i++) {
                    if (sectionList.get(i).getSectionID() == mSectionId) {
                        mTextviewSubsectionTitle.setText(sectionList.get(i).getSectionTitle());
                    }
                }
            }
            listQuestion = mDataBaseAdpter.getScheduleQuestionDetails(mSectionId, mSubSectionId);
            List<AnswerColumnsModel> answerType=mDataBaseAdpter.getScheduleAnswerType(0,0);
            mLinearlayout_question_container.removeAllViews();
            mLinearlayout_question_container.addView(mQuestionSectionView.createQuestionSection(mSectionId, mSubSectionId, listQuestion, answerSectionsList, answerType, answerTypeList.size()));

        }
    }

    private void scorePopupInit(View v) {

        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.score_popup_window, null, false);
       // int tabletSize =(int)getResources().getDimension(R.dimen.device_size);
        int popupWidth = 0;
        int popupHeight = 0;
        /*if(tabletSize>=720) {
            popupWidth = 1000;
            popupHeight = 550;
        }else{
            popupWidth = 500;
            popupHeight = 250;
        }*/
        popupWidth = (int)(getResources().getDimension(R.dimen.score_popup_width)/getResources().getDisplayMetrics().density);
        popupHeight = (int)(getResources().getDimension(R.dimen.score_popup_height)/getResources().getDisplayMetrics().density);
        final PopupWindow popupWindow = new PopupWindow(popupView, popupWidth, popupHeight, true);
        final LinearLayout overallprogress_container = (LinearLayout) popupView.findViewById(R.id.overallprogressContainer);
        final LinearLayout individualprogress_container = (LinearLayout) popupView.findViewById(R.id.sectionProgressContainer);


        List<AnswerColumnsModel> unSelectedListItem = mDataBaseAdpter.getQuestionAnswerDetails(0);
       ArrayList<String> mQuestionSections = new ArrayList<>();
        ArrayList<ScoreModel> mWholeQuestionSections = new ArrayList<>();
        ArrayList<ScoreModel> mMyQuestionSections = new ArrayList<>();
        Log.i("section id", "" + mSectionId);
        if (unSelectedListItem != null && unSelectedListItem.size() > 0) {
            for (AnswerColumnsModel answerColumnsModel : unSelectedListItem) {

                ScoreModel scoreModel = new ScoreModel();
                if(mQuestionSections!=null){
                    if(!mQuestionSections.contains(answerColumnsModel.getScheduleAnsOptions())){
                        mQuestionSections.add(answerColumnsModel.getScheduleAnsOptions());
                        scoreModel.setName(answerColumnsModel.getScheduleAnsOptions());
                        scoreModel.setValue(0);
                        mWholeQuestionSections.add(scoreModel);
                    }
                }else {
                    mQuestionSections.add(answerColumnsModel.getScheduleAnsOptions());
                    scoreModel.setName(answerColumnsModel.getScheduleAnsOptions());
                    scoreModel.setValue(0);
                    mWholeQuestionSections.add(scoreModel);
                }


            }

            for (int y=0;y<mWholeQuestionSections.size();y++) {
                ScoreModel scoreModel = mWholeQuestionSections.get(y);
                mMyQuestionSections.add(scoreModel);
            }
            List<AnswerColumnsModel> SelectedListItem = mDataBaseAdpter.getQuestionAnswerDetails(1);
            if (SelectedListItem != null && SelectedListItem.size() > 0) {
                for (AnswerColumnsModel answerColumnsModel : SelectedListItem) {
                    for (int y=0;y<mWholeQuestionSections.size();y++) {
                        ScoreModel scoreModel =mWholeQuestionSections.get(y);
                        if (scoreModel.getName().equalsIgnoreCase(answerColumnsModel.getScheduleAnsOptions())) {
                           int score = scoreModel.getValue();
                            score++;
                            ScoreModel temp = new ScoreModel();
                            temp.setValue(score);
                            temp.setName(scoreModel.getName());
                            mWholeQuestionSections.set(y,temp);
                            if(mSubSectionId!=0){
                                if(answerColumnsModel.getSubSectionID()==mSubSectionId){
                                    mMyQuestionSections.set(y,temp);
                                }
                            }else if(answerColumnsModel.getSectionID()!=0 && answerColumnsModel.getSectionID()==mSectionId){
                                    mMyQuestionSections.set(y,temp);
                            }

                        }
                    }


                }
            }

            Integer Total_Met_ans_count = 0, Remainings =0;
            if(mWholeQuestionSections!=null && mWholeQuestionSections.size()>0) {
                Total_Met_ans_count = mWholeQuestionSections.get(0).getValue();
                for (int y = 0; y < mWholeQuestionSections.size(); y++) {
                    TextView sectionsTextView = new TextView(this);
                    sectionsTextView.setPadding(5, 5, 5, 5);
                    sectionsTextView.setSingleLine(true);
                    sectionsTextView.setTextSize(14);
                    sectionsTextView.setTextColor(Color.BLACK);
                    sectionsTextView.setText(mWholeQuestionSections.get(y).getName());
                    overallprogress_container.addView(sectionsTextView);
                    TextView sepratorTextView = new TextView(this);
                    sepratorTextView.setPadding(5, 5, 5, 5);
                    sepratorTextView.setSingleLine(true);
                    sepratorTextView.setTextSize(14);
                    sepratorTextView.setTextColor(Color.BLACK);
                    sepratorTextView.setText(":");
                    overallprogress_container.addView(sepratorTextView);
                    TextView valueTextView = new TextView(this);
                    valueTextView.setPadding(5, 5, 5, 5);
                    valueTextView.setSingleLine(true);
                    valueTextView.setTextSize(14);
                    valueTextView.setTextColor(Color.BLACK);
                    valueTextView.setText(mWholeQuestionSections.get(y).getValue() + "");
                    overallprogress_container.addView(valueTextView);
                    if(!mWholeQuestionSections.get(y).getName().toLowerCase().contains("na")) {
                        Remainings = Remainings + mWholeQuestionSections.get(y).getValue();
                    }
                }
            }

            TextView textview_overallprogress_percentage = (TextView) popupView.findViewById(R.id.textview_overallprogress_percentage);
             double OverallProgress = ((double)Total_Met_ans_count/(double)Remainings)*100;
            textview_overallprogress_percentage.setText(getResources().getString(R.string.percentage_score)
                    +(int) Math.round(OverallProgress)+"%");

            Integer Total_My_Met_ans_count = 0, MyRemainings =0;
            if(mMyQuestionSections!=null && mMyQuestionSections.size()>0) {
                Total_My_Met_ans_count= mMyQuestionSections.get(0).getValue();
                for (int y = 0; y < mMyQuestionSections.size(); y++) {
                    Log.i("Title", mMyQuestionSections.get(y).getName());
                    Log.i("score",""+mMyQuestionSections.get(y).getValue());
                    TextView sectionsTextView = new TextView(this);
                    sectionsTextView.setPadding(5, 5, 5, 5);
                    sectionsTextView.setSingleLine(true);
                    sectionsTextView.setTextSize(14);
                    sectionsTextView.setTextColor(Color.BLACK);
                    sectionsTextView.setText(mMyQuestionSections.get(y).getName());
                    individualprogress_container.addView(sectionsTextView);
                    TextView sepratorTextView = new TextView(this);
                    sepratorTextView.setPadding(5, 5, 5, 5);
                    sepratorTextView.setSingleLine(true);
                    sepratorTextView.setTextSize(14);
                    sepratorTextView.setTextColor(Color.BLACK);
                    sepratorTextView.setText(":");
                    individualprogress_container.addView(sepratorTextView);
                    TextView valueTextView = new TextView(this);
                    valueTextView.setPadding(5, 5, 5, 5);
                    valueTextView.setSingleLine(true);
                    valueTextView.setTextSize(14);
                    valueTextView.setTextColor(Color.BLACK);
                    valueTextView.setText(mMyQuestionSections.get(y).getValue() + "");
                    individualprogress_container.addView(valueTextView);
                    if(!mMyQuestionSections.get(y).getName().toLowerCase().contains("na")) {
                        MyRemainings = MyRemainings + mMyQuestionSections.get(y).getValue();
                    }
                }
            }

            TextView textview_sectionscore_percentage = (TextView) popupView.findViewById(R.id.textview_sectionscore_percentage);
            double IndividualProgress = ((double)Total_My_Met_ans_count/(double)MyRemainings)*100;
            textview_sectionscore_percentage.setText(getResources().getString(R.string.percentage_score)
                    + (int) Math.round(IndividualProgress) + "%");
        }


        TextView textview_overallprogress_title = (TextView) popupView.findViewById(R.id.textview_overallprogress_title);
        textview_overallprogress_title.setPaintFlags(textview_overallprogress_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        TextView textview_sectionscore_title = (TextView) popupView.findViewById(R.id.textview_sectionscore_title);
        textview_sectionscore_title.setPaintFlags(textview_sectionscore_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        ImageView imageview_close = (ImageView) popupView.findViewById(R.id.imageview_close);
        imageview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.update();
        if (popupWindow != null) {
            // Closes the popup window when touch outside.
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            // Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(v, 0, 0);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == mRequestCamera) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    File picPath = getOutputPath();
                    Log.i("capture path-->", "" + picPath);
                    FileOutputStream fo = new FileOutputStream(picPath);
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 90, fo);
                    fo.close();
                    Bitmap myBitmap = decodeFile(picPath);
                    myBitmap = Bitmap.createScaledBitmap(myBitmap, 100, 100, true);
                    mQuestionSectionView.setImageBitmap(myBitmap, picPath.toString());
                } else if (requestCode == mRequestGallery) {
                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    Bitmap bitmap = getThumbnailBitmap(getRealPathFromURI(data.getData()),100);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                    Log.i("get the url path--->", "" + getRealPathFromURI(data.getData()));
                    mQuestionSectionView.setImageBitmap(bitmap, getRealPathFromURI(data.getData()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getThumbnailBitmap(final String path, final int thumbnailSize) {
        Bitmap bitmap;
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
            bitmap = null;
        }
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / thumbnailSize;
        bitmap = BitmapFactory.decodeFile(path, opts);
        return bitmap;
    }

    //check the file directory
    public File getOutputPath() {
        File mediaFile = null;
        try {
            File fileDir = new File(Environment.getExternalStorageDirectory() + "/MAudit/");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            mediaFile = new File(fileDir.getPath() + "/" + "IMG" + timeStamp + ".jpg");
            mediaFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaFile;
    }

    private Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String path = "";
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }




    public void alertDialogMessage(String message, final Context context, final Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SubmittedAuditorsSectionListActivity.this);
        // set title
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.title));
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        if (mCommonMethod.isConnectingToInternet()) {
                            //System.out.println("Submit Result--->"+ questionAnswer.toString());
                            new RejectChecklistAsync().execute("");
                        } else {
                            mCommonMethod.alertDialog("Unable to connect. Network not available", null);
                        }

                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }





    public class GetSectionListAsync extends AsyncTask<String, String, List<SectionModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle(getResources().getString(R.string.app_name));
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected List<SectionModel> doInBackground(String... params) {
            List<SectionModel> getSectionLists = null;
            String result = null;
            result = mWebservices.mGetScetionList(params[0], params[1]);
            //result = new Offline().sectionResponse;
            sectionList = new ArrayList<SectionModel>();
            sectionList.clear();
            subSectionList = new ArrayList<SubSectionModel>();
            questionsList = new ArrayList<QuestionsModel>();
            questionsList.clear();
            answerSectionsList = new ArrayList<AnswerSectionsModel>();
            answerSectionsList.clear();
            answerTypeList = new ArrayList<AnswerColumnsModel>();
            answersPreviousSelectionList = new ArrayList<AnswerColumnsModel>();
            mediaList = new ArrayList<MediaModel>();
            //parser the result xml
            getSectionLists = xmlParser(result);
            if (sectionList != null && sectionList.size() > 0) {
                for (int i=0;i<sectionList.size();i++){
                    if (mAssignedSections != null && mAssignedSections.length > 0) {
                        for (String sectionAbbr : mAssignedSections) {
                            SectionModel sectionModel = new SectionModel();
                            if (sectionAbbr.equalsIgnoreCase(sectionList.get(i).getSectionAbbr())) {
                                sectionModel.setSectionID(sectionList.get(i).getSectionID());
                                sectionModel.setSectionAbbr(sectionList.get(i).getSectionAbbr());
                                sectionModel.setSectionTitle(sectionList.get(i).getSectionTitle());
                                sectionModel.setResultStatus(sectionList.get(i).getResultStatus());
                                sectionModel.setIsSectionAssigned(1);
                                sectionList.set(i, sectionModel);
                            }
                        }
                    }
                }
                mDataBaseAdpter.insertScheduleSectionDetails(sectionList);
                sectionList = new ArrayList<SectionModel>();
            }
            if (subSectionList != null && subSectionList.size() > 0) {
                mDataBaseAdpter.insertScheduleSubSectionDetails(subSectionList);
            }
            if (questionsList != null && questionsList.size() > 0) {
                mDataBaseAdpter.insertScheduleQuestionnarie(questionsList, subSectionList);
            }

            if (answerSectionsList != null && answerSectionsList.size() > 0) {
                mDataBaseAdpter.insertScheduleAnswerMainSection(answerSectionsList);
            }

            if(mediaList !=null && mediaList.size()>0){
                int y= 0;
               for(int i=0;i<mediaList.size();i++){
                   QuestionsModel questionsModel =mDataBaseAdpter.getSectionSubSection(mediaList.get(i).getScheduleQuestionID());
                   MediaModel mMediaModel = new MediaModel();
                   mMediaModel.setImagePath(mediaList.get(i).getImagePath());
                   mMediaModel.setScheduleQuestionID(mediaList.get(i).getScheduleQuestionID());
                   mMediaModel.setIsFromServer(1);
                   if(questionsModel!=null) {
                       mMediaModel.setSubSectionId(questionsModel.getSubSectionID());
                       mMediaModel.setSectionId(questionsModel.getSectionID());
                   }
                   mMediaModel.setImageName("IMG1");
                   mMediaModel.setImageId(1);
                   mMediaModel.setQuestionMediaID(mediaList.get(i).getQuestionMediaID());

                   for(int j=0;j<questionsList.size();j++){
                       if(questionsList.get(j).getScheduleQuestionID()==mediaList.get(i).getScheduleQuestionID()){
                           mMediaModel.setCaptureId(j);
                       }
                   }
                    mDataBaseAdpter.insertScheduleQuestionMedia(mMediaModel);

               }
            }



            if (answersPreviousSelectionList!=null&&answersPreviousSelectionList.size()>0){
                mDataBaseAdpter.insertScheduleAnswerCheckedState(answersPreviousSelectionList,answerSectionsList);
            }
            if (answerTypeList != null && answerTypeList.size() > 0) {
                if (answerSectionsList != null && answerSectionsList.size() > 0) {
                    mDataBaseAdpter.insertScheduleAnswerSection(answerTypeList,answerSectionsList.size());
                    List<AnswerColumnsModel> answerType=mDataBaseAdpter.getScheduleAnswerType(0,0);
                    for(int i=0;i<questionsList.size();i++){
                        for (int j = 0; j < answerSectionsList.size(); j++) {
                            QuestionsModel questionsModel = questionsList.get(i);
                            List<AnswerColumnsModel> answerColumnsModelsList=new ArrayList<>();
                            for(AnswerColumnsModel answerColumnsModel:answerType){
                                if(answerColumnsModel.getAnswerColumnCount()==j){
                                    answerColumnsModelsList.add(answerColumnsModel);
                                }
                            }
                            mDataBaseAdpter.updateQuestionID(j,answerSectionsList.get(j).getScheduleAnswerSectionID(),questionsModel, answerColumnsModelsList);

                        }
                    }
                }else{
                    mDataBaseAdpter.insertScheduleAnswerSection(answerTypeList, 1);
                    List<AnswerColumnsModel> answerType=mDataBaseAdpter.getScheduleAnswerType(0,0);
                    for(int i=0;i<questionsList.size();i++) {
                        QuestionsModel questionsModel = questionsList.get(i);
                        mDataBaseAdpter.updateQuestionID(0,0,questionsModel, answerType);
                    }
                }
            }

            if (questionsList != null && questionsList.size() > 0) {
                questionsList = new ArrayList<QuestionsModel>();
                subSectionList = new ArrayList<SubSectionModel>();
            }

            sectionList = mDataBaseAdpter.getScheduleSectionDetails(mSession.getScheduleId());
            Log.i("section size-->", "" + getSectionLists.size());
            Log.i("subsection size-->", "" + subSectionList.size());
            Log.i("question size-->", "" + questionsList.size());
            Log.i("answer size-->", "" + answerTypeList.size());
           // Log.i("answersPreviousSelectionList size-->", "" + answersPreviousSelectionList.size());
            Log.i("answerSections size-->", "" + answerSectionsList.size());
            Log.i("Schedule id-->", "" + mSession.getScheduleId());

            return getSectionLists;
        }

        @Override
        protected void onPostExecute(List<SectionModel> result) {
            // create the header section button like as COP, IPSG
            if (result != null && result.size() > 0) {
                Log.i("result size", "");
                for (int i = 0; i < result.size(); i++) {
                    mBtnSection = new Button(SubmittedAuditorsSectionListActivity.this);
                    mBtnSection.setId(i);
                    mBtnSection.setTag(result.get(i).getSectionID());
                    mBtnSection.setText(result.get(i).getSectionAbbr());
                    // mBtnSection.setTypeface(null, R.style.buttonViewStyle);
                    if (i == 0) {
                        mSectionId = result.get(0).getSectionID();
                        mBtnSection.setBackgroundResource(R.drawable.btn_section);
                        mBtnSection.setTextColor(Color.parseColor("#FFFFFF"));
                        mBtnSection.setSelected(true);
                        if(mShowStatus == 1){
                            mlinearlayout_section_btn_container.setVisibility(View.VISIBLE);
                            showStatus();
                        }else {
                            mlinearlayout_section_btn_container.setVisibility(View.GONE);
                        }

                    } else {
                        mBtnSection.setBackgroundResource(R.drawable.btn_section_unselect);
                        mBtnSection.setSelected(false);
                    }


                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(5, 10, 5, 10);
                    mBtnSection.setLayoutParams(params);
                    mLinearlayout_header_btn_container.addView(mBtnSection);
                    mBtnSection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           closeKeypad();
                            Button btnSection = (Button) v;
                            int childcount = mLinearlayout_header_btn_container.getChildCount();
                            for (int i = 0; i < childcount; i++) {
                                View view = mLinearlayout_header_btn_container.getChildAt(i);
                                Button btn = (Button) view;
                                if (v.getId() == view.getId()) {
                                    btn.setBackgroundResource(R.drawable.btn_section);
                                    btn.setTextColor(Color.parseColor("#FFFFFF"));
                                    btn.setSelected(true);
                                } else {
                                    btn.setBackgroundResource(R.drawable.btn_section_unselect);
                                    btn.setTextColor(Color.parseColor("#000000"));
                                    btn.setSelected(false);
                                }
                            }
                            subSectionCount = 0;
                            mSubSectionPositionRight = 0;
                            mSubSectionPositionLeft = 0;
                            mSectionId = Integer.parseInt(v.getTag().toString());

                            mSession.setAssignedSection(false);

                            createSubSectionView(Integer.parseInt(v.getTag().toString()), 0);
                            mImageviewSubsectionLeft.setBackgroundResource(R.drawable.arrow_small_left);
                            showStatus();
                            if(mShowStatus == 1){
                                mlinearlayout_section_btn_container.setVisibility(View.VISIBLE);
                                showStatus();
                            }else {
                                mlinearlayout_section_btn_container.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                //get the question text based on  section_id or subsection_id
                if (subSectionList != null && subSectionList.size() > 0) {
                    mTextviewSubsectionTitle.setTag(subSectionList.get(0).getSubSectionID());
                    mTextviewSubsectionTitle.setText(subSectionList.get(0).getSubSectionAbbr() + "." + subSectionList.get(0).getSubSectionTitle());
                    mSubSectionId = subSectionList.get(0).getSubSectionID();
                    mSectionId = subSectionList.get(0).getSectionID();
                    createSubSectionView(subSectionList.get(0).getSectionID(), 0);
                } else {
                    mSectionId = sectionList.get(0).getSectionID();
                    createSubSectionView(sectionList.get(0).getSectionID(), 0);
                }


                    mButton_score.setVisibility(View.VISIBLE);
                    mButton_score.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scorePopupInit(v);
                        }
                    });
            }
            mProgressDialog.dismiss();
        }
    }

    public void showStatus(){
        if(!mSession.getEditable()||!mSession.getAssignedSection()){
            mBtnPass.setEnabled(false);
            mBtnFail.setEnabled(false);
            mBtnNA.setEnabled(false);
        }else {
            mBtnPass.setEnabled(true);
            mBtnFail.setEnabled(true);
            mBtnNA.setEnabled(true);
        }
        Integer failSectionResult=mDataBaseAdpter.getSectionResult(mSectionId);
        if(failSectionResult==1){
            mBtnPass.setTextColor(Color.WHITE);
            mBtnFail.setTextColor(Color.BLACK);
            mBtnNA.setTextColor(Color.BLACK);
            mBtnPass.setBackgroundResource(R.drawable.pass_btn_select_bg);
            mBtnFail.setBackgroundResource(R.drawable.fail_btn_unselect_bg);
            mBtnNA.setBackgroundResource(R.drawable.na_btn_unselect_bg);
        }else if(failSectionResult==2){
            mBtnPass.setTextColor(Color.BLACK);
            mBtnFail.setTextColor(Color.WHITE);
            mBtnNA.setTextColor(Color.BLACK);
            mBtnPass.setBackgroundResource(R.drawable.pass_btn_unselect_bg);
            mBtnFail.setBackgroundResource(R.drawable.fail_btn_select_bg);
            mBtnNA.setBackgroundResource(R.drawable.na_btn_unselect_bg);
        }else if(failSectionResult==3){
            mBtnPass.setTextColor(Color.BLACK);
            mBtnFail.setTextColor(Color.BLACK);
            mBtnNA.setTextColor(Color.WHITE);
            mBtnPass.setBackgroundResource(R.drawable.pass_btn_unselect_bg);
            mBtnFail.setBackgroundResource(R.drawable.fail_btn_unselect_bg);
            mBtnNA.setBackgroundResource(R.drawable.na_btn_select_bg);
        }else {
            mBtnPass.setTextColor(Color.BLACK);
            mBtnFail.setTextColor(Color.BLACK);
            mBtnNA.setTextColor(Color.BLACK);
            mBtnPass.setBackgroundResource(R.drawable.pass_btn_unselect_bg);
            mBtnFail.setBackgroundResource(R.drawable.fail_btn_unselect_bg);
            mBtnNA.setBackgroundResource(R.drawable.na_btn_unselect_bg);
        }
    }

    private void closeKeypad() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public class RejectChecklistAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle(getResources().getString(R.string.app_name));
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            result = mWebservices.mRejectChecklist(Integer.parseInt(mSession.getUserId()),mSession.getScheduleId(),mIsAcceptOrReject, params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
            SubmittedAuditorListActivity.defaultInstance().recallChecklist();
            finish();
        }
    }

    public void alertCloseSectionsDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SubmittedAuditorsSectionListActivity.this);
        // set title
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.title));
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                /*.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       finish();
                    }
                });*/
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        // show it
        alertDialog.show();

    }


    public class GetOtherRemarksAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle(getResources().getString(R.string.app_name));
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            result = mWebservices.mGetOtherRemarks(mSession.getScheduleId());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
            String result = getRemarksXmlParser(s);
            otherRemarksPopupInit(result);
        }
    }

    //xml parser for response value
    public String getRemarksXmlParser(String response) {
       String text = "", mAuditorComment = "";
       try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("AuditorComment")) {
                            mAuditorComment = parser.nextText();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("AuditorComment")) {
                            // add Section object to list
                            if(!mAuditorComment.equals("")){
                                mAuditorComment = text;
                            }
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mAuditorComment;
    }

    public class SaveOtherRemarksAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle(getResources().getString(R.string.app_name));
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            result = mWebservices.mSaveOtherRemarks(mSession.getScheduleId(),params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
        }
    }


}

package com.maudit.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.maudit.database.DataBaseAdpter;
import com.maudit.main.R;
import com.maudit.main.RemarksActivity;
import com.maudit.main.SectionListActivity;
import com.maudit.model.AnswerColumnsModel;
import com.maudit.model.AnswerSectionsModel;
import com.maudit.model.MediaModel;
import com.maudit.model.QuestionsModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class SubmittedQuestionSectionView extends LinearLayout   {
    private Context context;
    private LinearLayout mMainLinearLayoutView, mLinearLayout_single_top;
    private LinearLayout mLinearlayout_rightside_options,mLinearlayout_first_row_options_container,mLinearlayout_question_container,mLinearlayout_question_row_container,mLinearlayout_camera_options_container,mLinearlayout_second_row_options_container;
    private CustomTextView mCustomTextView;
    List<QuestionsModel>  questionsModelList;
    List<AnswerColumnsModel>  answerColumnsList;
    List<AnswerSectionsModel> answerSectionsList;
    private TextView mEvalautionView, sectionsTextView;
    View popupView,viewHorizontal,viewLine;
    PopupWindow popupWindow;
    private List<MediaModel> mListofCameraPath,mListofCameraPathLocalDB,mMediaDialogView;
    private ImageView imageview1,imageview2,imageview3;
    private Session mSession;
    int tabletSize=0,density=0,sectionId=0,subSectionId=0;
    private int  mEvalutionColumnWidth,questionItemColumnHeight,mRequestCamera=100,mRequestGallery=101, mImageViewButtonId =0,mQuestionRowCount=0,
                 answerColumnCount=0,mPopWindowWidth=0,mPopWindowHeight=0,mQuestionId=0,mSelectLeftPosition=0,mDialogImg=0,mSelectRightPosition=0;
    private MediaModel mMediaModel;
    private DataBaseAdpter mDataBaseAdpter;
    private QuestionsModel mQuestionsModel;
    private  ImageView mImageViewCameraSelection, mImageViewDisplay;
    private  Dialog dialogImageview;
    private CommonMethod mCommonMethod;
    Point p;
    HorizontalScrollView mHorizontalscrollview_container, mHorizontalTitlescrollview_container;
    private int _day;
    private int _month;
    private int _birthYear;
    Webservices mWebservices;
    ImageUrlValidator mImageUrlValidator;
    int currentPosition;
    private float x1,x2;
    private String mStoredSectionValue="";
    int headerEditxtViewId = 0;
    DatePickerDialog datePickerDialog = null;
    private Integer rowHeight = 0, tempColumnId = 0;
    final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
            ViewTreeObserver.OnScrollChangedListener() {

                @Override
                public void onScrollChanged() {
                    //do stuff here
                    int scrollX = mHorizontalscrollview_container.getScrollX(); //for horizontalScrollView
                    int scrollY = mHorizontalscrollview_container.getScrollY(); //for verticalScrollView
                    Log.i("scroll x",scrollX+"");
                    mHorizontalTitlescrollview_container.scrollTo(scrollX, scrollY);
                }
            };

    public SubmittedQuestionSectionView(Context context, AttributeSet attrs){
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
      // setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        this.context=context;
        mSession=new Session(context);
        mQuestionsModel=new QuestionsModel();
        mDataBaseAdpter=new DataBaseAdpter(context);
        mCommonMethod=new CommonMethod(context);
        mWebservices = new Webservices(context);
        mImageUrlValidator = new ImageUrlValidator();
        mListofCameraPath =new ArrayList<>();
        //density is check the device density like as mdpi, xhdpi
        density =(int)getResources().getDisplayMetrics().density;
        //tabletSize is check the tablet device is 7-inch or 10-inch
        tabletSize= (int)(getResources().getDimension(R.dimen.device_size)/getResources().getDisplayMetrics().density);
        mPopWindowWidth=(int)(getResources().getDimension(R.dimen.pop_window_width));
        mPopWindowHeight=(int)(getResources().getDimension(R.dimen.pop_window_height))-100;
        mDialogImg=(int)(getResources().getDimension(R.dimen.dialog_img_width));
        mEvalutionColumnWidth =(int)(getResources().getDimension(R.dimen.column_evaluation_option_width));
        questionItemColumnHeight=(int)(getResources().getDimension(R.dimen.question_item_column_height));


    }
    public LinearLayout createQuestionSection(final int sectionId, final int subSectionId, List<QuestionsModel> questionsModelList, final List<AnswerSectionsModel> answerSectionsList, List<AnswerColumnsModel> answerColumnsList, int answerColumnCount){
        this.sectionId=sectionId;
        this.subSectionId=subSectionId;
        this.questionsModelList=questionsModelList;
        this.answerColumnsList=answerColumnsList;
        this.answerColumnCount=answerColumnCount;
        this.answerSectionsList = answerSectionsList;

        mQuestionRowCount=questionsModelList.size();
                mMainLinearLayoutView = (LinearLayout) View.inflate(
                        context,
                        R.layout.fragment_cop, null);
        //mScrollView_container=(ScrollView)mMainLinearLayoutView.findViewById(R.id.scrollView_container);
        mLinearlayout_question_container=(LinearLayout) mMainLinearLayoutView.findViewById(R.id.linearlayout_question_container);
        mLinearlayout_rightside_options=(LinearLayout) mMainLinearLayoutView.findViewById(R.id.linearlayout_rightside_options);
        mLinearlayout_second_row_options_container=(LinearLayout) mMainLinearLayoutView.findViewById(R.id.linearlayout_second_row_options_container);
        mLinearlayout_first_row_options_container=(LinearLayout) mMainLinearLayoutView.findViewById(R.id.linearlayout_first_row_options_container);
        mLinearlayout_question_row_container=(LinearLayout) mMainLinearLayoutView.findViewById(R.id.linearlayout_question_row_container);
        mLinearlayout_camera_options_container=(LinearLayout) mMainLinearLayoutView.findViewById(R.id.linearlayout_camera_options_container);
        int fontSize = (int) (getResources().getDimension(R.dimen.txt_sub_row_title) / getResources().getDisplayMetrics().density);
        int edtTxtWidth = (int) (getResources().getDimension(R.dimen.top_edt_txt_width) / getResources().getDisplayMetrics().density);

        //check box section title
        if (answerSectionsList != null && answerSectionsList.size() > 0) {
            //  int viewColumnWidth = mEvalutionColumnWidth * answerColumnsList.size();
            int viewColumnWidth =mEvalutionColumnWidth*answerColumnCount;
            viewLine = new View(context);
            viewLine.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
            viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
            mLinearlayout_second_row_options_container.addView(viewLine);

                TextView mRepeatSectionTitle_txt = (TextView) mMainLinearLayoutView.findViewById(R.id.repeatSectionTitle_txt);
                if(answerSectionsList.get(0).getRepeatSectionTitle()!=null &&
                        !answerSectionsList.get(0).getRepeatSectionTitle().equalsIgnoreCase("")) {
                    mRepeatSectionTitle_txt.setVisibility(View.VISIBLE);
                    mRepeatSectionTitle_txt.setText(answerSectionsList.get(0).getRepeatSectionTitle());
                    mRepeatSectionTitle_txt.setTextSize(fontSize);
                }
                for (int i = 0; i < answerSectionsList.size(); i++) {
                    viewLine = new View(context);
                    viewLine.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
                    viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
                    mLinearLayout_single_top = new LinearLayout(context);
                    mLinearLayout_single_top.setOrientation(LinearLayout.HORIZONTAL);
                    mLinearLayout_single_top.setId(i+66);
                    LinearLayout.LayoutParams paramsLinear =
                            new LinearLayout.LayoutParams(viewColumnWidth, LayoutParams.MATCH_PARENT);
                    mLinearLayout_single_top.setGravity(Gravity.LEFT);
                    paramsLinear.gravity = Gravity.CENTER;
                    //paramsLinear.setMargins(5, 8, context.getResources().getInteger(R.integer.top_edt_right_padding), 8);
                    paramsLinear.setMargins(0, 8, 0, 8);
                    mLinearLayout_single_top.setLayoutParams(paramsLinear);
                    TextView sectionsTextView = new TextView(context);
                    sectionsTextView.setPadding(10, 0, 10, 0);
                    sectionsTextView.setId(i);
                    sectionsTextView.setTextSize(fontSize);
                    sectionsTextView.setText(answerSectionsList.get(i).getScheduleAnsSection());
                    sectionsTextView.setSingleLine(true);
                    sectionsTextView.setEllipsize(TextUtils.TruncateAt.END);
                    sectionsTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    if (Build.VERSION.SDK_INT < 23) {
                        sectionsTextView.setTextAppearance(context, R.style.textViewStyleHeader);
                    } else {
                        sectionsTextView.setTextAppearance(R.style.textViewStyleHeader);
                    }
                    final LayoutParams viewParams = new LayoutParams(viewColumnWidth, LayoutParams.WRAP_CONTENT);
                    viewParams.gravity = Gravity.CENTER;
                    viewParams.setMargins(5, 0, 15, 0);
                    sectionsTextView.setLayoutParams(viewParams);
                    mLinearLayout_single_top.addView(sectionsTextView);
                    mLinearlayout_first_row_options_container.addView(mLinearLayout_single_top);
                    mLinearlayout_first_row_options_container.addView(viewLine);
                    //update the sectionId OR SubSectionID to AnswerMainSectiontable
                    mDataBaseAdpter.updateSectionId(sectionId,subSectionId,answerSectionsList.get(i).getScheduleAnswerSectionID(),answerSectionsList.get(i).getScheduleAnsSection());
                }

        }

        if(answerSectionsList!=null && answerSectionsList.size()>0) {
            for (int j = 0; j < answerSectionsList.size(); j++) {
                createEvalationOption(j);
            }
        }else{
            createEvalationOption(0);
        }
        List<String> listItemWeight=new ArrayList<>();
        listItemWeight.add("1.8");
        listItemWeight.add("1.5");
        listItemWeight.add(".8");
        //bind the question elements like as S/N, Standards, Question elements
        for(int i=0;i<questionsModelList.size();i++){
            mCustomTextView=new CustomTextView(context);
            QuestionsModel questionsModel=questionsModelList.get(i);
            mCustomTextView.questionsElement(questionsModel.getScheduleQuestionID()+26,questionsModel, listItemWeight);
            viewLine=new View(context);
            viewLine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
            viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
            mLinearlayout_question_row_container.addView(mCustomTextView);
            mLinearlayout_question_row_container.addView(viewLine);
        }
        //get view width and height at runtime
        /*ViewTreeObserver observer = mLinearlayout_question_row_container.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                init();
                mLinearlayout_question_row_container.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });*/
        // evaluation options, design radio button
        tempColumnId = 0;
        for(int i=0;i<mQuestionRowCount;i++){
            LinearLayout linearLayoutOption=new LinearLayout(context);
            linearLayoutOption.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutOption.setBackgroundColor(Color.WHITE);
            linearLayoutOption.setId(i+129);
            linearLayoutOption.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            if(answerSectionsList!=null && answerSectionsList.size()>0){
                for (int j = 0; j < answerSectionsList.size(); j++) {
                    tempColumnId = tempColumnId+1876;
                    QuestionsModel questionsModel = questionsModelList.get(i);
                    AnswerSectionsModel answerSectionsModel = answerSectionsList.get(j);
                    List<AnswerColumnsModel> answerColumnsModelsList=new ArrayList<>();
                    for(AnswerColumnsModel answerColumnsModel:answerColumnsList){
                        if(answerColumnsModel.getAnswerColumnCount()==j){
                            answerColumnsModelsList.add(answerColumnsModel);
                        }
                    }
                    mDataBaseAdpter.updateQuestionID(j,answerSectionsModel.getScheduleAnswerSectionID(),questionsModel, answerColumnsModelsList);
                    List<AnswerColumnsModel> answerTypeList=new ArrayList<>();
                    answerTypeList= mDataBaseAdpter.getScheduleAnswerType(j,questionsModel.getScheduleQuestionID());
                    mCustomTextView = new CustomTextView(context);
                    mCustomTextView.radioButtonView(tempColumnId, answerSectionsModel, answerTypeList, questionsModel, sectionId, subSectionId);
                    linearLayoutOption.addView(mCustomTextView);
                }
            }else {
                QuestionsModel questionsModel = questionsModelList.get(i);
                AnswerSectionsModel answerSectionsModel = null;
                 mDataBaseAdpter.updateQuestionID(0,0,questionsModel,answerColumnsList);
                List<AnswerColumnsModel> answerTypeList=new ArrayList<>();
                answerTypeList= mDataBaseAdpter.getScheduleAnswerType(0,questionsModel.getScheduleQuestionID());
                mCustomTextView = new CustomTextView(context);
                mCustomTextView.radioButtonView(0, answerSectionsModel, answerTypeList, questionsModel, sectionId, subSectionId);
                linearLayoutOption.addView(mCustomTextView);
            }
            viewLine=new View(context);
            viewLine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
            viewLine.setPadding(5, 0, 0, 0);
            viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
            mLinearlayout_rightside_options.addView(linearLayoutOption);
            mLinearlayout_rightside_options.addView(viewLine);
        }

        cameraOption(questionsModelList);
        tempColumnId = 0;
        for(int i=0;i<mQuestionRowCount;i++) {
            if(answerSectionsList!=null && answerSectionsList.size()>0) {
                for (int j = 0; j < answerSectionsList.size(); j++) {
                    tempColumnId = tempColumnId+1876;
                    final QuestionsModel questionsModel = questionsModelList.get(i);
                    final LinearLayout chilLayut = (LinearLayout) mLinearlayout_question_row_container.findViewById(questionsModel.getScheduleQuestionID() + 26);
                    ViewTreeObserver observer = chilLayut.getViewTreeObserver();
                    final LinearLayout checkLayout = (LinearLayout)mLinearlayout_rightside_options.findViewById(i+129);
                    final RadioGroup radioGroup = (RadioGroup) mLinearlayout_rightside_options.findViewById(tempColumnId);
                    final LinearLayout cameraLayout = (LinearLayout)mLinearlayout_camera_options_container.findViewById(i+1999);

                    observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

                            rowHeight = chilLayut.getHeight();
                            questionsModel.setQuestionHeight(chilLayut.getHeight());
                            Log.i("Q1 Height", rowHeight + "");
                            checkLayout.getLayoutParams().height = rowHeight;
                            checkLayout.requestLayout();
                            if (radioGroup != null) {
                                radioGroup.getLayoutParams().height = rowHeight;
                                radioGroup.requestLayout();
                            }
                            cameraLayout.getLayoutParams().height = rowHeight;
                            cameraLayout.requestLayout();
                            chilLayut.getViewTreeObserver().removeGlobalOnLayoutListener(
                                    this);

                        }
                    });
                }
            }else {
                final QuestionsModel questionsModel = questionsModelList.get(i);
                final LinearLayout chilLayut = (LinearLayout) mLinearlayout_question_row_container.findViewById(questionsModel.getScheduleQuestionID() + 26);
                ViewTreeObserver observer = chilLayut.getViewTreeObserver();
                final LinearLayout checkLayout = (LinearLayout)mLinearlayout_rightside_options.findViewById(i+129);
                final RadioGroup radioGroup = (RadioGroup) mLinearlayout_rightside_options.findViewById(questionsModel.getScheduleQuestionID()+44);
                final LinearLayout cameraLayout = (LinearLayout)mLinearlayout_camera_options_container.findViewById(i+1999);

                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        rowHeight = chilLayut.getHeight();
                        questionsModel.setQuestionHeight(chilLayut.getHeight());
                        Log.i("Q2 Height", rowHeight + "");
                        checkLayout.getLayoutParams().height = rowHeight;
                        checkLayout.requestLayout();
                        if (radioGroup != null) {
                            radioGroup.getLayoutParams().height = rowHeight;
                            radioGroup.requestLayout();
                        }
                        cameraLayout.getLayoutParams().height = rowHeight;
                        cameraLayout.requestLayout();
                        chilLayut.getViewTreeObserver().removeGlobalOnLayoutListener(
                                this);

                    }
                });
            }
        }

        mLinearlayout_rightside_options.post(new Runnable() {
            public void run() {
                int width = mLinearlayout_rightside_options.getWidth();

                mLinearlayout_first_row_options_container.getLayoutParams().width = width;
                mLinearlayout_second_row_options_container.getLayoutParams().width = width;
                mLinearlayout_first_row_options_container.requestLayout();
                mLinearlayout_second_row_options_container.requestLayout();
                if(answerSectionsList!=null && answerSectionsList.size()>0) {
                    for (int j = 0; j < answerSectionsList.size(); j++) {
                        final LinearLayout headerLayout = (LinearLayout)  mLinearlayout_first_row_options_container.findViewById(j+66);
                        if(headerLayout!=null) {
                            Log.i("Q2 Height%%%%%%%%55555",  "&&&&&&&&&&&&");
                            headerLayout.getLayoutParams().width = (width/answerSectionsList.size())-1;
                            headerLayout.requestLayout();
                        }
                    }
                }
                //Toast.makeText(context,""+width,Toast.LENGTH_LONG).show();
            }
        });


        mHorizontalscrollview_container = (HorizontalScrollView) mMainLinearLayoutView.findViewById(R.id.horizontalscrollview_container);
        mHorizontalTitlescrollview_container = (HorizontalScrollView) mMainLinearLayoutView.findViewById(R.id.horizontaltitlescrollview_container);
        mHorizontalscrollview_container.setOnTouchListener(new OnTouchListener() {
            private ViewTreeObserver observer;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                if (observer == null) {
                    observer = mHorizontalscrollview_container.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                }
                else if (!observer.isAlive()) {
                    observer.removeOnScrollChangedListener(onScrollChangedListener);
                    observer = mHorizontalscrollview_container.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                }
                return false;
            }

        });
        mHorizontalTitlescrollview_container.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub


                return true;
            }

        });

        return mMainLinearLayoutView;
    }

    public SubmittedQuestionSectionView cameraOption(List<QuestionsModel> questionsList){
        //questionItemColumnHeight =(int)(context.getResources().getDimension(R.dimen.question_item_column_height)/context.getResources().getDisplayMetrics().density);
        // camera option column
        for(int i=0;i<questionsList.size();i++){
            QuestionsModel questionsModel=questionsList.get(i);
            LinearLayout linearLayoutOption=new LinearLayout(context);
            linearLayoutOption.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutOption.setBackgroundColor(Color.WHITE);
            linearLayoutOption.setId(i+1999);
            //linearLayoutOption.setGravity(Gravity.CENTER);
            LayoutParams linearContainerParam= new LayoutParams(LayoutParams.MATCH_PARENT,questionItemColumnHeight);
            linearLayoutOption.setLayoutParams(linearContainerParam);
            RelativeLayout cameraLayoutOption=new RelativeLayout(context);
            //cameraLayoutOption.setId(i);
            RelativeLayout.LayoutParams frameContainerParam= new RelativeLayout.LayoutParams((int)(getResources().getDimension(R.dimen.camera_icon_cell_width)),questionItemColumnHeight);
            frameContainerParam.addRule(RelativeLayout.CENTER_IN_PARENT);
            cameraLayoutOption.setLayoutParams(frameContainerParam);
            cameraLayoutOption.setGravity(Gravity.CENTER);
            mImageViewCameraSelection =new ImageView(context);
            mImageViewCameraSelection.setId(i+13);
            mImageViewCameraSelection.setTag(questionsModel);
            mImageViewCameraSelection.setImageResource(R.drawable.camera_icon);
            RelativeLayout.LayoutParams  mImageViewCameraParam= new RelativeLayout.LayoutParams((int)(getResources().getDimension(R.dimen.camera_icon_cell_width)), LayoutParams.WRAP_CONTENT);
            mImageViewCameraParam.addRule(RelativeLayout.CENTER_IN_PARENT);
            mImageViewCameraSelection.setLayoutParams(mImageViewCameraParam);
            final Button imageCountButton =  new Button(context);
            imageCountButton.setId(i+20);
            Drawable mdrawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mdrawable = getResources().getDrawable(R.drawable.sections_camera_count_bg,context.getTheme());
            } else {
                mdrawable = getResources().getDrawable(R.drawable.sections_camera_count_bg);
            }
            int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                imageCountButton.setBackgroundDrawable(mdrawable);
            } else {
                imageCountButton.setBackground(mdrawable);
            }
            imageCountButton.setGravity(Gravity.CENTER);
            imageCountButton.setTextColor(Color.WHITE);
            mQuestionsModel = questionsModel;
            mListofCameraPathLocalDB=mDataBaseAdpter.getScheduleQuestionMediaCaptureId(mQuestionsModel.getSectionID(), mQuestionsModel.getSubSectionID(), mQuestionsModel.getScheduleQuestionID());
            if (mListofCameraPathLocalDB != null && mListofCameraPathLocalDB.size() > 0) {
                imageCountButton.setText(""+mListofCameraPathLocalDB.size());
            }else {
                imageCountButton.setText("0");
            }
            imageCountButton.setTextSize(25);
            //imageCountButton.setTypeface(null, Typeface.BOLD);
            RelativeLayout.LayoutParams imageCountButtonlinearParam = new RelativeLayout.LayoutParams((int)(context.getResources().getDimension(R.dimen.camera_image_count_button_width)),(int)(context.getResources().getDimension(R.dimen.camera_image_count_button_width)));
            imageCountButtonlinearParam.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            imageCountButtonlinearParam.addRule(RelativeLayout.ALIGN_RIGHT, i + 13);
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int densityDpi = dm.densityDpi;
            if (tabletSize == 600) {
                imageCountButtonlinearParam.setMargins(0, 10, 40, 0);
            } else if (densityDpi <= 160) {
                imageCountButtonlinearParam.setMargins(0, 10, 40, 0);
            } else {
                imageCountButtonlinearParam.setMargins(0, 20, 50, 0);
            }
            cameraLayoutOption.addView(mImageViewCameraSelection);
            cameraLayoutOption.addView(imageCountButton, imageCountButtonlinearParam);
            linearLayoutOption.addView(cameraLayoutOption);
            mImageViewCameraSelection.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListofCameraPathLocalDB = new ArrayList<>();
                    mQuestionsModel = (QuestionsModel) v.getTag();
                    //check the local DB images based on the questionId& imageId
                    mImageViewButtonId = v.getId() - 13;
                    mQuestionId = mQuestionsModel.getScheduleQuestionID();
                    LinearLayout viewGroup = (LinearLayout) findViewById(R.id.linearlayout_capture_container);
                    LayoutInflater layoutInflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popupView = layoutInflater.inflate(R.layout.submitted_capture_image_popwindow, viewGroup);
                    popupWindow = new PopupWindow(context);
                    popupWindow.setContentView(popupView);
                    popupWindow.setWidth(mPopWindowWidth);
                    popupWindow.setHeight(mPopWindowHeight);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    imageview1 = (ImageView) popupView.findViewById(R.id.imageview1);
                    imageview2 = (ImageView) popupView.findViewById(R.id.imageview2);
                    imageview3 = (ImageView) popupView.findViewById(R.id.imageview3);

                    mListofCameraPathLocalDB=mDataBaseAdpter.getScheduleQuestionMediaCaptureId(mQuestionsModel.getSectionID(), mQuestionsModel.getSubSectionID(), mQuestionsModel.getScheduleQuestionID());

                    if (mListofCameraPathLocalDB != null && mListofCameraPathLocalDB.size() > 0) {
                        if(mListofCameraPathLocalDB.size() == 1) {
                            imageview1.setEnabled(true);
                            imageview2.setEnabled(false);
                            imageview3.setEnabled(false);
                        }else if(mListofCameraPathLocalDB.size() == 2) {
                            imageview1.setEnabled(true);
                            imageview2.setEnabled(true);
                            imageview3.setEnabled(false);
                        }else {
                            imageview1.setEnabled(true);
                            imageview2.setEnabled(true);
                            imageview3.setEnabled(true);
                        }
                    }else {
                        imageview1.setEnabled(false);
                        imageview2.setEnabled(false);
                        imageview3.setEnabled(false);
                    }

                        //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.tooltip));
                    int OFFSET_X = 30;
                    int OFFSET_Y = 30;
                    int[] location = new int[2];
                    Rect rectlocation = new Rect();
                    if (v != null) {
                        // Get the x, y location and store it in the location[] array
                        // location[0] = x, location[1] = y.
                        v.getLocationOnScreen(location);
                        p = new Point();
                        p.x = location[0] - 50;
                        p.y = location[1];

                           /* rectlocation.left = location[0]-20 ;
                            rectlocation.top = location[1];
                            rectlocation.right = rectlocation.left + v.getWidth();
                            rectlocation.bottom = rectlocation.top + v.getHeight();*/
                    }

                    imageview1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MediaModel mediaModel = (MediaModel) v.getTag();
                            showImageDialog(mQuestionsModel, mediaModel, 1);
                        }
                    });
                    imageview2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MediaModel mediaModel = (MediaModel) v.getTag();
                            showImageDialog(mQuestionsModel, mediaModel, 2);
                        }
                    });
                    imageview3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MediaModel mediaModel = (MediaModel) v.getTag();
                            showImageDialog(mQuestionsModel, mediaModel, 3);
                        }
                    });
                    setImageview();
                    if (mListofCameraPathLocalDB != null && mListofCameraPathLocalDB.size() > 0) {
                        popupWindow.showAsDropDown(v);
                    }
                    //popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
                    // popupWindow.showAsDropDown(v,p.x + OFFSET_X, p.y + OFFSET_Y);
                    //popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
                    // popupWindow.showAtLocation(popupView, Gravity.TOP | Gravity.LEFT, rectlocation.left, rectlocation.bottom);
                    //popupWindow.showAtLocation(popupView,Gravity.LEFT, rectlocation.left, 0);
                }
            });
            viewHorizontal= new View(context);
            viewHorizontal.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
            viewHorizontal.setBackgroundColor(Color.parseColor("#B1B3B2"));
            mLinearlayout_camera_options_container.addView(linearLayoutOption);
            linearLayoutOption.addView(viewHorizontal);
            sectionsTextView = new TextView(context);
            sectionsTextView.setPadding(5, 0, 5, 0);
            sectionsTextView.setId(questionsList.get(i).getScheduleQuestionID());
            sectionsTextView.setTag(questionsList.get(i).getRemark());
            if(questionsList.get(i).getRemark()==null) {
                Drawable mTextViewDrawable;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mTextViewDrawable = getResources().getDrawable(R.drawable.btn_edittext,context.getTheme());
                } else {
                    mTextViewDrawable = getResources().getDrawable(R.drawable.btn_edittext);
                }
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    sectionsTextView.setBackgroundDrawable(mTextViewDrawable);
                } else {
                    sectionsTextView.setBackground(mTextViewDrawable);
                }
                sectionsTextView.setEnabled(false);
            }else if(questionsList.get(i).getRemark()!=null) {
                if(!questionsList.get(i).getRemark().toString().trim().equalsIgnoreCase("")) {
                    sectionsTextView.setBackgroundResource(0);
                    sectionsTextView.setBackgroundColor(Color.WHITE);
                    sectionsTextView.setText(questionsList.get(i).getRemark());
                    sectionsTextView.setSingleLine(true);
                    sectionsTextView.setGravity(Gravity.LEFT);
                    sectionsTextView.setMaxEms(10);
                    if (Build.VERSION.SDK_INT < 23) {
                        sectionsTextView.setTextAppearance(context, R.style.textViewStyleBlack);
                    } else {
                        sectionsTextView.setTextAppearance(R.style.textViewStyleBlack);
                    }
                    sectionsTextView.setEnabled(false);
                }else {
                    Drawable mTextViewDrawable;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mTextViewDrawable = getResources().getDrawable(R.drawable.btn_edittext,context.getTheme());
                    } else {
                        mTextViewDrawable = getResources().getDrawable(R.drawable.btn_edittext);
                    }
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        sectionsTextView.setBackgroundDrawable(mTextViewDrawable);
                    } else {
                        sectionsTextView.setBackground(mTextViewDrawable);
                    }
                }
                sectionsTextView.setEnabled(true);
            }

                LayoutParams linearParamRemark = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                linearParamRemark.setMargins(10, 0, 20, 0);
                linearParamRemark.gravity = Gravity.CENTER;
                sectionsTextView.setLayoutParams(linearParamRemark);

                sectionsTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RemarksActivity.class);
                        intent.putExtra("Id", v.getId());
                        if(v.getTag()!=null) {
                            intent.putExtra("Remarks", v.getTag().toString());
                        }else {
                            intent.putExtra("Remarks", "");
                        }
                        context.startActivity(intent);
                    }
                });
                linearLayoutOption.addView(sectionsTextView);

            viewLine = new View(context);
            viewLine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
            viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
            mLinearlayout_camera_options_container.addView(viewLine);
        }
        return this;
    }

    public void setRemark(int id, String remarks) {
        sectionsTextView = (TextView) mLinearlayout_camera_options_container.findViewById(id);
        if(remarks.equalsIgnoreCase("")){
            mQuestionsModel = (QuestionsModel) sectionsTextView.getTag();
            mDataBaseAdpter.updateRemarks(remarks, mQuestionsModel.getScheduleQuestionID());
            sectionsTextView.setText(remarks);
            Drawable mTextViewDrawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mTextViewDrawable = getResources().getDrawable(R.drawable.btn_edittext,context.getTheme());
            } else {
                mTextViewDrawable = getResources().getDrawable(R.drawable.btn_edittext);
            }
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                sectionsTextView.setBackgroundDrawable(mTextViewDrawable);
            } else {
                sectionsTextView.setBackground(mTextViewDrawable);
            }
        }else {
            mQuestionsModel = (QuestionsModel) sectionsTextView.getTag();
            mDataBaseAdpter.updateRemarks(remarks, mQuestionsModel.getScheduleQuestionID());
            SectionListActivity.defaultInstance().selectedCount(1);
            sectionsTextView.setBackgroundResource(0);
            sectionsTextView.setBackgroundColor(Color.WHITE);
            sectionsTextView.setSingleLine(true);
            sectionsTextView.setMaxEms(10);
            sectionsTextView.setText(remarks);
        }
    }



    //set the image resource from onActivityResult method
    public void setImageBitmap(Bitmap bitMap, String path) {
        SectionListActivity.defaultInstance().selectedCount(1);

        if (popupWindow != null && popupWindow.isShowing()) {
            if (imageview1.getDrawable() == null) {
                mMediaModel = new MediaModel();
                mMediaModel.setImagePath(path);
                mMediaModel.setScheduleQuestionID(mQuestionsModel.getScheduleQuestionID());
                mMediaModel.setSubSectionId(mQuestionsModel.getSubSectionID());
                mMediaModel.setSectionId(mQuestionsModel.getSectionID());
                mMediaModel.setImageName("IMG1");
                mMediaModel.setCaptureId(mImageViewButtonId);
                mMediaModel.setImageId(1);
                mMediaModel.setQuestionMediaID(0);
                if(path!=null && !path.equalsIgnoreCase("")) {
                    mMediaModel.setImageBase64Value(encodeTobase64(path));
                }
                imageview1.setTag(mMediaModel);
                imageview1.setImageBitmap(bitMap);
                //mListofCameraPath.add(mMediaModel);
                mDataBaseAdpter.insertScheduleQuestionMedia(mMediaModel);
            } else if (imageview2.getDrawable() == null) {
                mMediaModel = new MediaModel();
                mMediaModel.setImagePath(path);
                mMediaModel.setScheduleQuestionID(mQuestionsModel.getScheduleQuestionID());
                mMediaModel.setSubSectionId(mQuestionsModel.getSubSectionID());
                mMediaModel.setSectionId(mQuestionsModel.getSectionID());
                mMediaModel.setImageName("IMG2");
                mMediaModel.setCaptureId(mImageViewButtonId);
                mMediaModel.setImageId(2);
                mMediaModel.setQuestionMediaID(0);
                if(path!=null && !path.equalsIgnoreCase("")) {
                    mMediaModel.setImageBase64Value(encodeTobase64(path));
                }
                imageview2.setImageBitmap(bitMap);
                imageview2.setTag(mMediaModel);
                //mListofCameraPath.add(mMediaModel);
                mDataBaseAdpter.insertScheduleQuestionMedia(mMediaModel);
            } else if (imageview3.getDrawable() == null) {
                mMediaModel = new MediaModel();
                mMediaModel.setImagePath(path);
                mMediaModel.setScheduleQuestionID(mQuestionsModel.getScheduleQuestionID());
                mMediaModel.setSubSectionId(mQuestionsModel.getSubSectionID());
                mMediaModel.setSectionId(mQuestionsModel.getSectionID());
                mMediaModel.setImageName("IMG3");
                mMediaModel.setCaptureId(mImageViewButtonId);
                mMediaModel.setImageId(3);
                mMediaModel.setQuestionMediaID(0);
                if(path!=null && !path.equalsIgnoreCase("")) {
                    mMediaModel.setImageBase64Value(encodeTobase64(path));
                }
                imageview3.setImageBitmap(bitMap);
                imageview3.setTag(mMediaModel);
                // mListofCameraPath.add(mMediaModel);
                mDataBaseAdpter.insertScheduleQuestionMedia(mMediaModel);
            }
            int id =mImageViewButtonId+20;
            Button imageCountButton =  (Button) mLinearlayout_camera_options_container.findViewById(id);
            mListofCameraPathLocalDB=mDataBaseAdpter.getScheduleQuestionMediaCaptureId(mQuestionsModel.getSectionID(), mQuestionsModel.getSubSectionID(), mQuestionsModel.getScheduleQuestionID());
            if (mListofCameraPathLocalDB != null && mListofCameraPathLocalDB.size() > 0) {
                imageCountButton.setText(""+mListofCameraPathLocalDB.size());
                if(mListofCameraPathLocalDB.size() == 1) {
                    imageview1.setEnabled(true);
                    imageview2.setEnabled(false);
                    imageview3.setEnabled(false);
                }else if(mListofCameraPathLocalDB.size() == 2) {
                    imageview1.setEnabled(true);
                    imageview2.setEnabled(true);
                    imageview3.setEnabled(false);
                }else {
                    imageview1.setEnabled(true);
                    imageview2.setEnabled(true);
                    imageview3.setEnabled(true);
                }
            }else {
                imageCountButton.setText("0");
                imageview1.setEnabled(false);
                imageview2.setEnabled(false);
                imageview3.setEnabled(false);
            }
        }
    }

    public String encodeTobase64(String imagePath) {
        Bitmap image =getThumbnailBitmap(imagePath,500);
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=null;
        try{
            System.gc();
            temp=Base64.encodeToString(b, Base64.DEFAULT);
        }catch(Exception e){
            e.printStackTrace();
        }catch(OutOfMemoryError e){
            baos=new  ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,50, baos);
            b=baos.toByteArray();
            temp=Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("EWN", "Out of memory error catched");
        }
        return temp;
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

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void createEvalationOption(int position) {
       /* final LinearLayout.LayoutParams viewParamsLinear = new LinearLayout.LayoutParams(500, LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout_Evaluation.setLayoutParams(viewParamsLinear);*/
        TextView mEvalautionView;
        //int mEvalutionColumnWidth = (int) (getResources().getDimension(R.dimen.option_width) / getResources().getDisplayMetrics().density);
        List<AnswerColumnsModel> answerColumnsModelsList=new ArrayList<>();
        for(AnswerColumnsModel answerColumnsModel:answerColumnsList){
            if(answerColumnsModel.getAnswerColumnCount()==position){
                answerColumnsModelsList.add(answerColumnsModel);
            }
        }
        for (int j = 0; j < answerColumnsModelsList.size(); j++) {
            AnswerColumnsModel answerColumnsModel=answerColumnsModelsList.get(j);
            mEvalautionView = new TextView(context);
            mEvalautionView.setPadding(5, 5, 5, 5);
            mEvalautionView.setId(j);
            //mEvalautionView.setSingleLine(true);
            mEvalautionView.setSingleLine(true);
            mEvalautionView.setEllipsize(TextUtils.TruncateAt.END);
            mEvalautionView.setGravity(Gravity.CENTER);
            if (Build.VERSION.SDK_INT < 23) {
                mEvalautionView.setTextAppearance(context, R.style.textViewStyleHeader);
            } else {
                mEvalautionView.setTextAppearance(R.style.textViewStyleHeader);
            }
            mEvalautionView.setText(answerColumnsModel.getScheduleAnsOptions());
            final LayoutParams viewParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            viewParams.gravity=Gravity.CENTER;
            viewParams.setMargins(5, 0, 5, 0);
            viewParams.weight =1;
            mEvalautionView.setLayoutParams(viewParams);
            mLinearlayout_second_row_options_container.addView(mEvalautionView);
        }
        viewLine = new View(context);
        viewLine.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
        viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
        mLinearlayout_second_row_options_container.addView(viewLine);
    }
    //view the selected imageview & remove the image
    public void showImageDialog(final QuestionsModel questionsModel, final MediaModel mediaModel, int selectedPosition) {
            dialogImageview = new Dialog((Activity) context);
            dialogImageview.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogImageview.setContentView(R.layout.dialog_image_view);
            dialogImageview.setCanceledOnTouchOutside(true);
            mImageViewDisplay = (ImageView) dialogImageview.findViewById(R.id.imageview_display);
            final ImageView imageViewLeft = (ImageView) dialogImageview.findViewById(R.id.imageview_left);
            final ImageView imageViewRight = (ImageView) dialogImageview.findViewById(R.id.imageview_right);
            mListofCameraPathLocalDB=mDataBaseAdpter.getScheduleQuestionMediaCaptureId(questionsModel.getSectionID(), questionsModel.getSubSectionID(), questionsModel.getScheduleQuestionID());
             currentPosition = selectedPosition;
            if (mListofCameraPathLocalDB != null && mListofCameraPathLocalDB.size() > 0) {
                if(mListofCameraPathLocalDB.size()==1){
                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                }
                if(mListofCameraPathLocalDB.size()==2){
                    if (currentPosition==1){
                        imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                        imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                    }else if(currentPosition ==2){
                        imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                        imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                    }
                }
                if(mListofCameraPathLocalDB.size()==3){
                    if (currentPosition==1){
                        imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                        imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                    }else if(currentPosition ==2){
                        imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                        imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                    }else {
                        imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                        imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                    }
                }
                for (int i = 0; i < mListofCameraPathLocalDB.size(); i++) {
                    MediaModel mediaModelView = mListofCameraPathLocalDB.get(i);
                    if(mediaModelView.getQuestionMediaID()==0) {
                        if (mediaModelView.getImageName().equals(mediaModel.getImageName())) {
                            Log.i("getImagePath()--->", "" + mediaModelView.getImagePath());
                            File file = new File(mediaModelView.getImagePath());
                            Bitmap myBitmap = decodeFile(file);
                            myBitmap = Bitmap.createScaledBitmap(myBitmap, mDialogImg, mDialogImg, true);
                            mImageViewDisplay.setTag(mediaModelView);
                            mImageViewDisplay.setImageBitmap(myBitmap);
                            break;
                        }
                    }else {
                        if (mediaModelView.getImageName().equals(mediaModel.getImageName())) {
                            Log.i("getImagePath()--->", "" + mediaModelView.getImagePath());
                            final String imageUrl = mWebservices.IMAGE_URL + mediaModelView.getImagePath();
                            Log.i("imageview 1 imageUrl---", imageUrl);
                            mImageViewDisplay.setTag(mediaModelView);
                            if (mImageUrlValidator.validate(imageUrl)) {
                                RequestManager
                                        .getInstance()
                                        .doRequest()
                                        .getImageLoader()
                                        .get(imageUrl, new ImageLoader.ImageListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError arg0) {

                                            }

                                            @Override
                                            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                                if (response.getBitmap() != null) {
                                                    mImageViewDisplay.setImageBitmap(response.getBitmap());
                                                }
                                            }
                                        });

                            }
                            break;
                        }
                    }
                }
            }
            imageViewLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("mediaModelList size-->", "" + mListofCameraPathLocalDB.size());
                    if(currentPosition!=1) {
                        currentPosition = currentPosition - 1;
                    }


                    if(mListofCameraPathLocalDB.size()==1){
                        imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                        imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                    }
                    if(mListofCameraPathLocalDB.size()==2){
                        if (currentPosition == 1) {
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                        } else if (currentPosition == 2) {
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                        }
                    }
                    if(mListofCameraPathLocalDB.size()==3){
                        if (currentPosition==1){
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                        }else if(currentPosition ==2){
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                        }else {
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                        }
                    }

                    Log.i("Left currentPosition-->", "" + currentPosition);

                    MediaModel mediaModelView = mListofCameraPathLocalDB.get(currentPosition-1);

                        if(mediaModelView.getQuestionMediaID()==0) {
                            Log.i("getImagePath()--->", "" + mediaModelView.getImagePath());
                            File file = new File(mediaModelView.getImagePath());
                            Bitmap myBitmap = decodeFile(file);
                            myBitmap = Bitmap.createScaledBitmap(myBitmap, mDialogImg, mDialogImg, true);
                            mImageViewDisplay.setTag(mediaModelView);
                            mImageViewDisplay.setImageBitmap(myBitmap);
                    }else {
                            Log.i("getImagePath()--->", "" + mediaModelView.getImagePath());
                            final String imageUrl = mWebservices.IMAGE_URL + mediaModelView.getImagePath();
                            Log.i("imageview 1 imageUrl---", imageUrl);
                            mImageViewDisplay.setTag(mediaModelView);
                            if (mImageUrlValidator.validate(imageUrl)) {
                                RequestManager
                                        .getInstance()
                                        .doRequest()
                                        .getImageLoader()
                                        .get(imageUrl, new ImageLoader.ImageListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError arg0) {

                                            }

                                            @Override
                                            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                                if (response.getBitmap() != null) {
                                                    mImageViewDisplay.setImageBitmap(response.getBitmap());
                                                }
                                            }
                                        });
                        }
                    }


                    /*MediaModel view = (MediaModel) mImageViewDisplay.getTag();
                    if (mSelectLeftPosition >= 0 && mSelectLeftPosition < mListofCameraPathLocalDB.size()) {
                        for (int i = 0; i < mListofCameraPathLocalDB.size(); i++) {
                            if (mListofCameraPathLocalDB.get(i).getImageName().equals(view.getImageName())) {
                                mSelectLeftPosition = i - 1;
                                break;
                            }
                        }
                        if (mSelectLeftPosition >= 0 && mSelectLeftPosition < mListofCameraPathLocalDB.size()) {
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                            MediaModel mediaModel = mListofCameraPathLocalDB.get(mSelectLeftPosition);
                            if(mediaModel.getQuestionMediaID()==0) {
                                File file = new File(mediaModel.getImagePath());
                                Bitmap myBitmap = decodeFile(file);
                                myBitmap = Bitmap.createScaledBitmap(myBitmap, mDialogImg, mDialogImg, true);
                                mImageViewDisplay.setTag(mediaModel);
                                mImageViewDisplay.setImageBitmap(myBitmap);
                            }else{
                                final String imageUrl = mWebservices.IMAGE_URL + mediaModel.getImagePath();
                                Log.i("imageview 1 imageUrl---", imageUrl);
                                mImageViewDisplay.setTag(mediaModel);
                                if (mImageUrlValidator.validate(imageUrl)) {
                                    RequestManager
                                            .getInstance()
                                            .doRequest()
                                            .getImageLoader()
                                            .get(imageUrl, new ImageLoader.ImageListener() {

                                                @Override
                                                public void onErrorResponse(VolleyError arg0) {

                                                }

                                                @Override
                                                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                                    if (response.getBitmap() != null) {
                                                        mImageViewDisplay.setImageBitmap(response.getBitmap());
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                        if (mSelectLeftPosition == 0) {
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                        }
                    }*/
                }
            });
            imageViewRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentPosition<mListofCameraPathLocalDB.size()) {
                        currentPosition = currentPosition+1;
                    }
                    if(mListofCameraPathLocalDB.size()==1){
                        imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                        imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                    }
                    if(mListofCameraPathLocalDB.size()==2){
                        if (currentPosition==1){
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                        }else if(currentPosition ==2){
                                imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                                imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);

                        }
                    }
                    if(mListofCameraPathLocalDB.size()==3){
                        if (currentPosition==1){
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                        }else if(currentPosition ==2){
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                        }else {
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                        }
                    }
                    Log.i("Right currentPosition-->", "" + currentPosition);
                    if(currentPosition<=mListofCameraPathLocalDB.size()) {
                        MediaModel mediaModelView = mListofCameraPathLocalDB.get(currentPosition - 1);

                        if (mediaModelView.getQuestionMediaID() == 0) {
                            Log.i("getImagePath()--->", "" + mediaModelView.getImagePath());
                            File file = new File(mediaModelView.getImagePath());
                            Bitmap myBitmap = decodeFile(file);
                            myBitmap = Bitmap.createScaledBitmap(myBitmap, mDialogImg, mDialogImg, true);
                            mImageViewDisplay.setTag(mediaModelView);
                            mImageViewDisplay.setImageBitmap(myBitmap);
                        } else {
                            Log.i("getImagePath()--->", "" + mediaModelView.getImagePath());
                            final String imageUrl = mWebservices.IMAGE_URL + mediaModelView.getImagePath();
                            Log.i("imageview 1 imageUrl---", imageUrl);
                            mImageViewDisplay.setTag(mediaModelView);
                            if (mImageUrlValidator.validate(imageUrl)) {
                                RequestManager
                                        .getInstance()
                                        .doRequest()
                                        .getImageLoader()
                                        .get(imageUrl, new ImageLoader.ImageListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError arg0) {

                                            }

                                            @Override
                                            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                                if (response.getBitmap() != null) {
                                                    mImageViewDisplay.setImageBitmap(response.getBitmap());
                                                }
                                            }
                                        });
                            }
                        }
                    }

                    /*MediaModel view = (MediaModel) mImageViewDisplay.getTag();
                    if (mSelectRightPosition >= 0 && mSelectRightPosition < mListofCameraPathLocalDB.size()) {
                        for (int i = 0; i < mListofCameraPathLocalDB.size(); i++) {
                            if (mListofCameraPathLocalDB.get(i).getImageName().equals(view.getImageName())) {
                                mSelectRightPosition = i + 1;
                                break;
                            }
                        }
                        if (mSelectRightPosition >= 0 && mSelectRightPosition < mListofCameraPathLocalDB.size()) {
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                            MediaModel mediaModel = mListofCameraPathLocalDB.get(mSelectRightPosition);
                            if(mediaModel.getQuestionMediaID()==0) {
                                File file = new File(mediaModel.getImagePath());
                                Bitmap myBitmap = decodeFile(file);
                                myBitmap = Bitmap.createScaledBitmap(myBitmap, mDialogImg, mDialogImg, true);
                                mImageViewDisplay.setTag(mediaModel);
                                mImageViewDisplay.setImageBitmap(myBitmap);
                            }else{
                                final String imageUrl = mWebservices.IMAGE_URL + mediaModel.getImagePath();
                                Log.i("imageview 1 imageUrl---", imageUrl);
                                mImageViewDisplay.setTag(mediaModel);
                                if (mImageUrlValidator.validate(imageUrl)) {
                                    RequestManager
                                            .getInstance()
                                            .doRequest()
                                            .getImageLoader()
                                            .get(imageUrl, new ImageLoader.ImageListener() {

                                                @Override
                                                public void onErrorResponse(VolleyError arg0) {

                                                }

                                                @Override
                                                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                                    if (response.getBitmap() != null) {
                                                        mImageViewDisplay.setImageBitmap(response.getBitmap());
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                        if (mSelectRightPosition == mListofCameraPathLocalDB.size() - 1) {
                            imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                            imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                        }
                    }*/
                }
            });
        mImageViewDisplay.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        if (x1 < x2) {
                            //Toast.makeText(context, "left2right swipe", Toast.LENGTH_SHORT).show();
                            Log.i("mediaModelList size-->", "" + mListofCameraPathLocalDB.size());
                            if(currentPosition!=1) {
                                currentPosition = currentPosition - 1;
                            }


                            if(mListofCameraPathLocalDB.size()==1){
                                imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                                imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                            }
                            if(mListofCameraPathLocalDB.size()==2){
                                if (currentPosition == 1) {
                                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                                } else if (currentPosition == 2) {
                                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                                }
                            }
                            if(mListofCameraPathLocalDB.size()==3){
                                if (currentPosition==1){
                                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                                }else if(currentPosition ==2){
                                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                                }else {
                                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                                }
                            }

                            Log.i("Left currentPosition-->", "" + currentPosition);

                            MediaModel mediaModelView = mListofCameraPathLocalDB.get(currentPosition-1);

                            if(mediaModelView.getQuestionMediaID()==0) {
                                Log.i("getImagePath()--->", "" + mediaModelView.getImagePath());
                                File file = new File(mediaModelView.getImagePath());
                                Bitmap myBitmap = decodeFile(file);
                                myBitmap = Bitmap.createScaledBitmap(myBitmap, mDialogImg, mDialogImg, true);
                                mImageViewDisplay.setTag(mediaModelView);
                                mImageViewDisplay.setImageBitmap(myBitmap);
                            }else {
                                Log.i("getImagePath()--->", "" + mediaModelView.getImagePath());
                                final String imageUrl = mWebservices.IMAGE_URL + mediaModelView.getImagePath();
                                Log.i("imageview 1 imageUrl---", imageUrl);
                                mImageViewDisplay.setTag(mediaModelView);
                                if (mImageUrlValidator.validate(imageUrl)) {
                                    RequestManager
                                            .getInstance()
                                            .doRequest()
                                            .getImageLoader()
                                            .get(imageUrl, new ImageLoader.ImageListener() {

                                                @Override
                                                public void onErrorResponse(VolleyError arg0) {

                                                }

                                                @Override
                                                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                                    if (response.getBitmap() != null) {
                                                        mImageViewDisplay.setImageBitmap(response.getBitmap());
                                                    }
                                                }
                                            });
                                }
                            }
                        }

                        if (x1 > x2) {
                            //Toast.makeText(context, "right2left swipe", Toast.LENGTH_SHORT).show();
                            if(currentPosition!=mListofCameraPathLocalDB.size()) {
                                currentPosition = currentPosition+1;
                            }
                            if(mListofCameraPathLocalDB.size()==1){
                                imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                                imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                            }
                            if(mListofCameraPathLocalDB.size()==2){
                                if (currentPosition==1){
                                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                                }else if(currentPosition ==2){
                                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);

                                }
                            }
                            if(mListofCameraPathLocalDB.size()==3){
                                if (currentPosition==1){
                                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left);
                                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                                }else if(currentPosition ==2){
                                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right_visible);
                                }else {
                                    imageViewLeft.setBackgroundResource(R.drawable.arrow_big_left_visible);
                                    imageViewRight.setBackgroundResource(R.drawable.arrow_big_right);
                                }
                            }
                            Log.i("Right currentPosition-->", "" + currentPosition);
                            if(currentPosition<=mListofCameraPathLocalDB.size()) {
                                MediaModel mediaModelView = mListofCameraPathLocalDB.get(currentPosition - 1);

                                if (mediaModelView.getQuestionMediaID() == 0) {
                                    Log.i("getImagePath()--->", "" + mediaModelView.getImagePath());
                                    File file = new File(mediaModelView.getImagePath());
                                    Bitmap myBitmap = decodeFile(file);
                                    myBitmap = Bitmap.createScaledBitmap(myBitmap, mDialogImg, mDialogImg, true);
                                    mImageViewDisplay.setTag(mediaModelView);
                                    mImageViewDisplay.setImageBitmap(myBitmap);
                                } else {
                                    Log.i("getImagePath()--->", "" + mediaModelView.getImagePath());
                                    final String imageUrl = mWebservices.IMAGE_URL + mediaModelView.getImagePath();
                                    Log.i("imageview 1 imageUrl---", imageUrl);
                                    mImageViewDisplay.setTag(mediaModelView);
                                    if (mImageUrlValidator.validate(imageUrl)) {
                                        RequestManager
                                                .getInstance()
                                                .doRequest()
                                                .getImageLoader()
                                                .get(imageUrl, new ImageLoader.ImageListener() {

                                                    @Override
                                                    public void onErrorResponse(VolleyError arg0) {

                                                    }

                                                    @Override
                                                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                                        if (response.getBitmap() != null) {
                                                            mImageViewDisplay.setImageBitmap(response.getBitmap());
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                        break;
                }
                return true;
            }
        });
            Button removeBtn = (Button) dialogImageview.findViewById(R.id.button_remove);
            removeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaModel mediaModel = (MediaModel) mImageViewDisplay.getTag();
                    alertRemoveImage("Do you want remove Image?", mQuestionsModel, mediaModel);
                }
            });
        if (!mSession.getEditable() || !mSession.getAssignedSection()) {
            removeBtn.setVisibility(View.INVISIBLE);
            removeBtn.setEnabled(false);
        }else {
            removeBtn.setVisibility(View.VISIBLE);
            removeBtn.setEnabled(true);
        }
            dialogImageview.show();

        }
    //bind the image source to imageview
    public void setImageview(){
        /*Log.i("question--->", mQuestionsModel.getQuestionText() + "");
        Log.i("setSubSectionId--->", mQuestionsModel.getSubSectionID() + "");
        Log.i("setSectionId--->", mQuestionsModel.getSectionID() + "");
        Log.i("ScheduleQuestionID--->", mQuestionsModel.getScheduleQuestionID() + "");*/
        mListofCameraPathLocalDB=mDataBaseAdpter.getScheduleQuestionMediaCaptureId(mQuestionsModel.getSectionID(), mQuestionsModel.getSubSectionID(), mQuestionsModel.getScheduleQuestionID());
        if (mListofCameraPathLocalDB != null && mListofCameraPathLocalDB.size() > 0) {
            for (int i = 0; i < mListofCameraPathLocalDB.size(); i++) {
                MediaModel mediaModel = mListofCameraPathLocalDB.get(i);
                if (mediaModel.getScheduleQuestionID() == mQuestionId) {
                    if (i == 0) {
                        if (imageview1.getDrawable() == null) {
                            imageview1.setTag(mediaModel);
                            if (mediaModel.getQuestionMediaID() == 0) {
                                File file = new File(mediaModel.getImagePath());
                                Bitmap myBitmap = decodeFile(file);
                                myBitmap = Bitmap.createScaledBitmap(myBitmap, mDialogImg, mDialogImg, true);
                                if (myBitmap != null) {
                                    imageview1.setImageBitmap(myBitmap);
                                }
                            } else {
                                final String imageUrl = mWebservices.IMAGE_URL + mediaModel.getImagePath();
                                Log.i("imageview 1 imageUrl---", imageUrl);
                                if (mImageUrlValidator.validate(imageUrl)) {
                                    RequestManager
                                            .getInstance()
                                            .doRequest()
                                            .getImageLoader()
                                            .get(imageUrl, new ImageLoader.ImageListener() {

                                                @Override
                                                public void onErrorResponse(VolleyError arg0) {

                                                }

                                                @Override
                                                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                                    if (response.getBitmap() != null) {
                                                        imageview1.setImageBitmap(response.getBitmap());
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    } else if (i == 1) {
                        if (imageview2.getDrawable() == null) {
                            imageview2.setTag(mediaModel);
                            if (mediaModel.getQuestionMediaID() == 0) {
                                File file = new File(mediaModel.getImagePath());
                                Bitmap myBitmap = decodeFile(file);
                                myBitmap = Bitmap.createScaledBitmap(myBitmap, mDialogImg, mDialogImg, true);
                                if (myBitmap != null) {
                                    imageview2.setImageBitmap(myBitmap);
                                }
                            } else {
                                final String imageUrl = mWebservices.IMAGE_URL + mediaModel.getImagePath();
                                Log.i("imageview 2 imageUrl---", imageUrl);
                                if (mImageUrlValidator.validate(imageUrl)) {
                                    RequestManager
                                            .getInstance()
                                            .doRequest()
                                            .getImageLoader()
                                            .get(imageUrl, new ImageLoader.ImageListener() {

                                                @Override
                                                public void onErrorResponse(VolleyError arg0) {

                                                }

                                                @Override
                                                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                                    if (response.getBitmap() != null) {
                                                        imageview2.setImageBitmap(response.getBitmap());
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    } else if (i == 2) {
                        if (imageview3.getDrawable() == null) {
                            imageview3.setTag(mediaModel);
                            if (mediaModel.getQuestionMediaID() == 0) {
                                File file = new File(mediaModel.getImagePath());
                                Bitmap myBitmap = decodeFile(file);
                                myBitmap = Bitmap.createScaledBitmap(myBitmap, mDialogImg, mDialogImg, true);
                                if (myBitmap != null) {
                                    imageview3.setImageBitmap(myBitmap);
                                }
                            } else {
                                final String imageUrl = mWebservices.IMAGE_URL + mediaModel.getImagePath();
                                Log.i("imageview 3 imageUrl---", imageUrl);
                                if (mImageUrlValidator.validate(imageUrl)) {
                                    RequestManager
                                            .getInstance()
                                            .doRequest()
                                            .getImageLoader()
                                            .get(imageUrl, new ImageLoader.ImageListener() {

                                                @Override
                                                public void onErrorResponse(VolleyError arg0) {

                                                }

                                                @Override
                                                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                                    if (response.getBitmap() != null) {
                                                        imageview3.setImageBitmap(response.getBitmap());
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    }
                }
            }
        }

    }
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale++;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    public void alertRemoveImage(String message,final QuestionsModel questionsModel,final MediaModel mediaModelView) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // set title
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.title));
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
                        for(MediaModel mediaModel:mListofCameraPathLocalDB){
                            if(mediaModel.getImageName()==mediaModelView.getImageName()){
                                mListofCameraPathLocalDB.remove(mediaModel);
                                mDataBaseAdpter.deleteImagepath(questionsModel, mediaModel);
                                break;
                            }
                        }
                        imageview1.setImageDrawable(null);
                        imageview2.setImageDrawable(null);
                        imageview3.setImageDrawable(null);
                        setImageview();
                        dialogImageview.dismiss();

                        int countid =mImageViewButtonId+20;
                        Button imageCountButton =  (Button) mLinearlayout_camera_options_container.findViewById(countid);
                        mListofCameraPathLocalDB=mDataBaseAdpter.getScheduleQuestionMediaCaptureId(mQuestionsModel.getSectionID(), mQuestionsModel.getSubSectionID(), mQuestionsModel.getScheduleQuestionID());
                        if (mListofCameraPathLocalDB != null && mListofCameraPathLocalDB.size() > 0) {
                            imageCountButton.setText(""+mListofCameraPathLocalDB.size());
                            if(mListofCameraPathLocalDB.size() == 1) {
                                imageview1.setEnabled(true);
                                imageview2.setEnabled(false);
                                imageview3.setEnabled(false);
                            }else if(mListofCameraPathLocalDB.size() == 2) {
                                imageview1.setEnabled(true);
                                imageview2.setEnabled(true);
                                imageview3.setEnabled(false);
                            }else {
                                imageview1.setEnabled(true);
                                imageview2.setEnabled(true);
                                imageview3.setEnabled(true);
                            }
                        }else {
                            imageCountButton.setText("0");
                            imageview1.setEnabled(false);
                            imageview2.setEnabled(false);
                            imageview3.setEnabled(false);
                        }

                        /*if (mListofCameraPathLocalDB != null && mListofCameraPathLocalDB.size() > 0) {
                            for (MediaModel mediaModelView : mListofCameraPathLocalDB) {
                                File file = new File(mediaModelView.getImagePath());
                                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                mImageViewDisplay.setTag(mediaModelView);
                                mImageViewDisplay.setImageBitmap(myBitmap);
                            }
                        }*/
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public class MyEditTextDatePicker  implements OnClickListener, DatePickerDialog.OnDateSetListener {
        EditText _editText;
        private int current_day;
        private int current_month;
        private int current_birthYear;
        private int answerSectionID;
        private Context _context;
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy");

        public MyEditTextDatePicker(Context context,int answerSectionID,int editTextViewID,int year, int monthOfYear, int dayOfMonth)
        {
            Activity act = (Activity)context;
            this._editText = (EditText)act.findViewById(editTextViewID);
            String mPreviousDate = _editText.getText().toString();
            if(!mPreviousDate.equalsIgnoreCase("")) {
                Date convertedDate = new Date();
                try {
                    Calendar c = Calendar.getInstance();
                    convertedDate = df.parse(mPreviousDate);
                    c.setTime(convertedDate);
                    this.current_birthYear = c.get(Calendar.YEAR);
                    this.current_month = c.get(Calendar.MONTH);
                    this.current_day = c.get(Calendar.DAY_OF_MONTH);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else {
                this.current_birthYear = year;
                this.current_month = monthOfYear;
                this.current_day = dayOfMonth;
            }
            this._editText.setOnClickListener(this);
            this._context = context;

            this.answerSectionID = answerSectionID;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _birthYear = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            updateDisplay();
        }
        @Override
        public void onClick(View v) {
            Calendar c = Calendar.getInstance();
            c.set(current_birthYear,current_month,current_day);
            String strDate = df.format(c.getTime());
           /* if(mStoredSectionValue.length()>0){
                _editText.setText(mStoredSectionValue);
            }else{
                _editText.setText(strDate);
            }*/
            _editText.setText(strDate);

            datePickerDialog =  new DatePickerDialog(_context, this, current_birthYear , current_month, current_day);
            datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Clear", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        dialog.dismiss();
                        _editText.setText("");
                    }
                }
            });
            datePickerDialog.show();
        }

        // updates the date in the birth date EditText
        private void updateDisplay() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(_birthYear, _month, _day);
            String strDate = df.format(calendar.getTime());
            _editText.setText(strDate);
            //update the sectionId OR SubSectionID to AnswerMainSectiontable
            mDataBaseAdpter.updateSectionId(0, 0, answerSectionID, _editText.getText().toString());
        }
    }

    protected void init() {
        int a= mHorizontalscrollview_container.getHeight();
        int b = mHorizontalscrollview_container.getWidth();
        rowHeight = mHorizontalscrollview_container.getWidth();
        Toast.makeText(context, "" + a + " " + b, Toast.LENGTH_LONG).show();
        // Gets the layout params that will allow you to resize the layout
        mHorizontalTitlescrollview_container.getLayoutParams().width =rowHeight;
    }

}

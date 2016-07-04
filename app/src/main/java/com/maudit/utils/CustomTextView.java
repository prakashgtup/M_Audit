package com.maudit.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.maudit.database.DataBaseAdpter;
import com.maudit.main.R;
import com.maudit.main.SectionListActivity;
import com.maudit.model.AnswerColumnsModel;
import com.maudit.model.AnswerSectionsModel;
import com.maudit.model.QuestionsModel;

import java.util.List;

/**
 *
 */
public class CustomTextView extends LinearLayout {
    Context mContext;
    private TextView mEvalautionView;
    View viewLine,popupView;
    PopupWindow popupWindow;
    private int mLinearColumnWidth=0,questionItemColumnHeight=0,mCameraContainerWidth=0;
    boolean mTempChecked=true;
    ImageView imageViewInfo=null;
    //The "x" and "y" position of the "Show Button" on screen.
    Point p;
    int tabletSize,density,radioButtonX=0,radioButtonY=0,radiusOuter=0,radiusInner=0, stroke =0,childcount=0,evalutionColumnWidth=0,questionId=0;
    DisplayMetrics metrics = new DisplayMetrics();
    private RadioButton mRadioBtn;
    RadioGroup mRadioGroup;
    private Session mSession;
    private DataBaseAdpter mDataBaseAdpter;
    boolean isChecked = true;
    Integer questionHeight = 1;
    public CustomTextView(Context context){
        super(context, null);
        mContext=context;
        mSession=new Session(mContext);
        mDataBaseAdpter=new DataBaseAdpter(context);
        //density is check the device density like as mdpi, xhdpi
        density =(int)getResources().getDisplayMetrics().density;
        //tabletSize is check the tablet device is 7-inch or 10-inch
        tabletSize= (int)(getResources().getDimension(R.dimen.device_size)/getResources().getDisplayMetrics().density);

        popupWindow=new PopupWindow(context);
        evalutionColumnWidth=(int)(getResources().getDimension(R.dimen.column_evaluation_option_width));
        questionItemColumnHeight=(int)(getResources().getDimension(R.dimen.question_item_column_height));
        mLinearColumnWidth= (int)(getResources().getDimension(R.dimen.column_width));
        setOrientation(LinearLayout.HORIZONTAL);
    }
    public CustomTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public CustomTextView initChild(){
        mEvalautionView  =new TextView(mContext);
        mEvalautionView.setPadding(5, 5, 5, 5);
        mEvalautionView.setTextAppearance(mContext, R.style.textViewStyleHeader);
        final LayoutParams classNameParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mEvalautionView, classNameParams);
        return this;
    }
    // create the linearlayout & add the childview for each column
    public CustomTextView linearLayoutView(int linearLayoutId,List<AnswerColumnsModel> answerColumnsList){
            LinearLayout linearLayoutColumn= new LinearLayout(mContext);
            linearLayoutColumn.setId(linearLayoutId);
            linearLayoutColumn.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            linearLayoutColumn.setLayoutParams(params);
            viewLine = new View(mContext);
            viewLine.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
            viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
            for (int i = 0;i<answerColumnsList.size(); i++) {
                mEvalautionView = new TextView(mContext);
                mEvalautionView.setPadding(5, 5, 5, 5);
                mEvalautionView.setId(i);
                //mEvalautionView.setSingleLine(true);
                mEvalautionView.setSingleLine(true);
                mEvalautionView.setEllipsize(TextUtils.TruncateAt.END);
                mEvalautionView.setGravity(Gravity.CENTER);
                if (Build.VERSION.SDK_INT < 23) {
                    mEvalautionView.setTextAppearance(mContext, R.style.textViewStyleHeader);
                } else {
                    mEvalautionView.setTextAppearance(R.style.textViewStyleHeader);
                }
                mEvalautionView.setText(answerColumnsList.get(i).getScheduleAnsOptions());
                final LayoutParams viewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                linearLayoutColumn.addView(mEvalautionView, viewParams);
            }
        //linearLayoutColumn.addView(viewLine);
        addView(linearLayoutColumn);
        return this;
    }
    // create the linearlayout & add the childview for each column
    public CustomTextView radioButtonView(final int count,final  AnswerSectionsModel answerSectionsModel, final List<AnswerColumnsModel> answerColumnsList,final QuestionsModel questionsModel, final int sectionId, final int subSectionId){
       Log.i("radio count-->",""+count);
//        Log.i("Answer Section",answerSectionsModel.getScheduleAnswerSectionID()+"");
//        Log.i("question Id", questionsModel.getScheduleQuestionID()+"");

        mRadioGroup = new RadioGroup(mContext);
        //Log.i("radio count&&&&&",answerSectionsModel.getScheduleAnswerSectionID()+questionsModel.getScheduleQuestionID()+"");
        if(count!=0) {
            mRadioGroup.setId(count);
        }else{
            mRadioGroup.setId(questionsModel.getScheduleQuestionID()+44);
        }
        mRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        mRadioGroup.setBackgroundColor(Color.WHITE);
        mRadioGroup.setGravity(Gravity.CENTER);

        int tempHeight = (int)(questionsModel.getQuestionHeight()/getResources().getDisplayMetrics().density);
        LayoutParams radioGroupParams=new LayoutParams(LayoutParams.MATCH_PARENT, questionItemColumnHeight, Float.parseFloat(".8"));
        radioGroupParams.setMargins(10, 0, 0, 0);
        radioGroupParams.gravity = Gravity.CENTER;
        mRadioGroup.setLayoutParams(radioGroupParams);
        viewLine = new View(mContext);
        LayoutParams viewParam=new LayoutParams(1, LayoutParams.MATCH_PARENT);
        viewLine.setLayoutParams(viewParam);
        viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));


        for (int i = 0; i <answerColumnsList.size(); i++) {
            //add the ScheduleQuestionId value to answerColumnsList
            mRadioBtn = new RadioButton(mContext);
            RadioGroup.LayoutParams radioParam=new RadioGroup.LayoutParams((int)mContext.getResources().getDimension(R.dimen.radiobtn_size),(int)mContext.getResources().getDimension(R.dimen.radiobtn_size));
            mRadioBtn.setId(i);
            AnswerColumnsModel answerColumnsModel=answerColumnsList.get(i);
            mRadioBtn.setTag(answerColumnsModel);
            // mRadioBtn.setText("sample");
            Drawable mUnSelectedDrawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUnSelectedDrawable = getResources().getDrawable(R.drawable.shape_check_unselected,mContext.getTheme());
                mRadioBtn.setBackground(mUnSelectedDrawable);
            } else {
                mUnSelectedDrawable = getResources().getDrawable(R.drawable.shape_check_unselected);
                mRadioBtn.setBackgroundDrawable(mUnSelectedDrawable);
            }
            mRadioBtn.setButtonDrawable(buttonGradient("#" + answerColumnsModel.getAnswerColor()));
            //Log.i("", questionsModel.getAnswersModel().getScheduleAnswerTypeID() + "");
            //Log.i("",answerColumnsModel.getScheduleAnswerTypeID()+"");
            // List<AnswerColumnsModel> answerChecked=  mDataBaseAdpter.getAnswerCheckedState(questionsModel.getScheduleQuestionID());
            //Log.i("answerChecked-->",""+answerColumnsModel.getIsChecked());
            if(answerColumnsModel.getIsChecked()==1&&answerColumnsModel.getScheduleQuestionID()==questionsModel.getScheduleQuestionID()){

                //Log.i("answerColumnsModel", answerColumnsModel.getScheduleQuestionID() + "");
                //Log.i("questionsModel",questionsModel.getScheduleQuestionID()+"");
                mRadioBtn.setChecked(true);
                mRadioBtn.setActivated(true);
            }else{
                mRadioBtn.setChecked(false);
                mRadioBtn.setActivated(false);
            }



           /* if (questionsModel.getAnswersModel().getScheduleAnswerTypeID() == answerColumnsModel.getScheduleAnswerTypeID()) {
                if (answerSectionsModel != null) {
                    if (questionsModel.getAnswersModel().getScheduleAnswerSectionID() == answerSectionsModel.getScheduleAnswerSectionID()&&questionsModel.getAnswersModel().getIsChecked()==1) {
                        mRadioBtn.setChecked(true);
                    } else {
                        mRadioBtn.setChecked(false);
                    }
                } else {
                    mRadioBtn.setChecked(true);
                }
            }*/

            if(tabletSize>=720){
                radioParam.setMargins(18, 0, 18, 0);
            }else {
                radioParam.setMargins(11, 0, 11, 0);
            }
            //if sections are view type means set the non-editablec
            if(!mSession.getEditable()||!mSession.getAssignedSection()){
                //if sections are AssignedSection or not
                mRadioBtn.setEnabled(false);
            }else {
                mRadioBtn.setEnabled(true);
            }
            mRadioBtn.setLayoutParams(radioParam);
            mRadioGroup.addView(mRadioBtn);
            childcount= mRadioGroup.getChildCount();
            mRadioBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("checklist size", "" + answerColumnsList.size());

                    questionId = questionsModel.getScheduleQuestionID();
                    RadioGroup radioGroup = null;
                    if(count!=0) {
                        radioGroup = (RadioGroup) findViewById(count);
                    }else {
                        radioGroup = (RadioGroup) findViewById(questionId+44);
                    }

                    for (int i = 0; i < answerColumnsList.size(); i++) {
                        AnswerColumnsModel answerColumnsModel = (AnswerColumnsModel) v.getTag();
                        View v1 = radioGroup.getChildAt(i);
                        RadioButton rBtn = (RadioButton) v1;
                        SectionListActivity.defaultInstance().selectedCount(1);
                        if (v.getId() == v1.getId()) {
                            if (!v.isActivated()) {
                                Log.i("Activate", "" + v.getId());
                                radioGroup.clearCheck();
                                rBtn.setChecked(true);
                                rBtn.setActivated(true);
                                answerColumnsModel.setIsChecked(1);
                            } else {
                                Log.i("UnActivate", "" + v.getId());
                                rBtn.setChecked(false);
                                rBtn.setActivated(false);
                                answerColumnsModel.setIsChecked(0);
                            }
                          /*  Log.i("answersModel clicked", "" + answerColumnsModel.getIsChecked());
                            Log.i("answersModel TypeID", "" + answerColumnsModel.getScheduleAnswerTypeID());
                            Log.i("question Id", "" + questionId);*/
                            Log.i("answerColumnsModel.getScheduleAnswerID()", "" + answerColumnsModel.getScheduleAnswerID());
                            answerColumnsModel.setScheduleAnswerID(answerColumnsModel.getScheduleAnswerID());
                            answerColumnsModel.setScheduleAnswerTypeID(answerColumnsModel.getScheduleAnswerTypeID());
                            answerColumnsModel.setScheduleQuestionID(questionId);
                            answerColumnsModel.setScheduleAnsOptionValue(answerColumnsModel.getScheduleAnsOptionValue());
                            answerColumnsModel.setAnswerColor(answerColumnsModel.getAnswerColor());
                            answerColumnsModel.setAnswerColumnCount(answerColumnsModel.getAnswerColumnCount());
                            mDataBaseAdpter.scheduleAnswerSectionSelected(answerColumnsModel);

                        } else{
                            rBtn.setChecked(false);
                            rBtn.setActivated(false);
                            Log.i("not clicked", "" + v1.getId());
                        }
                    }
                    SectionListActivity.calculateOverallProgress();
                }
            });
        }

        //set the answertype(radiobutton) values are checked, the values are retrieve from webservice
       /* for(int j=0;j<childcount;j++){
            View view = mRadioGroup.getChildAt(j);
            RadioButton rBtn= (RadioButton)view;
            AnswerColumnsModel answerColumnsModel=(AnswerColumnsModel)rBtn.getTag();
           // Log.i("radioView checked--> ", "" + rBtn.getTag());
            if(questionsModel.getAnswersModel().getScheduleAnswerTypeID()!=0&&rBtn.getTag()!=null){
                    if(answerColumnsModel.getScheduleAnswerTypeID()==questionsModel.getAnswersModel().getScheduleAnswerTypeID()){
                        rBtn.setChecked(true);
                    }else{
                        rBtn.setChecked(false);
                    }
            }
        }*/
        //Add line after every radio group
        viewLine = new View(mContext);
        LayoutParams lineParams = new LayoutParams(1, LayoutParams.MATCH_PARENT);
        viewLine.setLayoutParams(lineParams);
        viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
        mRadioGroup.addView(viewLine);
        addView(mRadioGroup);
        return this;
    }
    public CustomTextView cameraView(int linearLayoutId){
        final LinearLayout linearLayoutColumn= new LinearLayout(mContext);
        linearLayoutColumn.setId(linearLayoutId);
        linearLayoutColumn.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutColumn.setBackgroundColor(Color.WHITE);
        linearLayoutColumn.setLayoutParams(new LayoutParams(mLinearColumnWidth, questionItemColumnHeight));
        viewLine = new View(mContext);
        viewLine.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
        viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));ImageView imageViewCamera=new ImageView(mContext);
        imageViewCamera.setImageResource(R.drawable.camera_icon);
        imageViewCamera.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        linearLayoutColumn.addView(imageViewCamera);
        linearLayoutColumn.addView(viewLine);
        return this;
    }
    //Gradient For Button Background
    public StateListDrawable buttonGradient(String color)
    {
        //set the background selector programmatically
        StateListDrawable states =new StateListDrawable();

        Drawable mUnSelectedDrawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mUnSelectedDrawable = getResources().getDrawable(R.drawable.shape_check_unselected,mContext.getTheme());
        } else {
            mUnSelectedDrawable = getResources().getDrawable(R.drawable.shape_check_unselected);
        }


       /* GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color
                .parseColor(color));
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setStroke(2, Color.parseColor("#909090"));
        drawable.setSize(50, 50);*/

        Shape shape = new OvalShape();
        CustomBorderDrawable customBorderDrawable = new CustomBorderDrawable(shape);
        customBorderDrawable.setFillColour(Color.parseColor("#8A8A8A"));
        customBorderDrawable.getPaint().setColor(Color
                .parseColor(color));

        states.addState(new int[]{android.R.attr.state_checked}, customBorderDrawable);
        states.addState(new int[]{android.R.attr.state_pressed}, customBorderDrawable);
        //states.addState(new int[]{android.R.attr.state_pressed}, mUnSelectedDrawable);
        //states.addState(new int[]{android.R.attr.state_checked}, mUnSelectedDrawable);
        //states.addState(new int[]{}, mUnSelectedDrawable);
        //states.addState(new int[]{}, mUnSelectedDrawable);
        return states;
    }

    public class CustomBorderDrawable extends ShapeDrawable {
        private Paint fillpaint, strokepaint;
        private static final int WIDTH = 3;

        public CustomBorderDrawable(Shape s) {
            super(s);
            fillpaint = this.getPaint();
            strokepaint = new Paint();
            strokepaint.setStyle(Paint.Style.STROKE);
            strokepaint.setStrokeWidth(WIDTH);
        }

        @Override
        protected void onDraw(Shape shape, Canvas canvas, Paint fillpaint) {
            Log.i("tabletSize",tabletSize+"");
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            int densityDpi = dm.densityDpi;
            Log.i("densityDpi",densityDpi+"");
            if(tabletSize==600){
                canvas.drawCircle(canvas.getWidth() / 2, 0, 18, fillpaint);
                canvas.drawCircle(canvas.getWidth() / 2, 0, 18, strokepaint);
            }else if(densityDpi<=160){
                // canvas.drawCircle(canvas.getWidth() / 2, 0, 35, fillpaint);
                canvas.drawCircle(canvas.getWidth() / 2, 0, 18, fillpaint);
                canvas.drawCircle(canvas.getWidth() / 2, 0, 18, strokepaint);
            }else{
                canvas.drawCircle(canvas.getWidth() / 2, 0, 35, fillpaint);
                canvas.drawCircle(canvas.getWidth() / 2, 0, 35, strokepaint);
            }
            //shape.draw(canvas, fillpaint);
            //shape.draw(canvas, strokepaint);
        }

        public void setFillColour(int c){
            strokepaint.setColor(c);
        }
    }

   /* public void questionsElement(int linearLayoutId,List<String> listItem,List<String> listItemWeight){
        TextView textViewQuetion;
        final LinearLayout linearLayoutColumn= new LinearLayout(mContext);
        linearLayoutColumn.setId(linearLayoutId);
        linearLayoutColumn.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutColumn.setBackgroundColor(Color.WHITE);
        linearLayoutColumn.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, questionItemColumnHeight+1));
        for (int j = 0; j <listItem.size(); j++) {
            viewLine = new View(mContext);
            viewLine.setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
            viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
            textViewQuetion = new TextView(mContext);
            textViewQuetion.setId(j);
            textViewQuetion.setPadding(5,5,5,5);
            textViewQuetion.setTextAppearance(mContext, R.style.textViewStyleBlack);
            textViewQuetion.setText(listItem.get(j));
            final LinearLayout.LayoutParams viewParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,Float.parseFloat(listItemWeight.get(j)));
            linearLayoutColumn.addView(textViewQuetion, viewParams);
            linearLayoutColumn.addView(viewLine);
        }
        addView(linearLayoutColumn);
    }*/
   public void questionsElement(int linearLayoutId,QuestionsModel  questionsModel,List<String> listItemWeight){
      /* //increse the height of the row view based ont the qestiontext length
       if(questionsModel.getQuestionText().length()>=120){
           questionItemColumnHeight=questionItemColumnHeight+60;
       }else if(questionsModel.getQuestionText().length()>=150){
           questionItemColumnHeight=questionItemColumnHeight+80;
       }*/
       TextView textViewQuestion = null;
       final LinearLayout linearLayoutColumn= new LinearLayout(mContext);
       linearLayoutColumn.setId(linearLayoutId);
       linearLayoutColumn.setOrientation(LinearLayout.HORIZONTAL);
       linearLayoutColumn.setBackgroundColor(Color.WHITE);
       linearLayoutColumn.setGravity(Gravity.CENTER);
       linearLayoutColumn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
       for (int i = 0; i <3; i++) {
           viewLine = new View(mContext);
           viewLine.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
           viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
           textViewQuestion = new TextView(mContext);
           textViewQuestion.setId(i);
           textViewQuestion.setPadding(5, 5, 5, 5);
           textViewQuestion.setMinHeight(questionItemColumnHeight);
           textViewQuestion.setTextAppearance(mContext, R.style.textViewStyleBlack);
           textViewQuestion.setMovementMethod(new ScrollingMovementMethod());
           if(i==0){
               textViewQuestion.setText(questionsModel.getqAbbrv());
               textViewQuestion.setGravity(Gravity.CENTER);
           }else if(i==1){
               textViewQuestion.setText(questionsModel.getStandardORClauses());
               textViewQuestion.setGravity(Gravity.CENTER);
           }else{
               imageViewInfo=new ImageView(mContext);
               imageViewInfo.setId(i);
               if(questionsModel.getHint()!=null){
                   imageViewInfo.setTag(questionsModel.getHint());
                   imageViewInfo.setBackgroundResource(R.drawable.info);
               }else{
                   imageViewInfo.setBackgroundResource(R.drawable.info_invisble);
               }
               if(questionsModel.getIsMandatory()==1){
                   textViewQuestion.setText("* \n"+questionsModel.getQuestionText());
               }else{
                   textViewQuestion.setText(questionsModel.getQuestionText());
               }
               textViewQuestion.setGravity(Gravity.CENTER|Gravity.LEFT);
           }
           final LayoutParams viewTxtParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,Float.parseFloat(listItemWeight.get(i)));
           viewTxtParams.setMargins(0, 0, 0, 10);
           linearLayoutColumn.addView(textViewQuestion, viewTxtParams);
           if(imageViewInfo!=null){
               final LayoutParams viewParamsImageview = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
               //viewTxtParams.setMargins(0,0,0,10);
               linearLayoutColumn.addView(imageViewInfo, viewParamsImageview);
               imageViewInfo.setOnClickListener(new OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       //Log.i("tag", v.getTag().toString());
                       if (v.getTag() != null && !v.getTag().toString().equals("")) {
                           showPopup((Activity) mContext, v.getTag().toString(), v);
                       }

                   }
               });
           }
           linearLayoutColumn.addView(viewLine);
       }
       addView(linearLayoutColumn);
   }
    // The method that displays the popup.
    private void showPopup(final Activity context,String message,View v) {
        LinearLayout viewGroup = (LinearLayout)context.findViewById(R.id.linearlayout_popwindow);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.hint_popup_window, viewGroup);
        int popupWidth = 0;
        int popupHeight = 0;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;
        if (tabletSize == 600) {
            popupWidth = 600;
        } else if (densityDpi <= 160) {
            popupWidth = 600;
        } else {
            popupWidth = 800;
        }
        // Creating the PopupWindow
        popupWindow.setContentView(layout);
        popupWindow.setWidth(popupWidth);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // Inflate the popup_layout.xml
        TextView txt=(TextView)layout.findViewById(R.id.textview_info);
        ImageView imageView=(ImageView)layout.findViewById(R.id.imageview_close);
        int[] location = new int[2];
        Rect   rectlocation= new Rect();
        if(imageViewInfo!=null){
            // Get the x, y location and store it in the location[] array
            // location[0] = x, location[1] = y.
            imageViewInfo.getLocationOnScreen(location);
            rectlocation.left = location[0]-400;
            rectlocation.top = location[1];
            rectlocation.right = rectlocation.left + v.getWidth();
            rectlocation.bottom = rectlocation.top + v.getHeight();
            Log.i("position-->",""+rectlocation.right +"--->"+rectlocation.bottom+"location-->"+rectlocation.left );
        }
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        //Initialize the Point with x, and y positions
       /* p = new Point();
        p.x = location[0]-500;
        p.y = location[1];*/
        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 10;
        int OFFSET_Y = 10;
        txt.setText(Html.fromHtml(message));
        // Displaying the popup at the specified location, + offsets.
        //popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
        popupWindow.showAtLocation(layout, Gravity.TOP|Gravity.LEFT, rectlocation.left, rectlocation.bottom );
    }

}

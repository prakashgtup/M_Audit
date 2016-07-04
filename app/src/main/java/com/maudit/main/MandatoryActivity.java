package com.maudit.main;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maudit.model.QuestionsModel;

import java.util.ArrayList;
import java.util.List;

public class MandatoryActivity extends AppCompatActivity {
    private TextView mTxtChapter, mTxtSubSection, mTxtQuestionElements;
    private LinearLayout mLinearlayoutElementContainer;
    private ArrayList<QuestionsModel> mMandatoryList;
    View viewLine,viewHorizontal;
    LinearLayout linearLayoutColumn;
    Button mOkButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandatory);
        mLinearlayoutElementContainer =(LinearLayout)findViewById(R.id.linearlayout_element_container);
        mMandatoryList =AppController.getInstance().getGetMandatoryFields();
        /*if(getIntent().getSerializableExtra("MANDATORY_LIST")!=null){
            mMandatoryList =(ArrayList<QuestionsModel>)getIntent().getSerializableExtra("MANDATORY_LIST");
        }*/
        List<String> listItemWeight=new ArrayList<>();
        listItemWeight.add("1.3");
        listItemWeight.add("1.2");
        listItemWeight.add(".5");
        Log.i("mMandatoryList-->", "" + mMandatoryList.size());
        for(int i=0;i<mMandatoryList.size();i++){
            viewHorizontal = new View(this);
            viewHorizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            viewHorizontal.setBackgroundColor(Color.parseColor("#B1B3B2"));
            linearLayoutColumn= new LinearLayout(this);
            linearLayoutColumn.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutColumn.setBackgroundColor(Color.WHITE);
            linearLayoutColumn.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayoutColumn.setLayoutParams(layoutParams);
            for (int j = 0; j <3; j++) {
                viewLine = new View(this);
                viewLine.setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
                viewLine.setBackgroundColor(Color.parseColor("#B1B3B2"));
                TextView textViewElement=new TextView(this);
                textViewElement.setId(i);
                textViewElement.setPadding(5, 5, 5, 5);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textViewElement.setTextAppearance(R.style.textViewStyleBlack);
                }else{
                    textViewElement.setTextAppearance(this,R.style.textViewStyleBlack);
                }
                if(j==0){
                    textViewElement.setText(mMandatoryList.get(i).getSectionAbbr());
                }else if(j==1){
                    if(!mMandatoryList.get(i).getSubSectionAbbr().equalsIgnoreCase("")) {
                        textViewElement.setText(mMandatoryList.get(i).getSubSectionAbbr());
                    }else {
                        textViewElement.setText("Not Applicable");
                    }
                }else if(j==2){
                    textViewElement.setText(mMandatoryList.get(i).getQuestionText());
                }
                final LinearLayout.LayoutParams viewTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,Float.parseFloat(listItemWeight.get(j)));
                textViewElement.setLayoutParams(viewTxtParams);
                linearLayoutColumn.addView(textViewElement);
                linearLayoutColumn.addView(viewLine);

            }
            mLinearlayoutElementContainer.addView(linearLayoutColumn);
            mLinearlayoutElementContainer.addView(viewHorizontal);
        }
        mOkButton = (Button) findViewById(R.id.button_ok);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

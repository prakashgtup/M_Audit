package com.maudit.main;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.maudit.utils.CommonMethod;
import com.maudit.utils.Session;

public class RemarksActivity extends AppCompatActivity {
    EditText mRemarks_Edt_txt;
    ImageView mImagviewBack;
    Button mRemarks_done_button;
    private CommonMethod mCommonMethod;
    Session mSession;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarks);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        mSession =  new Session(RemarksActivity.this);
        mRemarks_Edt_txt = (EditText) findViewById(R.id.remarks_Edt_txt);
        mRemarks_done_button = (Button) findViewById(R.id.remarks_done_button);
        mCommonMethod = new CommonMethod(this);
        if (getIntent().getStringExtra("Remarks") != null) {
            mRemarks_Edt_txt.setText(getIntent().getStringExtra("Remarks"));
            id = getIntent().getIntExtra("Id",0);
            mRemarks_Edt_txt.setSelection(mRemarks_Edt_txt.getText().length());
        }
        if (!mSession.getEditable() || !mSession.getAssignedSection()) {
            mRemarks_Edt_txt.setEnabled(false);
            mRemarks_Edt_txt.setFocusable(false);
        } else {
            mRemarks_Edt_txt.setEnabled(true);
        }
        mImagviewBack = (ImageView) findViewById(R.id.imagview_back);
        mImagviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommonMethod.closeKeyPad(mRemarks_Edt_txt);
                finish();
            }
        });

        mRemarks_done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSession.getEditable() || !mSession.getAssignedSection()) {

                }else {
                    SectionListActivity.mQuestionSectionView.setRemark(id, mRemarks_Edt_txt.getText().toString());
                }
                mCommonMethod.closeKeyPad(mRemarks_Edt_txt);
                finish();//finishing activity
            }

        });

    }

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
                mCommonMethod.alertDialogMessage("Do you want to logout?", RemarksActivity.this, RemarksActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

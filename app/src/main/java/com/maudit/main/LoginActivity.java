package com.maudit.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.maudit.database.DataBaseAdpter;
import com.maudit.utils.CommonMethod;
import com.maudit.utils.PasswordEncryption;
import com.maudit.utils.Session;
import com.maudit.utils.Webservices;

import org.ksoap2.serialization.SoapObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class LoginActivity extends AppCompatActivity {

    private EditText mADID_edittext,mPassword_edittext;
    private Button mButton_login;
    private TextView mTextview_heading;
    private Webservices mWebservices;
    private CommonMethod mCommonMethod;
    private ProgressDialog mProgressDialog;
    private String mADID,mPassword;
    private Session mSession;
    private RadioGroup mRadioGroup_loginas;
    private int mRoleId=0,mRadioGroupPosition=-1;
    private boolean isChecked=false;
    private RadioButton mRadiobtn_supervisor,mRadiobtn_auditor;
    PasswordEncryption passwordEncryption;
    private String blockCharacterSet = "~#^|$%&*!";
    private DataBaseAdpter mDataBaseAdpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mWebservices=new Webservices(this);
        mCommonMethod=new CommonMethod(this);
        mDataBaseAdpter=new DataBaseAdpter(this);
        mSession=new Session(this);
      //  mTextview_heading=(TextView)findViewById(R.id.textview_heading);
        mADID_edittext=(EditText)findViewById(R.id.adid_edittext);
        //mADID_edittext.setFilters(new InputFilter[] { filter });
        mPassword_edittext=(EditText)findViewById(R.id.password_edittext);
       
        //mPassword_edittext.setFilters(new InputFilter[] { filter });
        mRadioGroup_loginas=(RadioGroup)findViewById(R.id.radiogroup_loginas);
        mRadiobtn_supervisor=(RadioButton)findViewById(R.id.radiobtn_supervisor);
        mRadiobtn_auditor=(RadioButton)findViewById(R.id.radiobtn_auditor);
        mButton_login=(Button)findViewById(R.id.button_login);
        passwordEncryption=new PasswordEncryption();
        mButton_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //passwordEncryption.decrypt(passwordEncryption.encrypt("123456"));
                if (mCommonMethod.isConnectingToInternet()) {
                    mRadioGroupPosition = mRadioGroup_loginas.indexOfChild(findViewById(mRadioGroup_loginas.getCheckedRadioButtonId()));
                    if (isValidation()) {
                        if (mRadioGroupPosition == 0) {
                            mRoleId = 4;
                        } else {
                            mRoleId = 5;
                        }
                        mPassword = passwordEncryption.encrypt(mPassword_edittext.getText().toString());
                        mPassword = mPassword.replace("\n", "");
                        mADID = mADID_edittext.getText().toString();
                        new LoginAsync().execute();
                    }
                } else {
                    //mCommonMethod.alertDialog("Please check your internet connection",null);
                    mCommonMethod.alertDialog("Unable to connect", null);
                }
              /*  Intent intentAuditor=new Intent(LoginActivity.this,SectionListActivity.class);
                mDataBaseAdpter.deleteSection();
                startActivity(intentAuditor);
                finish();*/
            }
        });
        mRadioGroup_loginas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isChecked = true;
                return false;
            }
        });
        mRadioGroup_loginas.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = true;
            }
        });
        mPassword_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if(mCommonMethod.isConnectingToInternet()){
                            mRadioGroupPosition = mRadioGroup_loginas.indexOfChild(findViewById(mRadioGroup_loginas.getCheckedRadioButtonId()));
                            if(isValidation()){
                                if(mRadioGroupPosition==0){
                                    mRoleId=4;
                                }else{
                                    mRoleId=5;
                                }
                                mPassword= passwordEncryption.encrypt(mPassword_edittext.getText().toString());
                                mPassword=mPassword.replace("\n", "");
                                mADID = mADID_edittext.getText().toString();
                                new LoginAsync().execute();
                            }
                        }else{
                            mCommonMethod.alertDialog("Unable to connect",null);
                        }
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        // next stuff
                        break;
                }
                return false;
            }
        });

    }
    public class LoginAsync extends AsyncTask<String,String,String>{
        String  result="",userId="",userName="", message="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle("Logging in");
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
           SoapObject resultSoapObject=null;
            resultSoapObject= mWebservices.mVerficationLogin(mADID,mPassword,mRoleId);
            if(resultSoapObject!=null && resultSoapObject.hasProperty("LoginResult")){
                SoapObject soapObject=(SoapObject)resultSoapObject.getProperty(0);
                result=soapObject.getProperty("Result").toString();
                message=soapObject.getProperty("MessageText").toString();
                    if(soapObject.hasProperty("UserID")){
                        userId=soapObject.getProperty("UserID").toString();
                    }
                    if(soapObject.hasProperty("Username")){
                        userName=soapObject.getProperty("Username").toString();
                    }
                    mSession.setUserId(userId);
                    mSession.setUserName(userName);
                   // mWebservices.mGetAllScheduledCheckLists();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                if(!mSession.iSFirstTime()){
                    c.add(Calendar.DATE, 7);
                    mSession.setInstalledDate(df.format(c.getTime()));
                }else {
                    String installedDate = mSession.getInstalledDate();
                    String currentDate =df.format(c.getTime());
                    Date convertedDate = new Date();
                    try {
                        convertedDate = df.parse(installedDate);
                        Date date2 = df.parse(currentDate);
                        if (date2.compareTo(convertedDate)==0)
                        {
                            System.out.println("Today is the day");
                            /*Intent intent = new Intent(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:"+getPackageName()));
                            startActivity(intent);*/
                        }
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }else{
                result="Network error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            if (result.equals("True")){
                //delete the store section values
                mDataBaseAdpter.deleteSection(0);
                Intent intentAuditor=null;
                if(mRoleId==4){
                    intentAuditor=new Intent(LoginActivity.this,AuditorListActivity.class);
                    mSession.setUserType("Auditor");
                    intentAuditor.putExtra("ROLE_TYPE","Auditor");
                }else{
                    intentAuditor=new Intent(LoginActivity.this,SubmittedAuditorListActivity.class);
                    mSession.setUserType("Supervisor");
                    intentAuditor.putExtra("ROLE_TYPE", "Supervisor");
                }
                startActivity(intentAuditor);
                finish();
            }else if(message!=null&&message.length()>0){
                mCommonMethod.alertDialog(message, null);
            }else{
                mCommonMethod.alertDialog("Unable to connect", null);
            }

        }
    }
    public boolean isValidation(){
        if(mADID_edittext.getText().toString().trim().equals("")){
            mCommonMethod.alertDialog("Please enter ADID",mADID_edittext);
            return false;
        }else if(mPassword_edittext.getText().toString().trim().equals("")){
            mCommonMethod.alertDialog("Please enter Password",mPassword_edittext);
            return false;
        }else if(mRadioGroupPosition==-1){
            mCommonMethod.alertDialog("Please select the option",mPassword_edittext);
            return false;
        }
        return true;
    }
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

}


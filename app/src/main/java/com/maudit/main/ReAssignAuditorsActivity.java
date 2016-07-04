package com.maudit.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maudit.model.GetAuditorsAndSection;
import com.maudit.model.ReAssignAuditorModel;
import com.maudit.model.UserSectionModel;
import com.maudit.utils.CommonMethod;
import com.maudit.utils.ExpandableHeightGridView;
import com.maudit.utils.Session;
import com.maudit.utils.Webservices;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReAssignAuditorsActivity extends AppCompatActivity {
    private Session mSession;
    private CommonMethod mCommonMethod;
    private ProgressDialog mProgressDialog;
    private Webservices mWebservices;
    List<ReAssignAuditorModel> getAuditorsLists = null, mTempAuditorsLists = null, getSelectionsList = new ArrayList<>(), getAssignedList = new ArrayList<>();
    List<Boolean> mSelectedLead =  new ArrayList<>();
    String mAuditScheduleID, mUserID, mLeadAuditorName;
    Integer mLeadAuditorID;
    private TextView mTextview_username, mSubTitle_TxtView, mCurrentDateTxtView, mLeadAuditorTxtView;
    ExpandableHeightGridView mSelected_lead_gridView, mAssigned_sections_gridView;
    LeadAuditorSelectionAdapter mLeadAuditorSelectionAdapter;
    SelectedAuditorsAdapter mSelectedAuditorsAdapter;
    ArrayList<UserSectionModel> getUserSectionsList = new ArrayList<>();
    Button mAssignButton;
    StringBuilder mUserSection =  new StringBuilder();
    Boolean isSavedChanges = false;
    private ImageView mImagviewBack;
    Calendar mCalender = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reassign_sections);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        mProgressDialog = new ProgressDialog(ReAssignAuditorsActivity.this);
        mWebservices = new Webservices(this);
        mCommonMethod = new CommonMethod(this);
        mSession = new Session(this);
        mUserID = mSession.getUserId();
        mTextview_username = (TextView) findViewById(R.id.textview_username);
        mSelected_lead_gridView = (ExpandableHeightGridView) findViewById(R.id.selected_lead_gridView);
        mAssigned_sections_gridView = (ExpandableHeightGridView) findViewById(R.id.assigned_sections_gridView);

        mSelected_lead_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView productItem = (TextView) view.findViewById(R.id.checkListTxt);
                mAssignButton.setEnabled(true);
                mAssignButton.setBackgroundResource(R.drawable.btn_selector);
                mAssignButton.setTextColor(Color.parseColor("#FFFFFF"));
                isSavedChanges = true;
                if (!mSelectedLead.get(position)) {
                    mSelectedLead.set(position, true);
                } else {
                    mSelectedLead.set(position, false);
                }
                ReAssignAuditorsActivity.this.mLeadAuditorSelectionAdapter.refresh();
                if (getUserSectionsList != null) {
                    for (int j = 0; j < getUserSectionsList.size(); j++) {
                        List<Boolean> mIsSelected = getUserSectionsList.get(j).getmSelectedMain();
                        List<Boolean> mTemp = getUserSectionsList.get(j).getmSelectedMain();
                        if (mIsSelected.contains(true)) {
                            for (int i = 0; i < mIsSelected.size(); i++) {
                                if (i == position) {
                                    //mCommonMethod.alertDialog("sub item remove"+i, null);
                                    mTemp.set(i, false);
                                }
                            }

                            getUserSectionsList.get(j).setmSelectedMain(mTemp);
                        }

                    }

                    if (mSelectedAuditorsAdapter != null) {
                        mSelectedAuditorsAdapter.refresh();
                    }
                }
            }
        });
        mTextview_username.setText("Welcome: " + mSession.getUserName());
        mSubTitle_TxtView = (TextView) findViewById(R.id.textview_title);
        mLeadAuditorTxtView = (TextView) findViewById(R.id.Lead_Auditor_Title);
        if(getIntent().getExtras()!=null){
          if(getIntent().getStringExtra("AuditScheduleID")!=null){
              mAuditScheduleID = getIntent().getStringExtra("AuditScheduleID");
              mSubTitle_TxtView.setText(getIntent().getStringExtra("SubTitle"));
          }
        }
        mCurrentDateTxtView = (TextView) findViewById(R.id.textview_date);
        mCurrentDateTxtView.setVisibility(View.VISIBLE);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        mCurrentDateTxtView.setText(df.format(mCalender.getTime()));

        if (mCommonMethod.isConnectingToInternet()) {
            new GetAuditorsAndAssignedSectionsAsync().execute();
        } else {
            mCommonMethod.alertDialog("Please check your internet connection", null);
        }

        mAssignButton = (Button) findViewById(R.id.button_assign_audit);
        mAssignButton.setEnabled(false);
        mAssignButton.setBackgroundResource(R.drawable.btn_invisible);
        mAssignButton.setTextColor(Color.parseColor("#BFC4C0"));

        mAssignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer mSelectedCount = 0;
                for (int j = 0; j < getUserSectionsList.size(); j++) {
                    List<Boolean> mIsSelected = getUserSectionsList.get(j).getmSelectedMain();
                    for (int h = 0; h < mIsSelected.size(); h++) {
                        if (mIsSelected.get(h)) {
                            mSelectedCount++;
                            Log.i("mSelectedCount", mIsSelected.get(h) + "");
                        }
                    }
                }
                for (int d = 0; d < mSelectedLead.size(); d++) {
                    if (mSelectedLead.get(d)) {
                        mSelectedCount++;
                    }
                }
                Log.i("mSelectedCount", mSelectedCount + "");
                Log.i("mSelectedCount", mSelectedLead.size() + "");
                if (!mSelectedLead.contains(true)) {
                    mCommonMethod.alertDialog("Please assign at least one section to" + " " + mLeadAuditorName, null);
                } else if (mSelectedCount < mSelectedLead.size()) {
                    mCommonMethod.alertDialog("All sections must be allocated to Auditors", null);
                } else {
                    ReAssignConfirmationDialog("Reassign Schedule?", ReAssignAuditorsActivity.this);
                }

            }
        });

        mImagviewBack = (ImageView) findViewById(R.id.imagview_back);
        mImagviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSavedChanges){
                    saveChangesConfirmationDialog("Unsaved changes will be lost. Do you want to continue?", ReAssignAuditorsActivity.this);
                }else {
                    finish();
                }
            }
        });
    }

    public void ReAssignConfirmationDialog(String message, final Context context) {
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
                        sendReassignToServer();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void saveChangesConfirmationDialog(String message, final Context context) {
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
                        finish();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(isSavedChanges){
            saveChangesConfirmationDialog("Unsaved changes will be lost. Do you want to continue?", ReAssignAuditorsActivity.this);
        }else {
            super.onBackPressed();
        }
    }

    private void sendReassignToServer(){
        ArrayList<UserSectionModel> mainSendList = new ArrayList<>();
        UserSectionModel mUserSectionModel =  new UserSectionModel();

        mUserSectionModel.setUsername(mLeadAuditorName);
        try {
            mUserSectionModel.setUserID(Integer.parseInt(mSession.getUserId()));
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Boolean> mTemp = new ArrayList<>();
        mTemp = mSelectedLead;

        mUserSectionModel.setmSelectedMain(mTemp);
        List<GetAuditorsAndSection> getAuditorsLists = new ArrayList<>();

        for (int y = 0; y < getSelectionsList.size(); y++) {
            GetAuditorsAndSection sectionModel = new GetAuditorsAndSection();
            sectionModel.setSectionID(getSelectionsList.get(y).getScheduleSectionID());
            sectionModel.setAbbrv(getSelectionsList.get(y).getAbbrv());
            getAuditorsLists.add(sectionModel);
        }

        mUserSectionModel.setGetAuditorsLists(getAuditorsLists);
        mainSendList.add(mUserSectionModel);

        for(int j=0;j<getUserSectionsList.size();j++) {
            List<Boolean> mIsSelected = getUserSectionsList.get(j).getmSelectedMain();
            if(mIsSelected.contains(true)) {
                mainSendList.add(getUserSectionsList.get(j));
            }
        }
        mUserSection =  new StringBuilder();

        if(mainSendList!=null) {
            for (int j = 0; j < mainSendList.size(); j++) {
                List<Boolean> mTempSelected = mainSendList.get(j).getmSelectedMain();
                List<GetAuditorsAndSection> mTempAuditorsLists = mainSendList.get(j).getGetAuditorsLists();
                for (int n = 0; n < mTempSelected.size(); n++) {
                    if (mainSendList.get(j).getUserID().toString().equalsIgnoreCase(mSession.getUserId())) {
                        if (mTempSelected.get(n)) {
                            mUserSection.append("<UserSection>\n" +
                                    "<ScheduleSectionID>" + mTempAuditorsLists.get(n).getSectionID() + "</ScheduleSectionID>\n" +
                                    "<UserID>" + mainSendList.get(j).getUserID() + "</UserID>\n" +
                                    "</UserSection>");
                        }
                    } else {
                        if (mTempSelected.get(n)) {
                            mUserSection.append("<UserSection>\n" +
                                    "<ScheduleSectionID>" + mTempAuditorsLists.get(n).getSectionID() + "</ScheduleSectionID>\n" +
                                    "<UserID>" + mainSendList.get(j).getUserID() + "</UserID>\n" +
                                    "</UserSection>");
                        }
                    }
                }
            }

            Log.i("mUserSection", mUserSection.toString());
        }

        if (mCommonMethod.isConnectingToInternet()) {
            new ReassignSectionAsync().execute();
        } else {
            mCommonMethod.alertDialog("Please check your internet connection", null);
        }
    }

    //Re Assign Auditor sections
    public class ReassignSectionAsync extends AsyncTask<String, String, String> {
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

            String result = null , parsedResult = null;
            try {
                Log.i("",mLeadAuditorID.toString());
                result = mWebservices.mReassignSection(mLeadAuditorID.toString(), mAuditScheduleID, mUserSection.toString());
                parsedResult = xmlRessignedParser(result);
            } catch (NullPointerException ex){
                ex.printStackTrace();
            }
            return parsedResult;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            //mCommonMethod.alertDialog(result,null);
            if(result!=null) {
                Intent intent = new Intent();
                setResult(2, intent);
                finish();//finishing activity
            }
        }
    }

    //Get the Assigned AuditorsList
    public class GetAuditorsAndAssignedSectionsAsync extends AsyncTask<String, String, String> {
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
            String result = null;
            result = mWebservices.mGetAuditorsAndAssignedSections(mAuditScheduleID, mUserID);
            xmlParser(result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            mProgressDialog.dismiss();
            mTempAuditorsLists = new ArrayList<>();
            for(ReAssignAuditorModel mReAssignAuditorModel:getAuditorsLists){
                if(mReAssignAuditorModel.isLeadAuditor()){
                    mLeadAuditorTxtView.setText(mReAssignAuditorModel.getUsername()+" "+"-"+" "+"Lead Auditor");
                    mLeadAuditorName = mReAssignAuditorModel.getUsername();
                    mLeadAuditorID = mReAssignAuditorModel.getUserID();
                }else {
                    mTempAuditorsLists.add(mReAssignAuditorModel);
                }
            }

            if(getSelectionsList!=null && getSelectionsList.size()>0) {
                for (int i = 0; i < getSelectionsList.size(); i++) {
                    mSelectedLead.add(false);
                }
                if(getAssignedList!=null) {
                    for (int j = 0; j < getAssignedList.size(); j++) {
                        if (getAssignedList.get(j).getUserID().equals(mLeadAuditorID)) {
                            for (int y = 0; y < getSelectionsList.size(); y++) {
                                if (getAssignedList.get(j).getScheduleSectionID().toString().equalsIgnoreCase(
                                        getSelectionsList.get(y).getScheduleSectionID().toString().toString())) {
                                    mSelectedLead.set(y, true);
                                }
                            }

                        }
                    }
                }

                mLeadAuditorSelectionAdapter =  new LeadAuditorSelectionAdapter(ReAssignAuditorsActivity.this,getSelectionsList,mSelectedLead);
                mSelected_lead_gridView.setAdapter(mLeadAuditorSelectionAdapter);
                mSelected_lead_gridView.setExpanded(true);


                getUserSectionsList = new ArrayList<>();


                for (int j = 0; j < mTempAuditorsLists.size(); j++) {
                    UserSectionModel mUserSectionModel = new UserSectionModel();
                    mUserSectionModel.setUsername(mTempAuditorsLists.get(j).getUsername());
                    mUserSectionModel.setUserID(mTempAuditorsLists.get(j).getUserID());
                    List<GetAuditorsAndSection> getAuditorsLists = new ArrayList<>();
                    List<Boolean> mTemp = new ArrayList<>();
                    for (int y = 0; y < getSelectionsList.size(); y++) {
                        GetAuditorsAndSection sectionModel = new GetAuditorsAndSection();
                        sectionModel.setSectionID(getSelectionsList.get(y).getScheduleSectionID());
                        sectionModel.setAbbrv(getSelectionsList.get(y).getAbbrv());
                        getAuditorsLists.add(sectionModel);
                        mTemp.add(false);
                    }

                    for (int m = 0; m < getAssignedList.size(); m++) {
                        if (getAssignedList.get(m).getUserID().toString().equalsIgnoreCase(mTempAuditorsLists.get(j).getUserID().toString())) {
                            for (int n = 0; n < getSelectionsList.size(); n++) {
                                if (getAssignedList.get(m).getScheduleSectionID().toString().equalsIgnoreCase(
                                        getSelectionsList.get(n).getScheduleSectionID().toString().toString())) {
                                    mTemp.set(n, true);
                                }
                            }

                        }
                    }

                    mUserSectionModel.setmSelectedMain(mTemp);
                    mUserSectionModel.setGetAuditorsLists(getAuditorsLists);
                    getUserSectionsList.add(mUserSectionModel);

                }

                mSelectedAuditorsAdapter = new SelectedAuditorsAdapter(ReAssignAuditorsActivity.this,getUserSectionsList);
                mAssigned_sections_gridView.setAdapter(mSelectedAuditorsAdapter);
                mAssigned_sections_gridView.setExpanded(true);
            }


        }
    }

    public String xmlRessignedParser(String response) {
        String text = "", result = "";
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

                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("MessageText")) {
                            // add Checklist object to list
                            result = text;
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
        return result;
    }

    public void xmlParser(String response) {
        List<ReAssignAuditorModel> mAuditorLists = new ArrayList<ReAssignAuditorModel>();
        List<ReAssignAuditorModel> mSectionsLists = new ArrayList<ReAssignAuditorModel>();
        List<ReAssignAuditorModel> mAssignedSectionsLists = new ArrayList<ReAssignAuditorModel>();
        ReAssignAuditorModel auditorModel = null, sectionModel = null, assignedModel = null;
        String text = "";
        boolean isTable = false, isTable1 = false, isTable2 = false;
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
                        // add Checklist object to list
                        if (tagname.equalsIgnoreCase("Table")) {
                            auditorModel = new ReAssignAuditorModel();
                            isTable = true;
                            isTable1 = false;
                            isTable2 = false;
                        }
                        if (tagname.equalsIgnoreCase("Table1")) {
                            sectionModel = new ReAssignAuditorModel();
                            isTable = false;
                            isTable1 = true;
                            isTable2 = false;
                        }
                        if (tagname.equalsIgnoreCase("Table2")) {
                            assignedModel = new ReAssignAuditorModel();
                            isTable = false;
                            isTable1 = false;
                            isTable2 = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                       if (tagname.equalsIgnoreCase("Table")) {
                            // add Checklist object to list
                            mAuditorLists.add(auditorModel);
                        } else if (isTable && tagname.equalsIgnoreCase("IsLeadAuditor")) {
                           auditorModel.setIsLeadAuditor(Boolean.parseBoolean(text));
                        } else if (isTable && tagname.equalsIgnoreCase("UserID")) {
                            auditorModel.setUserID(Integer.parseInt(text));
                        } else if (isTable && tagname.equalsIgnoreCase("Username")) {
                            auditorModel.setUsername(text);
                        }else if (tagname.equalsIgnoreCase("Table1")) {
                            mSectionsLists.add(sectionModel);
                        }else if (isTable1 && tagname.equalsIgnoreCase("ScheduleSectionID")) {
                            sectionModel.setScheduleSectionID(Integer.parseInt(text));
                        }else if (isTable1 && tagname.equalsIgnoreCase("Abbrv")) {
                            sectionModel.setAbbrv(text);
                       }else if (tagname.equalsIgnoreCase("Table2")) {
                            mAssignedSectionsLists.add(assignedModel);
                       } else if (isTable2 && tagname.equalsIgnoreCase("AuditorScheduleID")) {
                            assignedModel.setAuditorScheduleID(Integer.parseInt(text));
                       } else if (isTable2 && tagname.equalsIgnoreCase("UserID")) {
                            assignedModel.setUserID(Integer.parseInt(text));
                        }else if (isTable2 && tagname.equalsIgnoreCase("ScheduleSectionID")) {
                            assignedModel.setScheduleSectionID(Integer.parseInt(text));
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

        getAuditorsLists = mAuditorLists;
        getSelectionsList = mSectionsLists;
        getAssignedList = mAssignedSectionsLists;
    }


    public class LeadAuditorSelectionAdapter extends BaseAdapter {

        ReAssignAuditorModel tempValues = null;
        List<ReAssignAuditorModel> mCheckList;
        int i = 0;
        List<Boolean> mSelectedTemp;
        /***********
         * Declare Used Variables
         *********/
        private Activity activity;
        private LayoutInflater inflater = null;

        /*************
         * LocationAdapter Constructor
         *****************/
        public LeadAuditorSelectionAdapter(Activity a, List<ReAssignAuditorModel> checkList, List<Boolean> SelectedTemp) {
            /********** Take passed values **********/
            activity = a;
            /***********  Layout inflator to call external xml layout () ***********/
            inflater = LayoutInflater.from(a);
            mCheckList = checkList;
            mSelectedTemp = SelectedTemp;
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mCheckList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /******
         * Depends upon data size called for each row , Create each ListView row
         *****/
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            final ViewHolder holder;

            try {
                if (vi == null) {
                    /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                    vi = inflater.inflate(R.layout.assign_audit_selectedlist_item_layout, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    holder = new ViewHolder();
                    holder.text = (TextView) vi.findViewById(R.id.checkListTxt);
                    holder.selectedImageView = (ImageView) vi.findViewById(R.id.checkListImage);
                    /************  Set holder with LayoutInflater ************/
                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }
                /***** Get each Model object from Arraylist ********/
                tempValues = mCheckList.get(position);
                /************  Set Model values in Holder elements ***********/
                holder.text.setText(tempValues.getAbbrv());
                holder.text.setTag(tempValues.getScheduleSectionID());

                Drawable drawableImg;
                if (mSelectedTemp.get(position)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawableImg = getResources().getDrawable(R.drawable.check_sel,getTheme());
                    } else {
                        drawableImg = getResources().getDrawable(R.drawable.check_sel);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawableImg = getResources().getDrawable(R.drawable.check_normal,getTheme());
                    } else {
                        drawableImg = getResources().getDrawable(R.drawable.check_normal);
                    }
                }
                int sdk = Build.VERSION.SDK_INT;
                if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    holder.selectedImageView.setBackgroundDrawable(drawableImg);
                } else {
                    holder.selectedImageView.setBackground(drawableImg);
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return vi;
        }

        /*********
         * Create a holder Class to contain inflated xml file elements
         *********/
        public class ViewHolder {
            public TextView text;
            public ImageView selectedImageView;
        }
    }

    public class SelectedAuditorsAdapter extends BaseAdapter {

        UserSectionModel tempValues = null;
        List<UserSectionModel> mCheckList;
        List<GetAuditorsAndSection> mTempGetAuditorsLists = new ArrayList<>();
        int i = 0;
        List<Boolean> mSelected;
        /***********
         * Declare Used Variables
         *********/
        private Activity activity;
        private LayoutInflater inflater = null;

        /*************
         * LocationAdapter Constructor
         *****************/
        public SelectedAuditorsAdapter(Activity a, List<UserSectionModel> checkList) {
            /********** Take passed values **********/
            activity = a;
            /***********  Layout inflator to call external xml layout () ***********/
            inflater = LayoutInflater.from(a);
            mCheckList = checkList;
        }


        @Override
        public int getCount() {
            return mCheckList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        /******
         * Depends upon data size called for each row , Create each ListView row
         *****/
        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            final ViewHolder holder;

            try {
                if (vi == null) {
                    /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                    vi = inflater.inflate(R.layout.single_attribute_reassigned_layout, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    holder = new ViewHolder();
                    holder.text = (TextView) vi.findViewById(R.id.single_Auditor_Title);
                    holder.mSelectedGrid = (ExpandableHeightGridView) vi.findViewById(R.id.single_auditor_gridView);
                    holder.mSubTitleContainer = (RelativeLayout) vi.findViewById(R.id.single_auditor_title_container);
                    /************  Set holder with LayoutInflater ************/
                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }
                /***** Get each Model object from Arraylist ********/
                tempValues = mCheckList.get(position);
                /************  Set Model values in Holder elements ***********/
                holder.text.setText(tempValues.getUsername());
                holder.text.setTag(tempValues.getUserID());
                //holder.removeBtn.setTag(mChoose_Auditor_button.getTag());
                List<GetAuditorsAndSection> mTempLists = tempValues.getGetAuditorsLists();
                mSelected = tempValues.getmSelectedMain();

                final Sub_sectionsAdapter mtemp_sectionsAdapter =  new Sub_sectionsAdapter(ReAssignAuditorsActivity.this,mTempLists,mSelected);
                holder.mSelectedGrid.setAdapter(mtemp_sectionsAdapter);
                holder.mSelectedGrid.setExpanded(true);
                holder.mSelectedGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int grid_position, long id) {
                        TextView productItem = (TextView) view.findViewById(R.id.checkListTxt);
                        mAssignButton.setEnabled(true);
                        mAssignButton.setBackgroundResource(R.drawable.btn_selector);
                        mAssignButton.setTextColor(Color.parseColor("#FFFFFF"));
                        isSavedChanges = true;
                        List<Boolean> mTempSelected=mCheckList.get(position).getmSelectedMain();
                        if (!mTempSelected.get(grid_position)) {

                            if (getUserSectionsList != null) {
                                for (int j = 0; j < getUserSectionsList.size(); j++) {
                                    if(j==position) {
                                        Log.i("position","position");
                                        List<Boolean> mIsSelected = getUserSectionsList.get(j).getmSelectedMain();
                                        List<Boolean> mTemp = getUserSectionsList.get(j).getmSelectedMain();
                                            for (int i = 0; i < mIsSelected.size(); i++) {
                                                if (i == grid_position) {
                                                    Log.i("Selected", grid_position + "i==" + i);
                                                    mTemp.set(i, true);
                                                }
                                            }
                                            getUserSectionsList.get(j).setmSelectedMain(mTemp);
                                    }else {
                                        Log.i("remove","position");
                                        List<Boolean> mIsSelected = getUserSectionsList.get(j).getmSelectedMain();
                                        List<Boolean> mTemp = getUserSectionsList.get(j).getmSelectedMain();
                                        if (mIsSelected.contains(true)) {
                                            for (int i = 0; i < mIsSelected.size(); i++) {
                                                if (i == grid_position) {
                                                    Log.i("already Selected", grid_position + "i==" + i);
                                                    mTemp.set(i, false);
                                                }
                                            }

                                            getUserSectionsList.get(j).setmSelectedMain(mTemp);
                                        }
                                    }

                                }
                            }

                            //mTemp.set(grid_position, true);
                            mSelectedLead.set(grid_position, false);

                        } else {
                            if (getUserSectionsList != null) {
                                Log.i("remove","position");
                                for (int j = 0; j < getUserSectionsList.size(); j++) {
                                    if(j==position) {
                                        List<Boolean> mIsSelected = getUserSectionsList.get(j).getmSelectedMain();
                                        List<Boolean> mTemp = getUserSectionsList.get(j).getmSelectedMain();
                                        if (mIsSelected.contains(true)) {
                                            for (int i = 0; i < mIsSelected.size(); i++) {
                                                if (i == grid_position) {
                                                    Log.i("Selected remove", grid_position + "i==" + i);
                                                    mTemp.set(i, false);
                                                }
                                            }

                                            getUserSectionsList.get(j).setmSelectedMain(mTemp);
                                        }
                                    }

                                }
                            }
                            //mSelectedLead.set(position, true);
                        }
                        //mtemp_sectionsAdapter.refresh();
                        ReAssignAuditorsActivity.this.mLeadAuditorSelectionAdapter.refresh();
                        notifyDataSetChanged();
                    }
                });
                if(position%2==0) {
                    holder.mSubTitleContainer.setBackgroundColor(Color.parseColor("#FAFAFA"));
                    holder.mSelectedGrid.setBackgroundColor(Color.parseColor("#FAFAFA"));
                }else {
                    holder.mSubTitleContainer.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.mSelectedGrid.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return vi;
        }

        /*********
         * Create a holder Class to contain inflated xml file elements
         *********/
        public class ViewHolder {
            public TextView text;
            public ExpandableHeightGridView mSelectedGrid;
            public RelativeLayout mSubTitleContainer;
        }
    }

    public class Sub_sectionsAdapter extends BaseAdapter {

        GetAuditorsAndSection tempValues = null;
        List<GetAuditorsAndSection> mCheckList;
        List<Boolean> mSelectedTemp;
        int i = 0;
        List templist;
        /***********
         * Declare Used Variables
         *********/
        private Activity activity;
        private LayoutInflater inflater = null;

        /*************
         * LocationAdapter Constructor
         *****************/
        public Sub_sectionsAdapter(Activity a, List<GetAuditorsAndSection> checkList, List<Boolean> SelectedTemp) {
            /********** Take passed values **********/
            activity = a;
            /***********  Layout inflator to call external xml layout () ***********/
            inflater = LayoutInflater.from(a);
            mCheckList = checkList;
            mSelectedTemp = SelectedTemp;
        }


        @Override
        public int getCount() {
            return mCheckList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        /******
         * Depends upon data size called for each row , Create each ListView row
         *****/
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            final ViewHolder holder;

            try {
                if (vi == null) {
                    /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                    vi = inflater.inflate(R.layout.assign_audit_selectedlist_item_layout, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    holder = new ViewHolder();
                    holder.text = (TextView) vi.findViewById(R.id.checkListTxt);
                    holder.selectedImageView = (ImageView) vi.findViewById(R.id.checkListImage);
                    /************  Set holder with LayoutInflater ************/
                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }
                /***** Get each Model object from Arraylist ********/
                tempValues = mCheckList.get(position);
                /************  Set Model values in Holder elements ***********/
                holder.text.setText(tempValues.getAbbrv());
                holder.text.setTag(tempValues.getSectionID());

                Drawable drawableImg;
                if (mSelectedTemp.get(position)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawableImg = getResources().getDrawable(R.drawable.check_sel,getTheme());
                    } else {
                        drawableImg = getResources().getDrawable(R.drawable.check_sel);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawableImg = getResources().getDrawable(R.drawable.check_normal,getTheme());
                    } else {
                        drawableImg = getResources().getDrawable(R.drawable.check_normal);
                    }
                }
                int sdk = Build.VERSION.SDK_INT;
                if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    holder.selectedImageView.setBackgroundDrawable(drawableImg);
                } else {
                    holder.selectedImageView.setBackground(drawableImg);
                }


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return vi;
        }

        /*********
         * Create a holder Class to contain inflated xml file elements
         *********/
        public class ViewHolder {
            public TextView text;
            public ImageView selectedImageView;
        }
    }

}

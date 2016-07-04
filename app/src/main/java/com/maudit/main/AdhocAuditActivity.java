package com.maudit.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import com.maudit.database.DataBaseAdpter;
import com.maudit.model.AdhocListModel;
import com.maudit.model.CheckListsModel;
import com.maudit.model.GetAuditorsAndSection;
import com.maudit.model.UserSectionModel;
import com.maudit.utils.CommonMethod;
import com.maudit.utils.ImageUrlValidator;
import com.maudit.utils.Session;
import com.maudit.utils.Webservices;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdhocAuditActivity extends AppCompatActivity {
    View popupView;
    PopupWindow popupWindow;
    Button mAudit_type_button, mLocation_button, mChecklist_type_button, mStart_audit_button;
    List<AdhocListModel> getLocationLists = null;
    List<AdhocListModel> getAdhocCheckLists = null, getAdhocAuditorList = null;
    List<AdhocListModel> mTempCheckList = null;
    LocationAdapter locationAdapter;
    AuditTypeAdapter auditTypeAdapter;
    CheckListAdapter checkListAdapter;
    // List view
    private ListView popUpListView, mSelectedAuditorsListView;
    GridView checkListGrid;
    private SearchView searchView;
    private Session mSession;
    private TextView mTextview_username, mSelectAndAssign_auditor;
    private CommonMethod mCommonMethod;
    private ProgressDialog mProgressDialog;
    private Webservices mWebservices;
    private int selectedPosition;
    private boolean isSelected = false;
    private String selectedCheckListText,mAuditee, mPatientCaseNo;
    private Object selectedCheckListTag;
    ArrayList<String> mainUserNameList = new ArrayList<>();
    ArrayList<UserSectionModel> mainSendList = new ArrayList<>();
    Integer mAuditTypeID, mCheckListTypeID, mLocationID, mUserID;
    StringBuilder mUserSection =  new StringBuilder();
    Boolean isSavedChanges = false;
    EditText mAuditeeEdtTxt, mPatientCaseNoEdtTxt;
    private ImageView mImagviewBack;
    private  List<CheckListsModel> mCheckListsModelList;
    private DataBaseAdpter mDataBaseAdpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhoc_audit);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        mProgressDialog = new ProgressDialog(AdhocAuditActivity.this);
        mWebservices = new Webservices(this);
        mCommonMethod = new CommonMethod(this);
        mSession = new Session(this);
        mDataBaseAdpter=new DataBaseAdpter(this);
        mSelectedAuditorsListView = (ListView) findViewById(R.id.selected_auditors_list);
        mAudit_type_button = (Button) findViewById(R.id.audit_type_button);
        mLocation_button = (Button) findViewById(R.id.location_button);
        mChecklist_type_button = (Button) findViewById(R.id.checklist_type_button);
        mStart_audit_button = (Button) findViewById(R.id.button_start_audit);
        mPatientCaseNoEdtTxt = (EditText) findViewById(R.id.patient_Edt_txt);
        mAuditeeEdtTxt = (EditText) findViewById(R.id.auditee_Edt_txt);

        mAudit_type_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auditTypePopupInit(v);
            }
        });
        mLocation_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationPopupInit(v);
            }
        });
        mChecklist_type_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTempCheckList != null) {
                    checkListTypePopupInit(v);
                }
            }
        });
        mSelectAndAssign_auditor = (TextView) findViewById(R.id.assign_auditor);
        mSelectAndAssign_auditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if (mAudit_type_button.getText().toString().equalsIgnoreCase("") &&
                        mChecklist_type_button.getText().toString().equalsIgnoreCase("")) {
                    message = "Please Select Audit Type and Checklist Type";
                    mCommonMethod.alertDialog(message, null);
                } else if(mChecklist_type_button.getText().toString().equalsIgnoreCase("")) {
                    message = "Please Select Checklist Type";
                    mCommonMethod.alertDialog(message, null);
                } else {
                    Intent intent = new Intent(AdhocAuditActivity.this, AssignAuditorsActivity.class);
                    if(mainSendList.size()==0) {
                        intent.putExtra("AuditTypeID", mAudit_type_button.getTag().toString());
                        intent.putExtra("CheckListTypeID", selectedCheckListTag.toString());
                    }else {
                        intent.putExtra("AuditTypeID", mAudit_type_button.getTag().toString());
                        intent.putExtra("CheckListTypeID", selectedCheckListTag.toString());
                        intent.putExtra("IsDataAvailable",true);
                    }
                        intent.putExtra("SubTitle", mAudit_type_button.getText().toString() + "-" + selectedCheckListText);
                    startActivityForResult(intent, 1);
                }
            }
        });
        mStart_audit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAuditProcess();
            }
        });

        mTextview_username = (TextView) findViewById(R.id.textview_username);
        mTextview_username.setText("Welcome: " + mSession.getUserName());
        if (mCommonMethod.isConnectingToInternet()) {
            new GetChecklistsAsync().execute();

            //Offline mOffline = new Offline();
            //getLocationLists = xmlParser(mOffline.AdhocDropDownValues);
        } else {
            mCommonMethod.alertDialog("Please check your internet connection", null);
        }

        mImagviewBack = (ImageView) findViewById(R.id.imagview_back);
        mImagviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSavedChanges) {
                    saveChangesConfirmationDialog("Unsaved changes will be lost. Do you want to continue?", AdhocAuditActivity.this);
                } else {
                    finish();
                }
            }
        });
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
        if(isSavedChanges||!mAuditeeEdtTxt.getText().toString().equalsIgnoreCase("")||!mPatientCaseNoEdtTxt.getText().toString().equalsIgnoreCase("")){
            saveChangesConfirmationDialog("Unsaved changes will be lost. Do you want to continue?", AdhocAuditActivity.this);
        }else {
            super.onBackPressed();
        }
    }



    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (resultCode == 2) {
          /*  mainUserIdList = data.getIntegerArrayListExtra("UserId");
            mainSectionIdList = data.getIntegerArrayListExtra("SectionId");
            mainUserNameList = data.getStringArrayListExtra("UserNames");*/
             mainSendList = new ArrayList<>();
            mainUserNameList = new ArrayList<>();
            mainSendList =  AppController.getInstance().getMainSendList();
            mUserSection =  new StringBuilder();
            for (int j = 0; j < mainSendList.size(); j++) {
                //Log.i("Usernames", mainSendList.get(j).getUsername());
                mainUserNameList.add(mainSendList.get(j).getUsername());
            }
            if(mainSendList!=null) {
                for (int j = 0; j < mainSendList.size(); j++) {
                    List<Boolean> mTemp = mainSendList.get(j).getmSelectedMain();
                    List<GetAuditorsAndSection> mTempAuditorsLists = mainSendList.get(j).getGetAuditorsLists();
                    for (int n = 0; n < mTemp.size(); n++) {
                        Log.i("mTempAuditorsLists.get(n).getSectionID()",""+mTempAuditorsLists.get(n).getSectionID());
                        if (mainSendList.get(j).getUserID().toString().equalsIgnoreCase(mSession.getUserId())) {
                            if (!mTemp.get(n)) {
                                mUserSection.append("<UserSection>\n" +
                                        "<SectionID>" + mTempAuditorsLists.get(n).getSectionID() + "</SectionID>\n" +
                                        "<UserID>" + mainSendList.get(j).getUserID() + "</UserID>\n" +
                                        "</UserSection>");
                            }
                        } else {
                            if (mTemp.get(n)) {
                                mUserSection.append("<UserSection>\n" +
                                        "<SectionID>" + mTempAuditorsLists.get(n).getSectionID() + "</SectionID>\n" +
                                        "<UserID>" + mainSendList.get(j).getUserID() + "</UserID>\n" +
                                        "</UserSection>");
                            }
                        }
                    }
                }

                Log.i("mUserSection", mUserSection.toString());
            }
            SelectedAuditorsListAdapter mSelectedAuditorsListAdapter = new SelectedAuditorsListAdapter(AdhocAuditActivity.this,mainUserNameList);
            mSelectedAuditorsListView.setAdapter(mSelectedAuditorsListAdapter);
        }
    }

    private void startAuditProcess(){
        String message = "";
        if(mAudit_type_button.getText().toString().equalsIgnoreCase("") && mLocation_button.getText().toString().equalsIgnoreCase("") &&
                mChecklist_type_button.getText().toString().equalsIgnoreCase("")&&mainUserNameList.size()==0){
            message = "Please Select Audit Type, Checklist Type, Location, Auditor";
            mCommonMethod.alertDialog(message, null);
        } else if(mLocation_button.getText().toString().equalsIgnoreCase("") &&
                mChecklist_type_button.getText().toString().equalsIgnoreCase("")&&mainUserNameList.size()==0){
            message = "Please Select Checklist Type, Location, Auditor";
            mCommonMethod.alertDialog(message, null);
        }else if(mLocation_button.getText().toString().equalsIgnoreCase("")&&mainUserNameList.size()==0){
            message = "Please Select Location, Auditor";
            mCommonMethod.alertDialog(message, null);
        }else if(mainUserNameList.size()==0){
            message = "Please Select Auditor";
            mCommonMethod.alertDialog(message, null);
        }else if(mLocation_button.getText().toString().equalsIgnoreCase("")){
            message = "Please Select Location";
            mCommonMethod.alertDialog(message, null);
        }else {
            mAuditTypeID = Integer.parseInt(mAudit_type_button.getTag().toString());
            mCheckListTypeID = Integer.parseInt(selectedCheckListTag.toString());
            mLocationID = Integer.parseInt(mLocation_button.getTag().toString());
            mUserID = Integer.parseInt(mSession.getUserId());
            mAuditee = mAuditeeEdtTxt.getText().toString();
            mPatientCaseNo = mPatientCaseNoEdtTxt.getText().toString();
            if (mCommonMethod.isConnectingToInternet()) {
                new AddAdhocAuditAsync().execute();
            } else {
                mCommonMethod.alertDialog("Please check your internet connection", null);
            }
        }
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
                mCommonMethod.alertDialogMessage("Do you want to logout?",AdhocAuditActivity.this,AdhocAuditActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void locationPopupInit(View v) {
        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.drop_down_dialog, null, false);
        int popUpHeight = (int)getResources().getDimension(R.dimen.adhoc_audit_drop_down_pop_up_height);
        popupWindow = new PopupWindow(popupView, v.getWidth(), popUpHeight, true);

        searchView = (SearchView) popupView.findViewById(R.id.searchView);
        searchView.setGravity(Gravity.RIGHT);

        popUpListView = (ListView) popupView.findViewById(R.id.list_view);

        // Adding items to listview
        locationAdapter = new LocationAdapter(this, getLocationLists);
        popUpListView.setAdapter(locationAdapter);
        popUpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView productItem = (TextView) view.findViewById(R.id.product_name);
                Log.i("Selected", productItem.getText().toString());
                mLocation_button.setText(productItem.getText().toString());
                mLocation_button.setTag(productItem.getTag());
                isSavedChanges = true;
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AdhocAuditActivity.this.locationAdapter.getFilter().filter(newText);
                return false;
            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            if (getLocationLists.size() > 0) {
                popupWindow.showAsDropDown(v, 0, 0);
            } else {
                mCommonMethod.alertDialog("No Item Found", null);
            }
        }
        /*popupWindow.setFocusable(true);
        popupWindow.update();
        if (popupWindow != null) {
            // Closes the popup window when touch outside.
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            // Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }*/
    }

    private void auditTypePopupInit(View v) {
        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.drop_down_dialog, null, false);
        int popUpHeight = (int)getResources().getDimension(R.dimen.adhoc_audit_drop_down_pop_up_height);
        popupWindow = new PopupWindow(popupView, v.getWidth(), popUpHeight, true);

        searchView = (SearchView) popupView.findViewById(R.id.searchView);
        searchView.setGravity(Gravity.RIGHT);

        popUpListView = (ListView) popupView.findViewById(R.id.list_view);

        // Adding items to listview
        auditTypeAdapter = new AuditTypeAdapter(this, getAdhocAuditorList);

        Collections.sort(getAdhocAuditorList, new Comparator<AdhocListModel>() {
            public int compare(AdhocListModel v1, AdhocListModel v2) {
                return v1.getAuditType().toLowerCase().compareTo(v2.getAuditType().toLowerCase());
            }
        });

        popUpListView.setAdapter(auditTypeAdapter);
        popUpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView productItem = (TextView) view.findViewById(R.id.product_name);
                Log.i("Selected", productItem.getTag() + "");
                mAudit_type_button.setText(productItem.getText().toString());
                mAudit_type_button.setTag(productItem.getTag());
                isSavedChanges = true;
                mChecklist_type_button.setText("");
                selectedCheckListText = "";
                selectedCheckListTag = "";
                mTempCheckList = new ArrayList<AdhocListModel>();
                for (int i = 0; i < getAdhocCheckLists.size(); i++) {
                    if (getAdhocCheckLists.get(i).getAuditTypeID().equals(productItem.getTag())) {
                        Log.i("Selected", getAdhocCheckLists.get(i).getChecklistName());
                        mTempCheckList.add(getAdhocCheckLists.get(i));
                    }
                }
                isSelected = false;
                //clear the selected auditor list
                mainSendList = new ArrayList<>();
                mainUserNameList = new ArrayList<>();
                AppController.getInstance().setMainSendList(mainSendList);
                SelectedAuditorsListAdapter mSelectedAuditorsListAdapter = new SelectedAuditorsListAdapter(AdhocAuditActivity.this, mainUserNameList);
                mSelectedAuditorsListView.setAdapter(mSelectedAuditorsListAdapter);

                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }


            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AdhocAuditActivity.this.auditTypeAdapter.getFilter().filter(newText);
                return false;
            }
        });


        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            if (getAdhocAuditorList.size() > 0) {
                popupWindow.showAsDropDown(v, 0, 0);
            } else {
                mCommonMethod.alertDialog("No Item Found", null);
            }
        }
       /* popupWindow.setFocusable(true);
        popupWindow.update();
        if (popupWindow != null) {
            // Closes the popup window when touch outside.
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            // Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }*/
    }

    /*private void checkListTypePopupInit(View v) {
        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.adhoc_audit_checklist_dialog, null, false);
        int popUpHeight = (int)getResources().getDimension(R.dimen.adhoc_audit_checklist_pop_up);
        popupWindow = new PopupWindow(popupView, popUpHeight, popUpHeight, true);


         checkListGrid = (GridView) popupView.findViewById(R.id.Checklist_gridView);

        // Adding items to listview
        checkListAdapter = new CheckListAdapter(this, mTempCheckList);
        checkListGrid.setAdapter(checkListAdapter);
        checkListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView productItem = (TextView) view.findViewById(R.id.checkListTxt);
                Log.i("Selected", productItem.getText().toString());
                selectedCheckListText = productItem.getText().toString();
                selectedCheckListTag = productItem.getTag();
                AdhocAuditActivity.this.checkListAdapter.refresh(position, true);

            }
        });
        Button doneButton = (Button) popupView.findViewById(R.id.adhoc_audit_done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChecklist_type_button.setText(selectedCheckListText);
                mChecklist_type_button.setTag(selectedCheckListTag);
                //clear the selected auditor list
                mainSendList = new ArrayList<>();
                mainUserNameList = new ArrayList<>();
                AppController.getInstance().setMainSendList(mainSendList);
                SelectedAuditorsListAdapter mSelectedAuditorsListAdapter = new SelectedAuditorsListAdapter(AdhocAuditActivity.this,mainUserNameList);
                mSelectedAuditorsListView.setAdapter(mSelectedAuditorsListAdapter);

                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        if (popupWindow != null) {
            // Closes the popup window when touch outside.
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            // Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }*/

    private void checkListTypePopupInit(View v) {

        final Dialog mdialog = new Dialog(this);
        mdialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View mView = getLayoutInflater().inflate(R.layout.adhoc_audit_checklist_dialog, null);

        mdialog.setContentView(mView);


        checkListGrid = (GridView) mView.findViewById(R.id.Checklist_gridView);

        Collections.sort(mTempCheckList, new Comparator<AdhocListModel>() {
            public int compare(AdhocListModel v1, AdhocListModel v2) {
                return v1.getChecklistName().toLowerCase().compareTo(v2.getChecklistName().toLowerCase());
            }
        });

        // Adding items to listview
        checkListAdapter = new CheckListAdapter(this, mTempCheckList);
        checkListGrid.setAdapter(checkListAdapter);
        checkListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView productItem = (TextView) view.findViewById(R.id.checkListTxt);
                Log.i("Selected", productItem.getText().toString());
                selectedCheckListText = productItem.getText().toString();
                selectedCheckListTag = productItem.getTag();
                AdhocAuditActivity.this.checkListAdapter.refresh(position, true);

            }
        });
        Button doneButton = (Button) mView.findViewById(R.id.adhoc_audit_done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChecklist_type_button.setText(selectedCheckListText);
                mChecklist_type_button.setTag(selectedCheckListTag);
                //clear the selected auditor list
                mainSendList = new ArrayList<>();
                mainUserNameList = new ArrayList<>();
                AppController.getInstance().setMainSendList(mainSendList);
                SelectedAuditorsListAdapter mSelectedAuditorsListAdapter = new SelectedAuditorsListAdapter(AdhocAuditActivity.this,mainUserNameList);
                mSelectedAuditorsListView.setAdapter(mSelectedAuditorsListAdapter);
                mdialog.dismiss();
            }
        });

        mdialog.setCanceledOnTouchOutside(false);
        mdialog.show();
        int popUpHeight = (int)getResources().getDimension(R.dimen.adhoc_audit_checklist_pop_up);
        mdialog.getWindow().setLayout(popUpHeight, popUpHeight); //Controlling width and height.
    }

    public List<AdhocListModel> xmlParser(String response) {
        List<AdhocListModel> auditorLists = new ArrayList<AdhocListModel>();
        List<AdhocListModel> locationLists = new ArrayList<AdhocListModel>();
        List<AdhocListModel> auditorCheckLists = new ArrayList<AdhocListModel>();
        AdhocListModel locationModel = null, auditorModel = null, auditorChecklistModel = null;
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
                            auditorModel = new AdhocListModel();
                            isTable = true;
                            isTable1 = false;
                            isTable2 = false;
                        }
                        if (tagname.equalsIgnoreCase("Table1")) {
                            locationModel = new AdhocListModel();
                            isTable = false;
                            isTable1 = true;
                            isTable2 = false;
                        }
                        if (tagname.equalsIgnoreCase("Table2")) {
                            auditorChecklistModel = new AdhocListModel();
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
                            auditorLists.add(auditorModel);
                        } else if (isTable && tagname.equalsIgnoreCase("AuditTypeID")) {
                            auditorModel.setAuditTypeID(Integer.parseInt(text));
                        } else if (isTable && tagname.equalsIgnoreCase("AuditType")) {
                            auditorModel.setAuditType(text);
                        } else if (isTable && tagname.equalsIgnoreCase("StatusID")) {
                            auditorModel.setStatusID(Integer.parseInt(text));
                        } else if (isTable && tagname.equalsIgnoreCase("StatusType")) {
                            auditorModel.setStatusType(text);
                        } else if (tagname.equalsIgnoreCase("Table1")) {
                            // add Checklist object to list
                            locationLists.add(locationModel);
                        } else if (isTable1 && tagname.equalsIgnoreCase("LocationID")) {
                            locationModel.setLocationID(Integer.parseInt(text));
                        } else if (isTable1 && tagname.equalsIgnoreCase("LocationName")) {
                            locationModel.setLocationName(text);
                        } else if (tagname.equalsIgnoreCase("Table2")) {
                            auditorCheckLists.add(auditorChecklistModel);
                        } else if (isTable2 && tagname.equalsIgnoreCase("AuditTypeID")) {
                            auditorChecklistModel.setAuditTypeID(Integer.parseInt(text));
                        } else if (isTable2 && tagname.equalsIgnoreCase("ChecklistTypeID")) {
                            auditorChecklistModel.setChecklistTypeID(Integer.parseInt(text));
                        } else if (isTable2 && tagname.equalsIgnoreCase("AuditType")) {
                            auditorChecklistModel.setAuditType(text);
                        } else if (isTable2 && tagname.equalsIgnoreCase("ChecklistName")) {
                            auditorChecklistModel.setChecklistName(text);
                        } else if (isTable2 && tagname.equalsIgnoreCase("IconImagePath")) {
                            auditorChecklistModel.setIconImagePath(text);
                        } else if (isTable2 && tagname.equalsIgnoreCase("StatusID")) {
                            auditorChecklistModel.setStatusID(Integer.parseInt(text));
                        } else if (isTable2 && tagname.equalsIgnoreCase("StatusType")) {
                            auditorChecklistModel.setStatusType(text);
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
        getAdhocCheckLists = auditorCheckLists;
        getAdhocAuditorList = auditorLists;
        return locationLists;
    }

    public class GetChecklistsAsync extends AsyncTask<String, String, String> {
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
            result = mWebservices.mGetAllAdhocList(mSession.getUserId());
            getLocationLists = xmlParser(result);

            //xmlParser2(result);
            Log.i(" size-->", "" + getLocationLists.size());
            Log.i("Adhoc size-->", "" + getAdhocCheckLists.size());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            mProgressDialog.dismiss();
        }
    }



    public class AddAdhocAuditAsync extends AsyncTask<String, String, List<CheckListsModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle(getResources().getString(R.string.app_name));
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected List<CheckListsModel> doInBackground(String... params) {
            List<CheckListsModel> getCheckLists=null;
            String  result=null;

            result = mWebservices.mAddAdhocSchedule(mAuditTypeID, mCheckListTypeID, mLocationID, mUserID,mUserSection.toString(), mAuditee, mPatientCaseNo);

            getCheckLists=checkListXmlParser(result);
            return getCheckLists;
        }

        @Override
        protected void onPostExecute(List<CheckListsModel> result) {
            mProgressDialog.dismiss();
            mCheckListsModelList = result;
            if(mCheckListsModelList!=null && mCheckListsModelList.size()>0) {
                mDataBaseAdpter.deleteSection(0);
                Intent intentSectionList = new Intent(AdhocAuditActivity.this, SectionListActivity.class);
                for (CheckListsModel checkListsModel : mCheckListsModelList) {
                    mSession.setScheduleId(Integer.parseInt(checkListsModel.getAuditScheduleID()));
                    intentSectionList.putExtra("SCHEDULE_ID", checkListsModel.getAuditScheduleID());
                    intentSectionList.putExtra("LOCATION", checkListsModel.getLocationName());
                    intentSectionList.putExtra("CHECKLIST_NAME", checkListsModel.getChecklistName());
                    intentSectionList.putExtra("CREATED_BY_USER", checkListsModel.getCreatedBy());
                }
                startActivity(intentSectionList);
            }
            finish();
        }
    }

    public List<CheckListsModel> checkListXmlParser(String response){
        List<CheckListsModel> checkLists= new ArrayList<CheckListsModel>();
        CheckListsModel checkListsModelTable=null;
        String text="";
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput( new StringReader(response) );
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("ChecklistResult")) {
                            // create a new instance of checklist
                            checkListsModelTable = new CheckListsModel(AdhocAuditActivity.this);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();

                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("ChecklistResult")) {
                            // add Checklist object to list
                            checkLists.add(checkListsModelTable);
                        } else if (tagname.equalsIgnoreCase("AuditScheduleID")) {
                            checkListsModelTable.setAuditScheduleID(text);
                        } else if (tagname.equalsIgnoreCase("LocationName")) {
                            checkListsModelTable.setLocationName(text);
                        } else if (tagname.equalsIgnoreCase("IsLeadAuditor")) {
                            checkListsModelTable.setIsLeadAuditor(text);
                        } else if (tagname.equalsIgnoreCase("CreatedByUser")) {
                            checkListsModelTable.setCreatedBy(text);
                        } else if (tagname.equalsIgnoreCase("ChecklistName")) {
                            checkListsModelTable.setChecklistName(text);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        return checkLists;
    }

    public class LocationAdapter extends BaseAdapter {

        AdhocListModel tempValues = null;
        List<AdhocListModel> mStringFilterList, mLocationList;
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
        public LocationAdapter(Activity a, List<AdhocListModel> mLocation) {
            /********** Take passed values **********/
            activity = a;
            /***********  Layout inflator to call external xml layout () ***********/
            inflater = LayoutInflater.from(a);
            mStringFilterList = mLocation;
            mLocationList = mLocation;
        }

        @Override
        public int getCount() {
            return mLocationList.size();
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
            ViewHolder holder;

            try {
                if (vi == null) {
                    /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                    vi = inflater.inflate(R.layout.list_item, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    holder = new ViewHolder();
                    holder.text = (TextView) vi.findViewById(R.id.product_name);
                    /************  Set holder with LayoutInflater ************/
                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }
                /***** Get each Model object from Arraylist ********/
                tempValues = mLocationList.get(position);
                /************  Set Model values in Holder elements ***********/
                holder.text.setText(tempValues.getLocationName());
                holder.text.setTag(tempValues.getLocationID());

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return vi;
        }

        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults oReturn = new FilterResults();
                    final List<AdhocListModel> results = new ArrayList<AdhocListModel>();
                    List<AdhocListModel> orig = getLocationLists;

                    if (constraint != null && constraint.length() > 0) {
                        if (orig != null && orig.size() > 0) {
                            for (final AdhocListModel g : orig) {
                                if (g.getLocationName().toLowerCase()
                                        .contains(constraint.toString())) {
                                    results.add(g);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = mStringFilterList;
                    }
                    return oReturn;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    mLocationList = (ArrayList<AdhocListModel>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        /*********
         * Create a holder Class to contain inflated xml file elements
         *********/
        public class ViewHolder {
            public TextView text;
        }
    }

    public class AuditTypeAdapter extends BaseAdapter {

        AdhocListModel tempValues = null;
        List<AdhocListModel> mStringFilterList, mLocationList;
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
        public AuditTypeAdapter(Activity a, List<AdhocListModel> mLocation) {
            /********** Take passed values **********/
            activity = a;
            /***********  Layout inflator to call external xml layout () ***********/
            inflater = LayoutInflater.from(a);
            mStringFilterList = mLocation;
            mLocationList = mLocation;
        }

        @Override
        public int getCount() {
            return mLocationList.size();
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
            ViewHolder holder;

            try {
                if (vi == null) {
                    /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                    vi = inflater.inflate(R.layout.list_item, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    holder = new ViewHolder();
                    holder.text = (TextView) vi.findViewById(R.id.product_name);
                    /************  Set holder with LayoutInflater ************/
                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }
                /***** Get each Model object from Arraylist ********/
                tempValues = mLocationList.get(position);
                /************  Set Model values in Holder elements ***********/
                holder.text.setText(tempValues.getAuditType());
                holder.text.setTag(tempValues.getAuditTypeID());

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return vi;
        }

        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults oReturn = new FilterResults();
                    final List<AdhocListModel> results = new ArrayList<AdhocListModel>();
                    List<AdhocListModel> orig = getAdhocAuditorList;

                    if (constraint != null && constraint.length() > 0) {
                        if (orig != null && orig.size() > 0) {
                            for (final AdhocListModel g : orig) {
                                if (g.getAuditType().toLowerCase()
                                        .contains(constraint.toString())) {
                                    results.add(g);
                                }
                            }
                        }
                        oReturn.values = results;
                    } else {
                        oReturn.values = mStringFilterList;
                    }
                    return oReturn;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    mLocationList = (ArrayList<AdhocListModel>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        /*********
         * Create a holder Class to contain inflated xml file elements
         *********/
        public class ViewHolder {
            public TextView text;
        }
    }


    public class CheckListAdapter extends BaseAdapter {

        AdhocListModel tempValues = null;
        List<AdhocListModel> mCheckList;
        int i = 0;
        List templist;
        /***********
         * Declare Used Variables
         *********/
        private Activity activity;
        private LayoutInflater inflater = null;
        private String imageUrl;
        //ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        ImageUrlValidator mImageUrlValidator = new ImageUrlValidator();
        /*************
         * LocationAdapter Constructor
         *****************/
        public CheckListAdapter(Activity a, List<AdhocListModel> checkList) {
            /********** Take passed values **********/
            activity = a;
            /***********  Layout inflator to call external xml layout () ***********/
            inflater = LayoutInflater.from(a);
            mCheckList = checkList;
        }

        public void refresh(int position, boolean selected) {
            System.out.println("#--refresh notifydatasetchanged");
            selectedPosition = position;
            isSelected = selected;
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            try {
            if (convertView == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.adhoc_checklist_item_layout, null);
                holder = new ViewHolder();

                holder.text = (TextView) convertView.findViewById(R.id.checkListTxt);
                holder.imageView = (ImageView) convertView.findViewById(R.id.checkListImage);
                holder.selectedImageView = (ImageView) convertView.findViewById(R.id.checkListSelected);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            /*View vi = convertView;
            final ViewHolder holder;
            try {
                if (vi == null) {
                    *//***** Inflate tabitem.xml file for each row ( Defined below ) ******//*
                    vi = inflater.inflate(R.layout.adhoc_checklist_item_layout, null);
                   *//***** View Holder Object to contain tabitem.xml file elements *****//*
                    holder = new ViewHolder();
                    holder.text = (TextView) vi.findViewById(R.id.checkListTxt);
                    holder.imageView = (ImageView) vi.findViewById(R.id.checkListImage);
                    holder.selectedImageView = (ImageView) vi.findViewById(R.id.checkListSelected);
                   *//***********  Set holder with LayoutInflater ***********//*
                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }*/
                /***** Get each Model object from Arraylist ********/
                tempValues = mCheckList.get(position);
                /************  Set Model values in Holder elements ***********/
                holder.text.setText(tempValues.getChecklistName());
                holder.text.setTag(tempValues.getChecklistTypeID());
                Drawable drawableImg;
                if (position == selectedPosition && isSelected) {
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

                imageUrl = mWebservices.IMAGE_URL+tempValues.getIconImagePath();

                if(mImageUrlValidator.validate(imageUrl)) {
                    // If you are using normal ImageView
                    /*imageLoader.get(imageUrl, new ImageLoader.ImageListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("", "Image Load Error: " + error.getMessage());
                        }

                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                            if (response.getBitmap() != null&& arg1) {
                                // load image into imageview
                                holder.imageView.setImageBitmap(response.getBitmap());
                            }
                        }
                    });*/
                    /*RequestManager
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
                                        Log.i("imageUrl---"+position,imageUrl);
                                        holder.imageView.setImageBitmap(response.getBitmap());
                                    }
                                }
                            });*/
                    Picasso.with(activity)
                            .load(imageUrl)
                            .into(holder.imageView);
                }


            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return convertView;
        }

        /*********
         * Create a holder Class to contain inflated xml file elements
         *********/
        public class ViewHolder {
            public TextView text;
            public ImageView imageView, selectedImageView;
        }
    }

    public class SelectedAuditorsListAdapter extends BaseAdapter {
        ArrayList<String> mUserNames;
        /***********
         * Declare Used Variables
         *********/
        private Activity activity;
        private LayoutInflater inflater = null;

        /*************
         * LocationAdapter Constructor
         *****************/
        public SelectedAuditorsListAdapter(Activity a, ArrayList<String> nameList) {
            /********** Take passed values **********/
            activity = a;
            /***********  Layout inflator to call external xml layout () ***********/
            inflater = LayoutInflater.from(a);
            mUserNames = nameList;
        }

        @Override
        public int getCount() {
            return mUserNames.size();
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
                    vi = inflater.inflate(R.layout.selected_auditors_item_layout, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    holder = new ViewHolder();
                    holder.text = (TextView) vi.findViewById(R.id.checkListTxt);
                    holder.selectedImageView = (ImageView) vi.findViewById(R.id.checkListSelected);
                    /************  Set holder with LayoutInflater ************/
                    vi.setTag(holder);
                } else {
                    holder = (ViewHolder) vi.getTag();
                }
                /***** Get each Model object from Arraylist ********/

                /************  Set Model values in Holder elements ***********/
                holder.text.setText(mUserNames.get(position));

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

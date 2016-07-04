package com.maudit.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maudit.database.DataBaseAdpter;
import com.maudit.model.CheckListsModel;
import com.maudit.utils.CommonMethod;
import com.maudit.utils.Session;
import com.maudit.utils.Webservices;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class AuditorListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static AuditorListActivity rootInstance;
    public int isLocalDataAvail = 0 ,mLocalSchedule = 0;
    private RecyclerView mRecyclerView_auditor_list;
    private AuditorRecyclerviewAdapter mAuditorRecyclerviewAdapter;
    private ProgressDialog mProgressDialog;
    private Webservices mWebservices;
    private CommonMethod mCommonMethod;
    private Session mSession;
    private TextView mTextviewRole, mTextviewUsername, mTextviewInfo;
    private ImageView mImagviewBack;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DataBaseAdpter mDataBaseAdpter;
    private Button mAdhocAuditBtn;

    public static AuditorListActivity defaultInstance() {
        return rootInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auditor_list);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        mProgressDialog = new ProgressDialog(AuditorListActivity.this);
        mWebservices = new Webservices(this);
        mCommonMethod = new CommonMethod(this);
        mDataBaseAdpter = new DataBaseAdpter(this);
        mSession = new Session(this);
        rootInstance = this;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mTextviewRole = (TextView) findViewById(R.id.textview_role);
        mTextviewInfo = (TextView) findViewById(R.id.textview_info);
        mTextviewUsername = (TextView) findViewById(R.id.textview_username);
        mImagviewBack = (ImageView) findViewById(R.id.imagview_back);
        mImagviewBack.setVisibility(View.INVISIBLE);
        mRecyclerView_auditor_list = (RecyclerView) findViewById(R.id.recyclerView_auditor_list);
        if (getIntent().getStringExtra("ROLE_TYPE") != null) {
            mTextviewRole.setText("Role:  " + getIntent().getStringExtra("ROLE_TYPE"));
        }
        mTextviewUsername.setText("Welcome: " + mSession.getUserName());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mCommonMethod.isConnectingToInternet()) {
                    if (isLocalDataAvail == 0) {
                        mTextviewInfo.setVisibility(View.GONE);
                        new GetChecklistsAsync().execute();
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mTextviewInfo.setVisibility(View.GONE);
                        alertOfflineSyncDialog(getResources().getString(R.string.audit_list_offline_alert_message));
                    }
                } else {
                    mCommonMethod.alertDialog("Unable to refresh. Network not available", null);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mTextviewInfo.setVisibility(View.GONE);
                }
            }
        });
        if (mCommonMethod.isConnectingToInternet()) {
            new GetChecklistsAsync().execute();
        } else {
            mCommonMethod.alertDialog("Unable to connect", null);
        }
        mAdhocAuditBtn = (Button) findViewById(R.id.adhoc_audit_button);
        mAdhocAuditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommonMethod.isConnectingToInternet()) {
                    Intent intentSectionList = new Intent(AuditorListActivity.this, AdhocAuditActivity.class);
                    startActivity(intentSectionList);
                } else {
                    mCommonMethod.alertDialog("Unable to connect. Network not available", null);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_home, menu);
        return true;
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (resultCode == 2) {
            if (mCommonMethod.isConnectingToInternet()) {
                new GetChecklistsAsync().execute();
            } else {
                mCommonMethod.alertDialog("Unable to connect", null);
            }
        }
    }

    public void alertOfflineSyncDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AuditorListActivity.this);
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
                        mSession.setScheduleId(mLocalSchedule);
                        mDataBaseAdpter = new DataBaseAdpter(AuditorListActivity.this);
                        mDataBaseAdpter.deleteSection(0);
                        mDataBaseAdpter.deleteSection(1);
                        isLocalDataAvail = 0;
                        mLocalSchedule = 0;
                        refreshLayout();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mCommonMethod.alertDialogMessage("Do you want to logout?", AuditorListActivity.this, AuditorListActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                mTextviewInfo.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    public List<CheckListsModel> xmlParser(String response) {
        List<CheckListsModel> checkLists = new ArrayList<CheckListsModel>();
        List<CheckListsModel> sectionAuditorsAssignedList = new ArrayList<CheckListsModel>();
        List<CheckListsModel> assignedSectionsList = new ArrayList<CheckListsModel>();
        CheckListsModel checkListsModelTable = null, checkListsModelTable1 = null, checkListsModelTable2 = null;
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
                        if (tagname.equalsIgnoreCase("Table")) {
                            // create a new instance of checklist
                            isTable = true;
                            isTable1 = false;
                            isTable2 = false;
                            checkListsModelTable = new CheckListsModel(AuditorListActivity.this);
                        } else if (tagname.equalsIgnoreCase("Table1")) {
                            isTable = false;
                            isTable1 = true;
                            isTable2 = false;
                            checkListsModelTable1 = new CheckListsModel(AuditorListActivity.this);
                        } else if (tagname.equalsIgnoreCase("Table2")) {
                            isTable = false;
                            isTable1 = false;
                            isTable2 = true;
                            checkListsModelTable2 = new CheckListsModel(AuditorListActivity.this);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();

                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("Table")) {
                            // add Checklist object to list
                            checkLists.add(checkListsModelTable);
                        } else if (isTable && tagname.equalsIgnoreCase("AuditScheduleID")) {
                            checkListsModelTable.setAuditScheduleID(text);
                        } else if (tagname.equalsIgnoreCase("LocationID")) {
                            checkListsModelTable.setLocationID(text);
                        } else if (tagname.equalsIgnoreCase("LocationName")) {
                            checkListsModelTable.setLocationName(text);
                        } else if (tagname.equalsIgnoreCase("StartDate")) {
                            checkListsModelTable.setStartDate(text);
                        } else if (tagname.equalsIgnoreCase("SDate")) {
                            checkListsModelTable.setsDate(text);
                        } else if (tagname.equalsIgnoreCase("EndDate")) {
                            checkListsModelTable.setEndDate(text);
                        } else if (tagname.equalsIgnoreCase("AuditTime")) {
                            checkListsModelTable.setAuditTime(text);
                        } else if (tagname.equalsIgnoreCase("Username")) {
                            checkListsModelTable.setUsername(text);
                        } else if (tagname.equalsIgnoreCase("ScheduleStatus")) {
                            checkListsModelTable.setScheduleStatus(Integer.parseInt(text));
                        } else if (tagname.equalsIgnoreCase("AuditorStatus")) {
                            checkListsModelTable.setAuditorStatus(Integer.parseInt(text));
                        } else if (tagname.equalsIgnoreCase("ScheduleStatus1")) {
                            checkListsModelTable.setScheduleStatus1(text);
                        } else if (tagname.equalsIgnoreCase("StatusType")) {
                            checkListsModelTable.setStatusType(text);
                        } else if (tagname.equalsIgnoreCase("IsLeadAuditor")) {
                            checkListsModelTable.setIsLeadAuditor(text);
                        } else if (tagname.equalsIgnoreCase("CreatedBy")) {
                            checkListsModelTable.setCreatedBy(text);
                        } else if (tagname.equalsIgnoreCase("ChecklistName")) {
                            checkListsModelTable.setChecklistName(text);
                        } else if (tagname.equalsIgnoreCase("LastModifiedDate")) {
                            checkListsModelTable.setLastModifiedDate(text);
                        } else if (tagname.equalsIgnoreCase("Table1")) {
                            sectionAuditorsAssignedList.add(checkListsModelTable1);
                        } else if (isTable1 && tagname.equalsIgnoreCase("AuditScheduleID")) {
                            checkListsModelTable1.setAuditScheduleID(text);
                        } else if (tagname.equalsIgnoreCase("AuditorsAssigned")) {
                            checkListsModelTable1.setAuditorsAssigned(text);
                        } else if (tagname.equalsIgnoreCase("Table2")) {
                            assignedSectionsList.add(checkListsModelTable2);
                        } else if (isTable2 && tagname.equalsIgnoreCase("AuditScheduleID")) {
                            checkListsModelTable2.setAuditScheduleID(text);
                        } else if (tagname.equalsIgnoreCase("AssignedSections")) {
                            checkListsModelTable2.setAssignedSections(text);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
            //add AuditorsAssigned to checkLists based on the AuditScheduleID
            if (checkLists.size() > 0) {
                for (CheckListsModel checklistTable : checkLists) {
                    for (CheckListsModel sectionTable1 : sectionAuditorsAssignedList) {
                        if (checklistTable.getAuditScheduleID().equals(sectionTable1.getAuditScheduleID()) && sectionTable1.getAuditorsAssigned() != null) {
                            checklistTable.setAuditorsAssigned(sectionTable1.getAuditorsAssigned());
                        }
                    }
                }
            }
            //add AssignedSections to checkLists based on the AuditScheduleID
            if (checkLists.size() > 0) {
                for (CheckListsModel checklistTable : checkLists) {
                    for (CheckListsModel sectionTable2 : assignedSectionsList) {
                        if (checklistTable.getAuditScheduleID().equals(sectionTable2.getAuditScheduleID()) && sectionTable2.getAssignedSections() != null) {
                            checklistTable.setAssignedSections(sectionTable2.getAssignedSections());
                        }
                    }
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return checkLists;
    }

    public void recallChecklist() {
        if (mCommonMethod.isConnectingToInternet()) {
            new GetChecklistsAsync().execute();
        } else {
            mCommonMethod.alertDialog("Unable to connect", null);
        }
    }

    public void refreshLayout() {
        mAuditorRecyclerviewAdapter.notifyDataSetChanged();
        mRecyclerView_auditor_list.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class AuditorRecyclerviewAdapter extends RecyclerView.Adapter<AuditorRecyclerviewAdapter.DataViewHolder> {
        private Context mContext;
        private List<CheckListsModel> mCheckListsModelList;

        public AuditorRecyclerviewAdapter(Context context, List<CheckListsModel> checkListsModelList) {
            mContext = context;
            mCheckListsModelList = checkListsModelList;
        }

        @Override
        public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_audit_list, parent, false);
            DataViewHolder dataViewHolder = new DataViewHolder(view);
            return dataViewHolder;
        }

        @Override
        public int getItemCount() {
            return mCheckListsModelList.size();
        }

        @Override
        public void onBindViewHolder(DataViewHolder holder, int position) {
            CheckListsModel checkListsModel = mCheckListsModelList.get(position);
            holder.textview_location.setText(checkListsModel.getLocationName());
            holder.textview_checklist_name.setText(checkListsModel.getChecklistName());
            holder.textview_schedule_date.setText(checkListsModel.getEndDate());
            holder.textview_last_modified.setText(checkListsModel.getLastModifiedDate());
            holder.textview_create_by.setText(checkListsModel.getCreatedBy());
            holder.textview_section_assigned.setText(checkListsModel.getAssignedSections());
            holder.textview_other_assigned.setText(checkListsModel.getAuditorsAssigned());
            holder.textview_status.setText(checkListsModel.getStatusType());
            holder.button_start.setTag(checkListsModel.getAuditScheduleID());
            holder.button_reassign.setTag(checkListsModel.getAuditScheduleID());
            Integer checkLocalHadValue = mDataBaseAdpter.checkLocalHadValue(checkListsModel.getAuditScheduleID());
            if (checkLocalHadValue == 1) {
                // mCommonMethod.alertDialog("Locally Modified Schedule Id is" + checkListsModel.getAuditScheduleID(), null);
                holder.single_row_audit_list.setBackgroundColor(Color.parseColor("#FFFF00"));
                isLocalDataAvail = 1;
                mLocalSchedule = Integer.parseInt(checkListsModel.getAuditScheduleID());
            } else {
                holder.single_row_audit_list.setBackgroundColor(Color.TRANSPARENT);
            }
            if (checkListsModel.getStatusType() != null && !checkListsModel.getStatusType().equals("")) {
                if (checkListsModel.getStatusType().equals("In Progress")) {
                    holder.button_start.setText("Resume");
                    if (checkListsModel.getIsLeadAuditor() != null && checkListsModel.getIsLeadAuditor().equals("true")) {
                        holder.button_reassign.setBackgroundResource(R.drawable.btn_selector);
                        holder.button_reassign.setTextColor(Color.parseColor("#FFFFFF"));
                        holder.button_reassign.setEnabled(true);
                    } else {
                        holder.button_reassign.setBackgroundResource(R.drawable.btn_invisible);
                        holder.button_reassign.setTextColor(Color.parseColor("#BFC4C0"));
                        holder.button_reassign.setEnabled(false);
                    }
                } else if (checkListsModel.getStatusType().equals("Scheduled")) {
                    if (checkLocalHadValue == 1) {
                        holder.button_start.setText("Resume");
                    }else {
                        holder.button_start.setText("Start");
                    }
                    if (checkListsModel.getIsLeadAuditor() != null && checkListsModel.getIsLeadAuditor().equals("true")) {
                        holder.button_reassign.setBackgroundResource(R.drawable.btn_selector);
                        holder.button_reassign.setTextColor(Color.parseColor("#FFFFFF"));
                        holder.button_reassign.setEnabled(true);
                    } else {
                        holder.button_reassign.setBackgroundResource(R.drawable.btn_invisible);
                        holder.button_reassign.setTextColor(Color.parseColor("#BFC4C0"));
                        holder.button_reassign.setEnabled(false);
                    }
                }else if (checkListsModel.getStatusType().equals("Rejected")) {
                    holder.button_start.setText("Resume");
                    if (checkListsModel.getIsLeadAuditor() != null && checkListsModel.getIsLeadAuditor().equals("true")) {
                        holder.button_reassign.setBackgroundResource(R.drawable.btn_selector);
                        holder.button_reassign.setTextColor(Color.parseColor("#FFFFFF"));
                        holder.button_reassign.setEnabled(true);
                    } else {
                        holder.button_reassign.setBackgroundResource(R.drawable.btn_invisible);
                        holder.button_reassign.setTextColor(Color.parseColor("#BFC4C0"));
                        holder.button_reassign.setEnabled(false);
                    }
                } else if (checkListsModel.getStatusType().equals("Waiting for Acceptance")  || checkListsModel.getStatusType().equals("Submitted")) {
                    holder.button_start.setText("View");
                    holder.button_reassign.setBackgroundResource(R.drawable.btn_invisible);
                    holder.button_reassign.setTextColor(Color.parseColor("#BFC4C0"));
                    holder.button_reassign.setEnabled(false);
                }
            }
        }

        private void unSaveChangesPopupInit(final String mScheduleId) {
            final Dialog mdialog = new Dialog(AuditorListActivity.this);
            //mdialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            // set title
            mdialog.setTitle(getResources().getString(R.string.title));
            View mView = getLayoutInflater().inflate(R.layout.activity_local_sync_options, null);

            mdialog.setContentView(mView);

            final TextView mDiscardTxt = (TextView) mView.findViewById(R.id.discard);
            final TextView mCancelTxt = (TextView) mView.findViewById(R.id.cancel);
            final TextView mSaveTxt = (TextView) mView.findViewById(R.id.save);
            mDiscardTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLocalDataAvail = 0;
                    mSession.setScheduleId(Integer.parseInt(mScheduleId));
                    mDataBaseAdpter = new DataBaseAdpter(AuditorListActivity.this);
                    mDataBaseAdpter.deleteSection(0);
                    mDataBaseAdpter.deleteSection(1);
                    refreshLayout();
                    mdialog.dismiss();
                }
            });
            mCancelTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mdialog.dismiss();
                }
            });
            mSaveTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDataBaseAdpter.deleteSection(0);
                    Intent intentSectionList = new Intent(AuditorListActivity.this, SectionListActivity.class);
                    intentSectionList.putExtra("SCHEDULE_ID", mScheduleId);
                    mSession.setScheduleId(Integer.parseInt(mScheduleId));
                    for (CheckListsModel checkListsModel : mCheckListsModelList) {
                        if (mScheduleId.equalsIgnoreCase(checkListsModel.getAuditScheduleID())) {
                            intentSectionList.putExtra("LOCATION", checkListsModel.getLocationName());
                            intentSectionList.putExtra("CHECKLIST_NAME", checkListsModel.getChecklistName());
                            intentSectionList.putExtra("CREATED_BY_USER", checkListsModel.getCreatedBy());
                        }
                    }
                    intentSectionList.putExtra("CallSaveDraft", true);
                    intentSectionList.putExtra("GetFromLocal", true);
                    startActivity(intentSectionList);
                    mdialog.dismiss();
                }
            });
            mdialog.setCanceledOnTouchOutside(false);
            mdialog.show();
            //int width = (int) getResources().getDimension(R.dimen.other_remarks_popup_width);
            //int height = (int) getResources().getDimension(R.dimen.other_remarks_popup_height);
            //mdialog.getWindow().setLayout(width, height); //Controlling width and height.
        }

        public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textview_location, textview_checklist_name, textview_schedule_date,
                    textview_last_modified, textview_create_by, textview_section_assigned, textview_other_assigned, textview_status;
            Button button_start, button_reassign;
            LinearLayout single_row_audit_list;

            public DataViewHolder(View itemView) {
                super(itemView);
                single_row_audit_list = (LinearLayout) itemView.findViewById(R.id.single_row_audit_list);
                textview_location = (TextView) itemView.findViewById(R.id.textview_location);
                textview_checklist_name = (TextView) itemView.findViewById(R.id.textview_checklist_name);
                textview_schedule_date = (TextView) itemView.findViewById(R.id.textview_schedule_date);
                textview_last_modified = (TextView) itemView.findViewById(R.id.textview_last_modified);
                textview_create_by = (TextView) itemView.findViewById(R.id.textview_create_by);
                textview_section_assigned = (TextView) itemView.findViewById(R.id.textview_section_assigned);
                textview_other_assigned = (TextView) itemView.findViewById(R.id.textview_other_assigned);
                textview_status = (TextView) itemView.findViewById(R.id.textview_status);
                button_start = (Button) itemView.findViewById(R.id.button_start);
                button_start.setOnClickListener(this);
                button_reassign = (Button) itemView.findViewById(R.id.button_reassign);
                button_reassign.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_start:
                        Integer checkLocalHadValue = mDataBaseAdpter.checkLocalHadValue(v.getTag().toString());
                        if (mCommonMethod.isConnectingToInternet()) {
                            if (checkLocalHadValue == 1) {
                                unSaveChangesPopupInit(v.getTag().toString());
                            } else {
                                mDataBaseAdpter.deleteAllUnsavedSection(0);
                                mSession.setScheduleId(Integer.parseInt(v.getTag().toString()));
                                //mDataBaseAdpter = new DataBaseAdpter(AuditorListActivity.this);
                                //mDataBaseAdpter.deleteSection(0);
                                Intent intentSectionList = new Intent(AuditorListActivity.this, SectionListActivity.class);
                                intentSectionList.putExtra("SCHEDULE_ID", v.getTag().toString());
                                for (CheckListsModel checkListsModel : mCheckListsModelList) {
                                    if (v.getTag().toString().equals(checkListsModel.getAuditScheduleID())) {
                                        intentSectionList.putExtra("LOCATION", checkListsModel.getLocationName());
                                        intentSectionList.putExtra("CHECKLIST_NAME", checkListsModel.getChecklistName());
                                        intentSectionList.putExtra("CREATED_BY_USER", checkListsModel.getCreatedBy());
                                    }
                                    if (button_start.getText().toString().equals("View")) {
                                        mSession.setEditable(false);
                                    } else {
                                        mSession.setEditable(true);
                                    }
                                }
                                startActivity(intentSectionList);
                            }
                        } else {
                            if (checkLocalHadValue == 1) {
                                mDataBaseAdpter.deleteAllUnsavedSection(0);
                                Intent intentSectionList = new Intent(AuditorListActivity.this, SectionListActivity.class);
                                intentSectionList.putExtra("SCHEDULE_ID", v.getTag().toString());
                                mSession.setScheduleId(Integer.parseInt(v.getTag().toString()));
                                for (CheckListsModel checkListsModel : mCheckListsModelList) {
                                    if (v.getTag().toString().equals(checkListsModel.getAuditScheduleID())) {
                                        intentSectionList.putExtra("LOCATION", checkListsModel.getLocationName());
                                        intentSectionList.putExtra("CHECKLIST_NAME", checkListsModel.getChecklistName());
                                        intentSectionList.putExtra("CREATED_BY_USER", checkListsModel.getCreatedBy());
                                    }
                                    if (button_start.getText().toString().equals("View")) {
                                        mSession.setEditable(false);
                                    } else {
                                        mSession.setEditable(true);
                                    }
                                }
                                intentSectionList.putExtra("GetFromLocal", true);
                                startActivity(intentSectionList);
                            } else {
                                mCommonMethod.alertDialog(getResources().getString(R.string.no_connection), null);
                            }
                        }
                        break;
                    case R.id.button_reassign:
                        if (mCommonMethod.isConnectingToInternet()) {
                            Intent intent = new Intent(AuditorListActivity.this, ReAssignAuditorsActivity.class);
                            intent.putExtra("AuditScheduleID", v.getTag().toString());
                            for (CheckListsModel checkListsModel : mCheckListsModelList) {
                                if (v.getTag().toString().equals(checkListsModel.getAuditScheduleID())) {
                                    intent.putExtra("SubTitle", checkListsModel.getChecklistName());
                                }
                            }
                            startActivityForResult(intent, 2);
                        } else {
                            mCommonMethod.alertDialog("Unable to connect. Network not available", null);
                        }
                        break;
                    default:
                        break;
                }

            }
        }
    }

    public class GetChecklistsAsync extends AsyncTask<String, String, List<CheckListsModel>> {
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
            List<CheckListsModel> getCheckLists = null;
            String result = null;
            result = mWebservices.mGetAllScheduledCheckLists(mSession.getUserId());
            getCheckLists = xmlParser(result);
            return getCheckLists;
        }

        @Override
        protected void onPostExecute(List<CheckListsModel> result) {
            mAuditorRecyclerviewAdapter = new AuditorRecyclerviewAdapter(AuditorListActivity.this, result);
            mRecyclerView_auditor_list.setLayoutManager(new LinearLayoutManager(AuditorListActivity.this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView_auditor_list.setAdapter(mAuditorRecyclerviewAdapter);
            if (result != null && result.size() == 0) {
                mCommonMethod.alertDialog("No data", null);
            }
            mProgressDialog.dismiss();
        }
    }
}

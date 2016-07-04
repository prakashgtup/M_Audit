package com.maudit.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SubmittedAuditorListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static SubmittedAuditorListActivity rootInstance;
    public int isLocalDataAvail = 0;
    private RecyclerView mRecyclerView_auditor_list;
    private AuditorRecyclerviewAdapter mAuditorRecyclerviewAdapter;
    private ProgressDialog mProgressDialog;
    private Webservices mWebservices;
    private CommonMethod mCommonMethod;
    private Session mSession;
    private TextView mTextviewRole, mTextviewUsername, mTextviewInfo, mTextviewMainTitle, mTextviewDate;
    private ImageView mImagviewBack;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DataBaseAdpter mDataBaseAdpter;
    Calendar c = Calendar.getInstance();

    public static SubmittedAuditorListActivity defaultInstance() {
        return rootInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted_auditor_list);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        mProgressDialog = new ProgressDialog(SubmittedAuditorListActivity.this);
        mWebservices = new Webservices(this);
        mCommonMethod = new CommonMethod(this);
        mDataBaseAdpter = new DataBaseAdpter(this);
        mSession = new Session(this);
        rootInstance = this;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mTextviewMainTitle = (TextView) findViewById(R.id.textview_title);
        mTextviewMainTitle.setText(getResources().getString(R.string.submitted_audit_list_title));
        mTextviewDate = (TextView) findViewById(R.id.textview_date);
        mTextviewDate.setVisibility(View.VISIBLE);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        mTextviewDate.setText(df.format(c.getTime()));
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
                    mTextviewInfo.setVisibility(View.GONE);
                    new GetChecklistsAsync().execute();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mCommonMethod.alertDialog("Unable to connect", null);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mCommonMethod.alertDialogMessage("Do you want to logout?", SubmittedAuditorListActivity.this, SubmittedAuditorListActivity.this);
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
        boolean isTable = false;
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
                            checkListsModelTable = new CheckListsModel(SubmittedAuditorListActivity.this);
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
                        } else if (tagname.equalsIgnoreCase("AuditTypeID")) {
                            checkListsModelTable.setAuditTypeID(Integer.parseInt(text));
                        } else if (tagname.equalsIgnoreCase("ChecklistTypeID")) {
                            checkListsModelTable.setChecklistTypeID(Integer.parseInt(text));
                        } else if (tagname.equalsIgnoreCase("ChecklistName")) {
                            checkListsModelTable.setChecklistName(text);
                        } else if (tagname.equalsIgnoreCase("StartDate")) {
                            checkListsModelTable.setStartDate(text);
                        } else if (tagname.equalsIgnoreCase("EndDate")) {
                            checkListsModelTable.setEndDate(text);
                        } else if (tagname.equalsIgnoreCase("AuditTime")) {
                            checkListsModelTable.setAuditTime(text);
                        } else if (tagname.equalsIgnoreCase("CreatedDate")) {
                            checkListsModelTable.setsDate(text);
                        } else if (tagname.equalsIgnoreCase("AuditScheduleID")) {
                            checkListsModelTable.setAuditScheduleID(text);
                        } else if (tagname.equalsIgnoreCase("ScheduleChecklistID")) {
                            checkListsModelTable.setLastModifiedDate(text);
                        } else if (tagname.equalsIgnoreCase("AuditedBy")) {
                            checkListsModelTable.setCreatedBy(text);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_submitted_audit_list, parent, false);
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
            holder.textview_audit_date.setText(checkListsModel.getEndDate());
            holder.textview_audit_time.setText(checkListsModel.getAuditTime());
            holder.textview_audited_by.setText(checkListsModel.getCreatedBy());
            holder.button_view.setTag(checkListsModel.getAuditScheduleID());

        }


        public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textview_location, textview_checklist_name, textview_audit_date,
                    textview_audit_time, textview_audited_by;
            Button button_view;
            LinearLayout single_row_audit_list;

            public DataViewHolder(View itemView) {
                super(itemView);
                single_row_audit_list = (LinearLayout) itemView.findViewById(R.id.single_row_audit_list);
                textview_location = (TextView) itemView.findViewById(R.id.textview_location);
                textview_checklist_name = (TextView) itemView.findViewById(R.id.textview_checklist_name);
                textview_audit_date = (TextView) itemView.findViewById(R.id.textview_audit_date);
                textview_audit_time = (TextView) itemView.findViewById(R.id.textview_audit_time);
                textview_audited_by = (TextView) itemView.findViewById(R.id.textview_audited_by);
                button_view = (Button) itemView.findViewById(R.id.button_view);
                button_view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_view:
                        if (mCommonMethod.isConnectingToInternet()) {
                            mDataBaseAdpter.deleteAllUnsavedSection(0);
                            Intent intentSectionList = new Intent(SubmittedAuditorListActivity.this, SubmittedAuditorsSectionListActivity.class);
                            intentSectionList.putExtra("SCHEDULE_ID", v.getTag().toString());
                            mSession.setScheduleId(Integer.parseInt(v.getTag().toString()));
                            for (CheckListsModel checkListsModel : mCheckListsModelList) {
                                if (v.getTag().toString().equals(checkListsModel.getAuditScheduleID())) {
                                    intentSectionList.putExtra("LOCATION", checkListsModel.getLocationName());
                                    intentSectionList.putExtra("CHECKLIST_NAME", checkListsModel.getChecklistName());
                                    intentSectionList.putExtra("CREATED_BY_USER", checkListsModel.getCreatedBy());
                                }
                                mSession.setEditable(false);
                            }
                            startActivity(intentSectionList);
                        } else {

                            mCommonMethod.alertDialog("Unable to connect", null);

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
            result = mWebservices.mGetAllSubmittedChecklists(mSession.getUserId());
            getCheckLists = xmlParser(result);
            return getCheckLists;
        }

        @Override
        protected void onPostExecute(List<CheckListsModel> result) {
            mAuditorRecyclerviewAdapter = new AuditorRecyclerviewAdapter(SubmittedAuditorListActivity.this, result);
            mRecyclerView_auditor_list.setLayoutManager(new LinearLayoutManager(SubmittedAuditorListActivity.this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView_auditor_list.setAdapter(mAuditorRecyclerviewAdapter);
            if (result != null && result.size() == 0) {
                mCommonMethod.alertDialog("No data", null);
            }
            mProgressDialog.dismiss();
        }
    }
}

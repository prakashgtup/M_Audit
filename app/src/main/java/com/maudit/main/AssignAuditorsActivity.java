package com.maudit.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import com.maudit.model.GetAuditorsAndSection;
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
import java.util.ArrayList;
import java.util.List;

public class AssignAuditorsActivity extends AppCompatActivity {
    View popupView;
    PopupWindow popupWindow;
    Button mChoose_Auditor_button, mSet_Auditor_button,mDone_button;
    // List view
    private ListView popUpListView;
    private SearchView searchView;
    private Session mSession;
    private CommonMethod mCommonMethod;
    private ProgressDialog mProgressDialog;
    private Webservices mWebservices;
    List<GetAuditorsAndSection> getAuditorsLists = null, mTempAuditorsLists = null, getSelectionsList = new ArrayList<>();
    List<Boolean> mSelectedMain =  new ArrayList<>(), mSelectedLead =  new ArrayList<>();
    AuditorListAdapter auditorListAdapter;
    String mAuditTypeID, mCheckListTypeID, mLeadAuditorName;
    private TextView mTextview_username, mSubTitle_TxtView, mLeadAuditorTxtView;
    GridView mAvailable_sections_gridView, mSelected_lead_gridView, mAssigned_sections_gridView;
    Available_sectionsAdapter mAvailable_sectionsAdapter;
    LeadAuditorSelectionAdapter mLeadAuditorSelectionAdapter;
    SelectedAuditorsAdapter mSelectedAuditorsAdapter;
    List<UserSectionModel> getUserSectionsList = new ArrayList<>();
    Boolean isSavedChanges = false;
    private ImageView mImagviewBack;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_auditors);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        mProgressDialog = new ProgressDialog(AssignAuditorsActivity.this);
        mWebservices = new Webservices(this);
        mCommonMethod = new CommonMethod(this);
        mSession = new Session(this);
        mImagviewBack =(ImageView)findViewById(R.id.imagview_back);
        mImagviewBack.setVisibility(View.INVISIBLE);
        mTextview_username = (TextView) findViewById(R.id.textview_username);
        mAvailable_sections_gridView = (GridView) findViewById(R.id.available_sections_gridView);
        mSelected_lead_gridView = (GridView) findViewById(R.id.selected_lead_gridView);
        mAssigned_sections_gridView = (GridView) findViewById(R.id.assigned_sections_gridView);
        mAvailable_sections_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView productItem = (TextView) view.findViewById(R.id.checkListTxt);
                isSavedChanges = true;
                if(!mChoose_Auditor_button.getText().toString().equalsIgnoreCase("")) {
                    if(!mSelectedMain.get(position)) {
                        mSelectedMain.set(position, true);
                        mSelectedLead.set(position, true);
                    }else {
                        mSelectedMain.set(position, false);
                        mSelectedLead.set(position, false);
                    }
                    AssignAuditorsActivity.this.mAvailable_sectionsAdapter.refresh();
                    AssignAuditorsActivity.this.mLeadAuditorSelectionAdapter.refresh();
                    if(getUserSectionsList!=null) {
                        for (int j = 0; j < getUserSectionsList.size(); j++) {
                            List<Boolean> mIsSelected = getUserSectionsList.get(j).getmSelectedMain();
                            List<Boolean> mTemp =  getUserSectionsList.get(j).getmSelectedMain();
                            if (mIsSelected.contains(true)) {
                                for (int i = 0; i < mIsSelected.size(); i++) {
                                    if(i==position){
                                        //mCommonMethod.alertDialog("sub item remove"+i, null);
                                        mTemp.set(i,false);
                                    }
                                }

                                getUserSectionsList.get(j).setmSelectedMain(mTemp);
                            }

                        }

                        if(mSelectedAuditorsAdapter!=null) {
                            mSelectedAuditorsAdapter.refresh();
                        }
                    }
                }else {
                    mCommonMethod.alertDialog("Please select Auditor first", null);
                }
            }
        });
        mTextview_username.setText("Welcome: " + mSession.getUserName());
        mSubTitle_TxtView = (TextView) findViewById(R.id.textview_subTitle);
        mLeadAuditorTxtView = (TextView) findViewById(R.id.Lead_Auditor_Title);
        if(getIntent().getExtras()!=null){
          if(getIntent().getStringExtra("AuditTypeID")!=null &&getIntent().getStringExtra("CheckListTypeID")!=null){
              mAuditTypeID = getIntent().getStringExtra("AuditTypeID");
              mCheckListTypeID = getIntent().getStringExtra("CheckListTypeID");
              mSubTitle_TxtView.setText(getIntent().getStringExtra("SubTitle"));
          }
        }
        mChoose_Auditor_button = (Button) findViewById(R.id.choose_auditor_button);
        mChoose_Auditor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auditorListPopupInit(v);
            }
        });
        mSet_Auditor_button = (Button) findViewById(R.id.set_auditor_button);
        mSet_Auditor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChoose_Auditor_button.getText().toString().equalsIgnoreCase("")) {
                    mCommonMethod.alertDialog("Please select the Auditor", null);
                }else if(!mSelectedMain.contains(true)){
                    mCommonMethod.alertDialog("Please assign sections", null);
                }else {
                    UserSectionModel mUserSectionModel =  new UserSectionModel();

                    mUserSectionModel.setUsername(mChoose_Auditor_button.getText().toString());
                    try {
                        mUserSectionModel.setUserID(Integer.parseInt(mChoose_Auditor_button.getTag().toString()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    List<Boolean> mTemp = new ArrayList<>();
                    for(int j=0;j<mSelectedMain.size();j++) {
                        mTemp.add(mSelectedMain.get(j));
                    }
                    mUserSectionModel.setmSelectedMain(mTemp);
                    mUserSectionModel.setGetAuditorsLists(getSelectionsList);
                    getUserSectionsList.add(mUserSectionModel);


                    mSelectedAuditorsAdapter = new SelectedAuditorsAdapter(AssignAuditorsActivity.this,getUserSectionsList);
                    mAssigned_sections_gridView.setAdapter(mSelectedAuditorsAdapter);
                    //Remove from Auditor ListView
                    for(int j=0;j<mTempAuditorsLists.size();j++){
                        if(mTempAuditorsLists.get(j).getUserID().toString().equalsIgnoreCase(mChoose_Auditor_button.getTag().toString())){
                            mTempAuditorsLists.remove(j);
                        }
                    }
                    mChoose_Auditor_button.setText("");
                    mChoose_Auditor_button.setTag("");

                    for(int j=0;j<mSelectedMain.size();j++) {
                        if(mSelectedMain.get(j)){
                            mSelectedMain.set(j, false);
                        }
                    }

                    mAvailable_sectionsAdapter.refresh();
                }
            }
        });
        if (mCommonMethod.isConnectingToInternet()) {
            new GetAudtorListsAsync().execute();
        } else {
            mCommonMethod.alertDialog("Please check your internet connection", null);
        }

        mDone_button = (Button) findViewById(R.id.assign_audit_done_button);
        mDone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSelectedLead.contains(false)){
                    mCommonMethod.alertDialog("Please assign at least one section to"+" "+mLeadAuditorName, null);
                }else {
                    ArrayList<UserSectionModel> mainSendList = new ArrayList<>();

                    UserSectionModel mUserSectionModel =  new UserSectionModel();

                    mUserSectionModel.setUsername(mLeadAuditorName);
                    try {
                        mUserSectionModel.setUserID(Integer.parseInt(mSession.getUserId()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    List<Boolean> mTemp = new ArrayList<>();


                    for (int j = 0; j < mSelectedMain.size(); j++) {
                        if (mSelectedMain.get(j)) {
                            //mTemp.set(j, false);
                            mTemp.add(false);
                        }else if (!mSelectedLead.get(j)) {
                            //mTemp.set(j, false);
                            mTemp.add(false);
                        }else {
                            mTemp.add(true);
                        }
                    }


                    mUserSectionModel.setmSelectedMain(mTemp);
                    mUserSectionModel.setGetAuditorsLists(getSelectionsList);
                    mainSendList.add(mUserSectionModel);

                    for(int j=0;j<getUserSectionsList.size();j++) {
                        List<Boolean> mIsSelected = getUserSectionsList.get(j).getmSelectedMain();
                        if(mIsSelected.contains(true)) {
                            mainSendList.add(getUserSectionsList.get(j));
                        }
                    }


                    Intent intent = new Intent();
                    AppController.getInstance().clearMainSendList();
                    AppController.getInstance().setMainSendList(mainSendList);
                    setResult(2, intent);
                    finish();//finishing activity
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mCommonMethod.alertDialogMessage("Do you want to logout?",AssignAuditorsActivity.this,AssignAuditorsActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetAudtorListsAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle(AssignAuditorsActivity.this.getResources().getString(R.string.app_name));
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            result = mWebservices.mGetAuditorsAndSectionList(mAuditTypeID, mCheckListTypeID);
            getAuditorsLists = xmlParser(result);

            //xmlParser2(result);
            Log.i(" size-->", "" + getAuditorsLists.size());

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            mProgressDialog.dismiss();
            mTempAuditorsLists = new ArrayList<>();
            for(GetAuditorsAndSection mGetAuditorsAndSection:getAuditorsLists){
                if(mGetAuditorsAndSection.getUserID().toString().equalsIgnoreCase(mSession.getUserId())){
                    mLeadAuditorTxtView.setText(mGetAuditorsAndSection.getUsername()+" "+"-"+" "+"Lead Auditor");
                    mLeadAuditorName = mGetAuditorsAndSection.getUsername();
                }else {
                    mTempAuditorsLists.add(mGetAuditorsAndSection);
                }
            }
            if(getSelectionsList!=null && getSelectionsList.size()>0){
                for(int i=0;i<getSelectionsList.size();i++){
                    mSelectedMain.add(false);
                    mSelectedLead.add(false);
                }
                mAvailable_sectionsAdapter =  new Available_sectionsAdapter(AssignAuditorsActivity.this,getSelectionsList,mSelectedMain);
                mAvailable_sections_gridView.setAdapter(mAvailable_sectionsAdapter);
                mLeadAuditorSelectionAdapter =  new LeadAuditorSelectionAdapter(AssignAuditorsActivity.this,getSelectionsList,mSelectedLead);
                mSelected_lead_gridView.setAdapter(mLeadAuditorSelectionAdapter);
            }

            if (getIntent().getBooleanExtra("IsDataAvailable", false)) {
                List<UserSectionModel> getPreviousUserSection =  new ArrayList<>();
                getPreviousUserSection = AppController.getInstance().getMainSendList();
                int y = 0;
                for (int m = 0; m < getPreviousUserSection.size(); m++) {
                    UserSectionModel mUserSectionModel = getPreviousUserSection.get(m);
                    if (mUserSectionModel.getUserID().toString().equalsIgnoreCase(mSession.getUserId())) {
                        y = m;
                    }
                }
                getPreviousUserSection.remove(y);
                getUserSectionsList = getPreviousUserSection;
                mSelectedAuditorsAdapter = new SelectedAuditorsAdapter(AssignAuditorsActivity.this, getUserSectionsList);
                mAssigned_sections_gridView.setAdapter(mSelectedAuditorsAdapter);
                //Remove from Auditor ListView
                for (int i = 0; i < getUserSectionsList.size(); i++) {
                    for (int j = 0; j < mTempAuditorsLists.size(); j++) {
                        if (mTempAuditorsLists.get(j).getUserID().toString().equalsIgnoreCase(getUserSectionsList.get(i).getUserID().toString())) {
                            mTempAuditorsLists.remove(j);
                        }
                    }
                    List<Boolean> mTemp = getUserSectionsList.get(i).getmSelectedMain();
                    Log.i("mTemp",""+mTemp.size());
                    Log.i("mSelectedMain",""+mSelectedMain.size());
                    Log.i("mSelectedLead",""+mSelectedLead.size());

                    for(int n=0;n<mTemp.size();n++){
                        if(mTemp.get(n)) {
                            mSelectedMain.set(n, false);
                            mSelectedLead.set(n, true);
                        }
                    }

                }

                mAvailable_sectionsAdapter.refresh();
                mLeadAuditorSelectionAdapter.refresh();
            }


        }
    }

    public List<GetAuditorsAndSection> xmlParser(String response) {
        List<GetAuditorsAndSection> auditorLists = new ArrayList<GetAuditorsAndSection>();
        List<GetAuditorsAndSection> sectionsLists = new ArrayList<GetAuditorsAndSection>();
        GetAuditorsAndSection sectionModel = null, auditorModel = null;
        String text = "";
        boolean isTable = false, isTable1 = false;
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
                            sectionModel = new GetAuditorsAndSection();
                            isTable = true;
                            isTable1 = false;
                        }
                        if (tagname.equalsIgnoreCase("Table1")) {
                            auditorModel = new GetAuditorsAndSection();
                            isTable = false;
                            isTable1 = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("Table")) {
                            // add Checklist object to list
                            sectionsLists.add(sectionModel);
                        } else if (isTable && tagname.equalsIgnoreCase("SectionID")) {
                            sectionModel.setSectionID(Integer.parseInt(text));
                        } else if (isTable && tagname.equalsIgnoreCase("Abbrv")) {
                            sectionModel.setAbbrv(text);
                        }else if (tagname.equalsIgnoreCase("Table1")) {
                            // add Checklist object to list
                            auditorLists.add(auditorModel);
                        } else if (isTable1 && tagname.equalsIgnoreCase("UserID")) {
                            auditorModel.setUserID(Integer.parseInt(text));
                        } else if (isTable1 && tagname.equalsIgnoreCase("Username")) {
                            auditorModel.setUsername(text);
                        }else if (isTable1 && tagname.equalsIgnoreCase("Email")) {
                            auditorModel.setEmail(text);
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

        getSelectionsList = sectionsLists;
        return auditorLists;
    }

    public class Available_sectionsAdapter extends BaseAdapter {

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
        public Available_sectionsAdapter(Activity a, List<GetAuditorsAndSection> checkList, List<Boolean> SelectedTemp) {
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

    public class LeadAuditorSelectionAdapter extends BaseAdapter {

        GetAuditorsAndSection tempValues = null;
        List<GetAuditorsAndSection> mCheckList;
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
        public LeadAuditorSelectionAdapter(Activity a, List<GetAuditorsAndSection> checkList, List<Boolean> SelectedTemp) {
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
                holder.text.setTag(tempValues.getSectionID());

                Drawable drawableImg;
                if (mSelectedTemp.get(position)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawableImg = getResources().getDrawable(R.drawable.check_normal,getTheme());
                    } else {
                        drawableImg = getResources().getDrawable(R.drawable.check_normal);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawableImg = getResources().getDrawable(R.drawable.check_sel,getTheme());
                    } else {
                        drawableImg = getResources().getDrawable(R.drawable.check_sel);
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
                    vi = inflater.inflate(R.layout.single_auditor_assigned_layout, null);
                    /****** View Holder Object to contain tabitem.xml file elements ******/
                    holder = new ViewHolder();
                    holder.text = (TextView) vi.findViewById(R.id.single_Auditor_Title);
                    holder.removeBtn = (Button) vi.findViewById(R.id.button_remove_auditor);
                    holder.mSelectedGrid = (ExpandableHeightGridView) vi.findViewById(R.id.single_auditor_gridView);
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

                final Sub_sectionsAdapter mtemp_sectionsAdapter =  new Sub_sectionsAdapter(AssignAuditorsActivity.this,mTempLists,mSelected);
                holder.mSelectedGrid.setAdapter(mtemp_sectionsAdapter);
                holder.mSelectedGrid.setExpanded(true);
                holder.mSelectedGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView productItem = (TextView) view.findViewById(R.id.checkListTxt);
                        Log.i("Selected", productItem.getText().toString());
                        if (!mSelected.get(position)) {
                            mSelected.set(position, true);
                            mSelectedLead.set(position, true);
                        } else {
                            mSelected.set(position, false);
                            mSelectedLead.set(position, false);
                        }
                        mtemp_sectionsAdapter.refresh();
                        AssignAuditorsActivity.this.mLeadAuditorSelectionAdapter.refresh();
                    }
                });

                holder.removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Add Auditor ListView
                        for(int j=0;j<getAuditorsLists.size();j++){
                            if(getAuditorsLists.get(j).getUserID().toString().equalsIgnoreCase(tempValues.getUserID().toString())){
                                mTempAuditorsLists.add(getAuditorsLists.get(j));
                            }
                        }

                        for(int m=0;m<getUserSectionsList.get(position).getmSelectedMain().size();m++){
                            if(getUserSectionsList.get(position).getmSelectedMain().get(m)){
                                mSelectedLead.set(m,false);
                            }
                        }
                        mLeadAuditorSelectionAdapter.refresh();
                        getUserSectionsList.remove(position);
                        refresh();
                    }
                });
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
            public Button removeBtn;
            public ExpandableHeightGridView mSelectedGrid;
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

    private void auditorListPopupInit(View v) {
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
        auditorListAdapter = new AuditorListAdapter(this, mTempAuditorsLists);
        popUpListView.setAdapter(auditorListAdapter);
        popUpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView productItem = (TextView) view.findViewById(R.id.product_name);
                Log.i("Selected", productItem.getText().toString());
                mChoose_Auditor_button.setText(productItem.getText().toString());
                mChoose_Auditor_button.setTag(productItem.getTag());
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
                AssignAuditorsActivity.this.auditorListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            if (mTempAuditorsLists.size() > 0) {
                popupWindow.showAsDropDown(v, 0, 0);
            } else {
                mCommonMethod.alertDialog("No Item Found", null);
            }
        }
        /*if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            if (mTempAuditorsLists.size() > 0) {
                popupWindow.showAsDropDown(v, 0, 0);
            } else {
                mCommonMethod.alertDialog("No Item Found", null);
            }
        }
        popupWindow.setFocusable(true);
        popupWindow.update();
        if (popupWindow != null) {
            // Closes the popup window when touch outside.
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            // Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }*/
    }

    public class AuditorListAdapter extends BaseAdapter {

        GetAuditorsAndSection tempValues = null;
        List<GetAuditorsAndSection> mStringFilterList, mLocationList;
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
        public AuditorListAdapter(Activity a, List<GetAuditorsAndSection> mLocation) {
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
                holder.text.setText(tempValues.getUsername());
                holder.text.setTag(tempValues.getUserID());

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
                    final List<GetAuditorsAndSection> results = new ArrayList<GetAuditorsAndSection>();
                    List<GetAuditorsAndSection> orig = mTempAuditorsLists;

                    if (constraint != null && constraint.length() > 0) {
                        if (orig != null && orig.size() > 0) {
                            for (final GetAuditorsAndSection g : orig) {
                                if (g.getUsername().toLowerCase()
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
                    mLocationList = (ArrayList<GetAuditorsAndSection>) results.values;
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
}

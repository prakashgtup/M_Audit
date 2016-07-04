package com.maudit.main;


import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.maudit.model.QuestionsModel;
import com.maudit.model.UserSectionModel;
import com.maudit.utils.RequestManager;

import java.util.ArrayList;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;

    ArrayList<UserSectionModel> mainSendList = new ArrayList<>();
    ArrayList<QuestionsModel> getMandatoryFields = new ArrayList<>();

    public ArrayList<QuestionsModel> getGetMandatoryFields() {
        return getMandatoryFields;
    }

    public void setGetMandatoryFields(ArrayList<QuestionsModel> getMandatoryFields) {
        this.getMandatoryFields = getMandatoryFields;
    }

    public void clearMandatoryFields() {
        this.getMandatoryFields.clear();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RequestManager.getInstance(getApplicationContext());
        mInstance = this;
    }

    public ArrayList<UserSectionModel> getMainSendList() {
        return mainSendList;
    }

    public void setMainSendList(ArrayList<UserSectionModel> mainSendList) {
        this.mainSendList = mainSendList;
    }

    public void clearMainSendList() {
        this.mainSendList.clear();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    /*public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }*/

  /*  public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }*/
}
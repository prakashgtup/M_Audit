package com.maudit.utils;

import android.content.Context;

public class RequestManager {
	private static RequestManager instance;
	private RequestProxy mRequestProxy;

	private RequestManager(Context context) {
		mRequestProxy = new RequestProxy(context);
	}

	public RequestProxy doRequest() {
		return mRequestProxy;
	}

	public static synchronized RequestManager getInstance(Context context) {
		if (instance == null) {
			instance = new RequestManager(context);
		}
		return instance;
	}

	public static synchronized RequestManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					RequestManager.class.getSimpleName()
							+ " is not initialized, call getInstance(..) method first.");
		}
		return instance;
	}
}

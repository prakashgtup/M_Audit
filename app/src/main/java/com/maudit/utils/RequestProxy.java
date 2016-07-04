package com.maudit.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.maudit.main.AppController;

import org.json.JSONObject;

public class RequestProxy {
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	JSONObject myResponse;
	AppController myApp;
	protected String value;
	protected String dbValue;

	RequestProxy(Context context) {
		mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
		mImageLoader = new ImageLoader(this.mRequestQueue,
				new ImageLoader.ImageCache() {
					private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(
							10);

					@Override
					public Bitmap getBitmap(String url) {
						return mCache.get(url);
					}

					@Override
					public void putBitmap(String url, Bitmap bitmap) {
						mCache.put(url, bitmap);
					}

				});

		myApp = (AppController) context.getApplicationContext();
	}

	public RequestQueue getRequestQueue() {
		return this.mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		return this.mImageLoader;
	}

}

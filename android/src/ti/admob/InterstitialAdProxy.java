/**
 * Copyright (c) 2011 by Studio Classics. All Rights Reserved.
 * Author: Brian Kurzius
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.admob;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiContext.OnLifecycleEvent;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.view.TiUIView;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.mediation.MediationAdapter;

import android.app.Activity;
import android.location.Location;
import java.util.Date;

@Kroll.proxy(creatableInModule = AdmobModule.class)
public class InterstitialAdProxy extends KrollProxy {
	private InterstitialAd mInterstitialAd;
	private static final String TAG = "AdMobInterstitialAdProxy";
	protected Date birthday;
	protected int gender = AdRequest.GENDER_UNKNOWN;
	protected double latitude;
	protected double longitude;
	protected float accuracy = 0;
	protected Location location;
	protected String[] keywords;

	public InterstitialAdProxy() {
		//super();
	}
	
	// Handle creation options
	@Override
	public void handleCreationDict(KrollDict d)
	{
		/*
		if (d.containsKey("tag")) {
			this.tag = d.getString("tag");
		}
		*/
		if (d.containsKey("publisherId")) {
			Log.d(TAG, "has publisherId: " + d.getString("publisherId"));
			AdmobModule.PUBLISHER_ID = d.getString("publisherId");
		}
		if (d.containsKey("testing")) {
			Log.d(TAG, "has testing param: " + d.getBoolean("testing"));
			AdmobModule.TESTING = d.getBoolean("testing");
		}
		if (d.containsKey("gender")) {
			Log.d(TAG, "has gender param: " + d.getBoolean("gender"));
			if (d.getString("gender") == "male") {
				this.gender = AdRequest.GENDER_MALE;
			} else if (d.getString("gender") == "female") {
				this.gender = AdRequest.GENDER_FEMALE;
			}
		}
		if (d.containsKey("keywords")) {
			this.keywords = d.getString("keywords").trim().split("\\s*,\\s*");
		}
		if (d.containsKey("location")) {
			KrollDict loc = d.getKrollDict("location");
			this.latitude = TiConvert.toDouble(loc.getString("latitude"));
			this.longitude = TiConvert.toDouble(loc.getString("longitude"));
			if (loc.containsKey("accuracy"))
				this.accuracy = TiConvert.toFloat(loc.getString("accuracy"));
		}
		
		/*
		 *  TODO: Add birthday filter
		 * 
		if (d.containsKey("dateOfBirth")) {
			this.birthday = d.getString("dateOfBirth");
		}
		*/
		
		
		super.handleCreationDict(d);
		//createAd(d.getBoolean("testing"));
	}

	@Override
	protected KrollDict getLangConversionTable() {
		KrollDict table = new KrollDict();
		//table.put("title", "titleid");
		return table;
	}

	@Kroll.method
	public void show() {
		Log.d(TAG, "show()");
		//if (mInterstitialAd.isLoaded()) {
		this.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				mInterstitialAd.show();
			}
		});
		//}
	}
	
	@Kroll.method
	public void load() {
		Log.d(TAG, "load()");
		
		createAd();
	}

	// load the adMob ad
	public void createAd() {
		Log.d(TAG, "createAd()");
		// create the adView
		mInterstitialAd = new InterstitialAd(getActivity());
		//adView.setAdSize(AdSize.BANNER);
		mInterstitialAd.setAdUnitId(AdmobModule.PUBLISHER_ID);
		// set the listener
		mInterstitialAd.setAdListener(new AdListener() {
			public void onAdLoaded() {
				Log.d(TAG, "onAdLoaded()");
				//fireEvent(AdmobModule.AD_RECEIVED, new KrollDict());
				fireEvent("load", new KrollDict());
				//mInterstitialAd.show();
			}
			
			public void onAdFailedToLoad(int errorCode) {
				Log.d(TAG, "onAdFailedToLoad(): " + errorCode);
				//fireEvent(AdmobModule.AD_NOT_RECEIVED, new KrollDict());
				fireEvent("failure", new KrollDict());
			}

			public void onAdClosed() {
				Log.d(TAG, "onAdClosed()");
				//fireEvent(AdmobModule.AD_RECEIVED, new KrollDict());
				fireEvent("close", new KrollDict());
				//mInterstitialAd.show();
			}

			public void onAdOpened() {
				Log.d(TAG, "onAdOpened()");
				//fireEvent(AdmobModule.AD_RECEIVED, new KrollDict());
				fireEvent("open", new KrollDict());
				//mInterstitialAd.show();
			}
			
			public void onAdLeftApplication() {
				Log.d(TAG, "onAdLeftApplication()");
				//fireEvent(AdmobModule.AD_RECEIVED, new KrollDict());
				fireEvent("leftApplication", new KrollDict());
				//mInterstitialAd.show();
			}
			
		});

		final AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
		
		Log.d(TAG, "requestAd(Boolean testing) -- testing:" + AdmobModule.TESTING);
		if (AdmobModule.TESTING) {
			adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
		}
		
		adRequestBuilder.setGender(this.gender);
		
		if ((this.latitude != 0) && (this.longitude != 0)) {
			Location location = new Location("");
			location.setLatitude(this.latitude);  
			location.setLongitude(this.longitude);
			location.setAccuracy(this.accuracy);
			adRequestBuilder.setLocation(location);
		}
		
		int count = 0;
	    while (count < this.keywords.length) {
	    	adRequestBuilder.addKeyword(this.keywords[count]);
	        count++;
	    }
		
		loadAd(adRequestBuilder);
		
		//loadAd(AdmobModule.TESTING);
	}
	
	// load the adMob ad
	//public void loadAd(final Boolean testing) {
	public void loadAd(final AdRequest.Builder adRequestBuilder) {
		this.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				/*
				final AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
				Log.d(TAG, "requestAd(Boolean testing) -- testing:" + testing);
				if (testing) {
					adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
				}
				*/
				
				Bundle bundle = new Bundle();
				if (bundle.size() > 0) {
					Log.d(TAG, "extras.size() > 0 -- set ad properties");
					//adRequestBuilder.addNetworkExtras(new AdMobExtras(bundle));
					adRequestBuilder.addNetworkExtrasBundle(MediationAdapter.class, bundle);
				}
				
				mInterstitialAd.loadAd(adRequestBuilder.build());
				
			}
		});
		
	}
	
/*
	@Kroll.method
	public void requestAd() {
		Log.d(TAG, "requestAd()");
		adMob.requestAd();
	}

	@Kroll.method
	public void requestTestAd() {
		Log.d(TAG, "requestTestAd(): ");
		adMob.requestTestAd();
	}
	
	@Override
	public void onDestroy(Activity activity) {
		adMob.destroy();
	}

	@Override
	public void onPause(Activity activity) {
		adMob.pause();
	}

	@Override
	public void onResume(Activity activity) {
		adMob.resume();
	}

	@Override
	public void onStart(Activity activity) {
	}

	@Override
	public void onStop(Activity activity) {
	}
*/

}

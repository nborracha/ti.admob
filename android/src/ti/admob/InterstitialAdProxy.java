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
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.view.TiUIView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;

@Kroll.proxy(creatableInModule = AdmobModule.class)
public class InterstitialAdProxy extends KrollProxy {
	private InterstitialAd mInterstitialAd;
	private static final String TAG = "AdMobInterstitialAdProxy";

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
		super.handleCreationDict(d);
	}

	@Override
	protected KrollDict getLangConversionTable() {
		KrollDict table = new KrollDict();
		table.put("title", "titleid");
		return table;
	}
	
	@Kroll.method
	public void show() {
		Log.d(TAG, "show()");
		//if (mInterstitialAd.isLoaded()) {
		mInterstitialAd.show();
		//}
	}
	
	// load the adMob ad
	public void createAd(final Boolean testing) {
		Log.d(TAG, "createAd()");
		// create the adView
		mInterstitialAd = new InterstitialAd(getActivity());
		//adView.setAdSize(AdSize.BANNER);
		mInterstitialAd.setAdUnitId(AdmobModule.PUBLISHER_ID);
		// set the listener
		mInterstitialAd.setAdListener(new AdListener() {
			public void onAdLoaded() {
				Log.d(TAG, "onAdLoaded()");
				fireEvent(AdmobModule.AD_RECEIVED, new KrollDict());
			}
			
			public void onAdFailedToLoad(int errorCode) {
				Log.d(TAG, "onAdFailedToLoad(): " + errorCode);
				fireEvent(AdmobModule.AD_NOT_RECEIVED, new KrollDict());
			}
		});
		//adView.setPadding(prop_left, prop_top, prop_right, 0);
		// Add the AdView to your view hierarchy.
		// The view will have no size until the ad is loaded.
		//setNativeView(adView);
		loadAd(AdmobModule.TESTING);
	}
	
	// load the adMob ad
	public void loadAd(final Boolean testing) {
		this.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				final AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
				Log.d(TAG, "requestAd(Boolean testing) -- testing:" + testing);
				if (testing) {
					adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
				}
				/*
				Bundle bundle = createAdRequestProperties();
				if (bundle.size() > 0) {
					Log.d(TAG, "extras.size() > 0 -- set ad properties");
					adRequestBuilder.addNetworkExtras(new AdMobExtras(bundle));
				}
				*/
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

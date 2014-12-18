package com.example.electricscreen2d;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wandoujia.ads.sdk.AdListener;
import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.loader.Fetcher;
import com.wandoujia.ads.sdk.widget.AdBanner;

public class MainActivity extends Activity {
	// private static final String TAG = "Electric_Screen";

	private static final String ADS_APP_ID = "100013003";
	private static final String ADS_SECRET_KEY = "8c5f923f5cfa060dd6e16d6272807516";
	private static final String TAG_LIST = "37b3ec7ac0d720a17e82cb8d8884e1d1";
	private static final String TAG_BANNER = "580ca01dca508f822532c9c7ed7604be";
	private AdBanner adBanner;
	private View adBannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CustomSurfaceView view = new CustomSurfaceView(this, null);
		setContentView(view);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		try {
			Ads.init(this, ADS_APP_ID, ADS_SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Ads.preLoad(this, Fetcher.AdFormat.appwall, "APP", TAG_LIST,
				new AdListener() {
					@Override
					public void onAdLoaded() {
						if (Ads.getUpdateAdCount("APP") > 0) {
							drawUpdateIndicator(Color.RED, true);
						}

					}
				});

		Ads.preLoad(this, Fetcher.AdFormat.appwall, "GAME", TAG_LIST,
				new AdListener() {
					@Override
					public void onAdLoaded() {
						if (Ads.getUpdateAdCount("GAME") > 0) {
							drawUpdateIndicator(Color.GREEN, false);
						}
					}
				});
		showBannerAd();
	}

	void showBannerAd() {
		FrameLayout mLayout = (FrameLayout) LayoutInflater.from(this).inflate(
				R.layout.adbanner, null);
		FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, 80);
		lytp.gravity = Gravity.TOP;
		addContentView(mLayout, lytp);
		ViewGroup containerView = (ViewGroup) mLayout
				.findViewById(R.id.banner_ad_container);

		if (adBannerView != null
				&& containerView.indexOfChild(adBannerView) >= 0) {
			containerView.removeView(adBannerView);
		}
		adBanner = Ads.showBannerAd(this,
				(ViewGroup) findViewById(R.id.banner_ad_container), TAG_BANNER);
		adBannerView = adBanner.getView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle("Quit")
				.setMessage("要退出吗？")
				.setNegativeButton("我要退出",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								finish();
							}
						})
				.setPositiveButton("更多推荐",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Ads.showAppWall(MainActivity.this, TAG_LIST);
							}
						}).show();
	}

	private void drawUpdateIndicator(int color, boolean drawLeftOrRight) {
		ShapeDrawable smallerCircle = new ShapeDrawable(new OvalShape());
		smallerCircle.setIntrinsicHeight(60);
		smallerCircle.setIntrinsicWidth(60);
		smallerCircle.setBounds(new Rect(0, 0, 60, 60));
		smallerCircle.getPaint().setColor(color);
		smallerCircle.setPadding(50, 50, 50, 100);

		Drawable drawableleft = null;
		Drawable drawableRight = null;
		if (drawLeftOrRight) {
			drawableleft = smallerCircle;
		} else {
			drawableRight = smallerCircle;
		}
		// showAppWallButton.setCompoundDrawables(drawableleft, null,
		// drawableRight, null);
	}

	@Override
	protected void onStart() {
		adBanner.startAutoScroll();
		super.onStart();
	}

	@Override
	protected void onStop() {
		adBanner.stopAutoScroll();
		super.onStop();
	}

}

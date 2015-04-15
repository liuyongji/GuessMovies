package com.lyj.guessmovies.ui;

import com.lyj.guessmovies.app.MyApplication;
import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class PhotoDetailActivity extends Activity implements Const{
//	private JellyViewPager pager;
	private ViewPager pager;
	private MyApplication myApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moviedetail);
		int currentimage=getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		myApplication=(MyApplication) getApplicationContext();
		pager = (ViewPager) findViewById(R.id.myViewPager1);
		pager.setAdapter(new MyViewPagerAdapter(this, myApplication.getMovies()));
		pager.setCurrentItem(currentimage, false);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

}

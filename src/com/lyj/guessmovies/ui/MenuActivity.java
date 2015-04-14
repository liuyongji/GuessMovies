package com.lyj.guessmovies.ui;

import java.util.List;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lyj.guessmovies.app.MyApplication;
import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.model.Movie;
import com.lyj.guessmovies.util.AppUtil;
import com.lyj.guessmovies.util.SPUtils;
import com.lyj.guessmovies.util.Util;
import com.lyj.guessmusic.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends Activity implements OnClickListener,Const {
	private Button btnstart, btnaddcoin,btnanswers;
	private MyApplication myApplication;
	private TextView tvcurrentstage,tvcurrentversion;
	private int mCurrentStageIndex;
	private List<Movie> movies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		btnstart=(Button)findViewById(R.id.main_btnstart);
		btnaddcoin=(Button)findViewById(R.id.btn_get_coins);
		btnanswers=(Button)findViewById(R.id.btn_answers);
		tvcurrentstage=(TextView)findViewById(R.id.main_level);
		tvcurrentversion=(TextView)findViewById(R.id.tv_app_info);
		btnaddcoin.setOnClickListener(this);
		btnanswers.setOnClickListener(this);
		btnstart.setOnClickListener(this);
		AdManager.getInstance(this).init("3336b684c26b7540",
				"70229ffe9c877dfe");
		OffersManager.getInstance(this).onAppLaunch();
		myApplication=(MyApplication) this.getApplicationContext();
		initData();
		mCurrentStageIndex = (Integer) SPUtils.get(this, STAGEINDEX, 0);
		tvcurrentstage.setText(++mCurrentStageIndex+"");
		tvcurrentversion.setText("版本: "+AppUtil.getVersionName(this)+"，关卡数："+movies.size());
		
	}
	private void initData(){
		String result = Util.getFromAssets(this, "moviess.txt");
		Gson gson = new Gson();
		 movies = gson.fromJson(result, new TypeToken<List<Movie>>() {
		}.getType());
		myApplication.setMovies(movies);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_btnstart:
			Intent intent = new Intent(MenuActivity.this, MainActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_get_coins:
			OffersManager.getInstance(MenuActivity.this).showOffersWall();
			break;
		case R.id.btn_answers:
			Intent intent2 = new Intent(MenuActivity.this, PhotoActivity.class);
			startActivity(intent2);

		default:
			break;
		}

	}

}

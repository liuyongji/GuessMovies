package com.lyj.guessmovies.ui;

import java.util.List;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;

import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.ui.BmobMainActivity;
import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lyj.guessmovies.R.id;
import com.lyj.guessmovies.app.MyApplication;
import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.model.Movie;
import com.lyj.guessmovies.util.AppUtil;
import com.lyj.guessmovies.util.SPUtils;
import com.lyj.guessmovies.util.ToastUtil;
import com.lyj.guessmovies.util.Util;
import com.lyj.guessmovies.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity implements OnClickListener, Const {
	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;
	private Button btnstart, btnaddcoin, btnanswers, btnfeeback, btnnews,
			btnextras, btnranklist;
	private MyApplication myApplication;
	private TextView tvcurrentstage, tvcurrentversion;
	private int mCurrentStageIndex;
	private List<Movie> movies;
	private FeedbackAgent agent;
	private User muser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
		BmobPay.init(this, "6ac560f8ca942178c35e093c220e40b9");
		// BmobChat.DEBUG_MODE = true;
		muser = BmobUserManager.getInstance(this).getCurrentUser(User.class);
		AdManager.getInstance(this)
				.init("e091bce73f6dc900", "f759de2e4964d60f");
		OffersManager.getInstance(this).onAppLaunch();
		myApplication = (MyApplication) this.getApplicationContext();
		initData();
		
		if (muser != null) {
			mCurrentStageIndex = muser.getHighScore();
		} else {
			mCurrentStageIndex = (Integer) SPUtils.get(this, STAGEINDEX, 0);
		}
		tvcurrentversion.setText("版本: " + AppUtil.getVersionName(this)
				+ "，关卡数：" + movies.size() + "   "
				+ getResources().getString(R.string.author));
		agent = new FeedbackAgent(MenuActivity.this);
		agent.sync();
	}

	private void initView() {
		btnstart = (Button) findViewById(R.id.main_btnstart);
		btnaddcoin = (Button) findViewById(R.id.btn_get_coins);
		btnanswers = (Button) findViewById(R.id.btn_answers);
		tvcurrentstage = (TextView) findViewById(R.id.main_level);
		tvcurrentversion = (TextView) findViewById(R.id.tv_app_info);
		btnfeeback = (Button) findViewById(R.id.btn_feeback);
		btnnews = (Button) findViewById(R.id.btn_news);
		btnextras = (Button) findViewById(R.id.btn_shop);
		btnranklist = (Button) findViewById(R.id.btn_ranklist);
		btnranklist.setOnClickListener(this);
		btnnews.setOnClickListener(this);
		btnextras.setOnClickListener(this);
		btnfeeback.setOnClickListener(this);
		btnaddcoin.setOnClickListener(this);
		btnanswers.setOnClickListener(this);
		btnstart.setOnClickListener(this);
	}

	private void initData() {
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
		update();
		MobclickAgent.onResume(this);
	}

	private void update() {
		if (muser != null) {
			mCurrentStageIndex = muser.getHighScore();
		} else {
			mCurrentStageIndex = (Integer) SPUtils.get(this, STAGEINDEX, 0);
		}
		tvcurrentstage.setText(++mCurrentStageIndex + "");
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

			break;
		case R.id.btn_answers:
			Intent intent2 = new Intent(MenuActivity.this, PhotoActivity.class);
			startActivity(intent2);
			break;
		case R.id.btn_feeback:
			// ToastUtil.showShort(MenuActivity.this, "敬请期待……");
//			agent.startFeedbackActivity();
			Intent intent5=new Intent(MenuActivity.this, FeebackActivity.class);
			startActivity(intent5);
			break;
		case R.id.btn_news:
			ToastUtil.showShort(MenuActivity.this, "敬请期待……");
			break;
		case R.id.btn_shop:
			Intent intent4 = new Intent(MenuActivity.this, ShopActivity.class);
			startActivity(intent4);
			// ToastUtil.showShort(MenuActivity.this, "敬请期待……");
			break;
		case R.id.btn_ranklist:
			Intent intent3 = new Intent(MenuActivity.this,
					RankListActivity.class);
			startActivity(intent3);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

}

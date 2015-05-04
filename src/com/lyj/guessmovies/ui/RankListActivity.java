package com.lyj.guessmovies.ui;

import java.util.List;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.ui.BmobMainActivity;
import com.bmob.im.demo.ui.LoginActivity;
import com.bmob.im.demo.ui.SetMyInfoActivity;
import com.lyj.guessmovies.R;
import com.lyj.guessmovies.adapter.RankListAdapter;
import com.lyj.guessmovies.util.ToastUtil;
//import com.lyj.guessmovies.model.MoviesUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class RankListActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private RankListAdapter rankListAdapter;
	private ListView mListView;
	private TextView mtextView;
	private ImageButton btnback, btnceter;
	private List<User> list;
	private ProgressDialog progress;
	private BmobUserManager userManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranklist);
		initView();
		checkLogin();
	}

	private void checkLogin() {
		userManager = BmobUserManager.getInstance(this);
		if (userManager.getCurrentUser() == null) {
			ToastUtil.showShort(RankListActivity.this, "只有登陆用户才能进入排行榜");
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		} else {
			initData();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// checkLogin();
	}

	private void initView() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.listview_ranklist_item_top, null);
		mListView = (ListView) findViewById(R.id.listview_rank);
		btnback = (ImageButton) findViewById(R.id.titlelayout_btnback);
		btnceter = (ImageButton) findViewById(R.id.titlelayout_rightbtn);
		mtextView = (TextView) findViewById(R.id.titlelayout_title);
		mtextView.setText("排行榜");
		btnceter.setVisibility(View.VISIBLE);
		btnceter.setOnClickListener(this);
		btnback.setOnClickListener(this);
		mListView.addHeaderView(view);
		mListView.setOnItemClickListener(this);
	}

	private void initData() {

		progress = new ProgressDialog(RankListActivity.this);
		progress.setMessage("正在加载...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		BmobQuery<User> query = new BmobQuery<User>();
		query.setLimit(50);
		query.order("-highScore");
		query.findObjects(this, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> list) {
				if (progress.isShowing()) {
					progress.dismiss();
				}
				RankListActivity.this.list = list;
				rankListAdapter = new RankListAdapter(RankListActivity.this,
						list);
				mListView.setAdapter(rankListAdapter);
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i)
							.getUsername()
							.equals(userManager.getCurrentUser(User.class)
									.getUsername())) {
						ToastUtil.showLong(RankListActivity.this, "你的分数排在第"
								+ (i + 1) + "位");
						return;
					}
				}
				ToastUtil.showLong(RankListActivity.this, "你的分数太低，还没有进入排行版");
			}

			@Override
			public void onError(int arg0, String arg1) {
				ToastUtil.showLong(RankListActivity.this, "加载失败:" + arg1);
				progress.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titlelayout_btnback:
			finish();
			break;
		case R.id.titlelayout_rightbtn:
			Intent intent3 = new Intent(RankListActivity.this,
					BmobMainActivity.class);
			startActivity(intent3);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int location,
			long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(RankListActivity.this,
				SetMyInfoActivity.class);
		if (list.get(location-1).getUsername()
				.equals(userManager.getCurrentUser(User.class).getUsername())) {
			intent.putExtra("from", "me");
		} else {
			intent.putExtra("from", "add");
		}
		intent.putExtra("username", list.get(location-1).getUsername());
		startActivity(intent);
	}

}

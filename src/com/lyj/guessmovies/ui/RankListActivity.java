package com.lyj.guessmovies.ui;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.bean.BmobChatUser;

import com.bmob.im.demo.ui.BmobMainActivity;
import com.lyj.guessmovies.R;
import com.lyj.guessmovies.adapter.RankListAdapter;
//import com.lyj.guessmovies.model.MoviesUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class RankListActivity extends Activity implements OnClickListener{
	private RankListAdapter rankListAdapter;
	private ListView mliListView;
	private ImageButton btnback,btnceter;
	private List<BmobChatUser> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranklist);
		mliListView=(ListView)findViewById(R.id.listview_rank);
		list=new ArrayList<BmobChatUser>();
		
		for (int i = 0; i < 30; i++) {
			BmobChatUser moviesUser=new BmobChatUser();
			list.add(moviesUser);
		}
		rankListAdapter=new RankListAdapter(this, list);
		mliListView.setAdapter(rankListAdapter);
		btnback=(ImageButton)findViewById(R.id.titlelayout_btnback);
		btnceter=(ImageButton)findViewById(R.id.titlelayout_rightbtn);
		btnceter.setVisibility(View.VISIBLE);
		btnceter.setOnClickListener(this);
		btnback.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titlelayout_btnback:
			finish();
			break;
		case R.id.titlelayout_rightbtn:
			Intent intent3 = new Intent(RankListActivity.this, BmobMainActivity.class);
			startActivity(intent3);
			break;
			

		default:
			break;
		}
	}

}

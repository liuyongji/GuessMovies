package com.lyj.guessmovies.ui;

import java.util.List;

import cn.bmob.im.BmobUserManager;

import com.bmob.im.demo.bean.User;
import com.lyj.guessmovies.adapter.ImageAdapter;
import com.lyj.guessmovies.app.MyApplication;
import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.model.Movie;
import com.lyj.guessmovies.util.SPUtils;
import com.lyj.guessmovies.util.ToastUtil;
import com.lyj.guessmovies.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

public class PhotoActivity extends Activity implements Const {
	private GridView mGridView;
	private List<Movie> movies;
	private ImageButton btnback;
	private int mcurrentstage;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview_photo);
		User user=BmobUserManager.getInstance(this).getCurrentUser(User.class);
		if (user!=null) {
			mcurrentstage=user.getHighScore();
		}else {
			mcurrentstage=(Integer) SPUtils.get(this, STAGEINDEX, 0);
		}
		
		movies = ((MyApplication) getApplicationContext()).getMovies();
		ImageAdapter imageAdapter = new ImageAdapter(this, movies);
		btnback=(ImageButton)findViewById(R.id.titlelayout_btnback);
		btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mGridView = (GridView) findViewById(R.id.gv_photos);
		mGridView.setAdapter(imageAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position<mcurrentstage) {
					imageBrower(position);
				}else {
					ToastUtil.showShort(PhotoActivity.this, "你还没有通过该关卡");
				}
				
			}
		});
	};

	private void imageBrower(int position) {
		Intent intent = new Intent(PhotoActivity.this,
				PhotoDetailActivity.class);
		intent.putExtra(EXTRA_IMAGE_INDEX, position);
		startActivity(intent);

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

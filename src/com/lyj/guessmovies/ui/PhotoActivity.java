package com.lyj.guessmovies.ui;

import java.util.List;

import com.lyj.guessmovies.app.MyApplication;
import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.model.Movie;
import com.lyj.guessmovies.util.SPUtils;
import com.lyj.guessmovies.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class PhotoActivity extends Activity implements Const {
	private GridView mGridView;
	private List<Movie> movies;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview_photo);
		movies = ((MyApplication) getApplicationContext()).getMovies();
		ImageAdapter imageAdapter = new ImageAdapter(this, movies);
		mGridView = (GridView) findViewById(R.id.gv_photos);
		mGridView.setAdapter(imageAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				imageBrower(position);
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

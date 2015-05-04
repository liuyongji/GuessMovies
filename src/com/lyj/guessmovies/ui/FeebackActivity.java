package com.lyj.guessmovies.ui;

import com.lyj.guessmovies.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

public class FeebackActivity extends Activity{
	private ImageView imageView;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feeback);
		imageView=(ImageView)findViewById(R.id.imageView1);
		imageView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				
				return true;
			}
		});
	}
	;

}

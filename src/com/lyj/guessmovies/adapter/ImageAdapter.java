package com.lyj.guessmovies.adapter;

import java.util.List;

import cn.bmob.im.BmobUserManager;

import com.bmob.im.demo.bean.User;
import com.lyj.guessmovies.R;
import com.lyj.guessmovies.app.MyApplication;
import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.model.Movie;
import com.lyj.guessmovies.util.SPUtils;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter implements Const{
	private Activity activity;
	private static LayoutInflater inflater;
	private List<Movie> list;
	private int width;
	
	private int mCurrentIndex;

	@SuppressWarnings({ "deprecation" })
	public ImageAdapter(Activity a, final List<Movie> urls) {
		activity = a;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		list = urls;
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		User user=BmobUserManager.getInstance(a).getCurrentUser(User.class);
		if (user!=null) {
			mCurrentIndex=user.getHighScore();
		}else {
			mCurrentIndex=(Integer) SPUtils.get(a, STAGEINDEX, 0);
		}
	}

	@Override
	public int getCount() {
//		return mCurrentIndex;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position<mCurrentIndex) {
			convertView = inflater.inflate(R.layout.listview_item, null);
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.imageView1);

			imageView.setLayoutParams(new LinearLayout.LayoutParams(width / 3,
					width / 3));

			MyApplication.displayImage("assets://images/"+list.get(position).getUrl(), imageView);
		}else {
			convertView = inflater.inflate(R.layout.listview_item_empty, null);
			TextView textView=(TextView)convertView.findViewById(R.id.listview_item_textview);
			textView.setLayoutParams(new LinearLayout.LayoutParams(width / 3,
			width / 3));
			textView.setText(position+1+"");
		}
		

		return convertView;
	}

}

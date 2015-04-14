package com.lyj.guessmovies.ui;

import java.util.List;

import com.lyj.guessmovies.model.Movie;
import com.lyj.guessmovies.util.Util;
import com.lyj.guessmusic.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewPagerAdapter extends PagerAdapter{
	private Context context;
	private List<Movie> movies;
	private int mCurrentStage;
	public MyViewPagerAdapter(Context context,List<Movie> movies){
		this.context=context;
		this.movies=movies;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return movies.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView((View) object);
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(context).inflate(R.layout.frag_layout, null);
		final TextView tv1 = (TextView) view.findViewById(R.id.vpa_tv_title);
//		final TextView tv2 = (TextView) view.findViewById(R.id.textView2);
		ImageView iv = (ImageView) view.findViewById(R.id.vpa_iv_content);
		Movie movie=movies.get(position);
		iv.setImageBitmap(Util.getImageFromAssetsFile(context, "images/"+movie.getUrl()));
		tv1.setText(movie.getName());
		container.addView(view);
		return view;
	}

}

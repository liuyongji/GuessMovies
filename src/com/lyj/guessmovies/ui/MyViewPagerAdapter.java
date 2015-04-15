package com.lyj.guessmovies.ui;

import java.util.List;

import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.model.Movie;
import com.lyj.guessmovies.util.SPUtils;
import com.lyj.guessmovies.util.Util;
import com.lyj.guessmovies.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewPagerAdapter extends PagerAdapter implements Const{
	private Context context;
	private List<Movie> movies;
	private int mCurrentStage;
	private boolean visible = true;
	public MyViewPagerAdapter(Context context,List<Movie> movies){
		this.context=context;
		this.movies=movies;
		initData();
		
	}
	private void initData(){
		mCurrentStage=(Integer) SPUtils.get(context, STAGEINDEX, 0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCurrentStage;
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
		final TextView tv2 = (TextView) view.findViewById(R.id.textView2);
		ImageView iv = (ImageView) view.findViewById(R.id.vpa_iv_content);
		final Movie movie=movies.get(position);
		iv.setImageBitmap(Util.getImageFromAssetsFile(context, "images/"+movie.getUrl()));
		tv1.setText(movie.getName());
		if (movie.getBrief()!=null) {
			tv2.setVisibility(View.VISIBLE);
			tv2.setText(movie.getBrief());
		}
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!visible){
					visible = true;
					tv1.setVisibility(View.VISIBLE);
					if (movie.getBrief()!=null) {
						tv2.setVisibility(View.VISIBLE);
						tv2.setText(movie.getBrief());
					}
				}else{
					visible = false;
					tv1.setVisibility(View.INVISIBLE);
					tv2.setVisibility(View.INVISIBLE);
				}
			}
		});
		container.addView(view);
		return view;
	}

}

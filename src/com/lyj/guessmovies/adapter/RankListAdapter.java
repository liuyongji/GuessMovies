package com.lyj.guessmovies.adapter;

import java.util.List;


import cn.bmob.im.BmobUserManager;

import com.bmob.im.demo.bean.User;
import com.lyj.guessmovies.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankListAdapter extends BaseAdapter{
	private Context context;
	private List<User> list;
	private User muser;
	public RankListAdapter(Context context,List<User> list){
		this.context=context;
		this.list=list;
		initData();
	}
	private void initData(){
		muser=BmobUserManager.getInstance(context).getCurrentUser(User.class);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list.size()>20) {
			return 20;
		}
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int location, View arg1, ViewGroup arg2) {
		User user=list.get(location);
		View view= LayoutInflater.from(context).inflate(R.layout.listview_ranklist_item, null);
		LinearLayout layout=(LinearLayout)view.findViewById(R.id.listview_ranklist_item_linear);
		TextView textrank=(TextView)view.findViewById(R.id.textView_rank);
		TextView textname=(TextView)view.findViewById(R.id.textView_name);
		TextView textscore=(TextView)view.findViewById(R.id.textView_score);
		if (user.getUsername().equals(muser.getUsername())) {
			layout.setBackgroundColor(Color.YELLOW);
		}
//		ImageView image=(ImageView)view.findViewById(R.id.imageView_face);
		textname.setText(user.getUsername());
		textscore.setText(user.getHighScore()+"");
		textrank.setText(location+1+"");
//		image.set
		return view;
	}

}

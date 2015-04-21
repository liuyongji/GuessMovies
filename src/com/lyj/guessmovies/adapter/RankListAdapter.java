package com.lyj.guessmovies.adapter;

import java.util.List;

import cn.bmob.im.bean.BmobChatUser;

import com.lyj.guessmovies.R;
//import com.lyj.guessmovies.model.MoviesUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RankListAdapter extends BaseAdapter{
	private Context context;
	private List<BmobChatUser> list;
	public RankListAdapter(Context context,List<BmobChatUser> list){
		this.context=context;
		this.list=list;
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
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int location, View arg1, ViewGroup arg2) {
		BmobChatUser moviesUser=list.get(location);
		View view= LayoutInflater.from(context).inflate(R.layout.listview_ranklist_item, null);
		TextView textrank=(TextView)view.findViewById(R.id.textView_rank);
		textrank.setText(location+1+"");
		return view;
	}

}

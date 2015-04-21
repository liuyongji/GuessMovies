package com.lyj.guessmovies.ui;


import net.youmi.android.offers.OffersManager;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.lyj.guessmovies.R;
import com.lyj.guessmovies.util.ToastUtil;
import com.lyj.guessmovies.util.Util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ShopActivity extends Activity implements OnClickListener{
	private ImageButton imageBtnback;
	private Button btnbuycoin,btnbuyads,btngetcoins;
	private TextView texttitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		imageBtnback=(ImageButton)findViewById(R.id.titlelayout_btnback);
		texttitle=(TextView)findViewById(R.id.titlelayout_title);
		texttitle.setText("商城");
		btnbuycoin=(Button) findViewById(R.id.daoju_buycoin);
		btnbuyads=(Button) findViewById(R.id.daoju_buyads);
		btngetcoins=(Button)findViewById(R.id.daoju_getcoin);
		imageBtnback.setOnClickListener(this);
		btnbuyads.setOnClickListener(this);
		btnbuycoin.setOnClickListener(this);
		btngetcoins.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titlelayout_btnback:
			finish();
			break;
		case R.id.daoju_buyads:
			new BmobPay(ShopActivity.this).pay(6.00,"500金币",payListener);
			break;
		case R.id.daoju_buycoin:
			new BmobPay(ShopActivity.this).payByWX(10.00,"一键去除广告",payListener);
			break;
		case R.id.daoju_getcoin:
			
			OffersManager.getInstance(ShopActivity.this).showOffersWall();
			break;

		default:
			break;
		}
	}
	
private PayListener payListener=new PayListener() {
		
		@Override
		public void unknow() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void succeed() {
			// TODO Auto-generated method stub
			ToastUtil.showLong(ShopActivity.this, "success");
		}
		
		@Override
		public void orderId(String arg0) {
			// TODO Auto-generated method stub
			ToastUtil.showLong(ShopActivity.this, "orderId"+arg0);
		}
		
		@Override
		public void fail(int arg0, String arg1) {
			// TODO Auto-generated method stub
			ToastUtil.showLong(ShopActivity.this, "fail arg0"+arg0+"arg1"+arg1);
			if (arg0 == -3) {
				Util.installBmobPayPlugin(ShopActivity.this, "BmobPayPlugin.apk");
			}
		}
	};

}

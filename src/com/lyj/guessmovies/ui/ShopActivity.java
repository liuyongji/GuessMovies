package com.lyj.guessmovies.ui;


import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsManager;

import cn.bmob.im.BmobUserManager;

import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.ui.LoginActivity;
import com.bmob.im.demo.util.TimeUtil;
import com.bmob.im.demo.view.dialog.DialogTips;
import com.bmob.pay.tool.BmobPay;
import com.lyj.guessmovies.R;
import com.lyj.guessmovies.app.MyApplication;
import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.listener.LyjPayListener;
import com.lyj.guessmovies.util.SPUtils;
import com.lyj.guessmovies.util.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShopActivity extends Activity implements OnClickListener,PointsChangeNotify{
	private ImageButton imageBtnback;
	private Button btnbuycoin,btnbuyads,btngetcoins,btnshare,btnsign;
	private TextView texttitle,textbaricon;
	private DialogTips dialogTips;
	private RelativeLayout relativeLayout;
	private BmobUserManager userManager;
	
	private int which;
	private int price;
	private String brief;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		initView();
		checkLogin();
		PointsManager.getInstance(this).registerNotify(this);
		
	}
	private void checkLogin() {
		userManager = BmobUserManager.getInstance(this);
		if (userManager.getCurrentUser() == null) {
			ToastUtil.showShort(ShopActivity.this, "请先登录");
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		} else {
			if (userManager.getCurrentUser(User.class).getAdclear()) {
				btnbuyads.setEnabled(false);
			}
		}
	}
	
	private void initView(){
		imageBtnback=(ImageButton)findViewById(R.id.titlelayout_btnback);
		relativeLayout=(RelativeLayout)findViewById(R.id.relativelayout_bar_coin);
		relativeLayout.setVisibility(View.VISIBLE);
		texttitle=(TextView)findViewById(R.id.titlelayout_title);
		textbaricon=(TextView)relativeLayout.findViewById(R.id.txt_bar_coins);
		int myPointBalance = PointsManager.getInstance(this).queryPoints();
		textbaricon.setText(myPointBalance+"");
		texttitle.setText("商城");
		btnbuycoin=(Button) findViewById(R.id.daoju_buycoin);
		btnbuyads=(Button) findViewById(R.id.daoju_buyads);
		btngetcoins=(Button)findViewById(R.id.daoju_getcoin);
		btnshare=(Button)findViewById(R.id.daoju_share);
		btnsign=(Button)findViewById(R.id.daoju_sign);
		btnshare.setOnClickListener(this);
		btnsign.setOnClickListener(this);
		imageBtnback.setOnClickListener(this);
		btnbuyads.setOnClickListener(this);
		btnbuycoin.setOnClickListener(this);
		btngetcoins.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		View view=LayoutInflater.from(ShopActivity.this).inflate(R.layout.dialog_pay_content, null);
		ImageView iv_webchat=(ImageView)view.findViewById(R.id.pay_webchat);
		ImageView iv_zhifubao=(ImageView)view.findViewById(R.id.pay_zhifubao);
		iv_webchat.setOnClickListener(this);
		iv_zhifubao.setOnClickListener(this);
		dialogTips=new DialogTips(ShopActivity.this, "请选择支付方式：", view, null, false, true,false);
		
		switch (v.getId()) {
		case R.id.titlelayout_btnback:
			finish();
			break;
		case R.id.daoju_buyads:
			which=0;
			price=10;
			brief="一键去广告";
			dialogTips.show();
			break;
		case R.id.daoju_buycoin:
			which=1;
			price=6;
			brief="500金币";
			dialogTips.show();
			break;
		case R.id.daoju_getcoin:
			
			OffersManager.getInstance(ShopActivity.this).showOffersWall();
			break;
		case R.id.daoju_share:
			((MyApplication) getApplicationContext()).setShare(
					ShopActivity.this, null, null,false);
			break;
		case R.id.daoju_sign:
			String datetime=TimeUtil.getCurrentTime("MMDD");
			String oldtime=(String) SPUtils.get(ShopActivity.this, Const.DATE_STRING, "0000");
			if(TimeUtil.stringToLong(datetime, "MMDD")>TimeUtil.stringToLong(oldtime, "MMDD")){
				ToastUtil.showLong(ShopActivity.this, "成功签到");
				SPUtils.put(ShopActivity.this,  Const.DATE_STRING, datetime);
				PointsManager.getInstance(ShopActivity.this).awardPoints(20);
			}else {
				ToastUtil.showLong(ShopActivity.this, "你今天已经签到");
			}
			break;
		case R.id.pay_webchat:
//			ToastUtil.showLong(ShopActivity.this, which+"");
			new BmobPay(ShopActivity.this).payByWX(price,brief,new LyjPayListener(ShopActivity.this,which));
			dialogTips.dismiss();
			
			break;
		case R.id.pay_zhifubao:
			new BmobPay(ShopActivity.this).pay(price,brief,new LyjPayListener(ShopActivity.this,which));
			dialogTips.dismiss();
			break;
			
			

		default:
			break;
		}
	}
	@Override
	public void onPointBalanceChange(int pointsBalance) {
		// TODO Auto-generated method stub
		textbaricon.setText(pointsBalance + "");
	}

}

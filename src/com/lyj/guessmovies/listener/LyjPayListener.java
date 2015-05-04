package com.lyj.guessmovies.listener;

import net.youmi.android.offers.PointsManager;
import android.content.Context;
import android.util.Log;
import cn.bmob.im.BmobUserManager;
import com.bmob.im.demo.bean.User;
import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.model.OrderInfo;
import com.lyj.guessmovies.util.ToastUtil;
import com.lyj.guessmovies.util.Util;

public class LyjPayListener implements PayListener, Const {
	private Context context;
	private int paycode;
	private String orderId;
	private OrderInfo orderInfo;
	private User user = BmobUserManager.getInstance(context).getCurrentUser(
			User.class);

	public LyjPayListener(Context context, int paycode) {
		this.context = context;
		this.paycode = paycode;
	}

	@Override
	public void unknow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void succeed() {
		switch (paycode) {
		case 0:

			User user2 = new User();
			user2.setAdclear(true);
			user2.setObjectId(user.getObjectId());
			user2.update(context);

			break;
		case 1:
			Log.i("lyj", "successsss");
			PointsManager.getInstance(context).awardPoints(500);
			break;

		default:
			break;
		}
		orderInfo.setResult(true);
		orderInfo.setOrderId(orderId);
		orderInfo.setUsername(user.getUsername());
		orderInfo.update(context);
		ToastUtil.showLong(context, "购买成功");
	}

	@Override
	public void orderId(String arg0) {
		// TODO Auto-generated method stub
		this.orderId = arg0;
		orderInfo = new OrderInfo();
		orderInfo.setOrderId(arg0);
		switch (paycode) {
		case 0:
			orderInfo.setMoney(10);
			orderInfo.setBrief("去广告");
			break;
		case 1:
			orderInfo.setMoney(6);
			orderInfo.setBrief("500金币");
			break;

		default:
			break;
		}
		orderInfo.save(context);

		// ToastUtil.showLong(context, "orderId"+arg0);
	}

	@Override
	public void fail(int code, String arg1) {
		// TODO Auto-generated method stub
		switch (code) {
		case -3:
			ToastUtil.showLong(context, "你还没有安装微信安全支付插件");
			Util.installBmobPayPlugin(context, "BmobPayPlugin.apk");
			break;
		case 7777:
			ToastUtil.showLong(context, "你还没有安装微信客户端");
			break;
		case 8888:
			ToastUtil.showLong(context, "微信客户端版本不支持微信支付");
			break;
		case 10077:
			ToastUtil.showLong(context, "上一次支付操作尚未完成");
			BmobPay.ForceFree();
			break;

		default:
			ToastUtil.showLong(context, "支付失败:" + arg1);
			break;
		}
		// ToastUtil.showLong(context, "fail code"+code+"arg1"+arg1);
	}
}

package com.bmob.im.demo.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.config.BmobConstants;
import com.bmob.im.demo.ui.BaseActivity.OnLeftButtonClickListener;
import com.bmob.im.demo.util.CommonUtils;
import com.bmob.im.demo.view.HeaderLayout;
import com.bmob.im.demo.view.HeaderLayout.HeaderStyle;
import com.bmob.im.demo.view.dialog.DialogTips;
import com.lyj.guessmovies.R;
import com.lyj.guessmovies.ui.MenuActivity;
import com.lyj.guessmovies.ui.RankListActivity;

/**
 * @ClassName: LoginActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-3 ����4:41:42
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	EditText et_username, et_password;
	Button btn_login;
	TextView btn_register;
	private TextView texttitle;

	private MyBroadcastReceiver receiver = new MyBroadcastReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		
		initTopBarForLeft("登陆");
		//注册退出广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH);
		registerReceiver(receiver, filter);
	}

	
	private void init() {
		texttitle=(TextView)findViewById(R.id.titlelayout_title);
		texttitle.setText("登陆");
		et_username = (EditText) findViewById(R.id.login_username_edit);
		et_password = (EditText) findViewById(R.id.login_password_edit);
		btn_login = (Button) findViewById(R.id.login_btnlogin);
		btn_register = (TextView) findViewById(R.id.login_btnregister);
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}

	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && BmobConstants.ACTION_REGISTER_SUCCESS_FINISH.equals(intent.getAction())) {
				finish();
			}
		}

	}
	
	@Override
	public void onClick(View v) {
		if (v == btn_register) {
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
		} else if(v==btn_login) {
			boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
			if(!isNetConnected){
				ShowToast(R.string.network_tips);
				return;
			}
			login();
		}
	}
	
	private void login(){
		String name = et_username.getText().toString();
		String password = et_password.getText().toString();

		if (TextUtils.isEmpty(name)) {
			ShowToast(R.string.toast_error_username_null);
			return;
		}

		if (TextUtils.isEmpty(password)) {
			ShowToast(R.string.toast_error_password_null);
			return;
		}

		final ProgressDialog progress = new ProgressDialog(
				LoginActivity.this);
		progress.setMessage("正在登陆...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		User user = new User();
		user.setUsername(name);
		user.setPassword(password);
		userManager.login(user,new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						progress.setMessage("正在获取好友列表...");
					}
				});
				//更新用户的地理位置以及好友的资料
				updateUserInfos();
				progress.dismiss();
				Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int errorcode, String arg0) {
				// TODO Auto-generated method stub
				progress.dismiss();
				BmobLog.i(arg0);
				ShowToast(arg0);
			}
		});
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
}

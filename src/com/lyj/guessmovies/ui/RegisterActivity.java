package com.lyj.guessmovies.ui;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.SaveListener;

import com.lyj.guessmovies.R;
//import com.lyj.guessmovies.app.MyApplication;
//import com.lyj.guessmovies.model.MoviesUser;
import com.lyj.guessmovies.util.CommonUtils;
import com.lyj.guessmovies.util.ToastUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class RegisterActivity extends Activity implements OnClickListener{

	Button btn_register;
	ImageButton btn_back;
	EditText et_username, et_password, et_email;
//	BmobChatUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_no);

		
		btn_back=(ImageButton)findViewById(R.id.titlelayout_btnback);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_email = (EditText) findViewById(R.id.et_email);

		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}
	
	private void register(){
		final String name = et_username.getText().toString();
		final String password = et_password.getText().toString();
		String pwd_again = et_email.getText().toString();
		
		if (TextUtils.isEmpty(name)) {
			ToastUtil.showLong(RegisterActivity.this, "用户名不能为空");
			return;
		}

		if (TextUtils.isEmpty(password)) {
			ToastUtil.showLong(RegisterActivity.this, "密码不能为空");
			return;
		}
		if (!pwd_again.equals(password)) {
			ToastUtil.showLong(RegisterActivity.this, "两次输入密码不一样");
			return;
		}
		
		boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
		if(!isNetConnected){
			ToastUtil.showLong(RegisterActivity.this, "当前没有网络");
			return;
		}
		
		final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
		progress.setMessage("正在注册...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		final BmobChatUser bu = new BmobChatUser();
		bu.setUsername(name);
		bu.setPassword(password);
//		bu.setDeviceType("android");
//		bu.setInstallId(BmobInstallation.getInstallationId(this));
		bu.signUp(RegisterActivity.this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				progress.dismiss();
				ToastUtil.showLong(RegisterActivity.this, "注册成功");
//				((MyApplication)getApplicationContext()).setUser(name);
				BmobUserManager.getInstance(RegisterActivity.this).bindInstallationForRegister(bu.getUsername());
				Intent intent = new Intent(RegisterActivity.this,RankListActivity.class);
				startActivity(intent);
				finish();
				
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
				ToastUtil.showLong(RegisterActivity.this, "注册失败："+arg1);
				progress.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titlelayout_btnback:
			finish();
			break;
		case R.id.btn_register:
			register();
			break;

		default:
			break;
		}
		
	}

}

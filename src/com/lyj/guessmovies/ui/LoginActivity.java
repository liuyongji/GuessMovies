package com.lyj.guessmovies.ui;

import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;

import com.lyj.guessmovies.R;
import com.lyj.guessmovies.R.id;
import com.lyj.guessmovies.app.MyApplication;
//import com.lyj.guessmovies.model.MoviesUser;
import com.lyj.guessmovies.util.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private Button btnwx, btnqq, btnwb, btnlogin;
	private ImageButton btnback;
	private TextView btnregister,texttitle;
	private EditText editname, editpsd;
	private UMSocialService mController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		BmobChatUser bmobUser = BmobUser.getCurrentUser(this, BmobChatUser.class);
		if (bmobUser != null) {
			Intent intent=new Intent(LoginActivity.this,RankListActivity.class);
			startActivity(intent);
			finish();
		}else {
			ToastUtil.showLong(this, "只有登陆用户才能进入排行榜");
		}
		initView();
		mController = UMServiceFactory
				.getUMSocialService("com.lyj.guessmovies");
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
	}

	private void initView() {
		texttitle=(TextView)findViewById(R.id.titlelayout_title);
		texttitle.setText("登陆");
		btnback=(ImageButton)findViewById(R.id.titlelayout_btnback);
		editname = (EditText) findViewById(R.id.login_username_edit);
		editpsd = (EditText) findViewById(R.id.login_password_edit);
		btnlogin = (Button) findViewById(R.id.login_btnlogin);
		btnregister = (TextView) findViewById(R.id.login_btnregister);
		btnqq = (Button) findViewById(R.id.login_btnqq);
		btnwx = (Button) findViewById(R.id.login_btnwx);
		btnwb = (Button) findViewById(R.id.login_btnwb);
		btnqq.setOnClickListener(this);
		btnwx.setOnClickListener(this);
		btnwb.setOnClickListener(this);
		btnback.setOnClickListener(this);
		btnlogin.setOnClickListener(this);
		btnregister.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.titlelayout_btnback:
			finish();
			break;
		case R.id.login_btnqq:
			BmobUser.qqLogin(LoginActivity.this, "1104535806",
					otherLoginListener);
			break;
		case R.id.login_btnwx:

			break;
		case R.id.login_btnwb:


			mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.SINA,
					new UMAuthListener() {
						@Override
						public void onError(SocializeException e,
								SHARE_MEDIA platform) {
						}

						@Override
						public void onComplete(Bundle value,
								SHARE_MEDIA platform) {
							if (value != null
									&& !TextUtils.isEmpty(value
											.getString("uid"))) {
								ToastUtil.showLong(LoginActivity.this,
										"success");

								mController.getPlatformInfo(LoginActivity.this,
										SHARE_MEDIA.SINA, new UMDataListener() {
											@Override
											public void onStart() {
											}

											@Override
											public void onComplete(int status,
													Map<String, Object> info) {
												if (status == 200
														&& info != null) {
													StringBuilder sb = new StringBuilder();
													Set<String> keys = info
															.keySet();
													for (String key : keys) {
														sb.append(key
																+ "="
																+ info.get(key)
																		.toString()
																+ "\r\n");
														ToastUtil
																.showLong(
																		LoginActivity.this,
																		sb);
													}
												} else {
													ToastUtil.showLong(
															LoginActivity.this,
															"发生错误：" + status);
													Log.d("TestData", "发生错误："
															+ status);
												}

											}
										});
							} else {
								ToastUtil.showLong(LoginActivity.this, "登陆失败");
							}
						}

						@Override
						public void onCancel(SHARE_MEDIA platform) {
						}

						@Override
						public void onStart(SHARE_MEDIA platform) {
						}
					});
			break;
		case R.id.login_btnlogin:
			final String name = editname.getText().toString();
			final String password = editpsd.getText().toString();
			if (check(name, password)) {
				BmobChatUser moviesUser = new BmobChatUser();
				moviesUser.setUsername(name);
				moviesUser.setPassword(password);
				moviesUser.login(LoginActivity.this, new SaveListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						((MyApplication) getApplicationContext()).setUser(name);
						ToastUtil.showLong(LoginActivity.this, "登陆成功");
						Intent intent = new Intent(LoginActivity.this,
								RankListActivity.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ToastUtil.showLong(LoginActivity.this, "登陆失败：" + arg1);
					}
				});
			}

			break;
		case R.id.login_btnregister:

			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private OtherLoginListener otherLoginListener = new OtherLoginListener() {

		@Override
		public void onSuccess(JSONObject arg0) {
			// TODO Auto-generated method stub
			ToastUtil
					.showLong(LoginActivity.this, "success " + arg0.toString());
		}

		@Override
		public void onFailure(int arg0, String arg1) {
			// TODO Auto-generated method stub
			ToastUtil.showLong(LoginActivity.this, "onFailure " + arg1 + arg0);
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			ToastUtil.showLong(LoginActivity.this, "onFailure");
		}
	};

	private boolean check(String name, String password) {
		if (TextUtils.isEmpty(name)) {
			ToastUtil.showLong(LoginActivity.this, "用户名不能为空");
			return false;
		}

		if (TextUtils.isEmpty(password)) {
			ToastUtil.showLong(LoginActivity.this, "密码不能为空");
			return false;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}

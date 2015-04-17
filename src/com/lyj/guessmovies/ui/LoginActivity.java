package com.lyj.guessmovies.ui;

import java.util.Map;
import java.util.Set;

import com.lyj.guessmovies.R;
import com.lyj.guessmovies.util.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity implements OnClickListener {
	private Button btnwx, btnqq, btnwb;
	private UMSocialService mController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		mController = UMServiceFactory
				.getUMSocialService("com.lyj.guessmovies");
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
	}

	private void initView() {
		btnqq = (Button) findViewById(R.id.login_btnqq);
		btnwx = (Button) findViewById(R.id.login_btnwx);
		btnwb = (Button) findViewById(R.id.login_btnwb);
		btnqq.setOnClickListener(this);
		btnwx.setOnClickListener(this);
		btnwb.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.login_btnqq:

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
								// ToastUtil.showShort(LoginActivity.this,"success");
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
													}
												}

											}
										});
							} else {
								ToastUtil
										.showShort(LoginActivity.this, "登陆失败");
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

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/**使用SSO授权必须添加如下代码 */  
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}

}

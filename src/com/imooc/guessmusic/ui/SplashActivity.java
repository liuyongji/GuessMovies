package com.imooc.guessmusic.ui;



import com.imooc.guessmusic.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
/**
 * 闪屏
 * @author coku
 * @date:2011-12
 *
 */
public class SplashActivity extends Activity{
	public static int loading_process;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	   // getWindow().requestFeature(Window.FEATURE_PROGRESS); //去标题栏
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//全屏
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.splash);
		//checkVersion();
		//设置延迟，播放登陆界面
		new Handler().postDelayed(new Runnable() 
        { 
            
            public void run() 
            { 
            	RedirectMainActivity();
                
            } 
        },  3000); 
		
		
	}
	/**
	 * 跳转
	 */
	private void RedirectMainActivity(){
		System.out.println("tiaozhuan");
		Intent i = new Intent(SplashActivity.this,MainActivity.class);
		startActivity(i);
		SplashActivity.this.finish();
	}
	
	
	
}

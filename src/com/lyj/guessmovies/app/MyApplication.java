package com.lyj.guessmovies.app;

import java.util.List;

import com.lyj.guessmovies.model.Movie;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

public class MyApplication extends Application{
	private List<Movie> movies;
	private UMSocialService mController;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	public List<Movie> getMovies() {
		return movies;
	}
	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}
	public  void setShare(final Activity context, 
			String shareContent, Bitmap shareImage) {
		mController = UMServiceFactory.getUMSocialService("com.lyj.guessmovies");
		if (shareContent != null) {
			mController.setShareContent(shareContent);
		}

		mController.setShareMedia(new UMImage(context, shareImage));

		
//		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		
		UMWXHandler wxHandler = new UMWXHandler(context, "wxd0792b8632aa595b",
				"93f25458b2909f967e6ba19d089f14d7");
		wxHandler.addToSocialSDK();
		UMWXHandler wxCircleHandler = new UMWXHandler(context,
				"wxd0792b8632aa595b", "93f25458b2909f967e6ba19d089f14d7");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		WeiXinShareContent weixinContent = new WeiXinShareContent();
//		weixinContent.setTitle(context.getResources().getString(R.string.app_name));
//		weixinContent.setShareContent("shareContent");
		weixinContent.setShareImage(new UMImage(context, shareImage));
//		weixinContent.setTargetUrl(Result.url);
		mController.setShareMedia(weixinContent);
		
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareImage(new UMImage(context, shareImage));
//		circleMedia.setTargetUrl(Result.url);
		mController.setShareMedia(circleMedia);
		
		
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, "1101987894",
				"McgaoeK2xHK8T0qm");
		qqSsoHandler.addToSocialSDK();
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context,
				"1101987894", "McgaoeK2xHK8T0qm");
		qZoneSsoHandler.addToSocialSDK();
		
		QQShareContent qqShareContent = new QQShareContent();
//		qqShareContent.setShareContent(shareContent);
//		qqShareContent.setTitle(context.getResources().getString(R.string.app_name));
		qqShareContent.setShareImage(new UMImage(context, shareImage));
//		qqShareContent.setTargetUrl(Result.url);
		
		mController.setShareMedia(qqShareContent);
		
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(shareContent);
//		qzone.setTargetUrl(Result.url);
//		qzone.setTitle(context.getResources().getString(R.string.app_name));
		qzone.setShareImage(new UMImage(context, shareImage));
		mController.setShareMedia(qzone);
		
		mController.getConfig().setSinaCallbackUrl("http://www.sina.com");
		mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN);
		mController.openShare(context, false);

	}

}

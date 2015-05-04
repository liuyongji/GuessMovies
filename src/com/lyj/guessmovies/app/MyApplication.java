package com.lyj.guessmovies.app;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.youmi.android.offers.PointsManager;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;

import com.bmob.im.demo.util.CollectionUtils;
import com.bmob.im.demo.util.SharePreferenceUtil;
import com.lyj.guessmovies.R;
import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.model.Movie;
import com.lyj.guessmovies.util.SDCardUtils;
import com.lyj.guessmovies.util.SPUtils;
import com.lyj.guessmovies.util.ToastUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.widget.ImageView;

public class MyApplication extends Application {
	NotificationManager mNotificationManager;
	MediaPlayer mMediaPlayer;
	public static MyApplication mInstance;
	private List<Movie> movies;
	SharePreferenceUtil mSpUtil;
	private UMSocialService mController;
	public final static String URL = "http://guessmovie.bmob.cn/";

	private ImageLoaderConfiguration configuration;
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions options;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		init();
		mController = UMServiceFactory
				.getUMSocialService("com.lyj.guessmovies");
		mInstance=this;
		File file = null;
		if (SDCardUtils.isSDCardEnable()) {
			 file=new File(SDCardUtils.getSDCardPath()+"guessmovies");
			if(!file.exists()){
				file.mkdir();
			}
			
		}else {
			file=getApplicationContext().getCacheDir();
		}
		configuration = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPoolSize(3)
				.diskCache(new UnlimitedDiscCache(file)) // default
				.discCacheFileCount(100).build();
		imageLoader.init(configuration);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.loading)
				.cacheInMemory(true)
				.cacheOnDisc(true).build();
	}
//	public static void initImageLoader(Context context) {
//		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
//				"bmobim/Cache");// ��ȡ�������Ŀ¼��ַ
//		// ��������ImageLoader(���е�ѡ��ǿ�ѡ��,ֻʹ����Щ������붨��)����������趨��APPLACATION���棬����Ϊȫ�ֵ����ò���
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				context)
//				// �̳߳��ڼ��ص�����
//				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
//				.memoryCache(new WeakMemoryCache())
//				.denyCacheImageMultipleSizesInMemory()
//				.discCacheFileNameGenerator(new Md5FileNameGenerator())
//				// �������ʱ���URI�����MD5 ����
//				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.discCache(new UnlimitedDiscCache(cacheDir))// �Զ��建��·��
//				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//				.writeDebugLogs() // Remove for release app
//				.build();
//		// Initialize ImageLoader with configuration.
//		ImageLoader.getInstance().init(config);// ȫ�ֳ�ʼ��������
//	}
	private Map<String, BmobChatUser> contactList = new HashMap<String, BmobChatUser>();

	/**
	 * ��ȡ�ڴ��к���user list
	 * 
	 * @return
	 */
	public Map<String, BmobChatUser> getContactList() {
		return contactList;
	}

	/**
	 * ���ú���user list���ڴ���
	 * @param contactList
	 */
	public void setContactList(Map<String, BmobChatUser> contactList) {
		if (this.contactList != null) {
			this.contactList.clear();
		}
		this.contactList = contactList;
	}

	/**
	 * �˳���¼,��ջ������
	 */
	public void logout() {
		BmobUserManager.getInstance(getApplicationContext()).logout();
		setContactList(null);
	}
	
	public static void displayImage(String url,ImageView imageView){
		imageLoader.displayImage(url, imageView, options);
	}

	public List<Movie> getMovies() {
		return movies;
	}
	public static final String PREFERENCE_NAME = "_sharedinfo";

	public synchronized SharePreferenceUtil getSpUtil() {
		if (mSpUtil == null) {
			String currentId = BmobUserManager.getInstance(
					getApplicationContext()).getCurrentUserObjectId();
			String sharedName = currentId + PREFERENCE_NAME;
			mSpUtil = new SharePreferenceUtil(this, sharedName);
		}
		return mSpUtil;
	}
	
	

	public synchronized MediaPlayer getMediaPlayer() {
		if (mMediaPlayer == null)
			mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
		return mMediaPlayer;
	}
	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}
	public static MyApplication getInstance() {
		return mInstance;
	}
	
	private void init() {
		mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
//		initImageLoader(getApplicationContext());
		// ���û���½�����ȴӺ�����ݿ���ȡ������list�����ڴ���
		if (BmobUserManager.getInstance(getApplicationContext())
				.getCurrentUser() != null) {
			// ��ȡ���غ���user list���ڴ�,�����Ժ��ȡ����list
			contactList = CollectionUtils.list2map(BmobDB.create(getApplicationContext()).getContactList());
		}
	}

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}

	public void setShare(final Activity context, String shareContent,
			Bitmap shareImage,boolean isimage) {
		
		if (shareContent != null) {
			mController.setShareContent(shareContent);
		}
		if (shareImage != null) {
			mController.setShareMedia(new UMImage(context, shareImage));
		}
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		UMWXHandler wxHandler = new UMWXHandler(context, "wx21f5da6bf8bfeaca",
				"44b17186dd669255992c24b9fd96715c");
		wxHandler.addToSocialSDK();
		UMWXHandler wxCircleHandler = new UMWXHandler(context,
				"wx21f5da6bf8bfeaca", "44b17186dd669255992c24b9fd96715c");		
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, "1104535806",
				"TfVEwXEel7zQXA8B");
		qqSsoHandler.addToSocialSDK();
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context,
				"1104535806", "TfVEwXEel7zQXA8B");
		qZoneSsoHandler.addToSocialSDK();
		
		WeiXinShareContent weixinContent = new WeiXinShareContent();//微信
		CircleShareContent circleMedia = new CircleShareContent(); //朋友圈
		QQShareContent qqShareContent = new QQShareContent(); //QQ好友
		QZoneShareContent qzone = new QZoneShareContent();    //QQ空间
		
		
		weixinContent.setTitle(context.getResources().getString(
				R.string.app_name));
		if (isimage) {
			weixinContent.setShareImage(new UMImage(context, shareImage));
			circleMedia.setShareImage(new UMImage(context, shareImage));
		}else {
			weixinContent.setShareContent(Const.sharecontent);
			circleMedia.setShareContent(Const.sharecontent);
		}
		
		
		
		weixinContent.setTargetUrl(URL);
		mController.setShareMedia(weixinContent);
		circleMedia.setTargetUrl(URL);
		mController.setShareMedia(circleMedia);
		mController.registerListener(mSnsPostListener);

		// qqShareContent.setShareContent(shareContent);
		// qqShareContent.setTitle(context.getResources().getString(R.string.app_name));
		qqShareContent.setShareImage(new UMImage(context, shareImage));
		// qqShareContent.setTargetUrl(Result.url);

		mController.setShareMedia(qqShareContent);

		
		qzone.setShareContent(shareContent);
		// qzone.setTargetUrl(Result.url);
		// qzone.setTitle(context.getResources().getString(R.string.app_name));
		qzone.setShareImage(new UMImage(context, shareImage));
		mController.setShareMedia(qzone);

		mController.getConfig().setSinaCallbackUrl("http://www.sina.com");
		mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN);
		mController.openShare(context, false);

	}
	private SnsPostListener mSnsPostListener = new SnsPostListener() {

		@Override
		public void onStart() {

		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int stCode,
				SocializeEntity entity) {
			if (stCode == 200) {
				if ((Boolean) SPUtils.get(getApplicationContext(), Const.STAGESHARE, false)) {
					PointsManager.getInstance(getApplicationContext()).awardPoints(50);
					SPUtils.put(getApplicationContext(), Const.STAGESHARE, true);
				}
				ToastUtil.showShort(getApplicationContext(), "分享成功");
			} else {
				ToastUtil.showShort(getApplicationContext(), "分享失败");
			}
		}
	};
}

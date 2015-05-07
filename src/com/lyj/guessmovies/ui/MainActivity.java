package com.lyj.guessmovies.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsManager;
import net.youmi.android.spot.SpotManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

import com.bmob.im.demo.bean.User;
import com.lyj.guessmovies.R;
import com.lyj.guessmovies.app.MyApplication;
import com.lyj.guessmovies.data.Const;
import com.lyj.guessmovies.model.IWordButtonClickListener;
import com.lyj.guessmovies.model.Movie;
import com.lyj.guessmovies.model.WordButton;
import com.lyj.guessmovies.myui.MyGridView;
import com.lyj.guessmovies.util.MyLog;
import com.lyj.guessmovies.util.SPUtils;
import com.lyj.guessmovies.util.ToastUtil;
import com.lyj.guessmovies.util.Util;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

public class MainActivity extends Activity implements IWordButtonClickListener,
		OnClickListener, PointsChangeNotify, Const {

	public final static String TAG = "MainActivity";

	private Context context;

	/** 答案状态 —— 正确 */
	public final static int STATUS_ANSWER_RIGHT = 1;

	/** 答案状态 —— 错误 */
	public final static int STATUS_ANSWER_WRONG = 2;

	/** 答案状态 —— 不完整 */
	public final static int STATUS_ANSWER_LACK = 3;

	// 闪烁次数
	public final static int SPASH_TIMES = 6;

	// 唱片控件
	private ImageView mViewPan;

	// 文字框容器
	private ArrayList<WordButton> mAllWords;

	private ArrayList<WordButton> mBtnSelectWords;

	private MyGridView mMyGridView;

	// 已选择文字框UI容器
	private LinearLayout mViewWordsContainer, mPassView;
	private TextView mCurrentStage, mCurrentPoints, mpassname;
	private ImageButton btnshare, btngettip, btnbarback, btnbaraddcoin;
	private Button btnnext;
	private ImageView ivcontent;

	// 当前的歌曲
	private Movie mCurrentMovie;

	// 当前关的索引
	private int mCurrentStageIndex = -1;
	private boolean mIsAdClear = false;
	private List<Movie> movies;
	private boolean mIsFirst;
	private UMSocialService mController;
	private User mUser;
	
	private boolean mcontrol=false;

	// BackgroundMusic backgroundMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mController = UMServiceFactory
				.getUMSocialService("com.lyj.guessmovies");
		context = MainActivity.this;
		initView();
		initData();
		// 初始化游戏数据
		initCurrentStageData();
		PointsManager.getInstance(this).registerNotify(this);
//		mIsFirst = (Boolean) SPUtils.get(this, ISFIRST, true);
		
		SpotManager.getInstance(this).loadSpotAds();
	}

	private void initView() {
		mViewPan = (ImageView) findViewById(R.id.imageView1);
		mMyGridView = (MyGridView) findViewById(R.id.gridview);
		// 注册监听
		mMyGridView.registOnWordButtonClick(this);
		mViewWordsContainer = (LinearLayout) findViewById(R.id.word_select_container);
		mPassView = (LinearLayout) findViewById(R.id.pass_view);
		btnshare = (ImageButton) findViewById(R.id.share_button_icon);
		btngettip = (ImageButton) findViewById(R.id.btn_tip_answer);
		btnbarback = (ImageButton) findViewById(R.id.btn_bar_back);
		btnbaraddcoin = (ImageButton) findViewById(R.id.btn_bar_add_coins);
		btnnext = (Button) findViewById(R.id.passview_btnnext);
		btnnext.setOnClickListener(this);
		btnbarback.setOnClickListener(this);
		btnbaraddcoin.setOnClickListener(this);
		btnshare.setOnClickListener(this);
		btngettip.setOnClickListener(this);
		mCurrentStage = (TextView) findViewById(R.id.text_current_stage);
		mCurrentPoints = (TextView) findViewById(R.id.txt_bar_coins);
		ivcontent = (ImageView) findViewById(R.id.passview_image);
		mpassname = (TextView) mPassView.findViewById(R.id.passview_name);
		int myPointBalance = PointsManager.getInstance(context).queryPoints();
		mCurrentPoints.setText(myPointBalance + "");
	}

	private void initData() {
		mUser = BmobUserManager.getInstance(this).getCurrentUser(User.class);
		
		BmobQuery<User> query = new BmobQuery<User>();
		query.getObject(this, "ff819b4ede", new GetListener<User>() {

		    @Override
		    public void onSuccess(User object) {
		        // TODO Auto-generated method stub
		        MainActivity.this.mcontrol=object.getAdclear();
		    }

		    @Override
		    public void onFailure(int code, String arg0) {
		        // TODO Auto-generated method stub
		    }

		});
		
		movies = ((MyApplication) getApplicationContext()).getMovies();
		if (mUser!=null) {
			mCurrentStageIndex = mUser.getHighScore();
			mIsAdClear=mUser.getAdclear();
		}else {
			mCurrentStageIndex = (Integer) SPUtils.get(this, STAGEINDEX, 0);
			mIsAdClear =false;
		}
		mIsFirst=(Boolean)SPUtils.getonlyfirst(this, ISFIRST, true);
		if (mIsFirst) {
			SPUtils.putonlyfirst(this, ISFIRST, false);
			PointsManager.getInstance(context).awardPoints(100);
		}
		int myPointBalance = PointsManager.getInstance(context).queryPoints();
		mCurrentPoints.setText(myPointBalance + "");
		
	}

	@Override
	public void onWordButtonClick(WordButton wordButton) {
		// Toast.makeText(this, wordButton.mIndex + "",
		// Toast.LENGTH_SHORT).show();
		setSelectWord(wordButton);

		// 获得答案状态
		int checkResult = checkTheAnswer();

		// 检查答案
		if (checkResult == STATUS_ANSWER_RIGHT) {
			// 过关并获得奖励
			handlePassEvent();
		} else if (checkResult == STATUS_ANSWER_WRONG) {
			// 闪烁文字并提示用户
			sparkTheWrods(Color.RED, false);
		} else if (checkResult == STATUS_ANSWER_LACK) {
			// 设置文字颜色为白色（Normal）
			for (int i = 0; i < mBtnSelectWords.size(); i++) {
				mBtnSelectWords.get(i).mViewButton.setTextColor(Color.WHITE);
			}
		}
	}

	/**
	 * 处理过关界面及事件
	 */
	private void handlePassEvent() {

		sparkTheWrods(Color.GREEN, true);
		if (mCurrentStageIndex >= movies.size()) {
			ToastUtil.showLong(this,
					getResources().getString(R.string.mission_compelete));
			// TODO
			mCurrentStageIndex = mCurrentStageIndex-1;
			return ;
		}
		SPUtils.put(MainActivity.this, STAGEINDEX, ++mCurrentStageIndex);
		SPUtils.remove(MainActivity.this, STAGEWORDS);
		// ToastUtil.showShort(context,
		// getResources().getString(R.string.answer_right));
		
		PointsManager.getInstance(context).awardPoints(2);
		if (mUser != null) {
			if (mUser.getHighScore() < mCurrentStageIndex) {
				User user = new User();
				user.setObjectId(mUser.getObjectId());
				user.setHighScore(mCurrentStageIndex);
				user.update(context);
			}
		}
		
		// new next().execute();

	}

	private void showPassView() {
		mPassView.setVisibility(View.VISIBLE);
		ivcontent.setImageBitmap(Util.getImageFromAssetsFile(MainActivity.this,
				"images/" + mCurrentMovie.getUrl()));
		mpassname.setText(mCurrentMovie.getName());
	}

	class next extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// try {
			// Thread.sleep(600);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mViewWordsContainer.removeAllViews();
			initCurrentStageData();

		}

	}

	private void clearTheAnswer(WordButton wordButton) {
		wordButton.mViewButton.setText("");
		wordButton.mWordString = "";
		wordButton.mIsVisiable = false;

		// 设置待选框可见性
		setButtonVisiable(mAllWords.get(wordButton.mIndex), View.VISIBLE);
	}

	/**
	 * 设置答案
	 * 
	 * @param wordButton
	 */
	private void setSelectWord(WordButton wordButton) {
		for (int i = 0; i < mBtnSelectWords.size(); i++) {
			if (mBtnSelectWords.get(i).mWordString.length() == 0) {
				// 设置答案文字框内容及可见性
				mBtnSelectWords.get(i).mViewButton
						.setText(wordButton.mWordString);
				mBtnSelectWords.get(i).mIsVisiable = true;
				mBtnSelectWords.get(i).mWordString = wordButton.mWordString;
				// 记录索引
				mBtnSelectWords.get(i).mIndex = wordButton.mIndex;

				MyLog.d(TAG, "setSelectWord " + mBtnSelectWords.get(i).mIndex);

				// 设置待选框可见性
				setButtonVisiable(wordButton, View.INVISIBLE);

				break;
			}
		}
	}

	/**
	 * 设置待选文字框是否可见
	 * 
	 * @param button
	 * @param visibility
	 */
	private void setButtonVisiable(WordButton button, int visibility) {
		button.mViewButton.setVisibility(visibility);
		button.mIsVisiable = (visibility == View.VISIBLE) ? true : false;
		MyLog.d(TAG, "setButtonVisiable " + button.mIsVisiable);
	}

	@Override
	public void onPause() {
		mViewPan.clearAnimation();
		MobclickAgent.onPause(this);
		super.onPause();
	}

	private Movie loadMovieInfo(int stageIndex) {

		if (stageIndex >= movies.size()) {
			ToastUtil.showLong(this,
					getResources().getString(R.string.mission_compelete));
			// TODO
			stageIndex = stageIndex-1;
		}

		mCurrentMovie = movies.get(stageIndex);
		// String[] stage = null;
		mCurrentStage.setText((mCurrentStageIndex + 1) + "");
		mViewPan.setImageBitmap(Util.getImageFromAssetsFile(MainActivity.this,
				"images/" + mCurrentMovie.getUrl()));

		return mCurrentMovie;
	}

	private void initCurrentStageData() {
		mPassView.setVisibility(View.INVISIBLE);
		// 读取当前关信息
		mCurrentMovie = loadMovieInfo(mCurrentStageIndex);
		// 初始化已选择框
		mBtnSelectWords = initWordSelect();

		LayoutParams params = new LayoutParams(100, 100);

		for (int i = 0; i < mBtnSelectWords.size(); i++) {
			mViewWordsContainer.addView(mBtnSelectWords.get(i).mViewButton,
					params);
		}
		// 获得数据
		mAllWords = initAllWord();
		// // 更新数据- MyGridView
		mMyGridView.updateData(mAllWords);
		if ((mCurrentStageIndex + 1) % 5 == 0 && !mIsAdClear&&!mcontrol) {
			SpotManager.getInstance(this).showSpotAds(this);
		}
	}

	/**
	 * 初始化待选文字框
	 */
	private ArrayList<WordButton> initAllWord() {
		ArrayList<WordButton> data = new ArrayList<WordButton>();

		// 获得所有待选文字
		String[] words = generateWords();

		for (int i = 0; i < MyGridView.COUNTS_WORDS; i++) {
			WordButton button = new WordButton();

			button.mWordString = words[i];

			data.add(button);
		}

		return data;
	}

	/**
	 * 初始化已选择文字框
	 * 
	 * @return
	 */
	// List<View> views = new ArrayList<View>();
	private ArrayList<WordButton> initWordSelect() {
		ArrayList<WordButton> data = new ArrayList<WordButton>();

		// mCurrentSong.getNameLength()
		for (int i = 0; i < mCurrentMovie.getNamelength(); i++) {
			View view = Util.getView(MainActivity.this,
					R.layout.self_ui_gridview_item);
			// views.add(view);
			final WordButton holder = new WordButton();

			holder.mViewButton = (Button) view.findViewById(R.id.item_btn);
			holder.mViewButton.setTextColor(Color.WHITE);
			holder.mViewButton.setText("");
			holder.mIsVisiable = false;

			holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
			holder.mViewButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					clearTheAnswer(holder);
				}
			});

			data.add(holder);
		}

		return data;
	}

	/**
	 * 生成所有的待选文字
	 * 
	 * @return
	 */
	private String[] generateWords() {
		Random random = new Random();

		String[] words = new String[MyGridView.COUNTS_WORDS];

		if (SPUtils.contains(MainActivity.this, STAGEWORDS)) {
			words = (String[]) SPUtils.getsavewords(MainActivity.this,
					STAGEWORDS);
			return words;
		}
		for (int i = 0; i < mCurrentMovie.getNamelength(); i++) {
			words[i] = mCurrentMovie.getNameCharacters()[i] + "";
		}
		//
		// // 获取随机文字并存入数组
		// mCurrentSong.getNameLength()
		for (int i = mCurrentMovie.getNamelength(); i < MyGridView.COUNTS_WORDS; i++) {
			words[i] = Util.getRandomChar() + "";
		}

		// 打乱文字顺序：首先从所有元素中随机选取一个与第一个元素进行交换，
		// 然后在第二个之后选择一个元素与第二个交换，直到最后一个元素。
		// 这样能够确保每个元素在每个位置的概率都是1/n。
		for (int i = MyGridView.COUNTS_WORDS - 1; i >= 0; i--) {
			int index = random.nextInt(i + 1);

			String buf = words[index];
			words[index] = words[i];
			words[i] = buf;
		}
		SPUtils.setsavewords(MainActivity.this, STAGEWORDS, words);

		return words;
	}

	/**
	 * 检查答案
	 * 
	 * @return
	 */
	private int checkTheAnswer() {
		// 先检查长度
		for (int i = 0; i < mBtnSelectWords.size(); i++) {
			// 如果有空的，说明答案还不完整
			if (mBtnSelectWords.get(i).mWordString.length() == 0) {
				return STATUS_ANSWER_LACK;
			}
		}

		// 答案完整，继续检查正确性
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mBtnSelectWords.size(); i++) {
			sb.append(mBtnSelectWords.get(i).mWordString);
		}

		return (sb.toString().equals(mCurrentMovie.getName())) ? STATUS_ANSWER_RIGHT
				: STATUS_ANSWER_WRONG;
	}

	/**
	 * 文字闪烁
	 */
	private void sparkTheWrods(final int color, final boolean pass) {
		// 定时器相关
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			boolean mChange = false;
			int mSpardTimes = 0;

			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (++mSpardTimes > SPASH_TIMES) {
							if (pass) {
								showPassView();
								timer.cancel();
							}
							return;
						}

						// 执行闪烁逻辑：交替显示红色和白色文字
						for (int i = 0; i < mBtnSelectWords.size(); i++) {
							mBtnSelectWords.get(i).mViewButton
									.setTextColor(mChange ? color : Color.WHITE);
						}
						mChange = !mChange;
					}
				});
			}
		};

		timer.schedule(task, 1, 150);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		OffersManager.getInstance(context).onAppExit();
		PointsManager.getInstance(context).unRegisterNotify(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.share_button_icon:
			Bitmap bitmap = Util
					.convertViewToBitmap(findViewById(R.id.main_frame));
			((MyApplication) getApplicationContext()).setShare(
					(Activity) context, null, bitmap, true);
			break;
		case R.id.btn_tip_answer:
			boolean isSuccess = PointsManager.getInstance(context).spendPoints(
					10);
			if (isSuccess) {
				getTips();
			}
			break;
		case R.id.btn_bar_back:
			finish();
			break;
		case R.id.btn_bar_add_coins:
			OffersManager.getInstance(context).showOffersWall();
			break;
		case R.id.passview_btnnext:
			mPassView.setVisibility(View.INVISIBLE);
			new next().execute();
			break;
		default:
			break;
		}
	}

	@Override
	public void onPointBalanceChange(int pointsBalance) {
		// TODO Auto-generated method stub
		mCurrentPoints.setText(pointsBalance + "");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void getTips() {
		char[] answers = mCurrentMovie.getNameCharacters();
		for (int i = 0; i < mBtnSelectWords.size(); i++) {
			if (mBtnSelectWords.get(i).mWordString.length() == 0) {
				// 设置答案文字框内容及可见性
				mBtnSelectWords.get(i).mViewButton.setText(answers[i] + "");
				mBtnSelectWords.get(i).mIsVisiable = true;
				mBtnSelectWords.get(i).mWordString = answers[i] + "";

				int checkResult = checkTheAnswer();

				// 检查答案
				if (checkResult == STATUS_ANSWER_RIGHT) {
					// 过关并获得奖励
					// Toast.makeText(this, "STATUS_ANSWER_RIGHT",
					// Toast.LENGTH_SHORT).show();
					handlePassEvent();
				} else if (checkResult == STATUS_ANSWER_WRONG) {
					// 闪烁文字并提示用户
					sparkTheWrods(Color.RED, true);
				} else if (checkResult == STATUS_ANSWER_LACK) {
				}

				// 设置待选框可见性
				// setButtonVisiable(wordButton, View.INVISIBLE);
				for (int j = 0; j < mAllWords.size(); j++) {
					if (mAllWords.get(j).mWordString.equals(answers[i] + "")) {
						mBtnSelectWords.get(i).mIndex = mAllWords.get(j).mIndex;
						setButtonVisiable(mAllWords.get(j), View.INVISIBLE);
						return;
					}
				}

				return;
			}
		}
	}

}

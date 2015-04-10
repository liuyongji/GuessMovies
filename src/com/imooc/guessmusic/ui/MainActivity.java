package com.imooc.guessmusic.ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imooc.guessmusic.R;
import com.imooc.guessmusic.model.IWordButtonClickListener;
import com.imooc.guessmusic.model.Movie;
import com.imooc.guessmusic.model.WordButton;
import com.imooc.guessmusic.myui.MyGridView;
import com.imooc.guessmusic.util.MyLog;
import com.imooc.guessmusic.util.SPUtils;
import com.imooc.guessmusic.util.ToastUtil;
import com.imooc.guessmusic.util.Util;

public class MainActivity extends Activity implements IWordButtonClickListener,
		OnClickListener,PointsChangeNotify {

	public final static String TAG = "MainActivity";
	private final static String STAGEINDEX = "stageindex";
	private final static String STAGEWORDS = "stagewords";
	private final static String ISFIRST="isfirst";
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
	private LinearLayout mViewWordsContainer;
	private TextView mCurrentStage,mCurrentPoints;
	private ImageButton btnshare, btngettip,btnbarback,btnbaraddcoin;

	// 当前的歌曲
	// private Song mCurrentSong;
	private Movie mCurrentMovie;

	// 当前关的索引
	private int mCurrentStageIndex = -1;
	private List<Movie> movies;
	private boolean mIsFirst;

	// BackgroundMusic backgroundMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// backgroundMusic = new BackgroundMusic(this);
		
		mCurrentStageIndex = (Integer) SPUtils.get(this, STAGEINDEX, 0);
		context = MainActivity.this;
		initView();
		initData();
		// 初始化游戏数据
		initCurrentStageData();
		AdManager.getInstance(context).init("3336b684c26b7540", "70229ffe9c877dfe");
		OffersManager.getInstance(context).onAppLaunch();
		PointsManager.getInstance(context).registerNotify(this);
		mIsFirst=(Boolean) SPUtils.get(this, ISFIRST, true);
		if (mIsFirst) {
			SPUtils.put(this, ISFIRST, false);
			PointsManager.getInstance(context).awardPoints(100);
		}
	}
	private void initView(){
		mViewPan = (ImageView) findViewById(R.id.imageView1);
		mMyGridView = (MyGridView) findViewById(R.id.gridview);

		// 注册监听
		mMyGridView.registOnWordButtonClick(this);

		mViewWordsContainer = (LinearLayout) findViewById(R.id.word_select_container);

		btnshare = (ImageButton) findViewById(R.id.share_button_icon);
		btngettip = (ImageButton) findViewById(R.id.btn_tip_answer);
		btnbarback=(ImageButton)findViewById(R.id.btn_bar_back);
		btnbaraddcoin=(ImageButton)findViewById(R.id.btn_bar_add_coins);
		btnbarback.setOnClickListener(this);
		btnbaraddcoin.setOnClickListener(this);
		btnshare.setOnClickListener(this);
		btngettip.setOnClickListener(this);
		mCurrentStage = (TextView) findViewById(R.id.text_current_stage);
		mCurrentPoints=(TextView)findViewById(R.id.txt_bar_coins);
		int myPointBalance = PointsManager.getInstance(context).queryPoints();
		mCurrentPoints.setText(myPointBalance+"");
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
			// Toast.makeText(this, "STATUS_ANSWER_RIGHT",
			// Toast.LENGTH_SHORT).show();
			handlePassEvent();
		} else if (checkResult == STATUS_ANSWER_WRONG) {
			// 闪烁文字并提示用户
			sparkTheWrods();
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
		// mPassView = (LinearLayout)this.findViewById(R.id.pass_view);
		// mPassView.setVisibility(View.VISIBLE);
		// mViewPan.clearAnimation();
		// backgroundMusic.pauseBackgroundMusic();
		SPUtils.put(MainActivity.this, STAGEINDEX, ++mCurrentStageIndex);
		SPUtils.remove(MainActivity.this, STAGEWORDS);
		ToastUtil.showLong(context,
				getResources().getString(R.string.answer_right));
		sparkTheWrodsSuccess();
		new next().execute();
	}

	class next extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	private void initData() {
		String result = Util.getFromAssets(MainActivity.this, "moviess.txt");
		Gson gson = new Gson();

		movies = gson.fromJson(result, new TypeToken<List<Movie>>() {
		}.getType());
		

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

				MyLog.d(TAG, mBtnSelectWords.get(i).mIndex + "");

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
		MyLog.d(TAG, button.mIsVisiable + "");
	}

	@Override
	public void onPause() {
		mViewPan.clearAnimation();

		super.onPause();
	}

	private Movie loadMovieInfo(int stageIndex) {

		if (stageIndex > movies.size()) {
			ToastUtil.showLong(this,
					getResources().getString(R.string.mission_compelete));
			stageIndex = 0;
		}

		Movie movie = movies.get(stageIndex);
		// String[] stage = null;
		mCurrentMovie = movie;
		mCurrentStage.setText(movie.getId() + "");
		mViewPan.setImageBitmap(Util.getImageFromAssetsFile(MainActivity.this,
				"images/" + movie.getUrl()));
		// if(mCurrentStageIndex>0){
		// new nextMusic().execute();
		// }

		return movie;
	}

	private void initCurrentStageData() {
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
			words[i] = getRandomChar() + "";
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
	 * 生成随机汉字 http://www.cnblogs.com/skyivben/archive/2012/10/20/2732484.html
	 * 
	 * @return
	 */
	private char getRandomChar() {
		String str = "";
		int hightPos;
		int lowPos;

		Random random = new Random();

		hightPos = (176 + Math.abs(random.nextInt(39)));
		lowPos = (161 + Math.abs(random.nextInt(93)));

		byte[] b = new byte[2];
		b[0] = (Integer.valueOf(hightPos)).byteValue();
		b[1] = (Integer.valueOf(lowPos)).byteValue();

		try {
			str = new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str.charAt(0);
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
	private void sparkTheWrods() {
		// 定时器相关
		TimerTask task = new TimerTask() {
			boolean mChange = false;
			int mSpardTimes = 0;

			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (++mSpardTimes > SPASH_TIMES) {
							return;
						}

						// 执行闪烁逻辑：交替显示红色和白色文字
						for (int i = 0; i < mBtnSelectWords.size(); i++) {
							mBtnSelectWords.get(i).mViewButton
									.setTextColor(mChange ? Color.RED
											: Color.WHITE);
						}

						mChange = !mChange;
					}
				});
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, 1, 150);
	}

	/**
	 * 文字闪烁
	 */
	private void sparkTheWrodsSuccess() {
		// 定时器相关
		TimerTask task = new TimerTask() {
			boolean mChange = false;
			int mSpardTimes = 0;

			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (++mSpardTimes > SPASH_TIMES) {
							return;
						}

						// 执行闪烁逻辑：交替显示红色和白色文字
						for (int i = 0; i < mBtnSelectWords.size(); i++) {
							mBtnSelectWords.get(i).mViewButton
									.setTextColor(mChange ? Color.GREEN
											: Color.WHITE);
						}

						mChange = !mChange;
					}
				});
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, 1, 150);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		OffersManager.getInstance(context).onAppExit();
		PointsManager.getInstance(context).unRegisterNotify(this);
	}

	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.share_button_icon:
			
			break;
		case R.id.btn_tip_answer:
			boolean isSuccess = PointsManager.getInstance(context).spendPoints(20);
			if (isSuccess) {
				
			}
			break;
		case R.id.btn_bar_back:
			finish();
			break;
		case R.id.btn_bar_add_coins:
			OffersManager.getInstance(context).showOffersWall();
			break;			
		default:
			break;
		}
	}
	@Override
	public void onPointBalanceChange(int pointsBalance) {
		// TODO Auto-generated method stub
		mCurrentPoints.setText(pointsBalance+"");
	}

}
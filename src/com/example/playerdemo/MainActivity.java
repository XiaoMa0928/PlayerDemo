package com.example.playerdemo;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String TAG = "AudioPlayActivity";
	private MediaPlayer mMediaPlayer;// 定义音频控件
	private int mPosition;// 记录音频文件播放的位置
	private TextView mTextView;
	private EditText mEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("音频测试V0.0.3");

		mTextView = (TextView) findViewById(R.id.text);
		mEditText = (EditText) findViewById(R.id.seek);

		mMediaPlayer = new MediaPlayer();
//		mMediaPlayer = MediaPlayer.create(this, R.raw.music);// 将音频文件放到里面
		try {
			mMediaPlayer.setDataSource("/sdcard/test.mp3");
			mMediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediaPlayer.setLooping(false);// 不循环播放

		mPosition = mMediaPlayer.getCurrentPosition();// 保存当前播放点
		// mMediaPlayer.seekTo(mPosition);
		mTextView.setText("开始处：" + mPosition + '\r' + '\n');
		mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);// 输入类型为数字

		Button playButton = (Button) this.findViewById(R.id.play);
		Button goButton = (Button) this.findViewById(R.id.go);
		ButtonClickListener listener = new ButtonClickListener();// 定义按键监听器
		playButton.setOnClickListener(listener);
		goButton.setOnClickListener(listener);

	}

	private final class ButtonClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			Button button = (Button) v;// 得到button
			try {
				switch (v.getId()) {// 通过传过来的button，可以判断button的类型
				case R.id.play:// 播放
					if (mMediaPlayer.isPlaying()) {
						stop();
						mPosition = mMediaPlayer.getCurrentPosition();// 保存当前播放点
						mTextView.setText(mTextView.getText() + "段落："
								+ mPosition + '\r' + '\n');
					} else {
						play();
					}
					break;
				case R.id.go:// 进入预设的时间
					play();// 必须先用play()初始化，不然会有错误产生，而是也无法调到设定的时间
					stop();
					Log.i(TAG, "按下Go键");
					Log.i(TAG, "设定的跳转位置是mPosition= " + mPosition);
					mTextView.setText(mTextView.getText() + "设定点："
							+ (mEditText.getText().toString()) + '\r' + '\n');
					mMediaPlayer.seekTo(Integer.parseInt((mEditText.getText()
							.toString())));
					// mMediaPlayer.seekTo(mPosition);
					// mMediaPlayer.seekTo(30000);//调到30s
					Log.i(TAG,
							"跳转到的位置是mPosition= "
									+ mMediaPlayer.getCurrentPosition());
//					play();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	}

	private void play() throws IOException { // 此过程可能抛出异常
		// TODO Auto-generated method stub
		mMediaPlayer.start();// 播放
	}

	private void stop() throws IOException { // 此过程可能抛出异常
		// TODO Auto-generated method stub
		mMediaPlayer.pause();
	}

	// 关于播放位置的确定
	/*
	 * mPosition = mMediaPlayer.getCurrentPosition();//保存当前播放点
	 * mMediaPlay.seekTo(mPosition);
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// /getMenuInflater().inflate(R.menu.activity_main, menu);
		// /return true;
		super.onCreateOptionsMenu(menu);
		Log.i(TAG, "创建菜单");
		menu.add(0, 1, 1, "播放音频");
		menu.add(0, 2, 2, "目录");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		Log.i(TAG, "准备菜单");
		menu.clear();
		if (mMediaPlayer.isPlaying()) {
			menu.add(0, 1, 1, "暂停音频");
			menu.add(0, 2, 2, "目录");
		} else {
			menu.add(0, 1, 1, "播放音频");
			menu.add(0, 2, 2, "目录1");
		}

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) { // 选择“播放音频”
			Log.i(TAG, "菜单选择");
			try {
				// Log.i(TAG,mMediaPlayer.isPlaying()+"");
				if (mMediaPlayer.isPlaying()) {
					stop();
					Log.i(TAG, "菜单选择");
				} else
					play();

			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

		} else if (item.getItemId() == 2) { // 选择“目录”

		}
		return false;
	}

	@Override
	protected void onDestroy() {
		mMediaPlayer.release();
		super.onDestroy();
		Log.i(TAG, "OnDestroy");
	}

}

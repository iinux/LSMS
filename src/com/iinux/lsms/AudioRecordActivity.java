package com.iinux.lsms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.content.Context;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioRecordActivity extends Activity {
	private static String mFileName = null;
	// ¼����ť
	private RecordButton mRecordButton = null;
	private MediaRecorder mRecorder = null;
	// �طŰ�ť
	// private PlayButton mPlayButton = null;
	private MediaPlayer mPlayer = null;

	// ��¼����ť��clickʱ���ô˷�������ʼ��ֹͣ¼��
	private void onRecord(boolean start) {
		if (start) {
			startRecording();
		} else {
			stopRecording();
		}
	}

	// �����Ű�ť��clickʱ���ô˷�������ʼ��ֹͣ����
	private void onPlay(boolean start) {
		if (start) {
			startPlaying();
		} else {
			stopPlaying();
		}
	}

	private void startPlaying() {
		mPlayer = new MediaPlayer();
		try {
			// ����Ҫ���ŵ��ļ�
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			// ����֮
			mPlayer.start();
		} catch (Exception e) {
			General.out(AudioRecordActivity.this, "AudioRecordTest:prepare() failed", LogLevel.DEBUG);
			General.out(AudioRecordActivity.this, e, LogLevel.EXCEPTION);
		}
	}

	// ֹͣ����
	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	private void startRecording() {
		mRecorder = new MediaRecorder();
		// ������ԴΪMicphone
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// ���÷�װ��ʽ
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		// ���ñ����ʽ
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (Exception e) {
			General.out(AudioRecordActivity.this,
					"AudioRecordTest:prepare() failed", LogLevel.DEBUG);
			General.out(AudioRecordActivity.this, e, LogLevel.EXCEPTION);
		}

		mRecorder.start();
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	// ����¼����ť
	class RecordButton extends Button {
		boolean mStartRecording = true;

		OnClickListener clicker = new OnClickListener() {
			public void onClick(View v) {
				onRecord(mStartRecording);
				if (mStartRecording) {
					setText("Stop recording");
				} else {
					setText("Start recording");
				}
				mStartRecording = !mStartRecording;
			}
		};

		public RecordButton(Context ctx) {
			super(ctx);
			setText("Start recording");
			setOnClickListener(clicker);
		}
	}

	// ���岥�Ű�ť
	class PlayButton extends Button {
		boolean mStartPlaying = true;

		OnClickListener clicker = new OnClickListener() {
			public void onClick(View v) {
				onPlay(mStartPlaying);
				if (mStartPlaying) {
					setText("Stop playing");
				} else {
					setText("Start playing");
				}
				mStartPlaying = !mStartPlaying;
			}
		};

		public PlayButton(Context ctx) {
			super(ctx);
			setText("Start playing");
			setOnClickListener(clicker);
		}
	}

	// ���췽��
	@SuppressLint("SimpleDateFormat")
	public AudioRecordActivity() {
		mFileName = General.getAppPath();
		{
			File t = new File(mFileName);
			if (!t.exists()) {
				t.mkdir();
			}
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str = formatter.format(curDate);
		mFileName = mFileName + "/" + str + ".3gp";
		General.out(AudioRecordActivity.this, "AudioRecordTest:mFileName is "
				+ mFileName, LogLevel.DEBUG);
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// �������
		LinearLayout ll = new LinearLayout(this);
		mRecordButton = new RecordButton(this);
		ll.addView(mRecordButton, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 0));
		// mPlayButton = new PlayButton(this);
		// ll.addView(mPlayButton,
		// new LinearLayout.LayoutParams(
		// ViewGroup.LayoutParams.WRAP_CONTENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT,
		// 0));
		setContentView(ll);
	}

	@Override
	public void onPause() {
		super.onPause();
		// Activity��ͣʱ�ͷ�¼���Ͳ��Ŷ���
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}

		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
}
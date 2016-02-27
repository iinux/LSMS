package com.iinux.lsms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class MyReceiver extends BroadcastReceiver {
	public static MediaPlayer mMediaPlayer = null;
	public static StringBuilder sbInfo;
	public static int oldVolume = 7;
	public static AudioManager am;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null || intent.getAction() == null) {
			return;
		}
		SQLOperation mySQLOperation = new SQLOperation(context);
		String action = intent.getAction();

		if (mySQLOperation.settingSwitch(0, "4gsniffer")) {
			setNetworkBy4GSniffer(context);
		}

		if ("android.provider.Telephony.SMS_RECEIVED".compareToIgnoreCase(action) == 0
				&& mySQLOperation.settingSwitch(0, "switch")) {
			Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
			SmsMessage[] messages = new SmsMessage[pduArray.length];
			for (int i = 0; i < pduArray.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
			}
			sbInfo = new StringBuilder();

			ArrayList<String> whiteList = new ArrayList<String>();
			whiteList.add("18233135133");
			whiteList.add("18131063511");
			whiteList.add("18231190658");
			whiteList.add("15233225601");
			whiteList.add("15233226591");
			whiteList.add("18232006241");
			whiteList.add("15101717230");

			for (SmsMessage cur : messages) {
				sbInfo.append("from：");
				sbInfo.append(cur.getDisplayOriginatingAddress());
				sbInfo.append("\n");
				sbInfo.append("body：");
				// sb.append(cur.getDisplayMessageBody());

				for (String s : whiteList) {
					if ((cur.getDisplayOriginatingAddress().indexOf(s) != -1)
							|| (cur.getDisplayMessageBody().indexOf("兰") != -1)) {

						startAlert(context);
						break;
					}
				}

			}
		} else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
			General.out(null, "system boot completed", LogLevel.DEBUG);
		} else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
		} /*else if (action.equals(Intent.ACTION_TIME_TICK)) {
			Toast.makeText(context, "又一分钟过去了", Toast.LENGTH_LONG).show();
		}else if ("com.android.phone.NotificationMgr.MissedCall_intent".compareToIgnoreCase(action)==0) {
			sbInfo = new StringBuilder();
			String mMissCallCount = intent.getExtras().getString("MissedCallNumber");
			sbInfo.append(mMissCallCount); startAlert(context);
		}*/
		 
	}

	private void setNetworkBy4GSniffer(Context context) {
		ConnectivityManager connectivityManager;
		NetworkInfo info;
		connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			String name = info.getSubtypeName();
			General.out(context, "当前网络名称：" + name, LogLevel.TOAST);
			try {
				if (name.equals("WIFI") || name.equals("LTE")) {
					General.out(context, "即将打开移动网络", LogLevel.TOAST);
					setMobileData(connectivityManager, true);
				} else {
					General.out(context, "即将关闭移动网络", LogLevel.TOAST);
					setMobileData(connectivityManager, false);
				}
			} catch (Exception e) {
				General.out(context, e, LogLevel.EXCEPTION);
			}
		} else {
			General.out(context, "没有可用网络", LogLevel.TOAST);
		}
	}

	/**
	 * 设置手机的移动数据
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void setMobileData(ConnectivityManager mConnectivityManager,
			boolean pBoolean) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Class ownerClass = mConnectivityManager.getClass();
		Class[] argsClass = new Class[1];
		argsClass[0] = boolean.class;
		Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);
		method.invoke(mConnectivityManager, pBoolean);
	}

	/**
	 * 返回手机移动数据的状态
	 * 
	 * @param pContext
	 * @param arg
	 *            默认填null
	 * @return true 连接 false 未连接
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static boolean getMobileDataState(
			ConnectivityManager mConnectivityManager, Object[] arg) {
		try {
			Class ownerClass = mConnectivityManager.getClass();
			Class[] argsClass = null;
			if (arg != null) {
				argsClass = new Class[1];
				argsClass[0] = arg.getClass();
			}
			Method method = ownerClass.getMethod("getMobileDataEnabled",
					argsClass);
			Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);
			return isOpen;
		} catch (Exception e) {
			General.out(null, "得到移动数据状态出错", LogLevel.DEBUG);
			return false;
		}
	}

	private void startAlert(Context context) {
		Intent newIntent = new Intent(context, MainActivity.class);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newIntent);

		try {
			mMediaPlayer = new MediaPlayer();
			/* 重置MediaPlayer */
			mMediaPlayer.reset();
			/* 设置要播放的文件的路径 */
			mMediaPlayer.setDataSource(General.voiceFile);
			/* 准备播放 */
			mMediaPlayer.prepare();
			/* 调整音量 */
			am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			oldVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, 11,AudioManager.FLAG_PLAY_SOUND);
			/* 开始播放 */
			mMediaPlayer.start();
		} catch (Exception e) {
			General.out(context, e, LogLevel.EXCEPTION);
		}
	}

	protected void stopAlert() {

	}

	public void onReceive2(Context context, Intent intent) {
		General.out(context, "CallReceiver Start...", LogLevel.DEBUG);
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		CallListener customPhoneListener = new CallListener(context);

		telephony.listen(customPhoneListener,PhoneStateListener.LISTEN_CALL_STATE);

		Bundle bundle = intent.getExtras();
		String phoneNr = bundle.getString("incoming_number");
		General.out(context, "CallReceiver Phone Number : " + phoneNr, LogLevel.DEBUG);
	}
}

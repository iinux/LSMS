package com.iinux.lsms;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallListener extends PhoneStateListener {
	private static int lastetState = TelephonyManager.CALL_STATE_IDLE; // 最后的状态
	@SuppressWarnings("unused")
	private Context context;
 
	public CallListener(Context context) {
		super();
		this.context = context;
	}
 
	public void onCallStateChanged(int state, String incomingNumber) {
		Log.v(General.LogTag, "CallListener call state changed : " + incomingNumber);
		// 如果当前状态为空闲,上次状态为响铃中的话,则破觚为认为是未接来电
		if(lastetState ==  TelephonyManager.CALL_STATE_RINGING 
		        && state == TelephonyManager.CALL_STATE_IDLE){
			sendSmgWhenMissedCall(incomingNumber);
		}
 
		// 最后的时候改变当前值
		lastetState = state;
	}
 
	private void sendSmgWhenMissedCall(String incomingNumber) {
	     // ... 进行未接来电处理(发短信,发email等等通知)
	}
}

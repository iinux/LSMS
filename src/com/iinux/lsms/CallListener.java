package com.iinux.lsms;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallListener extends PhoneStateListener {
	private static int lastetState = TelephonyManager.CALL_STATE_IDLE; // ����״̬
	@SuppressWarnings("unused")
	private Context context;
 
	public CallListener(Context context) {
		super();
		this.context = context;
	}
 
	public void onCallStateChanged(int state, String incomingNumber) {
		Log.v(General.LogTag, "CallListener call state changed : " + incomingNumber);
		// �����ǰ״̬Ϊ����,�ϴ�״̬Ϊ�����еĻ�,������Ϊ��Ϊ��δ������
		if(lastetState ==  TelephonyManager.CALL_STATE_RINGING 
		        && state == TelephonyManager.CALL_STATE_IDLE){
			sendSmgWhenMissedCall(incomingNumber);
		}
 
		// ����ʱ��ı䵱ǰֵ
		lastetState = state;
	}
 
	private void sendSmgWhenMissedCall(String incomingNumber) {
	     // ... ����δ�����紦��(������,��email�ȵ�֪ͨ)
	}
}

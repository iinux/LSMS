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
import android.util.Log;
import android.widget.Toast;


public class MyReceiver extends BroadcastReceiver{
	public static MediaPlayer mMediaPlayer=null;
	public static StringBuilder sbInfo;
	public static int oldVolume=7;
	public static AudioManager am;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent==null || intent.getAction()==null){
			return;
		}
		SQLOperation mySQLOperation = new SQLOperation(context);
        String action = intent.getAction();
        
    	if(mySQLOperation.switch_4GSniffer(0)){
        	setNetworkBy4GSniffer(context);
    	}
    	
		if("android.provider.Telephony.SMS_RECEIVED".compareToIgnoreCase(action)==0&&mySQLOperation.lsmsSwitch(0))
		{
			Object[]pduArray= (Object[]) intent.getExtras().get("pdus");
			SmsMessage[] messages = new SmsMessage[pduArray.length];
			for (int i = 0; i<pduArray.length; i++) {
				messages[i] = SmsMessage.createFromPdu ((byte[])pduArray [i]);
			}
			sbInfo = new StringBuilder();
			
			ArrayList<String> whiteList=new ArrayList<String>();
			whiteList.add("18233135133");
			whiteList.add("18131063511");
			whiteList.add("18231190658");
			whiteList.add("15233225601");
			whiteList.add("15233226591");
			whiteList.add("18232006241");
			whiteList.add("15101717230");
			
			for (SmsMessage cur:messages)
			{
				sbInfo.append("from��");
				sbInfo.append(cur.getDisplayOriginatingAddress());
				sbInfo.append("\n");
				sbInfo.append("body��");
//				sb.append(cur.getDisplayMessageBody());
				
				for(String s : whiteList){
					if ((cur.getDisplayOriginatingAddress().indexOf(s)!=-1)||(cur.getDisplayMessageBody().indexOf("��")!=-1)){

						startAlert(context);
						break;
					}
				}
				
			}
		}else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("BootReceiver", "system boot completed");
        }else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
        }/*else if (action.equals(Intent.ACTION_TIME_TICK)) {
        	Toast.makeText(context, "��һ���ӹ�ȥ��", Toast.LENGTH_LONG).show();
        }else if ("com.android.phone.NotificationMgr.MissedCall_intent".compareToIgnoreCase(action)==0) {
			sbInfo = new StringBuilder();
            String mMissCallCount = intent.getExtras().getString("MissedCallNumber");
            sbInfo.append(mMissCallCount);
            startAlert(context);
        }*/
	}
	private void setNetworkBy4GSniffer(Context context){
        ConnectivityManager connectivityManager;
        NetworkInfo info;
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        info = connectivityManager.getActiveNetworkInfo();  
        if(info != null && info.isAvailable()) {
            String name = info.getSubtypeName();
            Toast.makeText(context, "��ǰ�������ƣ�" + name, Toast.LENGTH_SHORT).show();
        	try {
                if(name.equals("WIFI")||name.equals("LTE")){
                    Toast.makeText(context, "�������ƶ�����", Toast.LENGTH_SHORT).show();
					setMobileData(connectivityManager,true);
                }else{
                    Toast.makeText(context, "�����ر��ƶ�����", Toast.LENGTH_SHORT).show();
                	setMobileData(connectivityManager,false);
                }
			} catch (Exception e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
        } else {
            Toast.makeText(context, "û�п�������", Toast.LENGTH_SHORT).show();
        }
	}
	/** 
	 * �����ֻ����ƶ����� 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchMethodException 
	 */  
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void setMobileData(ConnectivityManager mConnectivityManager, boolean pBoolean) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {  
        Class ownerClass = mConnectivityManager.getClass();  
        Class[] argsClass = new Class[1];  
        argsClass[0] = boolean.class;  
        Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);  
        method.invoke(mConnectivityManager, pBoolean);  
	}  
	  
	/** 
	 * �����ֻ��ƶ����ݵ�״̬ 
	 * 
	 * @param pContext 
	 * @param arg 
	 *            Ĭ����null 
	 * @return true ���� false δ���� 
	 */  
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static boolean getMobileDataState(ConnectivityManager mConnectivityManager, Object[] arg) {  
	    try {  
	        Class ownerClass = mConnectivityManager.getClass();  
	        Class[] argsClass = null;  
	        if (arg != null) {  
	            argsClass = new Class[1];  
	            argsClass[0] = arg.getClass();  
	        }  
	        Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);  
	        Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);  
	        return isOpen;  
	    } catch (Exception e) {  
	        System.out.println("�õ��ƶ�����״̬����");  
	        return false;  
	    }  
	}
	private void startAlert(Context context){
		Intent newIntent = new Intent(context,MainActivity.class);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newIntent); 
		
		try {
			mMediaPlayer = new MediaPlayer();
			/* ����MediaPlayer */
			mMediaPlayer.reset();
			/* ����Ҫ���ŵ��ļ���·�� */
			mMediaPlayer.setDataSource(General.voiceFile);
			/* ׼������ */
			mMediaPlayer.prepare();
			/* ��������*/
			am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			oldVolume=am.getStreamVolume(AudioManager.STREAM_MUSIC);
			am.setStreamVolume(AudioManager.STREAM_MUSIC, 11, AudioManager.FLAG_PLAY_SOUND);
			/* ��ʼ���� */
			mMediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	protected void stopAlert(){
		
	}
	public void onReceive2(Context context, Intent intent) {
		Log.i(General.LogTag, "CallReceiver Start...");
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		CallListener customPhoneListener = new CallListener(context);
 
		telephony.listen(customPhoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);
 
		Bundle bundle = intent.getExtras();
		String phoneNr = bundle.getString("incoming_number");
		Log.i(General.LogTag, "CallReceiver Phone Number : " + phoneNr);
	}
}

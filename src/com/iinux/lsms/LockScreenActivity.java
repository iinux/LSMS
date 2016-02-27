package com.iinux.lsms;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;

public class LockScreenActivity extends Activity{
	private DevicePolicyManager mDevicepolicymanager;
	private ComponentName mComponentname;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		General.out(LockScreenActivity.this, "enter MyLcokScreen onCreate!", LogLevel.DEBUG);
		//��ȡ�豸�������
		mDevicepolicymanager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mComponentname = new ComponentName(this, MyDeviceAdminMonitor.class);
		/*
		* �������ж��Ƿ���Ȩ�ޣ����û�������activeManage()��Ȼ��������������finish()��
		* ��������������ģ���ΪactiveManage()���ܻ��ڵȴ���һ��Activity�Ľ������ô��ʱ��Ȼû��Ȩ��ȴ
		* ִ����lockNow()�������ͳ����ˡ�
		* ��������2����
		* 1������дOnActivityResult()�������������ж��Ƿ��ȡȨ�޳ɹ�������������finish()
		* �����������activeManage()��ȡȨ�ޣ��������������������Ч���ܺã�
		* 2������дOnActivityResult()��������һ�λ�ȡȨ�޺�����������finish()��������������˵Ҳ����
		* ʧ�ܣ�����Ȩ�޻�û��ȡ�þ�finish�ˣ����������ͻص����棬�����ٰ�һ��������������
		* �����Ƽ���һ�ַ�����*/

		//�ж��Ƿ�������Ȩ�ޣ����������������������Լ�����û�����ȡȨ��
		if (mDevicepolicymanager.isAdminActive(mComponentname)) {
			General.out(LockScreenActivity.this, "MyLockScreen:isAdminActive", LogLevel.DEBUG);
	        mDevicepolicymanager.lockNow();
	        
			for (int i = 0; i < 10; i++) {
				SystemClock.sleep(1000);
				PowerManager manager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
				if (manager.isScreenOn()) {
					mDevicepolicymanager.lockNow();
				}else{
					break;
				}
			}

	        this.finish();  
	    } else {//��һ�����г���
	    	General.out(LockScreenActivity.this, "MyLockScreen:not isAdminActive", LogLevel.DEBUG);
	        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);  
	        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mComponentname);  
	        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"One key lock screen need to active");  
	        startActivityForResult(intent, RESULT_OK);  
	    }
	}
}

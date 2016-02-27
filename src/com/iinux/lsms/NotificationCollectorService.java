package com.iinux.lsms;

import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationCollectorService extends NotificationListenerService {

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		Log.i("zpf", "open"+"-----"+sbn.toString());
		Intent dialogIntent = new Intent(getBaseContext(), NotificationCollectorActivity.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//用Bundle携带数据
	    Bundle bundle=new Bundle();
	    //传递name参数为tinyphp
	    bundle.putString("name", "tinyphp");
	    dialogIntent.putExtras(bundle);
		getApplication().startActivity(dialogIntent);
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		Log.i("zpf", "shut"+"-----"+sbn.toString());
		Intent dialogIntent = new Intent(getBaseContext(), NotificationCollectorActivity.class);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//用Bundle携带数据
	    Bundle bundle=new Bundle();
	    //传递name参数为tinyphp
	    bundle.putString("name", "tinyphp");
	    dialogIntent.putExtras(bundle);
		getApplication().startActivity(dialogIntent);
	}

}

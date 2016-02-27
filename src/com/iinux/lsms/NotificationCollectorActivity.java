package com.iinux.lsms;

import android.app.Activity;
import android.os.Bundle;

public class NotificationCollectorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String name;
        if (bundle==null)name="";
        else name= bundle.getString("name");
        General.out(this, name, LogLevel.DEBUG);
	}

}

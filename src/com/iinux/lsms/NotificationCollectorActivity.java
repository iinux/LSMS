package com.iinux.lsms;

import android.app.Activity;
import android.os.Bundle;

public class NotificationCollectorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//��ҳ���������
        Bundle bundle = this.getIntent().getExtras();
        //����nameֵ
        String name;
        if (bundle==null)name="";
        else name= bundle.getString("name");
        General.out(this, name, LogLevel.DEBUG);
	}

}

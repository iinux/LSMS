package com.iinux.lsms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SQLOperation {
	Mydatahelper dbhelper;
	Context context;
	SQLiteDatabase sqdb;
	SQLOperation(Context context){
		this.context=context;
		dbhelper=new Mydatahelper(context, "setting.db",1);
		sqdb=dbhelper.getReadableDatabase();
	}

	//op
	//0 query
	//1 set true
	//2 set false
	boolean settingSwitch(int op, String key){
		Cursor anser;
		int anserIndex;
		if(op == 0){
			anser= sqdb.rawQuery("select f_value from t_setting where f_key='" + key + "'", null);
			if(anser.getCount() < 1){
				ContentValues cv = new ContentValues();

		        cv.put("f_key", key);
		        cv.put("f_value", "true");

		        sqdb.insert("t_setting", null, cv);
				return true;
			}
			try{
				while (anser.moveToNext()) {
					anserIndex = anser.getColumnIndex("f_value");
					String f_value = anser.getString(anserIndex);
					if(f_value.equals("false")){
						return false;
					}else{
						return true;
					}
				}
				anser.close();
			}catch(Exception e){
				General.out(context, e, LogLevel.EXCEPTION);
			}
		}else if(op==1){
			ContentValues values = new ContentValues();
			values.put("f_value", "true");//key为字段名，value为值
			sqdb.update("t_setting", values, "f_key=?", new String[]{key}); 
			sqdb.close();
		}else if(op==2){
			ContentValues values = new ContentValues();
			values.put("f_value", "false");//key为字段名，value为值
			sqdb.update("t_setting", values, "f_key=?", new String[]{key}); 
			sqdb.close();
		}
		return true;
	}
	public void popSettingCheck(final String key, Activity activity){
		final ToggleButton tb = new ToggleButton(context);
		tb.setChecked(settingSwitch(0, key));
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (isChecked) {
					settingSwitch(1, key);
				} else {
					settingSwitch(2, key);
				}
			}
		});
		AlertDialog dlg = new AlertDialog.Builder(activity).setTitle(key + "开关").setView(tb).create();
		dlg.show();
	}
}
class Mydatahelper extends SQLiteOpenHelper{
	public Mydatahelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table t_setting(_id integer primary key autoincrement,f_key varchar(50),f_value varchar(50))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}
}

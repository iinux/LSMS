package com.iinux.lsms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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
	boolean lsmsSwitch(int op){
		Cursor anser;
		int anserIndex;
		if(op==0){
			anser= sqdb.rawQuery("select f_value from t_setting where f_key='switch'", null);
			if(anser.getCount()<1){
				ContentValues cv=new ContentValues();

		        cv.put("f_key", "switch");
		        cv.put("f_value", "true");

		        sqdb.insert("t_setting", null, cv);
				return true;
			}
			try{
				while (anser.moveToNext()) {
					anserIndex = anser.getColumnIndex("f_value");
					String f_value=anser.getString(anserIndex);
					if(f_value.equals("false")){
						return false;
					}else{
						return true;
					}
				}
				anser.close();
			}catch(Exception e){
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}else if(op==1){
			ContentValues values = new ContentValues();
			values.put("f_value", "true");//key为字段名，value为值
			sqdb.update("t_setting", values, "f_key=?", new String[]{"switch"}); 
			sqdb.close();
		}else if(op==2){
			ContentValues values = new ContentValues();
			values.put("f_value", "false");//key为字段名，value为值
			sqdb.update("t_setting", values, "f_key=?", new String[]{"switch"}); 
			sqdb.close();
		}
		return true;
	}
	//op
	//0 query
	//1 set true
	//2 set false
	boolean switch_4GSniffer(int op){
		Cursor anser;
		int anserIndex;
		if(op==0){
			anser= sqdb.rawQuery("select f_value from t_setting where f_key='4gsniffer'", null);
			if(anser.getCount()<1){
				ContentValues cv=new ContentValues();

		        cv.put("f_key", "4gsniffer");
		        cv.put("f_value", "true");

		        sqdb.insert("t_setting", null, cv);
				return true;
			}
			try{
				while (anser.moveToNext()) {
					anserIndex = anser.getColumnIndex("f_value");
					String f_value=anser.getString(anserIndex);
					if(f_value.equals("false")){
						return false;
					}else{
						return true;
					}
				}
				anser.close();
			}catch(Exception e){
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}else if(op==1){
			ContentValues values = new ContentValues();
			values.put("f_value", "true");//key为字段名，value为值
			sqdb.update("t_setting", values, "f_key=?", new String[]{"4gsniffer"}); 
			sqdb.close();
		}else if(op==2){
			ContentValues values = new ContentValues();
			values.put("f_value", "false");//key为字段名，value为值
			sqdb.update("t_setting", values, "f_key=?", new String[]{"4gsniffer"}); 
			sqdb.close();
		}
		return true;
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

package com.iinux.lsms;

import com.github.stepinto.asshd.SSHDActivity;
import com.iinux.lsms.ScreenListener.ScreenStateListener;
import com.iinux.lsms.R;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	public static MainActivity ma;
	private Button btnStop,btnCreateShortcut,btnExit,btnQR;
	private TextView tvFrom;
	private ScreenListener screenListener;
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Toast.makeText(this, "我退出了哦", Toast.LENGTH_SHORT).show();
		MainActivity.ma.finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(General.LogTag, "MainActivity onCreate,action is "+this.getIntent().getAction());
		ma=this;
		setContentView(R.layout.activity_main);
		btnStop=(Button) findViewById(R.id.btnStop);
		tvFrom=(TextView)findViewById(R.id.tvFrom);
		btnCreateShortcut=(Button)findViewById(R.id.btnCreateShortcut);
		btnExit=(Button)findViewById(R.id.btnExit);
		btnQR=(Button)findViewById(R.id.btnQR);
		
		btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		        myFinish();
			}
		});
		btnCreateShortcut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		        createShortCut();
			}
		});
		btnExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.ma.finish();
			}
		});
		btnQR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(MainActivity.ma,QRActivity.class);
				startActivity(i);
			}
		});
		
		if (MyReceiver.sbInfo!=null) tvFrom.setText(MyReceiver.sbInfo);
		
		screenListener = new ScreenListener(this);
        screenListener.begin(new ScreenStateListener() {
            @Override
            public void onUserPresent() {
                Log.i(General.LogTag, "onUserPresent");
//                myFinish();
            }
            @Override
            public void onScreenOn() {
                Log.i(General.LogTag, "onScreenOn");
                myFinish();
            }
            @Override
            public void onScreenOff() {
              Log.i(General.LogTag, "onScreenOff");
//            	myFinish();
            }
        });
	}
	private double latitude=0.0;
	private double longitude =0.0;
	private void getTude(){
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(location != null){
				Log.i(General.LogTag,"locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) is true,get location is not null");
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}else{
				Log.i(General.LogTag,"locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) is true,get location is null");
			}
		}else{
			LocationListener locationListener = new LocationListener() {
				// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
					
				}
				
				// Provider被enable时触发此函数，比如GPS被打开
				@Override
				public void onProviderEnabled(String provider) {
					
				}
				
				// Provider被disable时触发此函数，比如GPS被关闭 
				@Override
				public void onProviderDisabled(String provider) {
					
				}
				
				//当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发 
				@Override
				public void onLocationChanged(Location location) {
					if (location != null) {   
						Log.e("Map", "Location changed : Lat: "  
						+ location.getLatitude() + " Lng: "  
						+ location.getLongitude());   
					}
				}
			};
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);   
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(location != null){   
				latitude = location.getLatitude(); //经度
				longitude = location.getLongitude(); //纬度
			}
		}
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
		criteria.setAltitudeRequired(false);//不要求海拔
		criteria.setBearingRequired(false);//不要求方位
		criteria.setCostAllowed(true);//允许有花费
		criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
		//从可用的位置提供器中，匹配以上标准的最佳提供器
		String provider = locationManager.getBestProvider(criteria, true);
		//获得最后一次变化的位置
		Location location = locationManager.getLastKnownLocation(provider);
		if(location != null){   
			latitude = location.getLatitude(); //经度
			longitude = location.getLongitude(); //纬度
		}
		
		MyReceiver.sbInfo=new StringBuilder();
		MyReceiver.sbInfo.append("Message:\n");
		MyReceiver.sbInfo.append("经度："+latitude+"\n");
		MyReceiver.sbInfo.append("纬度："+longitude+"\n");
		if (MyReceiver.sbInfo!=null) tvFrom.setText(MyReceiver.sbInfo);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0,Menu.FIRST,0,"显示经纬度");
		menu.add(0,Menu.FIRST+1,0,"开关");
		menu.add(0,Menu.FIRST+2,0,"录音");
		menu.add(0,Menu.FIRST+3,0,"4GSniffer开关");
		menu.add(0,Menu.FIRST+4,0,"SSHD");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(General.LogTag,"onOptionsItemSelected item id is "+item.getItemId());
		switch(item.getItemId())
		{
		  case Menu.FIRST:
			  getTude();
		      break;
		  case Menu.FIRST+1:
				final ToggleButton tbChecked=new ToggleButton(this);
		  		tbChecked.setChecked(new SQLOperation(getApplicationContext()).lsmsSwitch(0));
			    tbChecked.setOnCheckedChangeListener(new OnCheckedChangeListener(){
		            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
		            	if(isChecked){
		            		new SQLOperation(getApplicationContext()).lsmsSwitch(1);
		            	}else{
		            		new SQLOperation(getApplicationContext()).lsmsSwitch(2);
		            	}
		            }
	
		        });
		        final AlertDialog dlg = new AlertDialog.Builder(this)
		            .setTitle("开关")
		            .setView(tbChecked)
		            .create();
		        dlg.show();
				break;
		  case Menu.FIRST+2:
			  Intent i=new Intent(MainActivity.ma,AudioRecordActivity.class);
			  startActivity(i);
			  break;
		  case Menu.FIRST+3:
				final ToggleButton tb_4gsniffer=new ToggleButton(this);
		  		tb_4gsniffer.setChecked(new SQLOperation(getApplicationContext()).switch_4GSniffer(0));
		  		tb_4gsniffer.setOnCheckedChangeListener(new OnCheckedChangeListener(){
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	            	if(isChecked){
	            		new SQLOperation(getApplicationContext()).switch_4GSniffer(1);
	            	}else{
	            		new SQLOperation(getApplicationContext()).switch_4GSniffer(2);
	            	}
	            }

	        });
	        final AlertDialog dlg_4gsniffer = new AlertDialog.Builder(this)
	            .setTitle("4GSniffer开关")
	            .setView(tb_4gsniffer)
	            .create();
	        dlg_4gsniffer.show();
			break;
		  case Menu.FIRST+4:
			  i=new Intent(MainActivity.ma,SSHDActivity.class);
			  startActivity(i);
			  break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void myFinish(){
		if (MyReceiver.mMediaPlayer!=null&&MyReceiver.mMediaPlayer.isPlaying())   
        {   
			//重置MediaPlayer到初始状态   
			MyReceiver.mMediaPlayer.stop();
			MyReceiver.mMediaPlayer.release();
			MyReceiver.mMediaPlayer=null;
			say("LSMS",true);
			MyReceiver.am.setStreamVolume(AudioManager.STREAM_MUSIC, MyReceiver.oldVolume, AudioManager.FLAG_PLAY_SOUND);
		}
	}
	/**
     * 创建快捷图标
     */
    private void createShortCut() {
        // 先判断该快捷是否存在
        if (!isExist()) {
            Intent intent = new Intent();
            // 指定动作名称
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            // 指定快捷方式的图标
            Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            // 指定快捷方式的名称
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Lock Screen");
            // 指定快捷图标激活哪个activity
            Intent i = new Intent(this,LockScreenActivity.class);
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
            
            sendBroadcast(intent);
            say("成功创建快捷方式！",false);
        }else{
        	say("快捷方式已存在！",false);
        }
    }
    /**
     * 判断快捷图标是否在数据库中已存在
     */
    private boolean isExist() {
        boolean isExist = false;
        int version = getSdkVersion();
        Uri uri = null;
        if (version < 2.0) {
            uri = Uri.parse("content://com.android.launcher.settings/favorites");
        } else {
            uri = Uri.parse("content://com.android.launcher2.settings/favorites");
        }
        String selection = " title = ?";
        String[] selectionArgs = new String[] { "Lock Screen" };
        Cursor c = getContentResolver().query(uri, null, selection, selectionArgs, null);

        if (c != null && c.getCount() > 0) {
            isExist = true;
        }

        if (c != null) {
            c.close();
        }

        return isExist;
    }

    /**
     * 得到当前系统SDK版本
     */
    private int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }
    private void say(String s,boolean l){
    	Toast toast;
    	if (l){
    		toast = Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG);
    	}else{
    		toast = Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT);
    	}
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
    }
//    int count = -1;
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//  
//        int action = event.getAction();
//  
//        if (action ==KeyEvent.ACTION_DOWN) {
//        	tvFrom.setText("+++++++++ACTION_DOWN++++++"+ count++);
//            return true;
//        }
//  
//        if (action== KeyEvent.ACTION_UP) {
//        	tvFrom.setText("+++++ACTION_UP++++++++++");
//            return true;
//        }
//  
//        return super.dispatchKeyEvent(event);
//    }
  
  
  
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//  
//        case KeyEvent.KEYCODE_VOLUME_DOWN:
//  
//        	  tvFrom.setText("-----------------"+count);
//            count--;
//  
//            return true;
//  
//        case KeyEvent.KEYCODE_VOLUME_UP:
//        	  tvFrom.setText("++++++++++++++++"+ count);
//            count++;
//            return true;
//        case KeyEvent.KEYCODE_VOLUME_MUTE:
//        	  tvFrom.setText("MUTE");
//  
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}

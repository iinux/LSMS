package com.iinux.lsms;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ShenfenzhengCompute {
	private int []code;
	private Context context;
	

	public ShenfenzhengCompute(Context context) {
		code=new int[20];
		for(int i=0;i<20;i+=10)
		{
			code[i+0]=2;
			code[i+1]=4;
			code[i+2]=8;
			code[i+3]=5;
			code[i+4]=10;
			code[i+5]=9;
			code[i+6]=7;
			code[i+7]=3;
			code[i+8]=6;
			code[i+9]=1;
		}
		this.context=context;
	}
	public void calc(String s)
	{
//		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
//		String sx="";
//		for (StackTraceElement ste : stack) {
//			sx+=ste.getClassName()+":"+ste.getMethodName()+">>";
//		}
		//Log.i(General.LogTag,sx);
		String checkBit;
		int mod;
		int s_len=s.length();
		//Integer.parseInt(tf1.getString(), 10);
		int result=0;
		
		if (s_len==17)
		{
			for (int i=0;i<s_len;i++)
			{
				result+=(s.charAt(s_len-i-1)-48)*code[i];
			}
			mod=result%11;
			if (mod>=3&&mod<=10){
				checkBit=(12-mod)+"";
			}else if(mod==2){
				checkBit="X";
			}else if(mod==0){
				checkBit="1";
			}else if(mod==1){
				checkBit="0";
			}else{
				checkBit="";
			}
			Toast.makeText(context, "你的输入为17位，计算得出最后一位应该是：[ "+checkBit+" ]", Toast.LENGTH_LONG).show();
			
			//df.setLabel(String.valueOf(result));
		}
		else if (s_len==18){
			for (int i=0;i<s_len-1;i++)
			{
				result+=(s.charAt(s_len-i-2)-48)*code[i];
			}
			mod=result%11;
			if (mod>=3&&mod<=10){
				checkBit=(12-mod)+"";
			}else if(mod==2){
				checkBit="X";
			}else if(mod==0){
				checkBit="1";
			}else if(mod==1){
				checkBit="0";
			}else{
				checkBit="";
			}
			Log.i(General.LogTag,"ShenfenzhengCompute>calc appendS is "+checkBit);
			Log.i(General.LogTag,"ShenfenzhengCompute>calc last char is "+s.charAt(s_len-1));
			if (checkBit.equals(s.charAt(s_len-1)+"")){
				Toast.makeText(context, "你的输入身份证号码[ "+s+" ]为正确的身份证号码", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(context, "你的输入身份证号码[ "+s+" ]为错误的身份证号码", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(context, "位数不符(你的输入为 "+s_len+" 位)，请确认输入一串17或18位的数字！", Toast.LENGTH_SHORT).show();
		}
	}

}

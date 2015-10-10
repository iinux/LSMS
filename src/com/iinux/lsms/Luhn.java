package com.iinux.lsms;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Luhn {
	private Context context;
	private int bit;
	
	public Luhn(Context context,int bit){
		this.context=context;
		this.bit=bit;
	}
	public void calc(String s){
		String checkBit;
		int mod,t;
		int s_len=s.length();
		//Integer.parseInt(tf1.getString(), 10);
		int result=0;
		
		if (s_len==bit-1)
		{
			for (int i=0;i<s_len;i++)
			{
				result+=(t=(s.charAt(s_len-i-1)-48)*2)>9?t-9:t;
				i++;
				result+=(s.charAt(s_len-i-1)-48);
			}
			mod=result%10;
			if (mod==0){
				checkBit="0";
			}else if(mod>=1&&mod<=9){
				checkBit=(10-mod)+"";
			}
			else{
				checkBit="";
			}
			Toast.makeText(context, "�������Ϊ "+(bit-1)+" λ������ó����һλӦ���ǣ�[ "+checkBit+" ]", Toast.LENGTH_LONG).show();
			
			//df.setLabel(String.valueOf(result));
		}
		else if (s_len==bit){
			for (int i=0;i<s_len-1;i++)
			{
				result+=(t=(s.charAt(s_len-i-2)-48)*2)>9?t-9:t;
				//Log.i(General.LogTag,"Luhn>calc result is "+result);
				i++;
				result+=(s.charAt(s_len-i-2)-48);
				//Log.i(General.LogTag,"Luhn>calc result is "+result);
			}
			mod=result%10;
			if (mod==0){
				checkBit="0";
			}else if(mod>=1&&mod<=9){
				checkBit=(10-mod)+"";
			}else{
				checkBit="";
			}
			Log.i(General.LogTag,"Luhn>calc appendS is "+checkBit);
			Log.i(General.LogTag,"Luhn>calc last char is "+s.charAt(s_len-1));
			if (checkBit.equals(s.charAt(s_len-1)+"")){
				Toast.makeText(context, "����������[ "+s+" ]Ϊ��ȷ�ĺ���", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(context, "����������[ "+s+" ]Ϊ����ĺ���", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(context, "λ������(�������Ϊ "+s_len+" λ)����ȷ������һ�� "+(bit-1)+" �� "+bit+" λ�����֣�", Toast.LENGTH_SHORT).show();
		}
	}
}

package com.iinux.lsms;

public class LsmsCompute {
	public LsmsCompute() {
	}

	public String calcShenfenzheng(String s) {
		// StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		// String sx="";
		// for (StackTraceElement ste : stack) {
		// sx+=ste.getClassName()+":"+ste.getMethodName()+">>";
		// }
		// Log.i(General.LogTag,sx);
		int s_len = s.length();
		if (s_len==0){ return "����������"; }
		String checkBit;
		int mod;
		int result = 0;
		int[] code = new int[20];
		for (int i = 0; i < 20; i += 10) {
			code[i + 0] = 2;
			code[i + 1] = 4;
			code[i + 2] = 8;
			code[i + 3] = 5;
			code[i + 4] = 10;
			code[i + 5] = 9;
			code[i + 6] = 7;
			code[i + 7] = 3;
			code[i + 8] = 6;
			code[i + 9] = 1;
		}
		// Integer.parseInt(tf1.getString(), 10);

		if (s_len == 17) {
			for (int i = 0; i < s_len; i++) {
				result += (s.charAt(s_len - i - 1) - 48) * code[i];
			}
			mod = result % 11;
			if (mod >= 3 && mod <= 10) {
				checkBit = (12 - mod) + "";
			} else if (mod == 2) {
				checkBit = "X";
			} else if (mod == 0) {
				checkBit = "1";
			} else if (mod == 1) {
				checkBit = "0";
			} else {
				checkBit = "";
			}
			return "�������Ϊ17λ������ó����һλӦ���ǣ�[ " + checkBit + " ]";

			// df.setLabel(String.valueOf(result));
		} else if (s_len == 18) {
			for (int i = 0; i < s_len - 1; i++) {
				result += (s.charAt(s_len - i - 2) - 48) * code[i];
			}
			mod = result % 11;
			if (mod >= 3 && mod <= 10) {
				checkBit = (12 - mod) + "";
			} else if (mod == 2) {
				checkBit = "X";
			} else if (mod == 0) {
				checkBit = "1";
			} else if (mod == 1) {
				checkBit = "0";
			} else {
				checkBit = "";
			}
			if (checkBit.equals(s.charAt(s_len - 1) + "")) {
				return "����������֤����[ " + s + " ]Ϊ��ȷ�����֤����";
			} else {
				return "����������֤����[ " + s + " ]Ϊ��������֤����";
			}
		} else {
			return "λ������(�������Ϊ " + s_len + " λ)����ȷ������һ��17��18λ�����֣�";
		}
	}

	public String calcLuhn(String s, int bit) {
		int s_len = s.length();
		if (s_len==0){ return "����������"; }
		String checkBit;
		int mod, t;
		// Integer.parseInt(tf1.getString(), 10);
		int result = 0;

		if (s_len == bit - 1) {
			for (int i = 0; i < s_len; i++) {
				result += (t = (s.charAt(s_len - i - 1) - 48) * 2) > 9 ? t - 9 : t;
				i++;
				{
					int j=s_len-i-1;
					if (j<0) break;
					result += (s.charAt(j) - 48);
				}
			}
			mod = result % 10;
			if (mod == 0) {
				checkBit = "0";
			} else if (mod >= 1 && mod <= 9) {
				checkBit = (10 - mod) + "";
			} else {
				checkBit = "";
			}
			return "�������Ϊ " + (bit - 1) + " λ������ó����һλӦ���ǣ�[ " + checkBit + " ]";
		} else if (s_len == bit) {
			for (int i = 0; i < s_len - 1; i++) {
				result += (t = (s.charAt(s_len - i - 2) - 48) * 2) > 9 ? t - 9 : t;
				i++;
				{
					int j=s_len-i-2;
					if (j<0) break;
					result += (s.charAt(j) - 48);
				}
			}
			mod = result % 10;
			if (mod == 0) {
				checkBit = "0";
			} else if (mod >= 1 && mod <= 9) {
				checkBit = (10 - mod) + "";
			} else {
				checkBit = "";
			}
			if (checkBit.equals(s.charAt(s_len - 1) + "")) {
				return "����������[ " + s + " ]Ϊ��ȷ�ĺ���";
			} else {
				return "����������[ " + s + " ]Ϊ����ĺ���";
			}
		} else {
			return "λ������(�������Ϊ " + s_len + " λ)����ȷ������һ�� " + (bit - 1) + " �� "
					+ bit + " λ�����֣�";
		}
	}

	public String calcTax(String s) {
		if (s.length()==0){
			return "�������Ϊ�գ�";
		}
		int XSum = Integer.parseInt(s) - 3500;
		int Rate = 0, Balan = 0, TSum;
		if (XSum <= 1500) {
			Rate = 3;
			Balan = 0;
		}
		if ((1500 < XSum) && (XSum <= 4500)) {
			Rate = 10;
			Balan = 105;
		}
		if ((4500 < XSum) && (XSum <= 9000)) {
			Rate = 20;
			Balan = 555;
		}
		if ((9000 < XSum) && (XSum <= 35000)) {
			Rate = 25;
			Balan = 1005;
		}
		if ((35000 < XSum) && (XSum <= 55000)) {
			Rate = 30;
			Balan = 2755;
		}
		if ((55000 < XSum) && (XSum <= 80000)) {
			Rate = 35;
			Balan = 5505;
		}
		if (XSum > 80000) {
			Rate = 45;
			Balan = 13505;
		}
		TSum = (XSum * Rate) / 100 - Balan;
		if (TSum < 0) {
			TSum = 0;
		}
		return "��ýɵ�˰�� " + TSum + " ";
	}
}

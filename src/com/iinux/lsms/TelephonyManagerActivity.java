package com.iinux.lsms;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

@SuppressWarnings({ "static-access", "unused" })
public class TelephonyManagerActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        /**
         * ���ص绰״̬
         * 
         * CALL_STATE_IDLE ���κ�״̬ʱ 
         * CALL_STATE_OFFHOOK ����绰ʱ
         * CALL_STATE_RINGING �绰����ʱ 
         */
        tm.getCallState();
        //���ص�ǰ�ƶ��ն˵�λ��
        CellLocation location=tm.getCellLocation();
        //����λ�ø��£�������½������㲥�����ն���Ϊע��LISTEN_CELL_LOCATION�Ķ�����Ҫ��permission����ΪACCESS_COARSE_LOCATION��
        location.requestLocationUpdate();
        /**
         * ��ȡ���ݻ״̬
         * 
         * DATA_ACTIVITY_IN ��������״̬��������ڽ�������
         * DATA_ACTIVITY_OUT ��������״̬��������ڷ�������
         * DATA_ACTIVITY_INOUT ��������״̬��������ڽ��ܺͷ�������
         * DATA_ACTIVITY_NONE ��������״̬������������ݷ��ͺͽ���
         */
        tm.getDataActivity();
        /**
         * ��ȡ��������״̬
         * 
         * DATA_CONNECTED ��������״̬��������
         * DATA_CONNECTING ��������״̬����������
         * DATA_DISCONNECTED ��������״̬���Ͽ�
         * DATA_SUSPENDED ��������״̬����ͣ
         */
        tm.getDataState();
        /**
         * ���ص�ǰ�ƶ��ն˵�Ψһ��ʶ
         * 
         * �����GSM���磬����IMEI�������CDMA���磬����MEID
         */
        tm.getDeviceId();
        //�����ƶ��ն˵�����汾�����磺GSM�ֻ���IMEI/SV�롣
        tm.getDeviceSoftwareVersion();
        //�����ֻ����룬����GSM������˵��MSISDN
        tm.getLine1Number();
        //���ص�ǰ�ƶ��ն˸����ƶ��ն˵���Ϣ
        List<NeighboringCellInfo> infos=tm.getNeighboringCellInfo();
        for(NeighboringCellInfo info:infos){
            //��ȡ�ھ�С����
            int cid=info.getCid();
            //��ȡ�ھ�С��LAC��LAC: λ�������롣Ϊ��ȷ���ƶ�̨��λ�ã�ÿ��GSM/PLMN�ĸ������������ֳ����λ������LAC�����ڱ�ʶ��ͬ��λ������
            info.getLac();
            info.getNetworkType();
            info.getPsc();
            //��ȡ�ھ�С���ź�ǿ��  
            info.getRssi();
        }
        //����ISO��׼�Ĺ����룬�����ʳ�;����
        tm.getNetworkCountryIso();
        //����MCC+MNC���� (SIM����Ӫ�̹��Ҵ������Ӫ���������)(IMSI)
        tm.getNetworkOperator();
        //�����ƶ�������Ӫ�̵�����(SPN)
        tm.getNetworkOperatorName();
        /**
         * ��ȡ��������
         * 
         * NETWORK_TYPE_CDMA ��������ΪCDMA
         * NETWORK_TYPE_EDGE ��������ΪEDGE
         * NETWORK_TYPE_EVDO_0 ��������ΪEVDO0
         * NETWORK_TYPE_EVDO_A ��������ΪEVDOA
         * NETWORK_TYPE_GPRS ��������ΪGPRS
         * NETWORK_TYPE_HSDPA ��������ΪHSDPA
         * NETWORK_TYPE_HSPA ��������ΪHSPA
         * NETWORK_TYPE_HSUPA ��������ΪHSUPA
         * NETWORK_TYPE_UMTS ��������ΪUMTS
         * 
         * ���й�����ͨ��3GΪUMTS��HSDPA���ƶ�����ͨ��2GΪGPRS��EGDE�����ŵ�2GΪCDMA�����ŵ�3GΪEVDO
         */
        tm.getNetworkType();
        /**
         * �����ƶ��ն˵�����
         * 
         * PHONE_TYPE_CDMA �ֻ���ʽΪCDMA������
         * PHONE_TYPE_GSM �ֻ���ʽΪGSM���ƶ�����ͨ
         * PHONE_TYPE_NONE �ֻ���ʽδ֪
         */
        tm.getPhoneType();
        //����SIM���ṩ�̵Ĺ��Ҵ���
        tm.getSimCountryIso();
        //����MCC+MNC���� (SIM����Ӫ�̹��Ҵ������Ӫ���������)(IMSI)
        tm.getSimOperator();
        tm.getSimOperatorName();
        //����SIM�������к�(IMEI)
        tm.getSimSerialNumber();
        /**
         * �����ƶ��ն�
         * 
         * SIM_STATE_ABSENT SIM��δ�ҵ�
         * SIM_STATE_NETWORK_LOCKED SIM�����类��������ҪNetwork PIN����
         * SIM_STATE_PIN_REQUIRED SIM��PIN����������ҪUser PIN����
         * SIM_STATE_PUK_REQUIRED SIM��PUK����������ҪUser PUK����
         * SIM_STATE_READY SIM������
         * SIM_STATE_UNKNOWN SIM��δ֪
         */
        tm.getSimState();
        //�����û�Ψһ��ʶ������GSM�����IMSI���
        tm.getSubscriberId();
        //��ȡ������������������ĸ��ʶ�� 
        tm.getVoiceMailAlphaTag();
        //���������ʼ�����
        tm.getVoiceMailNumber();
        tm.hasIccCard();
        //�����ֻ��Ƿ�������״̬
        tm.isNetworkRoaming();
        // tm.listen(PhoneStateListener listener, int events) ;
        
        //���ͣ�
        //IMSI�ǹ����ƶ��û�ʶ����ļ��(International Mobile Subscriber Identity)
        //IMSI����15λ����ṹ���£�
        //MCC+MNC+MIN
        //MCC��Mobile Country Code���ƶ������룬��3λ���й�Ϊ460;
        //MNC:Mobile NetworkCode���ƶ������룬��2λ
        //���й����ƶ��Ĵ���Ϊ��00��02����ͨ�Ĵ���Ϊ01�����ŵĴ���Ϊ03
        //���������ǣ�Ҳ��Android�ֻ���APN�����ļ��еĴ��룩��
        //�й��ƶ���46000 46002
        //�й���ͨ��46001
        //�й����ţ�46003
        //������һ�����͵�IMSI����Ϊ460030912121001

        //IMEI��International Mobile Equipment Identity �������ƶ��豸��ʶ���ļ��
        //IMEI��15λ������ɵġ����Ӵ��š�������ÿ̨�ֻ�һһ��Ӧ�����Ҹ�����ȫ����Ψһ��
        //�����Ϊ��
        //1. ǰ6λ��(TAC)�ǡ��ͺź�׼���롱��һ��������
        //2. ���ŵ�2λ��(FAC)�ǡ����װ��š���һ��������
        //3. ֮���6λ��(SNR)�ǡ����š���һ���������˳���
        //4. ���1λ��(SP)ͨ���ǡ�0�壬Ϊ�����룬Ŀǰ�ݱ���
    }
	private int readMissCall() {
        int result = 0;
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[] {
                Calls.TYPE
            }, " type=? and new=?", new String[] {
                    Calls.MISSED_TYPE + "", "1"
            }, "date desc");

        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;
    }
    private int getNewSmsCount() {
        int result = 0;
        Cursor csr = getContentResolver().query(Uri.parse("content://sms"), null,
                "type = 1 and read = 0", null, null);
        if (csr != null) {
            result = csr.getCount();
            csr.close();
        }
        return result;
    }
    private int getNewMmsCount() {
        int result = 0;
        Cursor csr = getContentResolver().query(Uri.parse("content://mms/inbox"),
                null, "read = 0", null, null);
        if (csr != null) {
            result = csr.getCount();
            csr.close();
        }
        return result;
    }
    private ContentObserver newMmsContentObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean selfChange) {
            int mNewSmsCount = getNewSmsCount() + getNewMmsCount();
        }
    };
    private void registerObserver() {
        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true,
                newMmsContentObserver);
    }

    private synchronized void unregisterObserver() {
        try {
            if (newMmsContentObserver != null) {
                getContentResolver().unregisterContentObserver(newMmsContentObserver);
            }
            if (newMmsContentObserver != null) {
                getContentResolver().unregisterContentObserver(newMmsContentObserver);
            }
        } catch (Exception e) {
            Log.e(General.LogTag, "unregisterObserver fail");
        }
    }
}
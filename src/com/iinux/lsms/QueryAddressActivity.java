package com.iinux.lsms;

import android.net.TrafficStats;

public class QueryAddressActivity {
	public void getDataUsage() {
		int uid = 0;
		/** ��ȡ�ֻ�ͨ�� 2G/3G ���յ��ֽ��������� */
		TrafficStats.getMobileRxBytes();
		/** ��ȡ�ֻ�ͨ�� 2G/3G ���յ����ݰ����� */
		TrafficStats.getMobileRxPackets();
		/** ��ȡ�ֻ�ͨ�� 2G/3G �������ֽ��������� */
		TrafficStats.getMobileTxBytes();
		/** ��ȡ�ֻ�ͨ�� 2G/3G ���������ݰ����� */
		TrafficStats.getMobileTxPackets();
		/** ��ȡ�ֻ�ͨ���������緽ʽ���յ��ֽ���������(���� wifi) */
		TrafficStats.getTotalRxBytes();
		/** ��ȡ�ֻ�ͨ���������緽ʽ���յ����ݰ�����(���� wifi) */
		TrafficStats.getTotalRxPackets();
		/** ��ȡ�ֻ�ͨ���������緽ʽ���͵��ֽ���������(���� wifi) */
		TrafficStats.getTotalTxBytes();
		/** ��ȡ�ֻ�ͨ���������緽ʽ���͵����ݰ�����(���� wifi) */
		TrafficStats.getTotalTxPackets();
		/** ��ȡ�ֻ�ָ�� UID ��Ӧ��Ӧ������ͨ���������緽ʽ���յ��ֽ���������(���� wifi) */
		TrafficStats.getUidRxBytes(uid);
		/** ��ȡ�ֻ�ָ�� UID ��Ӧ��Ӧ�ó���ͨ���������緽ʽ���͵��ֽ���������(���� wifi) */
		TrafficStats.getUidTxBytes(uid);

	}
}

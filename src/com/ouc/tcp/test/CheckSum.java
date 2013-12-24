package com.ouc.tcp.test;

import java.util.zip.CRC32;

import com.ouc.tcp.message.TCP_HEADER;
import com.ouc.tcp.message.TCP_PACKET;

public class CheckSum {
	
	/*计算TCP报文段校验和：只需校验TCP首部中的seq、ack和sum，以及TCP数据字段*/
	public static short computeChkSum(TCP_PACKET tcpPack) {
		int checkSum = 0;
		
		//计算校验和
		TCP_HEADER tcpH = tcpPack.getTcpH();
		CRC32 crc = new CRC32();
		crc.update(tcpH.getTh_ack());
		crc.update(tcpH.getTh_seq());
		for(int d : tcpPack.getTcpS().getData()) {
			crc.update(d);
		}
		checkSum = (int) crc.getValue();
		
		return (short) checkSum;
	}
	
}

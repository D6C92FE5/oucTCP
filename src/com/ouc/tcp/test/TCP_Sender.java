package com.ouc.tcp.test;

import com.ouc.tcp.client.TCP_Sender_ADT;
import com.ouc.tcp.message.*;
import com.ouc.tcp.tool.TCP_TOOL;

public class TCP_Sender extends TCP_Sender_ADT {
	
	private TCP_PACKET tcpPack;	//待发送的TCP数据报
	
	/*构造函数*/
	public TCP_Sender() {
		super();	//调用超类构造函数
		super.initTCP_Sender(this);	//初始化TCP发送端
	}
	
	@Override
	//可靠发送（应用层调用）：封装应用层数据，产生TCP数据报
	public void rdt_send(int dataIndex, int[] appData) {
		
		//生成TCP数据报（设置序号和数据字段）
		tcpH.setTh_seq(dataIndex * appData.length + 1);
		tcpS.setData(appData);
		tcpPack = new TCP_PACKET(tcpH, tcpS, destinAddr);
		
		//发送TCP数据报
		udt_send(tcpPack);
		
		//等待ACK报文
		waitACK();
		
	}
	
	@Override
	//不可靠发送：将打包好的TCP数据报通过不可靠传输信道发送
	public void udt_send(TCP_PACKET tcpPack) {
		
		//设置错误控制标志
		tcpH.setTh_eflag((byte)0);	//eFlag=0，信道无错误
		
		//发送数据报
		client.send(tcpPack);
		
	}
	
	@Override
	//等待期望的ACK报文
	public void waitACK() {
		//循环检查确认号对列中是否有新收到的ACK
		while(true) {
			if(!ackQueue.isEmpty() && ackQueue.poll() == tcpPack.getTcpH().getTh_seq()) {
				break;
			}
		}
	}

	@Override
	//接收到ACK报文：将确认号插入ack队列
	public void recv(TCP_PACKET recvPack) {
		ackQueue.add(recvPack.getTcpH().getTh_ack());
		System.out.println();
	}
	
}

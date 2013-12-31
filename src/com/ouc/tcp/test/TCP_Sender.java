package com.ouc.tcp.test;

import java.util.TimerTask;

import com.ouc.tcp.client.TCP_Sender_ADT;
import com.ouc.tcp.client.UDT_Timer;
import com.ouc.tcp.message.*;

public class TCP_Sender extends TCP_Sender_ADT {
	
	private TCP_PACKET tcpPack;	//待发送的TCP数据报
	private TCP_Window tcpWindow = new TCP_Window(5); //TCP窗口

	private int waitTime = 100; //重发等待时间
	private volatile boolean isTimeout = false; //是否等待超时

	private class TimeoutTask extends TimerTask {
		@Override
		public void run() {
			isTimeout = true;
		}
	}

	/*构造函数*/
	public TCP_Sender() {
		super();	//调用超类构造函数
		super.initTCP_Sender(this);	//初始化TCP发送端
	}
	
	private void sendPacketsIfWindowFullThenWaitACK() {
		if(tcpWindow.isFull()) {
			//发送TCP数据报
			for(int i : tcpWindow) {
				udt_send(tcpWindow.getPacket(i));
			}
			//等待ACK报文
			waitACK();
		}
	}

	@Override
	//可靠发送（应用层调用）：封装应用层数据，产生TCP数据报
	public void rdt_send(int dataIndex, int[] appData) {
		
		//生成TCP数据报（设置序号和数据字段）
		tcpH = new TCP_HEADER();
		tcpH.setTh_seq(dataIndex * appData.length + 1);
		tcpS = new TCP_SEGMENT();
		tcpS.setData(appData);
		tcpPack = new TCP_PACKET(tcpH, tcpS, destinAddr);
		tcpH.setTh_sum(CheckSum.computeChkSum(tcpPack));

		tcpWindow.queuePacket(tcpPack);

		sendPacketsIfWindowFullThenWaitACK();
	}
	
	@Override
	//不可靠发送：将打包好的TCP数据报通过不可靠传输信道发送
	public void udt_send(TCP_PACKET tcpPack) {
		
		//设置错误控制标志
		tcpPack.getTcpH().setTh_eflag((byte)7);	//eFlag=0，信道无错误
		
		//发送数据报
		client.send(tcpPack);
		
	}
	
	@Override
	//等待期望的ACK报文
	public void waitACK() {
		//等待窗口内所有的包被确认或者超时
		isTimeout = false;
		UDT_Timer udtTimer = new UDT_Timer();
		udtTimer.schedule(new TimeoutTask(), waitTime, waitTime);
		while(!tcpWindow.isEmpty() && !isTimeout) ;
		udtTimer.cancel();

		//窗口可能完全没有滑动
		sendPacketsIfWindowFullThenWaitACK();
	}

	@Override
	//接收到ACK报文：将确认号插入ack队列
	public void recv(TCP_PACKET recvPack) {
		tcpWindow.receiveACK(recvPack.getTcpH().getTh_ack());
		System.out.println();
	}
	
}

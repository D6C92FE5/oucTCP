package com.ouc.tcp.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.ouc.tcp.client.TCP_Receiver_ADT;
import com.ouc.tcp.message.*;
import com.ouc.tcp.tool.TCP_TOOL;

public class TCP_Receiver extends TCP_Receiver_ADT {
	
	private TCP_PACKET ackPack;	//回复的ACK报文段
	private HashMap<Integer, TCP_PACKET> packets = //收到的包们
			new HashMap<Integer, TCP_PACKET>();
	private int expectedSeq = 1; //期望收到的报文段号
	
	/*构造函数*/
	public TCP_Receiver() {
		super();	//调用超类构造函数
		super.initTCP_Receiver(this);	//初始化TCP接收端
	}

	@Override
	//接收到数据报：设置回复的ACK报文段
	public void rdt_recv(TCP_PACKET recvPack) {
		//出错丢弃
		if(CheckSum.computeChkSum(recvPack) != recvPack.getTcpH().getTh_sum()) {
			return;
		}

		//生成ACK报文段（设置确认号）
		int recvSeq = recvPack.getTcpH().getTh_seq();
		tcpH.setTh_ack(recvSeq);
		ackPack = new TCP_PACKET(tcpH, tcpS, recvPack.getSourceAddr());
		
		//回复ACK报文段
		reply(ackPack);
		
		//如果需要这个包就把它留下来
		if (recvSeq >= expectedSeq && !packets.containsKey(recvSeq)) {
			packets.put(recvSeq, recvPack);
		}

		//将接收到的数据有序地插入data队列，准备交付，注意不可以使data队列中的长度超过20
		while(packets.containsKey(expectedSeq) && dataQueue.size() < 20) {
			TCP_PACKET packet = packets.remove(expectedSeq);
			expectedSeq += packet.getTcpS().getData().length;
			dataQueue.add(packet.getTcpS().getData());
		}
		
		//交付数据（每20组数据交付一次）
		if(dataQueue.size() == 20) {
			deliver_data();
		}
		
		System.out.println();
	}

	@Override
	//交付数据（将接收到的正确有序的数据写入文件）
	public void deliver_data() {	
		File fw = new File("recvData.txt");
		BufferedWriter writer;
		
		try {
			writer = new BufferedWriter(new FileWriter(fw, true));
			
			//循环检查data队列中是否有新交付数据
			while(!dataQueue.isEmpty()) {
				int[] data = dataQueue.poll();
				
				//将数据写入文件
				for(int i = 0; i < data.length; i++) {
					writer.write(data[i] + "\n");
				}
				
				writer.flush();		//清空输出缓存
			}
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	//回复ACK报文段
	public void reply(TCP_PACKET replyPack) {
		//设置错误控制标志
		tcpH.setTh_eflag((byte)7);	//eFlag=0，信道无错误
		
		//发送数据报
		client.send(replyPack);		
	}
	
}

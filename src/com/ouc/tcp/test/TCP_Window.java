package com.ouc.tcp.test;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

import com.ouc.tcp.message.TCP_PACKET;

public class TCP_Window implements Iterable<Integer> {

	private ConcurrentSkipListMap<Integer, TCP_PACKET> packets = //窗口中的包们
			new ConcurrentSkipListMap<Integer, TCP_PACKET>();

	private int length = 1; //窗口大小
	private int ssthresh = 16; //慢开始门限大小
	private int nextIndex = 0; //下一个包的序号

	/**
	 * 把一个包加入窗口，请先检测窗口是否未满
	 * @param tcpPack 要加入窗口的包
	 */
	public void queuePacket(TCP_PACKET tcpPack) {
		packets.put(nextIndex, tcpPack);
		nextIndex += 1;
	}

	/**
	 * 收到了ACK包
	 * @param ack ACK号
	 */
	public void receiveACK(int ack) {
		for(int i : packets.navigableKeySet()) {
			if(packets.get(i).getTcpH().getTh_seq() == ack) {
				packets.remove(i);
				length = length < ssthresh ? length * 2 : length + 1;
			}
		}
	}

	/**
	 * 获取窗口是否为空
	 * @return 窗口是否为空
	 */
	public boolean isEmpty() {
		return packets.size() == 0;
	}

	/**
	 * 获取窗口是否已满
	 * @return 窗口是否已满
	 */
	public boolean isFull() {
		return !isEmpty() && (nextIndex - packets.firstKey()) >= length;
	}

	/**
	 * 获取迭代器
	 * @return 迭代器
	 */
	public Iterator<Integer> iterator() {
		return packets.navigableKeySet().iterator();
	}

	/**
	 * 根据包的序号获取包
	 * @param index 包的序号
	 * @return 包
	 */
	public TCP_PACKET getPacket(int index) {
		return packets.get(index);
	}

	/**
	 * 通知窗口发生了拥塞
	 */
	public void congestionOccurred() {
		ssthresh = Math.max(length / 2, 2);
		length = 1;
	}
}

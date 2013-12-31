package com.ouc.tcp.test;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

import com.ouc.tcp.message.TCP_PACKET;

public class TCP_Window implements Iterable<Integer> {

	private ConcurrentSkipListMap<Integer, TCP_PACKET> packets = new ConcurrentSkipListMap<Integer, TCP_PACKET>();

	private int length = 1;
	private int ssthresh = 16;
	private int nextIndex = 0;

	public void queuePacket(TCP_PACKET tcpPack) {
		System.out.println(length + " " + ssthresh);
		packets.put(nextIndex, tcpPack);
		nextIndex += 1;
	}

	public void receiveACK(int ack) {
		for(int i : packets.navigableKeySet()) {
			if(packets.get(i).getTcpH().getTh_seq() == ack) {
				packets.remove(i);
				length = length < ssthresh ? length * 2 : length + 1;
			}
		}
	}

	public boolean isEmpty() {
		return packets.size() == 0;
	}

	public boolean isFull() {
		return !isEmpty() && (nextIndex - packets.firstKey()) >= length;
	}

	public Iterator<Integer> iterator() {
		return packets.navigableKeySet().iterator();
	}

	public TCP_PACKET getPacket(int index) {
		return packets.get(index);
	}

	public void congestionOccurred() {
		ssthresh = Math.max(length / 2, 2);
		length = 1;
	}
}

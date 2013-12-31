package com.ouc.tcp.test;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

import com.ouc.tcp.message.TCP_PACKET;

public class TCP_Window implements Iterable<Integer> {

	private ConcurrentSkipListMap<Integer, TCP_PACKET> packets = new ConcurrentSkipListMap<Integer, TCP_PACKET>();

	private int length;
	private int nextIndex = 0;

	public TCP_Window(int length) {
		this.length = length;
	}

	public void queuePacket(TCP_PACKET tcpPack) {
		assert !isFull();
		packets.put(nextIndex, tcpPack);
		nextIndex += 1;
	}

	public void receiveACK(int ack) {
		for(int i : packets.navigableKeySet()) {
			if(packets.get(i).getTcpH().getTh_seq() == ack) {
				packets.remove(i);
			}
		}
	}

	public boolean isEmpty() {
		return packets.size() == 0;
	}

	public boolean isFull() {
		return !isEmpty() && (nextIndex - packets.firstKey()) == length;
	}

	public Iterator<Integer> iterator() {
		return packets.navigableKeySet().iterator();
	}

	public TCP_PACKET getPacket(int index) {
		return packets.get(index);
	}
}

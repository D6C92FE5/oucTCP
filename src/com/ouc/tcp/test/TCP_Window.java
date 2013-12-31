package com.ouc.tcp.test;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

import com.ouc.tcp.message.TCP_PACKET;

public class TCP_Window implements Iterable<Integer> {

	private ConcurrentSkipListMap<Integer, TCP_PACKET> packets = new ConcurrentSkipListMap<Integer, TCP_PACKET>();

	private int length;

	public TCP_Window(int length) {
		this.length = length;
	}

	public void queuePacket(TCP_PACKET tcpPack) {
		assert !isFull();
		int index = isEmpty() ? 0 : packets.lastKey() + 1;
		packets.put(index, tcpPack);
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
		return !isEmpty() && (packets.lastKey() - packets.firstKey()) == length - 1;
	}

	public Iterator<Integer> iterator() {
		return packets.navigableKeySet().iterator();
	}

	public TCP_PACKET getPacket(int index) {
		return packets.get(index);
	}
}

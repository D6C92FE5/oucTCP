package com.ouc.tcp.test;

import com.ouc.tcp.client.TCP_Sender_ADT;
import com.ouc.tcp.message.*;
import com.ouc.tcp.tool.TCP_TOOL;

public class TCP_Sender extends TCP_Sender_ADT {
	
	private TCP_PACKET tcpPack;	//�����͵�TCP���ݱ�
	
	/*���캯��*/
	public TCP_Sender() {
		super();	//���ó��๹�캯��
		super.initTCP_Sender(this);	//��ʼ��TCP���Ͷ�
	}
	
	@Override
	//�ɿ����ͣ�Ӧ�ò���ã�����װӦ�ò����ݣ�����TCP���ݱ�
	public void rdt_send(int dataIndex, int[] appData) {
		
		//����TCP���ݱ���������ź������ֶΣ�
		tcpH.setTh_seq(dataIndex * appData.length + 1);
		tcpS.setData(appData);
		tcpPack = new TCP_PACKET(tcpH, tcpS, destinAddr);
		
		//����TCP���ݱ�
		udt_send(tcpPack);
		
		//�ȴ�ACK����
		waitACK();
		
	}
	
	@Override
	//���ɿ����ͣ�������õ�TCP���ݱ�ͨ�����ɿ������ŵ�����
	public void udt_send(TCP_PACKET tcpPack) {
		
		//���ô�����Ʊ�־
		tcpH.setTh_eflag((byte)0);	//eFlag=0���ŵ��޴���
		
		//�������ݱ�
		client.send(tcpPack);
		
	}
	
	@Override
	//�ȴ�������ACK����
	public void waitACK() {
		//ѭ�����ȷ�ϺŶ������Ƿ������յ���ACK
		while(true) {
			if(!ackQueue.isEmpty() && ackQueue.poll() == tcpPack.getTcpH().getTh_seq()) {
				break;
			}
		}
	}

	@Override
	//���յ�ACK���ģ���ȷ�ϺŲ���ack����
	public void recv(TCP_PACKET recvPack) {
		ackQueue.add(recvPack.getTcpH().getTh_ack());
		System.out.println();
	}
	
}

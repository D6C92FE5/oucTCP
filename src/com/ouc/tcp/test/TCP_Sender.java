package com.ouc.tcp.test;

import com.ouc.tcp.client.TCP_Sender_ADT;
import com.ouc.tcp.client.UDT_RetransTask;
import com.ouc.tcp.client.UDT_Timer;
import com.ouc.tcp.message.*;
import com.ouc.tcp.tool.TCP_TOOL;

public class TCP_Sender extends TCP_Sender_ADT {
	
	/*���캯��*/
	public TCP_Sender() {
		super();	//���ó��๹�캯��
		super.initTCP_Sender(this);		//��ʼ��TCP���Ͷ�
	}
	
	@Override
	//�ɿ����ͣ�Ӧ�ò���ã�����װӦ�ò����ݣ�����TCP���ݱ�
	public void rdt_send(int dataIndex, int[] appData) {
		//����TCP���ݱ���������ź������ֶ�/У��͹��㣩
		
		//����udt_send()����
		
		//�ȴ�ACK����
	}
	
	@Override
	//���ɿ����ͣ�������õ�TCP���ݱ�ͨ�����ɿ������ŵ�����
	public void udt_send(TCP_PACKET tcpPack) {
		//���ô�����Ʊ�־
		
		//����У��ͣ�����TCP�ײ����´��
		
		//�������ݱ�
		//client.send(tcpPack);
	}
	
	@Override
	//�ȴ�������ACK����
	public void waitACK() {
		//ѭ�����ackQueue
	}

	@Override
	//���յ�ACK���ģ����У��ͣ���ȷ�ϺŲ���ack����
	public void recv(TCP_PACKET recvPack) {
		
	}
	
}

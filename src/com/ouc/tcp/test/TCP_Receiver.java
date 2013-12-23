package com.ouc.tcp.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ouc.tcp.client.TCP_Receiver_ADT;
import com.ouc.tcp.message.*;
import com.ouc.tcp.tool.TCP_TOOL;

public class TCP_Receiver extends TCP_Receiver_ADT {
		
	/*���캯��*/
	public TCP_Receiver() {
		super();	//���ó��๹�캯��
		super.initTCP_Receiver(this);	//��ʼ��TCP���ն�
	}

	@Override
	//���յ����ݱ������У��ͣ����ûظ���ACK���Ķ�
	public void rdt_recv(TCP_PACKET recvPack) {
		//����ACK���ĶΣ�����ȷ�Ϻ�/У��͹��㣩
		
		//����reply()�ظ�ACK���Ķ�
		
		//�����ݲ���data���У���������
	}

	@Override
	//�������ݣ�������д���ļ���
	public void deliver_data() {
		//���dataQueue��������д���ļ�
		File fw = new File("recvData.txt");
		BufferedWriter writer;
		
		try {
			writer = new BufferedWriter(new FileWriter(fw, true));
			
			//ѭ�����data�������Ƿ����½�������
			while(!dataQueue.isEmpty()) {
				int[] data = dataQueue.poll();
				
				//������д���ļ�
				for(int i = 0; i < data.length; i++) {
					writer.write(data[i] + "\n");
				}
				
				writer.flush();		//����������
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	//�ظ�ACK���Ķ�
	public void reply(TCP_PACKET replyPack) {
		//���ô�����Ʊ�־
		
		//����У��ͣ�����TCP�ײ����´��
		
		//�������ݱ�
		//client.send(tcpPack);
	}
	
}

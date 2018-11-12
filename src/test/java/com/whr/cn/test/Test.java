package com.whr.cn.test;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.whr.rpc.thrift.MessageForwardsService;

public class Test {
	
	public static void main(String[] args) {
		TTransport transport = new TSocket("192.17.200.47", 9091, 1000);
		try {
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			MessageForwardsService.Client client = new MessageForwardsService.Client(
					protocol);
			String state = client.getDeviceState("b0f8932f0a39");
			System.out.println(state);
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

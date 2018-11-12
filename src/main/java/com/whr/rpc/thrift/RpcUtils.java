package com.whr.rpc.thrift;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whr.rpc.zookeeper.ZkProviders;

public class RpcUtils {
	private static Logger logger = LoggerFactory.getLogger(RpcUtils.class);

	// private static List<String> clousters;

//	public static void execute(Object[] objs) {
//		String[] rpcUrl = getRpcUrl();
////		String[] array = new String[] { "192.17.200.28", "192.17.200.27" };
//		TTransport transport = null;
//		for (int i = 0; i < rpcUrl.length; i++) {
//			transport = new TSocket(rpcUrl[i], 9090, 1000);
//			try {
//				transport.open();
//
//				TProtocol protocol = new TBinaryProtocol(transport);
//				MessageForwardsService.Client client = new MessageForwardsService.Client(protocol);
//
//				try {
//					for (int j = 0; j < objs.length; j++) {
//						int rel = client.sendToPeer(null, objs[i].toString());
//						
//					}
//
//				} catch (TException e) {
//					e.printStackTrace();
//				}
//			} catch (TTransportException e) {
//				e.printStackTrace();
//			} finally {
//				transport.close();
//			}
//		}
//	}

	public static String queryState(String mac) {
		// TODO: 这里需要完善
		String[] rpcUrl = getRpcUrl();
//		String[] array = new String[] { "192.9.200.190", "192.9.200.192",
//				"192.9.200.193" };
		TTransport transport = null;
		String state = null;

		for (int i = 0; i < rpcUrl.length; i++) {
			transport = new TSocket(rpcUrl[i], 9090, 1000);
			try {
				transport.open();

				TProtocol protocol = new TBinaryProtocol(transport);
				MessageForwardsService.Client client = new MessageForwardsService.Client(protocol);

				try {
					state = client.getDeviceState(mac);
					if (null != state)
						break;
				} catch (TException e) {
					e.printStackTrace();
				}
			} catch (TTransportException e) {
				e.printStackTrace();
			} finally {
				transport.close();
			}
		}
		return state;
	}

	public static int sendToPeer(Object object) {
		String[] rpcUrl = getRpcUrl();
		//String[] array = new String[] { "59.110.25.45", "59.110.22.202" };
		TTransport transport = null;
		int result = 1;
		for (int i = 0; i < rpcUrl.length; i++) {
			transport = new TSocket(rpcUrl[i], 9090, 10000);
			try {
				transport.open();

				TProtocol protocol = new TBinaryProtocol(transport);
				MessageForwardsService.Client client = new MessageForwardsService.Client(protocol);

				try {
					result = client.sendToPeer(null, object.toString());
					if(result==0)
						return result;
				} catch (TException e) {
					logger.error("sendToPeer error",e);
				}
			} catch (TTransportException e) {
				e.printStackTrace();
			} finally {
				transport.close();
			}
		}
		return result;

	}

	public static String[] getRpcUrl() {
		ZkProviders.getInstance();
		ArrayList<String> clousters = new ArrayList<String>();
		List<String> clousterLists = ZkProviders.clousters;
		for (String clousterList : clousterLists) {
			String[] clousterArr = clousterList.split(":");
			clousters.add(clousterArr[0]);
		}
		String[] clousterArrs = clousters.toArray(new String[clousters.size()]);

		return clousterArrs;

	}
}

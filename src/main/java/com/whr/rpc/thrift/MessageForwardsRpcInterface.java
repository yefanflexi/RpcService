package com.whr.rpc.thrift;



import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageForwardsRpcInterface implements MessageForwardsService.Iface {
	private static Logger logger = LoggerFactory.getLogger(MessageForwardsRpcInterface.class);

	@Override
	public int emulateDevice(String clientserver, String msg) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public String getAllChannels() throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeviceState(String mac) throws TException {
		logger.info("请求设备状态的mac地址是："+mac);
		return RpcUtils.queryState(mac);
	}


	@Override
	public int sendToPeer(String clientserver, String msg) throws TException {
		logger.info("请求的msg是："+msg);
		return RpcUtils.sendToPeer(msg);
	}
	
	
}
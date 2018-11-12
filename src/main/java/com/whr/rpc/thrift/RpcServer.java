package com.whr.rpc.thrift;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whr.rpc.zookeeper.ZkProviders;


public class RpcServer{
	
	private static MessageForwardsRpcInterface handler;
	private static Logger logger = LoggerFactory.getLogger(RpcServer.class);
	
	@SuppressWarnings("rawtypes")
	private static MessageForwardsService.Processor processor;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void start() {
		logger.info("async TNonblockingServer start ....");
	
		 try {
			 handler = new MessageForwardsRpcInterface();
			 processor = new MessageForwardsService.Processor(handler);
			 Runnable simple = new Runnable() {
				 public void run() {
					 server(processor);
				 }
			 };
			 new Thread(simple).start();
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}
	
	@SuppressWarnings("rawtypes")
	 public static void server(MessageForwardsService.Processor processor) {
		 try {
			 TServerTransport serverTransport = new TServerSocket(9091,2000);
			 TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
			
			 logger.info("Starting the rpc server... on port:"+9091);
			 server.serve();
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }
	public static void main(String[] args) {
		RpcServer.start();
		ZkProviders.getInstance().regServer();
	}

}


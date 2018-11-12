package com.whr.rpc.zookeeper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.whirlpool.commonutils.WhrPublicConstants;


public class ZkProviders implements Watcher {
	private Logger logger = LoggerFactory.getLogger(ZkProviders.class);
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    public  static List<String> 	clousters=new ArrayList<String>();
    private static ZkClient    	zkClient;
   
    private static ZkProviders   	_instance=null;
    
    public static ZkProviders getInstance(){
    	if(_instance==null)_instance=new ZkProviders();
    	return _instance;
    }
    private ZkProviders(){
    	zkClient = new ZkClient(WhrPublicConstants.zk_host ,5000);
    	if(!zkClient.exists(WhrPublicConstants.zk_path)){
    		zkClient.createPersistent(WhrPublicConstants.zk_path , true);   		
    	}
    	if(!zkClient.exists(WhrPublicConstants.zk_path + "/servers")){
    		zkClient.createPersistent(WhrPublicConstants.zk_path + "/servers" , true);   		
    	}

    	clousters = zkClient.getChildren(WhrPublicConstants.zk_path + "/servers" );    	
    }
    public void regServer(){
    	subscribeServer();
    }
    public void subscribeServer(){
    	logger.info("订阅zookeeper");
    	zkClient.subscribeChildChanges(WhrPublicConstants.zk_path + "/servers", new IZkChildListener(){

			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				logger.info("服务器上线/下线事件:"+currentChilds.size() + " : " + currentChilds.toString());
				clousters = currentChilds;
			}});
    }

 
    @Override
    public void process(WatchedEvent event) {
        if (KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
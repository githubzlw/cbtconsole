package ceRong.tools.servlet;

import ceRong.tools.bean.CustomOrderBean;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface FileDataService extends Remote {
	
	// 这里的filename应该是该文件存放在服务器端的地址..
	public List<CustomOrderBean> upload(String filename, List<byte[]> fileContent, String sourceImg, String sourceUrl, int catId, String catName, String maxIndexName, List<CustomOrderBean> picIdList) throws RemoteException;
	
}

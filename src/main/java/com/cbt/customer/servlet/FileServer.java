package com.cbt.customer.servlet;

import ceRong.tools.servlet.FileDataService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class FileServer {

    FileDataService fileDataService;

    public FileServer() {
        try {
        	System.out.println("rmi服务开始启动……");
            fileDataService = new FileDataServiceImpl();
            LocateRegistry.createRegistry(6600);
            Naming.rebind("rmi://192.168.1.120:6600/FileDataService", fileDataService);
            System.out.println("rmi服务启动成功");
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new FileServer();
        
    }

}
package com.gf.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.csource.fastdfs.UploadCallback;

public class FastDfsUtil {
	public void uploadFile(String localFile)
	{
		try{
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("fdfs_client.conf");
			Properties prop = new Properties();
			prop.load(is);
			System.out.println("prop==="+prop);
			ClientGlobal.initByProperties(prop);
			System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
			System.out.println("charset=" + ClientGlobal.g_charset);
			
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);
			
			NameValuePair[] metaList = new NameValuePair[1];
			metaList[0] = new NameValuePair("fileName", localFile);
			String fileId = client.upload_file1(localFile, null, metaList);
			System.out.println("upload success. file id is: " + fileId);
			
			trackerServer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void uploadFileCallback(String localFile,UploadCallback callback)
	{
		try{
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("fdfs_client.conf");
			Properties prop = new Properties();
			prop.load(is);
			System.out.println("prop==="+prop);
			ClientGlobal.initByProperties(prop);
			File f = new File(localFile);
			long fileSize = f.length();
			
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);
			
			NameValuePair[] metaList = new NameValuePair[1];
			metaList[0] = new NameValuePair("fileName", localFile);
			String fileId = client.upload_file1(null,fileSize,callback,localFile, metaList);
			System.out.println("upload success. file id is: " + fileId);
			
			trackerServer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public byte[] downloadFile(String fileId)
	{
		try
		{
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("fdfs_client.conf");
			Properties prop = new Properties();
			prop.load(is);
			System.out.println("prop==="+prop);
			ClientGlobal.initByProperties(prop);
			
			System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
			System.out.println("charset=" + ClientGlobal.g_charset);
			
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);
			
			byte[] result = client.download_file1(fileId);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		String conf = "D:\\workspace2\\FiProj\\src\\main\\resources\\fdfs_client.conf";
		String file = "H:\\my.png";
		UploadbackImpl upback = new UploadbackImpl();
		
		FastDfsUtil fdu = new FastDfsUtil();
		fdu.uploadFile(file);
		
		byte[] data = fdu.downloadFile("group1/M00/00/00/wKgZg1xIRLiAApePAAH9u1jEkSs437.png");
		try
		{
			FileOutputStream fos = new FileOutputStream("h:/my3.png");
			fos.write(data);
			fos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

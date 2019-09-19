package com.zhengqing.utils;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.csource.fastdfs.*;

public class FastDfsApiUtils {

    public static String CONF_FILENAME  = FastDfsApiUtils.class.getClassLoader().getResource("config/fast_client.conf").getFile();
    private static final Log logger = LogFactory.getLog(FastDfsApiUtils.class);

    /**
     * 上传文件
     */
    public static  String upload(String path,String extName) {
        logger.debug("<---------------- start 上传文件到fastdfs ------------------>");
        try {
            ClientGlobal.init(CONF_FILENAME);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            String fileIds[] = storageClient.upload_file(path, extName,null);
//            System.out.println(fileIds.length);
//            System.out.println("组名：" + fileIds[0]);
//            System.out.println("路径: " + fileIds[1]);
            String filePath = "http://39.106.45.72:8888"+"/"+fileIds[0]+"/"+fileIds[1];
            logger.debug("文件地址:"+filePath);
            logger.debug("<---------------- end 上传文件到fastdfs ------------------>");
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * 下载文件
     */
    public static byte[] download(String groupName,String fileName) {
        try {
            ClientGlobal.init(CONF_FILENAME);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            byte[] b = storageClient.download_file(groupName, fileName);
            return  b;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * 删除文件
     */
    public static void delete(String groupName,String fileName){
        try {
            ClientGlobal.init(CONF_FILENAME);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            int i = storageClient.delete_file(groupName,fileName);
            System.out.println( i==0 ? "删除成功" : "删除失败:"+i);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("删除异常,"+e.getMessage());
        }
    }
}

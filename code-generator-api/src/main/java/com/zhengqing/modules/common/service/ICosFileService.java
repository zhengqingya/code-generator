package com.zhengqing.modules.common.service;

import java.io.File;

/**
 * 腾讯对象存储接口
 * https://cloud.tencent.com/document/product/436/10199
 *
 * @author xiaojie
 * @date 2017/3/9
 */
public interface ICosFileService {

    /**
     * 上传图片
     *
     * @param key
     * @param localFilePath
     * @return
     */
    String uploadFile(String key, String localFilePath);

    /**
     * 上传图片
     *
     * @param key
     * @param localFile
     * @return
     */
    String uploadFile(String key, File localFile);
}

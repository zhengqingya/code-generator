package com.zhengqing.modules.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.zhengqing.modules.common.service.ICosFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.Date;

@Service
@Slf4j
public class CosFileServiceImpl implements ICosFileService {

    private static COSClient cosclient;
    private static String bucketName;

    @PostConstruct
    public void init() {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials("AKIDoTv9B0wzA7rNgmJMr8B1d2ECwTmrjNo1", "A6pKbeU6VgeOyLUP6aqshIfplTyFI7i6");
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region("ap-chengdu"));
        // 3 生成cos客户端
        cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        bucketName = "jxi-1251616562";
    }

    @Override
    public String uploadFile(String key, String filePath) {
        return uploadFile(key, new File(filePath));
    }

    @Override
    public String uploadFile(String key, File localFile) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        cosclient.putObject(putObjectRequest);
        Date expiration = new Date(new Date().getTime() + 5 * 60 * 10000);
        URL url = cosclient.generatePresignedUrl(bucketName, key, expiration);
        log.debug(JSON.toJSONString(url));
        return url.toString();
    }
}

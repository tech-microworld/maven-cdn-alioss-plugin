/*
 * Copyright (c) 2016 - 2017, fengjx.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fengjx.maven.cdn.common;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 阿里云oss管理工具
 */
public final class AliyunOssUtills {

    private static final int MAX_KEYS = 1000;

    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String bucketName;
    private static boolean supportCname;

    private AliyunOssUtills() {
    }

    public synchronized static void init(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
            boolean supportCname) {
        AliyunOssUtills.endpoint = endpoint;
        AliyunOssUtills.accessKeyId = accessKeyId;
        AliyunOssUtills.accessKeySecret = accessKeySecret;
        AliyunOssUtills.bucketName = bucketName;
        AliyunOssUtills.supportCname = supportCname;
    }

    public static boolean isInited() {
        return StrUtils.notBlank(AliyunOssUtills.endpoint) && StrUtils.notBlank(AliyunOssUtills.accessKeyId)
                && StrUtils.notBlank(AliyunOssUtills.accessKeySecret) && StrUtils.notBlank(AliyunOssUtills.bucketName);
    }

    private static OSSClient newOSSClient() {
        // 创建ClientConfiguration实例，按照您的需要修改默认参数
        ClientConfiguration conf = new ClientConfiguration();
        // 开启支持CNAME选项
        conf.setSupportCname(supportCname);
        return new OSSClient(endpoint, accessKeyId, accessKeySecret, conf);
    }

    public static void createBucket(String bucketName) {
        OSSClient ossClient = newOSSClient();
        try {
            ossClient.createBucket(bucketName);
        } finally {
            ossClient.shutdown();
        }
    }

    public static List<Bucket> listBuckets() {
        OSSClient ossClient = newOSSClient();
        try {
            return ossClient.listBuckets();
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 上传文件
     *
     * @param path 文件路径
     * @param file 文件对象
     */
    public static void putObject(String path, File file) {
        OSSClient ossClient = newOSSClient();
        try {
            ossClient.putObject(bucketName, path, file);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 上传文件
     *
     * @param path 文件路径
     * @param file 文件对象
     * @param meta 媒体类型
     */
    public static void putObject(String path, File file, ObjectMetadata meta) {
        OSSClient ossClient = newOSSClient();
        try {
            ossClient.putObject(bucketName, path, file, meta);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 上传byte数组
     *
     * @param path 文件路径
     * @param data byte数组
     * @param meta 媒体类型
     */
    public static void putObject(String path, byte[] data, ObjectMetadata meta) {
        OSSClient ossClient = newOSSClient();
        try {
            putObject(path, new ByteArrayInputStream(data), meta);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 上传文件流
     *
     * @param path 文件路径
     * @param in 上传文件流
     * @param meta 媒体类型
     */
    public static void putObject(String path, InputStream in, ObjectMetadata meta) {
        OSSClient ossClient = newOSSClient();
        try {
            ossClient.putObject(bucketName, path, in, meta);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 查询文件列表每次最多查询100条
     * @return 文件刘表对象
     */
    public static ObjectListing listObjects() {
        ListObjectsRequest request = new ListObjectsRequest();
        request.setMaxKeys(MAX_KEYS);
        request.setBucketName(bucketName);
        return listObjects(request);
    }

    /**
     * 查询文件列表每次最多查询100条
     *
     * @param prefix 路径前缀
     * @return 文件刘表对象
     */
    public static ObjectListing listObjects(String prefix) {
        ListObjectsRequest request = new ListObjectsRequest();
        request.setMaxKeys(MAX_KEYS);
        request.setBucketName(bucketName);
        request.setPrefix(prefix);
        return listObjects(request);
    }

    public static ObjectListing listObjects(ListObjectsRequest listObjectsRequest) {
        OSSClient ossClient = newOSSClient();
        try {
            return ossClient.listObjects(listObjectsRequest);
        } finally {
            ossClient.shutdown();
        }
    }


}

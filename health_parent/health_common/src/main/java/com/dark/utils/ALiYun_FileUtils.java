package com.dark.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

//阿里云文件处理工具类
public class ALiYun_FileUtils {
    /**
     * 上传文件-byte数组
     *
     * @param bucketName 云存储空间名称
     * @param fileName   要存储的文件名称
     * @param bytes      文件对应的字节流
     */
    public static void upload(String bucketName, String fileName, byte[] bytes) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FghAjbhHn4fAkimSrSQ";
        String accessKeySecret = "vHJbKH1k7IcZeJ05s2WZ4SCuF9mZwH";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 上传文件-输入流
     *
     * @param bucketName  云存储空间名称
     * @param fileName    要存储的文件名称
     * @param inputStream 文件对应的网络流或文件流
     */
    public static void upload(String bucketName, String fileName, InputStream inputStream) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4FghAjbhHn4fAkimSrSQ";
        String accessKeySecret = "vHJbKH1k7IcZeJ05s2WZ4SCuF9mZwH";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        ossClient.putObject(bucketName, fileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 上传本地文件-系统磁盘路径
     *
     * @param bucketName 云存储空间名称
     * @param fileName   要存储的文件名称
     * @param filePath   本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt
     */
    public static void upload(String bucketName, String fileName, String filePath) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FghAjbhHn4fAkimSrSQ";
        String accessKeySecret = "vHJbKH1k7IcZeJ05s2WZ4SCuF9mZwH";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件。
        ossClient.putObject(bucketName, fileName, new File(filePath));

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 删除单个文件
     *
     * @param bucketName 云存储空间名称
     * @param fileName   要删除的文件名称
     */
    public static void delete(String bucketName, String fileName) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FghAjbhHn4fAkimSrSQ";
        String accessKeySecret = "vHJbKH1k7IcZeJ05s2WZ4SCuF9mZwH";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。
        ossClient.deleteObject(bucketName, fileName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 批量删除
     *
     * @param bucketName 云存储空间名称
     * @param fileNames  要删除的文件名称集合
     * @return 删除成功的文件名称集合
     */
    public static List<String> delete(String bucketName, List<String> fileNames) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FghAjbhHn4fAkimSrSQ";
        String accessKeySecret = "vHJbKH1k7IcZeJ05s2WZ4SCuF9mZwH";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(fileNames));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();

        // 关闭OSSClient。
        ossClient.shutdown();

        return deletedObjects;
    }
}

package com.dark.jobs;

import com.dark.constant.RedisConstant;
import com.dark.utils.ALiYun_FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 定时清理垃圾图片
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        //获取redis中图片名称大小set集合的差集
        Set<String> subSet =
                jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //判断差集是否为空
        if (subSet != null && subSet.size() > 0) {
            //set --> list
            List<String> fileList = new ArrayList<>(subSet);
            //清除阿里云存储空间内的垃圾图片
            ALiYun_FileUtils.delete("practice01", fileList);
            //清除大set集合中的垃圾图片名称
            String[] fileArray = (String[]) fileList.toArray();
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, fileArray);
        }
    }
}

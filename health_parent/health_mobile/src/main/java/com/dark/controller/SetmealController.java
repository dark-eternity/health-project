package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.constant.MessageConstant;
import com.dark.constant.RedisConstant;
import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.entity.Result;
import com.dark.pojo.CheckGroup;
import com.dark.pojo.Setmeal;
import com.dark.service.CheckGroupService;
import com.dark.service.SetmealService;
import com.dark.utils.ALiYun_FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @Reference
    private CheckGroupService checkGroupService;
    @Autowired
    private Result result;
    @Autowired
    private PageResult pageResult;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(path = "/upload")
    public Result upload(@RequestParam(name = "imgFile") MultipartFile image) {
        try {
            //图片上传成功
            //获取文件字节数组
            byte[] bytes = image.getBytes();
            //获取文件名
            String originalFilename = image.getOriginalFilename();
            //获取文件类型
            String extension = FilenameUtils.getExtension(originalFilename);
            //生成唯一文件名
            String fileName = UUID.randomUUID().toString() + "." + extension;
            //使用工具类上传文件
            ALiYun_FileUtils.upload("practice02", fileName, bytes);
            //设置响应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.PIC_UPLOAD_SUCCESS);
            result.setData(fileName);
            //将上传成功的文件的文件名保存到redis的set大集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
        } catch (Exception ex) {
            //图片上传失败
            //设置响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.PIC_UPLOAD_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/add")
    public Result add(Integer[] checkgroupIds, @RequestBody Setmeal setmeal) {
        try {
            //业务逻辑处理正常
            setmealService.add(checkgroupIds, setmeal);
            //设置响应信息
            result.setFlag(true);
            result.setMessage(MessageConstant.ADD_SETMEAL_SUCCESS);
            result.setData(null);
            //将添加成功的套餐信息中的文件的文件名保存到redis的set小集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.ADD_SETMEAL_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            //业务逻辑处理正常
            pageResult = setmealService.findByPage(queryPageBean);
            //设置返回数据
            return pageResult;
        } catch (Exception ex) {
            //业务逻辑处理异常
            //返回null
            return null;
        }
    }

    @RequestMapping(path = "/delete")
    public Result delete(@RequestBody Setmeal setmeal) {
        try {
            //业务逻辑处理正常
            setmealService.deleteById(setmeal.getId());
            //设置正确响应信息
            result.setFlag(true);
            result.setMessage("套餐信息删除成功！");
            result.setData(null);
            //删除redis连接池中set小集合内的指定套餐对应的文件名
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage("套餐信息删除失败！");
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/findMealAnnoGroup")
    public Result findMealAnnoGroup(Integer id) {
        try {
            //业务逻辑处理正常
            List<Integer> list = setmealService.findMealAnnoGroup(id);
            //设置正确响应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.QUERY_SETMEAL_SUCCESS);
            result.setData(list);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_SETMEAL_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/update")
    public Result update(Integer[] checkgroupIds, @RequestBody Setmeal setmeal) {
        try {
            //根据id获取对应的旧套餐数据
            Setmeal oldSetmeal = setmealService.findById(setmeal.getId());
            //业务逻辑处理正常
            setmealService.update(checkgroupIds, setmeal);
            //设置正确响应信息
            result.setFlag(true);
            result.setMessage("套餐信息更改成功！");
            result.setData(null);
            //判断图片是否更改
            String oldImg = oldSetmeal.getImg();
            String newImg = setmeal.getImg();
            //新照存
            if (newImg != null && newImg.length() > 0) {
                //老照存
                if (oldImg != null && oldImg.length() > 0) {
                    //照片更改
                    if (!oldImg.equals(newImg)) {
                        //删除redis中的set小集合中指定的旧图片名数据
                        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, oldImg);
                        //往redis中的set小集合中添加新图片名数据
                        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, newImg);
                    }
                } else {
                    //老照无
                    //往redis中的set小集合中添加新图片名数据
                    jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, newImg);
                }
            }
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage("套餐信息更改失败！");
            result.setData(null);
        }
        return result;
    }



    @RequestMapping(path = "/getSetmeal")
    public Result getSetmeal() {
        try {
            //业务逻辑处理正常
            List<Setmeal> list = setmealService.findAll();
            //设置正确响应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.QUERY_SETMEAL_SUCCESS);
            result.setData(list);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_SETMEAL_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/findById")
    public Result findById(Integer id) {
        try {
            //业务逻辑处理正常
            Setmeal setmeal = setmealService.findMsg(id);
            //设置正确响应信息
            result.setFlag(true);
            result.setMessage(MessageConstant.QUERY_SETMEAL_SUCCESS);
            result.setData(setmeal);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_SETMEAL_FAIL);
            result.setData(null);
        }
        return result;
    }
}

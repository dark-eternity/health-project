package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.constant.MessageConstant;
import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.entity.Result;
import com.dark.pojo.Setmeal;
import com.dark.service.SetmealService;
import com.dark.utils.ALiYun_uploadUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping(path = "/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @Autowired
    private Result result;
    @Autowired
    private PageResult pageResult;

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
            ALiYun_uploadUtils.upload("practice02", fileName, bytes);
            //设置响应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.PIC_UPLOAD_SUCCESS);
            result.setData(fileName);
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
}

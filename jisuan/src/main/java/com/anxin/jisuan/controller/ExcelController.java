package com.anxin.jisuan.controller;

import com.alibaba.excel.EasyExcel;
import com.anxin.jisuan.dao.UploadDAO;
import com.anxin.jisuan.model.UploadData;
import com.anxin.jisuan.util.UploadDataListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Excel导入导出
 *
 * @author: ly
 * @date: 2019/11/20 15:21
 * https://alibaba-easyexcel.github.io/
 */
@Controller
public class ExcelController {

    @Autowired
    private UploadDAO uploadDAO;

    /**
     * 文件上传
     * <p>1. 创建excel对应的实体对象
     * <p>2. 由于默认异步读取excel，所以需要创建excel一行一行的回调监听器
     * <p>3. 直接读即可
     */
    @ApiOperation(value = "Excel导入简称", notes = "Excel导入描述")
    @PostMapping("upload")
    @ResponseBody
    public String upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), UploadData.class, new UploadDataListener(uploadDAO)).sheet().doRead();
        return "success";
    }
}

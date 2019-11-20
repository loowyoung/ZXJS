package com.anxin.jisuan.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: ly
 * @date: 2019/11/20 15:25
 */
@Data
public class UploadData {
    @ExcelProperty("姓名")
    private String string;
    @ExcelProperty("时间")
    private Date date;
    @ExcelProperty("金额")
    private Double doubleData;
}

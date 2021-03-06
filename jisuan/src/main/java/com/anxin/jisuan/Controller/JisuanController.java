package com.anxin.jisuan.controller;

import com.anxin.jisuan.model.CountParam;
import com.anxin.jisuan.model.CountVo;
import com.anxin.jisuan.service.Count;
import com.anxin.jisuan.util.FileOperation;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: ly
 * @date: 2018/12/7 10:14
 */
@CrossOrigin
@Controller
public class JisuanController {

    @Autowired
    private FileOperation fileOperation;

    @Value("${SLAB.filePath}")
    private String SlabPath; //OUTPR4 文件位置

    @ApiOperation(value = "计算示例value", notes = "计算示例notes")
    @PostMapping("/getCount")
    @ResponseBody
    public List<CountVo> getCount(@RequestBody CountParam params) {
        String[][] arr = new String[0][];
        try {
            arr = fileOperation.getFileArray(SlabPath + "/OUTPR4");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Count count = new Count();
        List<CountVo> result = count.sum(arr, params.getXc(), params.getBx(), params.getBetax());
        return result;
    }


}

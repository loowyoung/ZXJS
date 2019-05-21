package com.anxin.jisuan.Controller;

import com.anxin.jisuan.model.CountVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @GetMapping("/getCount")
    @ResponseBody
    public List<CountVo> getCount(double xc, double bx, double betax) {
        String[][] arr = new String[0][];
        try {
            arr = fileOperation.getFileArray(SlabPath + "/OUTPR4");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Count count = new Count();
        List<CountVo> result = count.sum(arr, xc, bx, betax);
        return result;
    }


}

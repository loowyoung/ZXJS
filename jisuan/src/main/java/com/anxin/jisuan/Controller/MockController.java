package com.anxin.jisuan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ly
 * @date: 2020/2/18 11:44
 */
@RestController
public class MockController {
    @GetMapping("/hello")
    public String helloZuoYang(String hi) {
        return hi + " 左羊";
    }
    @GetMapping("/query")
    public List<Map<String,Object>> query(String hi) {
        List<Map<String,Object>> dataList= new ArrayList<>();
        Map m=new HashMap();
        m.put("name","test");
        dataList.add(m);
        return dataList;
    }
}

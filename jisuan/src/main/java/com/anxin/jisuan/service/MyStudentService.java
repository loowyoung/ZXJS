package com.anxin.jisuan.service;

import com.anxin.jisuan.dao.MyStudentDao;
import com.anxin.jisuan.model.MyStudentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: ly
 * @date: 2020/2/13 14:25
 */
@Service
@Transactional
public class MyStudentService {
    @Autowired
    private MyStudentDao dao;

    public List<MyStudentModel> findAll() {
        return dao.findAll();
    }
}

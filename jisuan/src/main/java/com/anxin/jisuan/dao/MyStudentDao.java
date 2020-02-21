package com.anxin.jisuan.dao;

import com.anxin.jisuan.model.MyStudentModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: ly
 * @date: 2020/2/13 14:11
 */
@Mapper
public interface MyStudentDao {
    List<MyStudentModel> findAll();

    Object findMapBySql(String s, List<Object> objects);

    Object findTotalCountBySql(String s, Object any);
}

package com.anxin.accidentsimulation.dao;

import com.anxin.accidentsimulation.entity.dto.ChemicalModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: ly
 * @date: 2020/1/10 16:10
 */
@Mapper
public interface ChemicalDao {
    //查询所有化学物质
    List<ChemicalModel> list(@Param("sourceName") String sourceName);
}

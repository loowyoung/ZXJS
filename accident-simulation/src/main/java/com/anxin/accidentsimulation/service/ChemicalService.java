package com.anxin.accidentsimulation.service;

import com.anxin.accidentsimulation.dao.ChemicalDao;
import com.anxin.accidentsimulation.entity.dto.ChemicalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: ly
 * @date: 2020/1/10 16:17
 */
@Service
public class ChemicalService {
    @Autowired
    private ChemicalDao dao;

    /**
     * 查询所有化学物质
     *
     * @param sourceName
     * @return
     */
    public List<ChemicalModel> list(String sourceName) {
        return dao.list(sourceName);
    }

    public static void main(String[] args) {
        ChemicalService service = new ChemicalService();
        service.list("");
    }
}

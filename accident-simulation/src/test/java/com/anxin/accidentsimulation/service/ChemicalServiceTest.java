package com.anxin.accidentsimulation.service;

import com.anxin.accidentsimulation.entity.dto.ChemicalModel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: ly
 * @date: 2020/1/10 16:20
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class ChemicalServiceTest {
    @Autowired
    private ChemicalService service;

    @Test
    void list() {
        List<ChemicalModel> chemicalModels = service.list("");
        System.out.println(chemicalModels);
    }
}
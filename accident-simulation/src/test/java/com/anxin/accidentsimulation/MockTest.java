package com.anxin.accidentsimulation;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 接口测试基类，所有接口测试类继承此类
 *
 * @author: ly
 * @date: 2020/2/21 15:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccidentSimulationApplication.class)
@AutoConfigureMockMvc
public class MockTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}

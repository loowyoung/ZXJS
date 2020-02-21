package com.anxin.jisuan.controller;

import com.anxin.jisuan.JisuanApplication;
import com.anxin.jisuan.dao.MyStudentDao;
import javafx.application.Application;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.junit.Assert.*;

/**
 * mock组装数据模拟测试
 *
 * @author: ly
 * @date: 2020/2/18 12:00
 */
//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
@RunWith(SpringRunner.class)
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
@SpringBootTest(classes = JisuanApplication.class)
@AutoConfigureMockMvc
public class MockControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void helloZuoYang() throws Exception {
        String responseString = mockMvc.perform(MockMvcRequestBuilders.get("/hello?hi=hi") //请求的url,请求的方法是get
                .contentType(MediaType.APPLICATION_JSON_UTF8) //数据的格式
                .accept(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(MockMvcResultMatchers.status().isOk())  //返回的状态是200
                .andDo(MockMvcResultHandlers.print()) //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString(); //将相应的数据转换为字符

        System.out.println(responseString);
    }

    @MockBean
    private MyStudentDao studentDao;

    @Test
    public void TestLy() throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map m = new HashMap();
        m.put("name", "test");
        dataList.add(m);
        Mockito.when(studentDao.findMapBySql(Mockito.anyString(), Mockito.anyList())).thenReturn(dataList);
        Mockito.when(studentDao.findTotalCountBySql(Mockito.anyString(), Mockito.any())).thenReturn((long) 1);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/query")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("xxx", "xxx")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        System.out.println(result);
    }

    /**
     * 模拟iterator迭代
     * 多次触发返回不同值
     */
    @Test
    public void testIterator() {
        //mock一个Iterator类
        Iterator iterator = Mockito.mock(Iterator.class);
        //预设当iterator调用next()时第一次返回hello，第n次都返回world
        Mockito.when(iterator.next()).thenReturn("hello").thenReturn("world");
        //使用mock的对象
        String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
        //验证结果
        Assert.assertEquals("hello world world", result);
    }

    /**
     * 参数匹配
     */
    @Test
    public void with_arguments(){
        B b = Mockito.mock(B.class);
        //预设根据不同的参数返回不同的结果
        Mockito.when(b.getSex(1)).thenReturn("男");
        Mockito.when(b.getSex(2)).thenReturn("女");
        Assert.assertEquals("男", b.getSex(1));
        Assert.assertEquals("女", b.getSex(2));
        //对于没有预设的情况会返回默认值
        Assert.assertEquals(null, b.getSex(0));
    }
    class B{
        public String getSex(Integer sex){
            if(sex==1){
                return "man";
            }else{
                return "woman";
            }
        }
    }


}
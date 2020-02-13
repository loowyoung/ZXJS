package com.anxin.jisuan.service;

import com.anxin.jisuan.JisuanApplication;
import com.anxin.jisuan.model.MyStudentModel;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * org.dbunit.database.AmbiguousTableNameException: ACT_GE_BYTEARRAY
 * 因为SQL语句不好修改，若能修改，加上SCHEMA名或OWNER名应能消除该问题;
 * 一种解决方法是，缩窄当前数据库用户的访问权限，从dba角色降级为connect和resource角色。只能访问自己的schema下的表，从而避免表名重复。
 *
 * @author: ly
 * @date: 2020/2/13 14:27
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JisuanApplication.class)
@ActiveProfiles("local")//用户是test，只有test数据库权限
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
@Transactional("transactionManager")
public class MyStudentServiceTest {

    @Autowired
    private MyStudentService service;

    @Test
    @DatabaseSetup("/dbtest/sampleData.xml")
    public void findAll() {
        List<MyStudentModel> all = service.findAll();
        assertThat(all, is(notNullValue()));
        assertThat(all.size(), is(1));
    }
}
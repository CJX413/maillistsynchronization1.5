package com.weixin.maillistsynchronization;

import com.weixin.maillistsynchronization.Model.Department;
import com.weixin.maillistsynchronization.Service.DepartmentService;
import com.weixin.maillistsynchronization.Utils.Tools;
import com.weixin.maillistsynchronization.mapper.DepartmentDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class sendTest {
    @Autowired
    private DepartmentService service;
    @Autowired
    private DepartmentDao departmentDao;

    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    @Test
    public void send() {

    }

    //@Test
    public void test1() {

    }
}

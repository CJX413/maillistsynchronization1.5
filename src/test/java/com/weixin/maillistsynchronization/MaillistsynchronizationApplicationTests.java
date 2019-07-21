package com.weixin.maillistsynchronization;

import com.google.gson.JsonObject;
import com.weixin.maillistsynchronization.Model.Department;
import com.weixin.maillistsynchronization.mapper.DepartmentDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MaillistsynchronizationApplicationTests {
    @Autowired
    private DepartmentDao departmentDao;
    @Test
    public void contextLoads() {
        JsonObject object = new JsonObject();
        object.addProperty("name", "广州研发中心");
        object.addProperty("parentid", "1");
        System.out.println(object.toString());
    }

}

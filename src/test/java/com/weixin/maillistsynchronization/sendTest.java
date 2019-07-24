package com.weixin.maillistsynchronization;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.weixin.maillistsynchronization.Model.Department;
import com.weixin.maillistsynchronization.Model.Staff;
import com.weixin.maillistsynchronization.Service.DepartmentService;
import com.weixin.maillistsynchronization.Service.StaffService;
import com.weixin.maillistsynchronization.Utils.FileUtils;
import com.weixin.maillistsynchronization.Utils.HttpClientUtils;
import com.weixin.maillistsynchronization.Utils.Tools;
import com.weixin.maillistsynchronization.mapper.DepartmentDao;
import com.weixin.maillistsynchronization.mapper.StaffDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class sendTest {
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private StaffService staffService;
    @Autowired
    private DepartmentService departmentService;

    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    //@Test
    public void send() {
        String access_token="QxAGHZY0XYRnZ798cXoKFn1K60WM-eU7VytMW6oxKvcz3LeR5tGD029MJuQ8pqy20oiVEC9HNRYYrNEa7MUeYMejyTxR4KNpyNpbjvDYyhec59ZDABdK1iW0ZAyN-W7LBT2a8ERpPu1e3E4nCw7TCIAGIV4XTb6HV15XV8QfKW-B1R1DRrVXDAvt6CTM2Pi92BK_6btZRER7GT3VBKiZNQ";
        String createUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=" + access_token;
        Staff staff = new Staff();
        String result = new String();
        staff.setUserid("10");
        staff.setName("池亭其");
        staff.setDepartment(10);
        staff.setMobile("18534773165");
        staff.setEmail("hoaf1755657huang3@163.com");
        staff.setGender("1");
        staff.setPosition("组长");
        result = Tools.sendDate(staff, createUrl);
        System.out.println(result);


    }

    @Test
    public void test1() {
        String access_token="BFIWN1MOA9uIAC8VoT7HgJJV3iI4fFt436lKon2Khhuae1eM4kJ9_J1CI0d-kFLlfw-4iIgpGXAah2J2sTUoIMbCjrwcCxctZoYWOSly6gjafw4xz8aqLFBtWW29q1X1cH0RQ5UTwu8lCU1b-oZeN7OeIX9wxoZ6H-BLgtxY6mXLiAAL_T0Z1l_7VMvKRhSH-bxg28S0mVebI4brqt82fA";
        String path = "src/main/resources/static/CSV/";
        String type = ".csv";
        String sendfileUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=" + access_token + "&type=file";
        String replacepartyUrl = "https://qyapi.weixin.qq.com/cgi-bin/batch/replaceparty?access_token=" + access_token;
        String replaceuserUrl = "https://qyapi.weixin.qq.com/cgi-bin/batch/replaceuser?access_token=" + access_token;
        List<Department> departmentList = departmentService.queryAll();
        List<Staff> staffList = staffService.queryAll();
        List<List<Object>> departmentTable = Tools.toDepartmentCsv(departmentList);
        List<List<Object>> staffTable = Tools.toStaffCsv(staffList);
        String departmentname = path + Tools.getRandomString(15) + type;
        String staffname = path + Tools.getRandomString(15) + type;
        File departmentfile = new File(departmentname);
        File stafffile = new File(staffname);
        if (FileUtils.createCSV(departmentfile, departmentTable) && FileUtils.createCSV(stafffile, staffTable)) {
            if (Tools.fileSynchronization(sendfileUrl, replacepartyUrl, departmentfile) && Tools.fileSynchronization(sendfileUrl, replaceuserUrl, stafffile)) {
                System.out.println("同步成功");
            }
        }
    }
}

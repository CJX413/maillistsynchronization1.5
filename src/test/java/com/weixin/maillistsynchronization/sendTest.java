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
    private DepartmentService service;

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

    //@Test
    public void test1() {
        List<Staff> list=staffService.getAllStaff();
        List<List<Object>> date=new ArrayList<>();
        List<Object> rowList;
        String head="姓名,帐号,手机号,邮箱,所在部门,职位,性别,是否部门内领导,排序,别名,地址,座机,禁用,禁用项说明：(0-启用;1-禁用)";
        rowList =new ArrayList<>();
        rowList.add(head);
        date.add(rowList);
        for (int i = 1; i < list.size(); i++) {
            rowList = new ArrayList<>();
            rowList.add(list.get(i).getName()+",");
            rowList.add(list.get(i).getUserid()+",");
            rowList.add(list.get(i).getMobile()+",");
            rowList.add(list.get(i).getEmail()+",");
            rowList.add(list.get(i).getDepartment()+",");
            rowList.add(list.get(i).getPosition()+",");
            rowList.add(list.get(i).getGender()+",");
            rowList.add(",");
            rowList.add(",");
            rowList.add(",");
            rowList.add(",");
            rowList.add(",");
            rowList.add("0,");
            date.add(rowList);
        }
        File file=new File("src/main/resources/static/CSV/batch_user_sample.csv");
        File newfile=new File("src/main/resources/static/CSV/csv1.csv");
        //FileUtils.copyFileByStream(file,newfile);
        FileUtils.createCSV(newfile,date);
        String access_token="5PFc5fsqTVjfXbl36kYXOiXIjzmMik-ijmE4_r2aVcL_6N2USBOpkzNUw4-4FxiLe8Q8XB2xOCCLMZJtDslMRMIgkxtGF3YteGXKOdrEk58mLuzp1Bcgapvc_Hw-9zQW_vrnBBzKuTMbDxn6N7mfYVQzwSWXyKrQvGh7R485z-z-qKXKcBJRWQPmrBLyp0psfTycV6t_MUS0z3XtDEFwBg";
        String url="https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token="+access_token+"&type=file";
        try {
            System.out.println(HttpClientUtils.postFile(url,newfile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

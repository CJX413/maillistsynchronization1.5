package com.weixin.maillistsynchronization.Controller;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.weixin.maillistsynchronization.Model.Department;
import com.weixin.maillistsynchronization.Model.Staff;
import com.weixin.maillistsynchronization.Service.DepartmentService;
import com.weixin.maillistsynchronization.Service.StaffService;
import com.weixin.maillistsynchronization.Utils.HttpClientUtils;
import com.weixin.maillistsynchronization.Utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class DataController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private StaffService staffService;

    /**
     * 通讯录同步
     *
     * @param access_token
     * @return
     */
    @PostMapping("/synchronization")
    public String Synchronization(@RequestParam(value = "access_token") String access_token) {
        String resultDepartment;
        String resultStaff;
        String departmentUrl = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=" + access_token;
        String staffUrl="https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=" + access_token;
        List<Department> DepartmentList = departmentService.getDepartmentTree();
        List<Staff> staffList=staffService.getAllStaff();
        resultDepartment=Tools.sendDate(DepartmentList,departmentUrl);
        resultStaff=Tools.sendDate(staffList,staffUrl);
        if(resultDepartment.equals("上传成功")&&resultStaff.equals("上传成功")){
                return "通讯录同步成功";
        }else if(resultDepartment.equals("access_token已失效")||resultStaff.equals("access_token已失效")){
            return "access_token已失效";
        }else {
            return "通讯录同步失败";
        }
    }

    /**
     * 获取access_token
     *
     * @return
     */
    @PostMapping("/access_token")
    public String get_access_token(@RequestParam(value = "corpid") String corpid,
                                   @RequestParam(value = "corpsecret") String corpsecret) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret;
        JsonObject json = HttpClientUtils.sendPost(url,null);
        if(json.get("errcode").equals("0")){
            return json.get("access_token").toString();
        }else {
            return json.toString();
        }

    }
}

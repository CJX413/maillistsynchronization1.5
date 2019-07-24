package com.weixin.maillistsynchronization.Controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.weixin.maillistsynchronization.Model.Department;
import com.weixin.maillistsynchronization.Model.Staff;
import com.weixin.maillistsynchronization.Service.DepartmentService;
import com.weixin.maillistsynchronization.Service.StaffService;
import com.weixin.maillistsynchronization.Utils.FileUtils;
import com.weixin.maillistsynchronization.Utils.HttpClientUtils;
import com.weixin.maillistsynchronization.Utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
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
        Gson gson = new Gson();
        String departmentUrl = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=" + access_token;
        String staffUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=" + access_token;
        List<Department> DepartmentList = departmentService.getDepartmentTree();
        List<Staff> staffList = staffService.getAllStaff();
        if (!DepartmentList.isEmpty() && !staffList.isEmpty()) {
            resultDepartment = Tools.sendDate(DepartmentList, departmentUrl);
            resultStaff = Tools.sendDate(staffList, staffUrl);
            if (resultDepartment.equals("0") && resultStaff.equals("0")) {
                return "通讯录同步成功";
            } else if (resultDepartment.equals("access_token已失效") || resultStaff.equals("access_token已失效")) {
                return "access_token已失效";
            } else {
                return Tools.erroMessage(resultDepartment, resultStaff);
            }
        } else {
            return "数据表错误";
        }
    }

    /**
     * 另一种通讯录同步
     */
    @PostMapping("/synchronization1")
    public String Synchronization1(@RequestParam(value = "access_token") String access_token) {
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
                departmentfile.delete();
                stafffile.delete();
                return "通讯录同步成功";
            }
        }
        return "同步失败";
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
        JsonObject json = HttpClientUtils.sendPost(url, null);
        if (json.get("errcode").toString().equals("0")) {
            return json.get("access_token").toString();
        } else {
            return json.toString();
        }
    }

    /**
     * 更新部门
     *
     * @return
     */
    @PostMapping("/department/update")
    public String departmentUpdate(@RequestParam(value = "access_token") String access_token,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "parentid", required = false) String parentid,
                                   @RequestParam(value = "id") String id) {
        String updateUrl = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=" + access_token;
        Department department = new Department();
        String result = new String();
        department.setId(Integer.valueOf(id));
        department.setName(name);
        department.setParentid(Integer.valueOf(parentid));
        if (departmentService.update(department) == 1) {
            result = Tools.sendDate(department, updateUrl);
        }
        if (result.equals("0")) {
            return "修改成功";
        } else
            return "修改失败";
    }

    /**
     * 删除部门
     *
     * @return
     */
    @PostMapping("/department/delete")
    public String departmentDelete(@RequestParam(value = "access_token") String access_token,
                                   @RequestParam(value = "id") String id) {
        String deleteUrl = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=" + access_token + "&id=" + id;
        String result = new String();
        if (departmentService.deleteById(Integer.valueOf(id)) == 1) {
            result = Tools.sendDate(deleteUrl);
        }
        if (result.equals("0")) {
            return "删除成功";
        } else
            return "删除失败";
    }

    /**
     * 新建部门
     *
     * @return
     */
    @PostMapping("/department/create")
    public String departmentCreate(@RequestParam(value = "access_token") String access_token,
                                   @RequestParam(value = "name") String name,
                                   @RequestParam(value = "parentid") String parentid,
                                   @RequestParam(value = "id", required = false) String id) {
        String updateUrl = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=" + access_token;
        Department department = new Department();
        String result = new String();
        department.setId(Integer.valueOf(id));
        department.setName(name);
        department.setParentid(Integer.valueOf(parentid));
        if (departmentService.insert(department) == 1) {
            result = Tools.sendDate(department, updateUrl);
        }
        if (result.equals("0")) {
            return "新建成功";
        } else
            return "新建失败";
    }


    /**
     * 更新员工
     *
     * @return
     */
    @PostMapping("/user/update")
    public String userUpdate(@RequestParam(value = "access_token") String access_token,
                             @RequestParam(value = "userid") String userid,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "department", required = false) String department,
                             @RequestParam(value = "mobile", required = false) String mobile,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "gender", required = false) String gender,
                             @RequestParam(value = "position", required = false) String position) {
        String updateUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=" + access_token;
        Staff staff = new Staff();
        String result = new String();
        staff.setUserid(userid);
        staff.setName(name);
        staff.setDepartment(Integer.valueOf(department));
        staff.setMobile(mobile);
        staff.setEmail(email);
        staff.setGender(gender);
        staff.setPosition(position);
        if (staffService.update(staff) == 1) {
            result = Tools.sendDate(staff, updateUrl);
        }
        if (result.equals("0")) {
            return "修改成功";
        } else
            return "修改失败";
    }

    /**
     * 删除员工
     *
     * @return
     */
    @PostMapping("/user/delete")
    public String userDelete(@RequestParam(value = "access_token") String access_token,
                             @RequestParam(value = "userid") String userid) {
        String deleteUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=" + access_token + "&userid=" + userid;
        String result = new String();
        if (staffService.deleteByUserId(userid) == 1) {
            result = Tools.sendDate(deleteUrl);
        }
        if (result.equals("0")) {
            return "删除成功";
        } else
            return "删除失败";
    }

    /**
     * 新建员工
     *
     * @return
     */
    @PostMapping("/user/create")
    public String userCreate(@RequestParam(value = "access_token") String access_token,
                             @RequestParam(value = "userid") String userid,
                             @RequestParam(value = "name") String name,
                             @RequestParam(value = "department") String department,
                             @RequestParam(value = "mobile") String mobile,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "gender") String gender,
                             @RequestParam(value = "position") String position) {
        String createUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=" + access_token;
        Staff staff = new Staff();
        String result = new String();
        staff.setUserid(userid);
        staff.setName(name);
        staff.setDepartment(Integer.valueOf(department));
        staff.setMobile(mobile);
        staff.setEmail(email);
        staff.setGender(gender);
        staff.setPosition(position);
        if (staffService.insert(staff) == 1) {
            result = Tools.sendDate(staff, createUrl);
        }
        if (result.equals("0")) {
            return "新建成功";
        } else
            return "新建失败";

    }

}

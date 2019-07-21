package com.weixin.maillistsynchronization.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.weixin.maillistsynchronization.Model.Department;
import com.weixin.maillistsynchronization.Model.Staff;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Tools {
    /**
     * 检查Department的数据是否符合要求
     *
     * @param list
     * @return
     */
    public static boolean checkDepartmentDate(List<Department> list) {
        HashSet<Integer> hashSet = new HashSet<>();
        list.forEach(department -> hashSet.add(department.getId()));
        for (Department department:list) {
            if (!hashSet.contains(department.getParentid())) {
                return false;
            }
        }
        return true;
    }
    /**
     * 检查员工表是否合法
     * @param staffList
     * @param departmentList
     * @return
     */
    public static boolean checkStaffDate(List<Staff> staffList, List<Department> departmentList) {
        HashSet<Integer> hashSet = new HashSet<>();
        departmentList.forEach(department -> hashSet.add(department.getId()));
        for (Staff staff : staffList) {
            if (!hashSet.contains(staff.getDepartment())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 发送数据到微信服务器
     */
    public static String sendDate(List<?> list, String url) {
        int succseflag = 0;
        long time = 0;
        boolean invalid = false;
        Date start = new Date();
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        while (succseflag < list.size() && (time / (1000 * 60)) < 20) {
            JsonObject object = gson.fromJson(gson.toJson(list.get(succseflag)), JsonObject.class);
            JsonObject result = HttpClientUtils.sendPost(url, object);
            if (result.get("errcode").toString().equals("0")) {
                succseflag++;
            } else if (result.get("errcode").toString().equals("42001")) {
                //过期了
                invalid = true;
                break;
            } else if (result.get("errcode").toString().equals("60008")) {
                //部门已创建
                succseflag++;
            } else {
                jsonArray.add(object);
            }
            Date now = new Date();
            time = now.getTime() - start.getTime();
        }
        if (succseflag == list.size()) {
            return "上传成功";
        } else if (invalid) {
            return "access_token已失效";
        } else {
            return jsonArray.toString();
        }
    }
}

package com.weixin.maillistsynchronization.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.weixin.maillistsynchronization.Model.Department;
import com.weixin.maillistsynchronization.Model.Staff;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

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
        for (Department department : list) {
            if (!hashSet.contains(department.getParentid()) && department.getParentid() != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查员工表是否合法
     *
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
        boolean invalid = false;
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < list.size(); i++) {
            JsonObject object = gson.fromJson(gson.toJson(list.get(i)), JsonObject.class);
            System.out.println(object);
            JsonObject result = HttpClientUtils.sendPost(url, object);
            System.out.println(result);
            if (result.get("errcode").toString().equals("0")) {
                succseflag++;
            } else if (result.get("errcode").toString().equals("42001")) {
                //过期了
                invalid = true;
                break;
            } else if (result.get("errcode").toString().equals("60008") || result.get("errcode").toString().equals("60104")
                    || result.get("errcode").toString().equals("60102") || result.get("errcode").toString().equals("60106")) {
                succseflag++;
            } else {
                jsonArray.add(object);
            }
        }
        if (succseflag == list.size()) {
            return "0";
        } else if (invalid) {
            return "access_token已失效";
        } else {
            return jsonArray.toString();
        }
    }

    public static String sendDate(Object object, String url) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(gson.toJson(object), JsonObject.class);
        JsonObject result = HttpClientUtils.sendPost(url, jsonObject);
        if (result.get("errcode").toString().equals("0")) {
            return "0";
        } else
            System.out.println(result);
        return "上传失败";
    }

    public static String sendDate(String url) {
        JsonObject result = HttpClientUtils.sendPost(url, null);
        if (result.get("errcode").toString().equals("0")) {
            return "0";
        } else
            return "上传失败";
    }

    /**
     * 发送同步失败的数据
     *
     * @param resultDepartment
     * @param resultStaff
     * @return
     */
    public static String erroMessage(String resultDepartment, String resultStaff) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        JsonArray jsonArray;
        if (!resultDepartment.equals("0") && resultStaff.equals("0")) {
            jsonArray = gson.fromJson(resultDepartment, JsonArray.class);
            jsonObject.add("department", jsonArray);
            jsonObject.addProperty("errostaff", 0);
            jsonObject.addProperty("errodepartment", 1);
            return jsonObject.toString();
        } else if (resultDepartment.equals("0") && !resultStaff.equals("0")) {
            jsonArray = gson.fromJson(resultStaff, JsonArray.class);
            jsonObject.add("staff", jsonArray);
            jsonObject.addProperty("errostaff", 1);
            jsonObject.addProperty("errodepartment", 0);
            return jsonObject.toString();
        } else {
            jsonArray = gson.fromJson(resultDepartment, JsonArray.class);
            jsonObject.add("department", jsonArray);
            jsonObject.addProperty("errodepartment", 1);
            jsonArray = gson.fromJson(resultStaff, JsonArray.class);
            jsonObject.add("staff", jsonArray);
            jsonObject.addProperty("errostaff", 1);
            return jsonObject.toString();
        }
    }

    /**
     * List《Department》转表格
     */
    public static List<List<Object>> toDepartmentCsv(List<Department> list) {
        List<List<Object>> date = new ArrayList<>();
        List<Object> rowList;
        String head = "部门名称,部门ID,父部门ID,排序";
        rowList = new ArrayList<>();
        rowList.add(head);
        date.add(rowList);
        for (int i = 1; i < list.size(); i++) {
            rowList = new ArrayList<>();
            rowList.add(list.get(i).getName() + ",");
            rowList.add(list.get(i).getId() + ",");
            rowList.add(list.get(i).getParentid() + ",");
            date.add(rowList);
        }
        return date;
    }

    /**
     * List《Staff》转表格
     */
    public static List<List<Object>> toStaffCsv(List<Staff> list) {
        List<List<Object>> date = new ArrayList<>();
        List<Object> rowList;
        String head = "姓名,帐号,手机号,邮箱,所在部门,职位,性别,是否部门内领导,排序,别名,地址,座机,禁用,禁用项说明：(0-启用;1-禁用)";
        rowList = new ArrayList<>();
        rowList.add(head);
        date.add(rowList);
        for (int i = 1; i < list.size(); i++) {
            rowList = new ArrayList<>();
            rowList.add(list.get(i).getName() + ",");
            rowList.add(list.get(i).getUserid() + ",");
            rowList.add(list.get(i).getMobile() + ",");
            rowList.add(list.get(i).getEmail() + ",");
            rowList.add(list.get(i).getDepartment() + ",");
            rowList.add(list.get(i).getPosition() + ",");
            rowList.add(list.get(i).getGender() + ",");
            rowList.add(",");
            rowList.add(",");
            rowList.add(",");
            rowList.add(",");
            rowList.add(",");
            rowList.add("0,");
            date.add(rowList);
        }
        return date;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static boolean fileSynchronization(String updatefileUrl, String replaceUrl, File file) {
        JsonObject result = null;
        try {
            result = HttpClientUtils.postFile(updatefileUrl, file);
            if(result.get("errcode").toString().equals("0")) {
                JsonElement media_id = result.get("media_id");
                JsonObject json = new JsonObject();
                json.add("media_id",media_id);
                //json.addProperty("to_invite", true);
                System.out.println(json.toString());
                result = HttpClientUtils.sendPost(replaceUrl, json);
                if (result.get("errcode").toString().equals("0")) {
                    return true;
                } else {
                    return false;
                }
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

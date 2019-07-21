package com.weixin.maillistsynchronization.Service;


import com.weixin.maillistsynchronization.Model.Department;
import com.weixin.maillistsynchronization.Model.Staff;
import com.weixin.maillistsynchronization.Utils.Tools;
import com.weixin.maillistsynchronization.mapper.DepartmentDao;
import com.weixin.maillistsynchronization.mapper.StaffDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImp implements StaffService{

    @Autowired
    private StaffDao staffDao;
    @Autowired
    private DepartmentDao departmentDao;
    /**
     * 新建一个员工
     */
    public int insert(Staff staff){

        int insert=staffDao.insert(staff);
        return insert;
    }
    /**
     * 查询所有员工
     */
    public List<Staff> queryAll(){

        List<Staff> staffList=staffDao.queryAll();
        return staffList;

    }
    /**
     * 经过校验后返回所有职员
     */
    public List<Staff> getAllStaff(){
        List<Staff> staffList=staffDao.queryAll();
        List<Department> departmentList=departmentDao.queryAll();
        if(Tools.checkStaffDate(staffList,departmentList)){
            return staffList;
        }else {
            return null;
        }
    }
    /**
     * 按userID查询
     */
    public  List<Staff> queryByUserId(String userId){
        List<Staff> list=staffDao.queryByUserId(userId);
        return  list;
    }
    /**
     * 按名字查询
     */
    public  List<Staff> queryByName(String name){
        List<Staff> list=staffDao.queryByName(name);
        return  list;
    }
    /**
     * 按部门ID查询
     */
    public List<Staff> queryByDepartment(Integer department)
    {
        List<Staff> list=staffDao.queryByDepartment(department);
        return  list;
    }
    /**
     * 按部门职务信息查询
     */
    //public  List<Staff> queryByPosition(String position){

    // }
    /**
     * 根据userID删除该员工
     */
    public  int deleteByUserId(String userId){
        staffDao.deleteByUserId(userId);
        return 1;
    }
    /**
     * 根据部门ID删除该部门的所有员工
     */
    public  int deleteByDepartment(Integer department){
        staffDao.deleteByDepartment(department);
        return 1;
    }
    /**
     * 根据userId更新员工信息
     */
    public int update(Staff staff){
        int update=staffDao.update(staff);
        return  update;
    }

}

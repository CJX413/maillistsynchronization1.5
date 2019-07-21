package com.weixin.maillistsynchronization.Service;

import com.google.gson.JsonArray;
import com.weixin.maillistsynchronization.Model.Department;
import java.util.List;

public interface DepartmentService {
    /**
     * 新建一个部门
     */
    int insert(Department department);

    /**
     * 查询整个部门
     */
    List<Department> queryAll();

    /**
     * 查询该部门的直接上级部门
     * 输入该部门的parentId
     */
    List<Department> queryParentDepartment(Department department);

    /**
     * 查询该部门的直接下级部门
     * 输入该部门的Id
     */

    //List<Department> querySonDepartment(Department department);

    /**
     * 按ID查询
     */
    List<Department> queryById(Integer id);

    /**
     * 按ParentID查询
     */
    List<Department> queryByParentId(Integer parentId);

    /**
     * 按部门名称查询
     */
    List<Department> queryByName(String name);

    /**
     * 根据ID删除该部门
     */
    int deleteById(Integer id);

    /**
     * 根据ParentID删除该部门
     */
    int deleteByParentId(Integer id);

    /**
     * 根据学生ID更新部门信息
     */
    int update(Department student);

    /**
     * 查询该部门的所有下级部门
     * 输入该部门的id
     */
    //List<Department> queryAllSonDepartment(Department department);

    /**
     * 查询该部门的所有上级部门
     * 输入该部门的parentId
     */
    //List<Department> queryAllParentDepartment(Department department);
    /**
     * 生成部门树
     */
    List<Department> getDepartmentTree();
}

package com.weixin.maillistsynchronization.Service;


import com.weixin.maillistsynchronization.Model.Department;
import com.weixin.maillistsynchronization.Utils.Tools;
import com.weixin.maillistsynchronization.mapper.DepartmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepartmentServiceImp implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 获取部门树
     *
     * @return
     */
    public List<Department> getDepartmentTree() {
        List<Department> newlist = new ArrayList<>();
        List<Department> departments = departmentDao.queryAll();
        if (Tools.checkDepartmentDate(departments)) {
            Map<Integer, List<Department>> map = new HashMap<>();
            for (Department department : departments) {
                List<Department> list = map.getOrDefault(department.getParentid(), null);
                if (list == null) {
                    list = new ArrayList<>();
                    map.put(department.getParentid(), list);
                }
                list.add(department);
            }
            Queue<Integer> queue = new ArrayDeque<>();
            queue.add(1);
            while (!queue.isEmpty()) {
                Integer integer = queue.poll();
                List<Department> l = map.get(integer);
                if (l != null) {
                    newlist.addAll(l);
                    l.forEach(i -> queue.add(i.getId()));
                }
            }

        }
        return newlist;
    }

    /**
     * 新建一个部门
     */
    public int insert(Department department) {
        int save = departmentDao.insert(department);
        return save;
    }

    /**
     * 查询整个部门树
     */
    public List<Department> queryAll() {
        List<Department> list = departmentDao.queryAll();
        return list;
    }

    /**
     * 查询该部门的直接上级部门
     * 输入该部门的parentId
     */
    public List<Department> queryParentDepartment(Department department) {
        List<Department> list = departmentDao.queryByParentId(department.getParentid());
        return list;
    }
    /**
     * 查询该部门的直接下级部门
     * 输入该部门的Id
     */
    // public List<Department> querySonDepartment(Department department){

    //}

    /**
     * 按ID查询
     */
    public List<Department> queryById(Integer id) {
        List<Department> list = departmentDao.queryById(id);
        return list;
    }

    /**
     * 按ParentID查询
     */
    public List<Department> queryByParentId(Integer parentId) {
        List<Department> list = departmentDao.queryByParentId(parentId);
        return list;
    }

    /**
     * 按部门名称查询
     */
    public List<Department> queryByName(String name) {
        List<Department> list = departmentDao.queryByName(name);
        return list;
    }

    /**
     * 根据ID删除该部门
     */
    public int deleteById(Integer id) {
        departmentDao.deleteById(id);
        return 1;
    }

    /**
     * 根据ParentID删除该部门
     */
    public int deleteByParentId(Integer id) {
        departmentDao.deleteByParentId(id);
        return 1;
    }

    /**
     * 根据学生ID更新部门信息
     */
    public int update(Department student) {
        int update = departmentDao.update(student);
        return update;
    }
    /**
     * 查询该部门的所有下级部门
     * 输入该部门的id
     */
    // public  List<Department> queryAllSonDepartment(Department department)
    // {

    // }
    /**
     * 查询该部门的所有上级级部门
     * 输入该部门的parentId
     */
    //List<Department> queryAllParentDepartment(Department department);

}

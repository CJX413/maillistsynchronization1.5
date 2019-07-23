package com.weixin.maillistsynchronization.mapper;


import com.weixin.maillistsynchronization.Model.Department;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DepartmentDao {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "parentid", column = "parentid"),})
    /**
     * 新建一个部门
     */
    @Insert({
            "insert into department (id, name, ",
            "parentid)",
            "values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
            "#{parentid,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Department record);

    /**
     * 查询整个部门树
     */
    @Select({
            "select",
            "id, name, parentid",
            "from department"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="parentid", property="parentid", jdbcType=JdbcType.INTEGER)
    })
    List<Department> queryAll();
    /**
     * 按ParentID查询
     */
    @Select({
            "select",
            "id, name, parentid",
            "from department",
            "where parentid = #{parentid,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="parentid", property="parentid", jdbcType=JdbcType.INTEGER)
    })
    List<Department> queryByParentId(@Param("parentid") Integer parentid);

    /**
     * 按ID查询
     */
    @Select({
            "select",
            "id, name, parentid",
            "from department",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "parentid", property = "parentid", jdbcType = JdbcType.INTEGER)
    })
    List<Department> queryById(@Param("id")Integer id);

    /**
     * 按部门名称查询
     */
    @Select({
            "select",
            "id, name, parentid",
            "from department",
            "where name = #{name,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "parentid", property = "parentid", jdbcType = JdbcType.INTEGER)
    })
    List<Department> queryByName(@Param("name") String name);

    /**
     * 根据ID删除该部门
     */
    @Delete({
            "delete from department",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteById(@Param("id") Integer id);

    /**
     * 根据ParentID删除该部门
     */
    @Delete({
            "delete from department",
            "where parentid = #{parentid,jdbcType=INTEGER}"
    })
    int deleteByParentId(@Param("parentid") Integer parentid);

    /**
     * 根据学生ID更新部门信息
     */
    @Update({
            "<script>",
            "update department set",
            "<if test='name!=null'>",
            " name = #{name,jdbcType=VARCHAR}",
            "</if>",
            "<if test='name!=null and parentid!=null'>",
            ",",
            "</if>",
            "<if test='parentid!=null'>",
            " parentid = #{parentid,jdbcType=INTEGER}",
            "</if>",
            " where id = #{id,jdbcType=INTEGER}",
            "</script>"
    })
    int update(Department record);

}

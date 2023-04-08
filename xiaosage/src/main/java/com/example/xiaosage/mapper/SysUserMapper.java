package com.example.xiaosage.mapper;

import com.example.xiaosage.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liu
 * @since 2023-03-08
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

//    @Select("SELECT * FROM sys_user")
//    List<User> findAll();
//
//    @Insert("INSERT INTO sys_user(username,password,nickname,email,phone,address) VALUES (#{username}," +
//            "#{password},#{nickname},#{email},#{phone},#{address})")
//    int insert(User user);
//
//    int update(User user);
//
//    @Delete("delete from sys_user where id = #{id}")
//    int deleteById(@Param("id") Integer id);
//
//    @Select("select * from sys_user where username like concat('%', #{username}, '%') limit #{pageNum}, #{pageSize}")
//    List<User> selectPage(Integer pageNum, Integer pageSize, String username);
//
//    @Select("select count(*) from sys_user where username like concat('%', #{username}, '%')")
//    int selectTotal(String username);
}

package com.example.xiaosage.mapper;

import com.example.xiaosage.entity.SysMenu;
import com.example.xiaosage.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liu
 * @since 2023-03-20
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("select id from sys_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);
}

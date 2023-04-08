package com.example.xiaosage.service;

import com.example.xiaosage.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liu
 * @since 2023-03-20
 */
public interface ISysRoleService extends IService<SysRole> {

    Object setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);
}

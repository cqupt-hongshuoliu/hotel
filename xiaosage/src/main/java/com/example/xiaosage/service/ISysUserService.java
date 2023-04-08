package com.example.xiaosage.service;

import com.example.xiaosage.common.Result;
import com.example.xiaosage.controller.dto.PasswordDTO;
import com.example.xiaosage.controller.dto.UserDTO;
import com.example.xiaosage.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liu
 * @since 2023-03-08
 */
public interface ISysUserService extends IService<SysUser> {
    Result saveUser(SysUser sysUser);

    Result login(UserDTO userDTO);

    Result register(UserDTO userDTO);

    SysUser findOne(String username);

    Result modifyPassword(String username, PasswordDTO passwordDTO);
}

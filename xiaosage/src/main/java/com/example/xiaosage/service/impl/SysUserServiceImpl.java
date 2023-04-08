package com.example.xiaosage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.xiaosage.common.Constants;
import com.example.xiaosage.common.Result;
import com.example.xiaosage.controller.dto.PasswordDTO;
import com.example.xiaosage.controller.dto.UserDTO;
import com.example.xiaosage.entity.SysMenu;
import com.example.xiaosage.entity.SysUser;
import com.example.xiaosage.exception.ServiceException;
import com.example.xiaosage.mapper.SysRoleMapper;
import com.example.xiaosage.mapper.SysRoleMenuMapper;
import com.example.xiaosage.mapper.SysUserMapper;
import com.example.xiaosage.service.ISysMenuService;
import com.example.xiaosage.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xiaosage.utils.BASE64Util;
import com.example.xiaosage.utils.MD5Utils;
import com.example.xiaosage.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liu
 * @since 2023-03-08
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private static final Log LOG =Log.get();

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Resource
    private ISysMenuService sysMenuService;

    /**
     * 若无id则插入，反之更新
     * @param sysUser
     * @return
     */
    @Override
    public Result saveUser(SysUser sysUser) {
        if (sysUser.getId() == null) {
            return this.save(sysUser) ? Result.success(null, "插入成功") : Result.error(Constants.CODE_500, "插入失败");
        } else {
            return this.updateById(sysUser) ? Result.success(null, "插入成功") : Result.error(Constants.CODE_500, "插入失败");
        }
    }

    /**
     * 设置token，分配路由
     * @param userDTO
     * @return
     */
    @Override
    public Result login(UserDTO userDTO) {

        SysUser one = this.findOne(userDTO.getUsername());
        if (one != null) {
            String password = BASE64Util.decode(userDTO.getPassword());
            password = password.substring(2, password.length() - 2);
            String salt = one.getSalt();
            password = MD5Utils.inputPassToDBPass(password, salt);
            boolean equals = password.equals(one.getPassword());
            if (equals) {
                BeanUtil.copyProperties(one, userDTO, true);
                String token = TokenUtils.generateToken(one.getId().toString(), one.getPassword());
                userDTO.setToken(token);
                List<SysMenu> roleMenus = this.getRoleMenus(userDTO.getRole());
                userDTO.setMenus(roleMenus);
                return Result.success(userDTO, "登陆成功");
            } else {
                return Result.error(Constants.CODE_402, "密码错误");
            }
        } else {
            return Result.error(Constants.CODE_402, "用户名错误");
        }
    }

    /**
     * 用户注册，设置默认角色
     * @param userDTO
     * @return
     */
    @Override
    public Result register(UserDTO userDTO) {
        SysUser one = this.findOne(userDTO.getUsername());
        if (one == null) {
            String password = BASE64Util.decode(userDTO.getPassword());
            password = password.substring(2, password.length() - 2);
            String salt = IdUtil.fastSimpleUUID();
            password = MD5Utils.inputPassToDBPass(password, salt);
            userDTO.setPassword(password);
            userDTO.setSalt(salt);
            userDTO.setRole("ROLE_USER");
            SysUser sysUser = new SysUser();
            BeanUtil.copyProperties(userDTO, sysUser, true);
            Result result = this.saveUser(sysUser);
            return result.getCode() == "200" ? Result.success(null, "注册成功") : result;
        } else {
            return Result.error(Constants.CODE_401, "用户名已存在");
        }
    }

    /**
     * 依据 username 查找user
     * @param username
     * @return
     */
    @Override
    public SysUser findOne(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        SysUser one;
        try {
            one = getOne(queryWrapper);
        }catch (Exception e){
            LOG.error(e);
            throw new  ServiceException(Constants.CODE_401,"字段重复");
        }

        if (one != null){
            return one;
        }
        return null;
    }

    @Override
    public Result modifyPassword(String username, PasswordDTO passwordDTO) {
        SysUser sysUser = findOne(username);
        String formPassword = BASE64Util.decode(passwordDTO.getPassword());
        formPassword = formPassword.substring(2, formPassword.length() - 2);
        String oldPassword = MD5Utils.inputPassToDBPass(formPassword, sysUser.getSalt());
        if (oldPassword.equals(sysUser.getPassword())) {
            String formConfirmPassword = BASE64Util.decode(passwordDTO.getNewPassword());
            formConfirmPassword = formConfirmPassword.substring(2, formConfirmPassword.length() - 2);
            String newPassword = MD5Utils.inputPassToDBPass(formConfirmPassword, sysUser.getSalt());
            sysUser.setPassword(newPassword);
            Result result = saveUser(sysUser);
            return result.getCode() == "200" ? Result.success((Object)null, "修改成功") : Result.error((String)null, "修改失败");
        } else {
            return Result.error(null, "旧密码错误");
        }
    }

    /**
     * 根据当前用户的角色，获取当前用户的权限菜单列表，仅登陆时调用
     * @param role
     * @return
     */
    private List<SysMenu> getRoleMenus(String role){
        //  依据用户的角色找到角色对应id
        Integer roleId = sysRoleMapper.selectByFlag(role);
        //  依据角色id找到角色菜单关系表中的菜单id
        List<Integer> menuIds = sysRoleMenuMapper.selectByRoleId(roleId);
        //查出系统所有的菜单
        List<SysMenu> menus = sysMenuService.findMenus("");

        List<SysMenu> roleMenus = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menuIds.contains(menu.getId())){
                //  根据菜单id进行比对，有则加入
                roleMenus.add(menu);
            }
            List<SysMenu> children = menu.getChildren();
            //  移除 children 里不在 menuIds 集合里的元素
            boolean isSuccess = children.removeIf(child -> !menuIds.contains(child.getId()));
            if (children.size() != 0 && isSuccess){
                //  加入有孩子的父亲菜单
                roleMenus.add(menu);
            }
        }
        return roleMenus;
    }

}

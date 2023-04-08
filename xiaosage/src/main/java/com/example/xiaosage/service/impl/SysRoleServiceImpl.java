package com.example.xiaosage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.xiaosage.entity.SysRole;
import com.example.xiaosage.entity.SysRoleMenu;
import com.example.xiaosage.mapper.SysRoleMapper;
import com.example.xiaosage.mapper.SysRoleMenuMapper;
import com.example.xiaosage.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liu
 * @since 2023-03-20
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 角色页面绑定角色与菜单关系，生成角色菜单关系表
     * @param roleId
     * @param menuIds
     * @return
     */
    @Transactional
    @Override
    public Object setRoleMenu(Integer roleId, List<Integer> menuIds) {

        //  先删除当前角色id所有的绑定关系
        sysRoleMenuMapper.deleteByRoleId(roleId);

        //  再把前端传过来的菜单id数组绑定到当前的角色id上去
        for (Integer menuId : menuIds) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuMapper.insert(sysRoleMenu);
        }
        return null;
    }

    /**
     * 角色页面获取角色与菜单的关系表
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return sysRoleMenuMapper.selectByRoleId(roleId);
    }
}

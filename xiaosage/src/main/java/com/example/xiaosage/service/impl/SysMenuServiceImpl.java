package com.example.xiaosage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.xiaosage.entity.SysMenu;
import com.example.xiaosage.mapper.SysMenuMapper;
import com.example.xiaosage.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liu
 * @since 2023-03-20
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    /**
     * name用于模糊查询,生成菜单页面
     * @param name
     * @return
     */
    @Override
    public List<SysMenu> findMenus(String name) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isBlank(name)){
            queryWrapper.like("name",name);
        }
        List<SysMenu> list = list(queryWrapper);
        //pId为null的一级菜单
        List<SysMenu> parentNodes = list.stream().filter(menu ->menu.getPid() == null).collect(Collectors.toList());
        //找出一级菜单的子菜单
        for (SysMenu sysMenu : parentNodes) {
            sysMenu.setChildren(list.stream().filter(m -> sysMenu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }
}

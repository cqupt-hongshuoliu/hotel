package com.example.xiaosage.service;

import com.example.xiaosage.entity.SysMenu;
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
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> findMenus(String name);
}

package com.example.xiaosage.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xiaosage.common.Constants;
import com.example.xiaosage.common.Enum.DictTypeEnum;
import com.example.xiaosage.common.Result;
import com.example.xiaosage.entity.SysDict;
import com.example.xiaosage.entity.SysMenu;
import com.example.xiaosage.entity.SysRole;
import com.example.xiaosage.mapper.SysDictMapper;
import com.example.xiaosage.service.ISysMenuService;
import com.example.xiaosage.service.ISysRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2023-03-20
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {
    @Resource
    private ISysMenuService sysMenuService;

    @Resource
    private SysDictMapper sysDictMapper;

    /**
     * 插入或更新
     * @param sysMenu
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu){
        //新增或更新
        if (sysMenuService.saveOrUpdate(sysMenu)){
            return Result.success(null, "保存成功");
        } else {
            return Result.error(Constants.CODE_500, "保存失败");
        }
    }

    /**
     * 依据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        if (sysMenuService.removeById(id)){
            return Result.success(null, "删除成功");
        }else {
            return Result.error(Constants.CODE_401,"删除失败");
        }
    }

    /**
     * 菜单展示列表
     * @param name
     * @return
     */
    @GetMapping("/list")
    public Result findAll(@RequestParam(defaultValue = "") String name){
        return Result.success(sysMenuService.findMenus(name), "");
    }

//    @GetMapping("/{id}")
//    public Result findOne(@PathVariable Integer id){
//        return Result.success(sysMenuService.getById(id));
//    }

    /**
     * 获取菜单图标
     * @return
     */
    @GetMapping("/icons")
    public Result getIcons(){
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", DictTypeEnum.icon);
        return Result.success(sysDictMapper.selectList(queryWrapper), "");
    }

}

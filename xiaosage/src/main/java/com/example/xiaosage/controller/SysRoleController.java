package com.example.xiaosage.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xiaosage.common.Constants;
import com.example.xiaosage.common.Enum.DictTypeEnum;
import com.example.xiaosage.common.Result;
import com.example.xiaosage.entity.SysDict;
import com.example.xiaosage.entity.SysRole;
import com.example.xiaosage.entity.SysUser;
import com.example.xiaosage.mapper.SysDictMapper;
import com.example.xiaosage.mapper.SysRoleMenuMapper;
import com.example.xiaosage.service.ISysRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2023-03-20
 */
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private SysDictMapper sysDictMapper;

    /**
     * 插入或更新
     * @param sysRole
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysRole sysRole){
        //新增或更新
        if (sysRoleService.saveOrUpdate(sysRole)){
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
        if (sysRoleService.removeById(id)){
            return Result.success(null, "删除成功");
        }else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

//    @GetMapping("/list")
//    public Result findAll(){
//        return Result.success(sysRoleService.list());
//    }


//    @GetMapping("/{id}")
//    public Result findOne(@PathVariable Integer id){
//        return Result.success(sysRoleService.getById(id));
//    }

    /**
     * 分页实现
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String name){
        IPage<SysRole> page = new Page<>(pageNum,pageSize);

        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        queryWrapper.orderByDesc("id");

        IPage<SysRole> sysRoleIPage = sysRoleService.page(page, queryWrapper);
        return Result.success(sysRoleIPage,"");
    }

    /**
     * 获取角色flag属性列表
     * @return
     */
    @GetMapping("/flag")
    public Result getRoleFlag(){
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", DictTypeEnum.role);
        return Result.success(sysDictMapper.selectList(queryWrapper),"");
    }

    /**
     * 绑定角色和菜单的关系
     * @param roleId
     * @param menuIds
     * @return
     */
    @PostMapping("/setRoleMenu/{roleId}")
    public Result setRoleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds){
       return Result.success(sysRoleService.setRoleMenu(roleId, menuIds), "绑定成功");
    }

    /**
     * 获取角色与菜单关系表
     * @param roleId
     * @return
     */
    @GetMapping("/getRoleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId){
        return Result.success(sysRoleService.getRoleMenu(roleId), "");
    }

}

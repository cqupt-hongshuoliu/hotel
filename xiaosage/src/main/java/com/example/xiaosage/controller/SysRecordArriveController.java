package com.example.xiaosage.controller;


import cn.hutool.core.lang.func.Func;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xiaosage.common.Constants;
import com.example.xiaosage.common.Result;
import com.example.xiaosage.entity.SysRecordArrive;
import com.example.xiaosage.entity.SysRoom;
import com.example.xiaosage.service.ISysRecordArriveService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 入住登记表 前端控制器
 * </p>
 *
 * @author liu
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/record-arrive")
public class SysRecordArriveController {

    @Resource
    private ISysRecordArriveService sysRecordArriveService;

    /**
     * 插入或更新
     * @param sysRecordArrive
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysRecordArrive sysRecordArrive){
        //新增或更新
        if (sysRecordArriveService.saveOrUpdate(sysRecordArrive)){
            return Result.success();
        } else {
            return Result.error(Constants.CODE_500, "插入失败");
        }
    }

    /**
     * 依据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        if (sysRecordArriveService.removeById(id)){
            return Result.success();
        }else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

//    //查询所有
//    @GetMapping("/list")
//    public Result findAll(){
//        return Result.success(sysRecordArriveService.list());
//    }

    /**
     * 分页实现，可依据时间范围检索信息
     * @param pageNum
     * @param pageSize
     * @param arriveTime
     * @param username
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime arriveTime,
                           @RequestParam(defaultValue = "") String username){


        IPage<SysRecordArrive> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SysRecordArrive> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(arriveTime != null,
                "date_format (arrive_time,'%Y-%m-%d') >= date_format('" + arriveTime + "','%Y-%m-%d')");

        queryWrapper.like("username", username);
        queryWrapper.orderByDesc("arrive_id");
        IPage<SysRecordArrive> sysRoomIPage = sysRecordArriveService.page(page, queryWrapper);
        return Result.success(sysRoomIPage,"");
    }
}

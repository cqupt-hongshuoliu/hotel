package com.example.xiaosage.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xiaosage.common.Constants;
import com.example.xiaosage.common.Result;
import com.example.xiaosage.entity.SysRecordArrive;
import com.example.xiaosage.entity.SysRecordLeave;
import com.example.xiaosage.service.ISysRecordLeaveService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 退房登记表 前端控制器
 * </p>
 *
 * @author liu
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/record-leave")
public class SysRecordLeaveController {

    @Resource
    private ISysRecordLeaveService sysRecordLeaveService;

    /**
     * 插入或删除
     * @param sysRecordLeave
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysRecordLeave sysRecordLeave){
        //新增或更新
        if (sysRecordLeaveService.saveOrUpdate(sysRecordLeave)){
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
        if (sysRecordLeaveService.removeById(id)){
            return Result.success();
        }else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

    /**
     * 分页实现，可依据时间范围查询信息
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


        IPage<SysRecordLeave> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SysRecordLeave> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(arriveTime != null ,
                "date_format (leave_time,'%Y-%m-%d') >= date_format('" + arriveTime + "','%Y-%m-%d')");
        queryWrapper.like("username", username);
        queryWrapper.orderByDesc("leave_id");
        IPage<SysRecordLeave> sysRoomIPage = sysRecordLeaveService.page(page, queryWrapper);
        return Result.success(sysRoomIPage,"");
    }
}

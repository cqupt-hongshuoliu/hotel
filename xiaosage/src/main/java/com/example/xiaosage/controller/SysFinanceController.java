package com.example.xiaosage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xiaosage.common.Constants;
import com.example.xiaosage.common.Result;
import com.example.xiaosage.entity.SysFinance;
import com.example.xiaosage.service.ISysFinanceService;
import java.time.LocalDateTime;
import javax.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/finance"})
public class SysFinanceController {
    @Resource
    private ISysFinanceService sysFinanceService;

    @PostMapping({"/save"})
    public Result save(@RequestBody SysFinance sysFinance) {
        return sysFinanceService.saveFinance(sysFinance) ? Result.success(null, "保存成功") : Result.error("500", "保存失败");
    }

    @DeleteMapping({"/{id}"})
    public Result delete(@PathVariable Integer id) {
        return sysFinanceService.removeById(id) ? Result.success(null, "删除失败") : Result.error(Constants.CODE_500, "删除失败");
    }

    @GetMapping({"/page"})
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime arriveTime,
                           @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime leaveTime) {
        IPage<SysFinance> page = new Page(pageNum, pageSize);
        QueryWrapper<SysFinance> queryWrapper = new QueryWrapper();
        queryWrapper.apply(arriveTime != null, "date_format (arrive_time,'%Y-%m-%d') >= date_format('" + arriveTime + "','%Y-%m-%d')", new Object[0]);
        queryWrapper.apply(leaveTime != null, "date_format (leave_time,'%Y-%m-%d') <= date_format('" + leaveTime + "','%Y-%m-%d')", new Object[0]);
        queryWrapper.orderByDesc("finance_id");
        IPage<SysFinance> sysFinanceIPage = sysFinanceService.page(page, queryWrapper);
        return Result.success(sysFinanceIPage, "");
    }
}
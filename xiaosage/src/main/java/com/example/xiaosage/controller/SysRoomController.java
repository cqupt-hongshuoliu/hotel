package com.example.xiaosage.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xiaosage.common.Constants;
import com.example.xiaosage.common.Enum.DictTypeEnum;
import com.example.xiaosage.common.Result;
import com.example.xiaosage.config.AuthAccess;
import com.example.xiaosage.entity.SysDict;
import com.example.xiaosage.entity.SysRoom;
import com.example.xiaosage.mapper.SysDictMapper;
import com.example.xiaosage.service.ISysOrderService;
import com.example.xiaosage.service.ISysRoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liu
 * @since 2023-03-24
 */
@RestController
@RequestMapping("/room")
public class SysRoomController {

    @Resource
    private ISysRoomService sysRoomService;

    @Resource
    private SysDictMapper sysDictMapper;

    @Resource
    private ISysOrderService sysOrderService;

    /**
     * 插入或更新
     * @param sysRoom
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysRoom sysRoom){
        //新增或更新
        if (sysRoomService.saveRoom(sysRoom)){
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
        if (sysRoomService.removeById(id)){
            return Result.success(null, "删除成功");
        }else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

//    //查询所有
//    @GetMapping("/list")
//    public Result findAll(){
//        return Result.success(sysRoomService.list());
//    }

    /**
     * 分页实现
     * @param pageNum
     * @param pageSize
     * @param roomType
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String roomType){
        IPage<SysRoom> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SysRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("room_type",roomType);
        queryWrapper.orderByDesc("room_id");
        IPage<SysRoom> sysRoomIPage = sysRoomService.page(page, queryWrapper);
        return Result.success(sysRoomIPage, "");
    }

    /**
     * 获取房间类型列表
     * @return
     */
    @GetMapping("/type")
    public Result getRoomType(){
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", DictTypeEnum.room_type);
        return Result.success(sysDictMapper.selectList(queryWrapper), "");
    }

    /**
     * 获取房间状态列表
     * @return
     */
    @GetMapping("/state")
    public Result getRoomState(){
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", DictTypeEnum.room_state);
        return Result.success(sysDictMapper.selectList(queryWrapper), "");
    }

    @AuthAccess
    @GetMapping({"/getRoomFront"})
    public Result getRoomFront(@RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime arriveTime, @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime leaveTime, @RequestParam(defaultValue = "") String roomType) {
        List<SysRoom> list = sysRoomService.getRoomFront(arriveTime, leaveTime, roomType);
        return Result.success(list, "");
    }

}

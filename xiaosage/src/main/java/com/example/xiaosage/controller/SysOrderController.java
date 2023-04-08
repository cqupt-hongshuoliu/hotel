package com.example.xiaosage.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xiaosage.common.Constants;
import com.example.xiaosage.common.Enum.DepositEnum;
import com.example.xiaosage.common.Enum.DictTypeEnum;
import com.example.xiaosage.common.Enum.OrderStateEnum;
import com.example.xiaosage.common.Enum.RoomStateEnum;
import com.example.xiaosage.common.Result;
import com.example.xiaosage.config.AuthAccess;
import com.example.xiaosage.controller.dto.OrderDTO;
import com.example.xiaosage.controller.dto.UserDTO;
import com.example.xiaosage.entity.SysDict;
import com.example.xiaosage.entity.SysOrder;
import com.example.xiaosage.entity.SysRecordArrive;
import com.example.xiaosage.entity.SysRoom;
import com.example.xiaosage.mapper.SysDictMapper;
import com.example.xiaosage.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Iterator;
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
@RequestMapping("/order")
public class SysOrderController {

    @Resource
    private ISysOrderService sysOrderService;

    @Resource
    private SysDictMapper sysDictMapper;

    @Resource
    private ISysRoomService sysRoomService;

    @Resource
    private ISysRecordArriveService sysRecordArriveService;

    @Resource
    private ISysRecordLeaveService sysRecordLeaveService;

    @Resource
    private ISysFinanceService sysFinanceService;

    /**
     * 插入或更新
     * @param sysOrder
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysOrder sysOrder){
        //新增或更新
        return this.sysOrderService.saveOrder(sysOrder) ? Result.success(null, "保存成功") : Result.error(Constants.CODE_500, "保存失败");
    }

    /**
     * 依据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        if (sysOrderService.removeById(id)){
            return Result.success(null, "删除成功");
        }else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

    /**
     * 分页实现
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping({"/page"})
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String orderConsumerName) {
        IPage<SysOrder> page = new Page((long)pageNum, (long)pageSize);
        QueryWrapper<SysOrder> queryWrapper = new QueryWrapper();
        queryWrapper.like("order_consumer_name", orderConsumerName);
        queryWrapper.orderByDesc("order_id");
        IPage<SysOrder> sysOrderIPage = this.sysOrderService.page(page, queryWrapper);
        return Result.success(sysOrderIPage, "");
    }

    /**
     * 获取订单状态列表
     * @return
     */
    @GetMapping("/state")
    public Result getOrderState(){
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", DictTypeEnum.order_state);
        return Result.success(sysDictMapper.selectList(queryWrapper),"");
    }

    /**
     * 取消订单时更新订单状态
     * @param orderId
     * @return
     */
    @PutMapping({"/cancel/{orderId}"})
    public Result orderCancel(@PathVariable Integer orderId) {
        boolean result = this.sysOrderService.updateOrderState(OrderStateEnum.已取消, orderId);
        return result ? Result.success(null, "取消成功") : Result.error("500", "取消失败");
    }

    /**
     * 入住登记时改变订单状态，并变更房间状态为入住
     * @param orderId
     * @return
     */
    @PostMapping({"/arrive/{orderId}"})
    public Result orderArrive(@PathVariable Integer orderId, @RequestBody UserDTO userDTO) {
        SysOrder sysOrder = sysOrderService.getById(orderId);
        String username = userDTO.getUsername();
        boolean result1 = sysRecordArriveService.saveByOrder(sysOrder, username);
        if (!result1) {
            return Result.error("500", "入住登记保存失败");
        } else {
            boolean result2 = sysRoomService.updateRoomStateByOrderId(orderId, RoomStateEnum.入住);
            if (!result2) {
                return Result.error("500", "房间状态变更失败");
            } else {
                boolean result3 = sysOrderService.updateOrderState(OrderStateEnum.已入住, orderId);
                return result3 ? Result.success((Object)null, "入住成功") : Result.error("500", "订单状态变更失败");
            }
        }
    }

    /**
     * 离店登记时改变订单状态，并变更房间状态为空闲
     * @param orderId
     * @return
     */
    @PostMapping({"/leave/{orderId}"})
    public Result orderLeave(@PathVariable Integer orderId, @RequestBody UserDTO userDTO) {
        SysOrder sysOrder = sysOrderService.getById(orderId);
        String username = userDTO.getUsername();
        boolean saveByOrder = sysRecordLeaveService.saveByOrder(sysOrder, username);
        if (!saveByOrder) {
            return Result.error("500", "退房登记保存失败");
        } else {
            boolean saveFinance = sysFinanceService.saveByOrder(sysOrder);
            if (!saveFinance) {
                return Result.error("500", "财务管理新增失败");
            } else {
                boolean b = sysRecordArriveService.deleteByOrderNum(sysOrder.getOrderNum());
                if (!b) {
                    return Result.error("500", "入住删除失败");
                } else {
                    boolean r = sysRoomService.updateRoomStateByOrderId(orderId, RoomStateEnum.空闲);
                    if (!r) {
                        return Result.error("500", "房间状态变更失败");
                    } else {
                        boolean result = this.sysOrderService.updateOrderState(OrderStateEnum.已退房, orderId);
                        return result ? Result.success(null, "退房成功") : Result.error("500", "订单状态变更失败");
                    }
                }
            }
        }
    }

    @AuthAccess
    @PostMapping({"/addOrder"})
    public Result addOrder(@RequestBody OrderDTO orderDTO) {
        List<SysRoom> roomFront = sysRoomService.getRoomFront(orderDTO.getArriveTime(), orderDTO.getLeaveTime(), orderDTO.getRoom().getRoomType());
        Iterator var3 = roomFront.iterator();

        SysRoom sysRoom;
        do {
            if (!var3.hasNext()) {
                return Result.error("401", "房间该时间段已被预订，请选购其他时间");
            }

            sysRoom = (SysRoom)var3.next();
        } while(!sysRoom.getRoomNumber().equals(orderDTO.getRoom().getRoomNumber()));

        SysOrder sysOrder = new SysOrder();
        sysOrder.setOrderRoomNum(orderDTO.getRoom().getRoomNumber());
        sysOrder.setOrderRoomTitle(orderDTO.getRoom().getRoomTitle());
        sysOrder.setOrderConsumerName(orderDTO.getConsumerName());
        sysOrder.setOrderPhone(orderDTO.getPhone());
        sysOrder.setOrderIdentity(orderDTO.getIdentity());
        sysOrder.setArriveTime(orderDTO.getArriveTime());
        sysOrder.setLeaveTime(orderDTO.getLeaveTime());
        sysOrder.setOrderPrice(orderDTO.getRoom().getRoomPrice());
        sysOrder.setOrderState(String.valueOf(OrderStateEnum.已支付));
        boolean b = sysOrderService.saveOrder(sysOrder);
        if (b) {
            QueryWrapper<SysRoom> queryWrapper = new QueryWrapper();
            queryWrapper.eq("room_number", orderDTO.getRoom().getRoomNumber());
            SysRoom one = sysRoomService.getOne(queryWrapper);
            one.setRoomState(String.valueOf(RoomStateEnum.预订));
            boolean b1 = sysRoomService.saveRoom(one);
            if (b1) {
                return Result.success(null, "预定成功");
            } else {
                return Result.error("500", "房间状态修改失败");
            }
        } else {
            return Result.error("500", "预订失败");
        }
    }


}

package com.example.xiaosage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.xiaosage.common.Enum.RoomStateEnum;
import com.example.xiaosage.entity.SysOrder;
import com.example.xiaosage.entity.SysRoom;
import com.example.xiaosage.mapper.SysRoomMapper;
import com.example.xiaosage.service.ISysOrderService;
import com.example.xiaosage.service.ISysRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liu
 * @since 2023-03-24
 */
@Service
public class SysRoomServiceImpl extends ServiceImpl<SysRoomMapper, SysRoom> implements ISysRoomService {

    @Resource
    private ISysOrderService sysOrderService;


    @Override
    public boolean saveRoom(SysRoom sysRoom) {
        if (sysRoom.getRoomId() == null){
            return save(sysRoom);
        }else {
            return updateById(sysRoom);
        }
    }

    /**
     * 依据订单id，查找到对应房间并更改房间状态
     * @param orderId
     * @param roomState
     * @return
     */
    @Override
    public boolean updateRoomStateByOrderId(Integer orderId, RoomStateEnum roomState) {
        SysOrder sysOrder = sysOrderService.getById(orderId);
        String orderRoomNum = sysOrder.getOrderRoomNum();
        QueryWrapper<SysRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_number",orderRoomNum);
        SysRoom sysRoom = getOne(queryWrapper);
        UpdateWrapper<SysRoom> roomUpdateWrapper = new UpdateWrapper<>();
        roomUpdateWrapper.set("room_state",roomState).in("room_id", sysRoom.getRoomId());
        return update(roomUpdateWrapper);
    }

    @Override
    public List<SysRoom> getRoomFront(LocalDateTime arriveTime, LocalDateTime leaveTime, String roomType) {
        if (arriveTime == null) {
            arriveTime = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0);
        }

        if (leaveTime == null) {
            leaveTime = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).plusDays(1L);
        }

        List<SysOrder> orders = sysOrderService.getOrderFront(arriveTime, leaveTime);
        List<String> roomNums = new ArrayList();
        Iterator var6 = orders.iterator();

        while(var6.hasNext()) {
            SysOrder order = (SysOrder)var6.next();
            roomNums.add(order.getOrderRoomNum());
        }

        QueryWrapper<SysRoom> queryWrapper = new QueryWrapper();
        queryWrapper.like("room_type", roomType);
        queryWrapper.eq("room_state", RoomStateEnum.空闲);
        List<SysRoom> list2 = list(queryWrapper);
        Iterator var8 = roomNums.iterator();

        while(var8.hasNext()) {
            String roomNum = (String)var8.next();
            QueryWrapper<SysRoom> roomQueryWrapper = new QueryWrapper();
            roomQueryWrapper.eq("room_number", roomNum);
            SysRoom one = getOne(roomQueryWrapper);
            list2.add(one);
        }

        if (roomType.length() != 0) {
            list2.removeIf((l) -> {
                return !roomType.equals(l.getRoomType());
            });
        }

        return list2;
    }


}

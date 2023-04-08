package com.example.xiaosage.service;

import com.example.xiaosage.common.Enum.OrderStateEnum;
import com.example.xiaosage.common.Enum.RoomStateEnum;
import com.example.xiaosage.entity.SysRoom;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liu
 * @since 2023-03-24
 */
public interface ISysRoomService extends IService<SysRoom> {

    boolean saveRoom(SysRoom sysRoom);

    boolean updateRoomStateByOrderId(Integer orderId, RoomStateEnum roomState);

    List<SysRoom> getRoomFront(LocalDateTime arriveTime, LocalDateTime leaveTime, String roomType);

}

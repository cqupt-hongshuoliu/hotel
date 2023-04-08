package com.example.xiaosage.service;

import com.example.xiaosage.common.Enum.OrderStateEnum;
import com.example.xiaosage.entity.SysOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

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
public interface ISysOrderService extends IService<SysOrder> {

    List<SysOrder> getOrderFront( LocalDateTime arriveTime,  LocalDateTime leaveTime);

    boolean saveOrder(SysOrder sysOrder);

    boolean updateOrderState(OrderStateEnum orderState, Integer orderId);

}

package com.example.xiaosage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.xiaosage.common.Enum.OrderStateEnum;
import com.example.xiaosage.entity.SysOrder;
import com.example.xiaosage.mapper.SysOrderMapper;
import com.example.xiaosage.service.ISysOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
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
public class SysOrderServiceImpl extends ServiceImpl<SysOrderMapper, SysOrder> implements ISysOrderService {

    public List<SysOrder> getOrderFront(LocalDateTime arriveTime, LocalDateTime leaveTime) {
        QueryWrapper<SysOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_state", OrderStateEnum.已支付);
        queryWrapper.and((wrapper) -> {
            QueryWrapper var10000 = (QueryWrapper)((QueryWrapper)((QueryWrapper)wrapper.apply(arriveTime != null, "date_format (arrive_time,'%Y-%m-%d') >= date_format('" + leaveTime + "','%Y-%m-%d')", new Object[0])).or()).apply(leaveTime != null, "date_format (leave_time,'%Y-%m-%d') <= date_format('" + arriveTime + "','%Y-%m-%d')", new Object[0]);
        });
        return list(queryWrapper);
    }

    @Override
    public boolean saveOrder(SysOrder sysOrder) {
        if (sysOrder.getOrderId() == null) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddhhmmss");
            String orderNum = sysOrder.getOrderRoomNum() + "-" + dateFormat.format(date);
            sysOrder.setOrderNum(orderNum);
            return save(sysOrder);
        } else {
            return updateById(sysOrder);
        }
    }

    /**
     * 更改订单状态
     * @param orderState
     * @param orderId
     * @return
     */
    @Override
    public boolean updateOrderState(OrderStateEnum orderState, Integer orderId) {
        UpdateWrapper<SysOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("order_state",orderState).in("order_id",orderId);
        return update(updateWrapper);
    }

}

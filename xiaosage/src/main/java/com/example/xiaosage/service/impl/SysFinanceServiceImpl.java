package com.example.xiaosage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xiaosage.entity.SysFinance;
import com.example.xiaosage.entity.SysOrder;
import com.example.xiaosage.mapper.SysFinanceMapper;
import com.example.xiaosage.service.ISysFinanceService;
import org.springframework.stereotype.Service;

@Service
public class SysFinanceServiceImpl extends ServiceImpl<SysFinanceMapper, SysFinance> implements ISysFinanceService {
    public SysFinanceServiceImpl() {
    }

    public boolean saveFinance(SysFinance sysFinance) {
        return sysFinance.getFinanceId() == null ? save(sysFinance) : updateById(sysFinance);
    }

    public boolean saveByOrder(SysOrder sysOrder) {
        SysFinance sysFinance = new SysFinance();
        sysFinance.setOrderRoomNum(sysOrder.getOrderRoomNum());
        sysFinance.setOrderRoomTitle(sysOrder.getOrderRoomTitle());
        sysFinance.setConsumer(sysOrder.getOrderConsumerName());
        sysFinance.setPhone(sysOrder.getOrderPhone());
        sysFinance.setArriveTime(sysOrder.getArriveTime());
        sysFinance.setLeaveTime(sysOrder.getLeaveTime());
        sysFinance.setPrice(sysOrder.getOrderPrice());
        return saveFinance(sysFinance);
    }
}
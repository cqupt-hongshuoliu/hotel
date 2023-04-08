package com.example.xiaosage.service.impl;

import com.example.xiaosage.common.Enum.DepositEnum;
import com.example.xiaosage.entity.SysOrder;
import com.example.xiaosage.entity.SysRecordLeave;
import com.example.xiaosage.mapper.SysRecordLeaveMapper;
import com.example.xiaosage.service.ISysRecordLeaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 退房登记表 服务实现类
 * </p>
 *
 * @author liu
 * @since 2023-03-25
 */
@Service
public class SysRecordLeaveServiceImpl extends ServiceImpl<SysRecordLeaveMapper, SysRecordLeave> implements ISysRecordLeaveService {

    @Override
    public boolean saveByOrder(SysOrder sysOrder, String username) {
        SysRecordLeave recordLeave = new SysRecordLeave();
        recordLeave.setOrderNum(sysOrder.getOrderNum());
        recordLeave.setUsername(username);
        recordLeave.setConsumerName(sysOrder.getOrderConsumerName());
        recordLeave.setPhone(sysOrder.getOrderPhone());
        recordLeave.setIdentity(sysOrder.getOrderIdentity());
        recordLeave.setDeposit(String.valueOf(DepositEnum.已退));
        return save(recordLeave);
    }
}

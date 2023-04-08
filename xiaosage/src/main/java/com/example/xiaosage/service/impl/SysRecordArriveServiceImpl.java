package com.example.xiaosage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.xiaosage.common.Enum.DepositEnum;
import com.example.xiaosage.entity.SysOrder;
import com.example.xiaosage.entity.SysRecordArrive;
import com.example.xiaosage.mapper.SysRecordArriveMapper;
import com.example.xiaosage.service.ISysRecordArriveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 入住登记表 服务实现类
 * </p>
 *
 * @author liu
 * @since 2023-03-25
 */
@Service
public class SysRecordArriveServiceImpl extends ServiceImpl<SysRecordArriveMapper, SysRecordArrive> implements ISysRecordArriveService {

    @Resource
    private SysRecordArriveMapper sysRecordArriveMapper;

    @Override
    public boolean saveByOrder(SysOrder sysOrder, String username) {
        SysRecordArrive recordArrive = new SysRecordArrive();
        recordArrive.setOrderNum(sysOrder.getOrderNum());
        recordArrive.setUsername(username);
        recordArrive.setConsumerName(sysOrder.getOrderConsumerName());
        recordArrive.setPhone(sysOrder.getOrderPhone());
        recordArrive.setIdentity(sysOrder.getOrderIdentity());
        recordArrive.setDeposit(String.valueOf(DepositEnum.已付));
        return save(recordArrive);
    }

    @Override
    public boolean deleteByOrderNum(String orderNum) {
        QueryWrapper<SysRecordArrive> queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_num", orderNum);
        int delete = sysRecordArriveMapper.delete(queryWrapper);
        return delete == 1;
    }
}

package com.example.xiaosage.service;

import com.example.xiaosage.entity.SysOrder;
import com.example.xiaosage.entity.SysRecordArrive;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 入住登记表 服务类
 * </p>
 *
 * @author liu
 * @since 2023-03-25
 */
public interface ISysRecordArriveService extends IService<SysRecordArrive> {

    boolean saveByOrder(SysOrder sysOrder, String username);

    boolean deleteByOrderNum(String orderNum);
}

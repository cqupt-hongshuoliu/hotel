package com.example.xiaosage.service;

import com.example.xiaosage.entity.SysOrder;
import com.example.xiaosage.entity.SysRecordLeave;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 退房登记表 服务类
 * </p>
 *
 * @author liu
 * @since 2023-03-25
 */
public interface ISysRecordLeaveService extends IService<SysRecordLeave> {

    boolean saveByOrder(SysOrder sysOrder, String username);
}

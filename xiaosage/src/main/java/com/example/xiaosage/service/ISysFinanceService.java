package com.example.xiaosage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xiaosage.entity.SysFinance;
import com.example.xiaosage.entity.SysOrder;

public interface ISysFinanceService extends IService<SysFinance> {
    boolean saveFinance(SysFinance sysFinance);

    boolean saveByOrder(SysOrder sysOrder);
}
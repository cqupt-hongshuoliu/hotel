package com.example.xiaosage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class SysFinance {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "finance_id", type = IdType.AUTO)
    private Integer financeId;

    /**
     * 房间号
     */
    private String orderRoomNum;

    /**
     * 房间标题
     */
    private String orderRoomTitle;

    /**
     * 顾客
     */
    private String consumer;

    /**
     * 电话
     */
    private String phone;

    /**
     * 入住时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime arriveTime;

    /**
     * 离店时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime leaveTime;

    /**
     * 单价
     */
    private Integer price;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}

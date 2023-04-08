package com.example.xiaosage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 入住登记表
 * </p>
 *
 * @author liu
 * @since 2023-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRecordArrive implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "arrive_id", type = IdType.AUTO)
    private Integer arriveId;

    /**
     * 消费者姓名
     */
    private String consumerName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证
     */
    private String identity;

    /**
     * 是否支付押金
     */
    private String deposit;

    /**
     * 入住登记时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime recordTime;

    /**
     * 登记人
     */
    private String username;

    /**
     * 订单号
     */
    private String orderNum;

}

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
 * 
 * </p>
 *
 * @author liu
 * @since 2023-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 预约房间号
     */
    private String orderRoomNum;

    /**
     * 预约房间名
     */
    private String orderRoomTitle;

    /**
     * 预约者姓名
     */
    private String orderConsumerName;

    /**
     * 预约者电话
     */
    private String orderPhone;

    /**
     * 预约者身份证
     */
    private String orderIdentity;

    /**
     * 入住日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime arriveTime;

    /**
     * 到期日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime leaveTime;

    /**
     * 预定房间价格
     */
    private Integer orderPrice;

    /**
     * 订单状态
     */
    private String orderState;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 订单号
     */
    private String orderNum;

}

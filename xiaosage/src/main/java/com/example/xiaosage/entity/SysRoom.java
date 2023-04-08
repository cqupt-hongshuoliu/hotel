package com.example.xiaosage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class SysRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "room_id", type = IdType.AUTO)
    private Integer roomId;

    /**
     * 房间类型
     */
    private String roomType;

    /**
     * 房间标题
     */
    private String roomTitle;

    /**
     * 房间号
     */
    private String roomNumber;

    /**
     * 房间价格
     */
    private Integer roomPrice;

    /**
     * 是否已入住
     */
    private String roomState;

    /**
     * 房间首页图
     */
    private String roomPicture;

    /**
     * 房间描述
     */
    private String roomDescription;

}

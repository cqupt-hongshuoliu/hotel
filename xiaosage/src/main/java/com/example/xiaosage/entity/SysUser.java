package com.example.xiaosage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author liu
 * @since 2023-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
//    @JsonIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     *  UUID盐值
     */
    private String salt;

    /**
     * 角色
     */
    private String role;

}

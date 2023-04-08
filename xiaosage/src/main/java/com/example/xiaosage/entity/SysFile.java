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
 * @since 2023-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 下载链接
     */
    private String url;

    /**
     * 是否删除
     */
    private Boolean isDelete;

    /**
     * 是否禁用
     */
    private Boolean enable;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件md5
     */
    private String md5;

    /**
     * 使用途径
     */
    private String purpose;

}

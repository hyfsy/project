package com.hyf.async.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class AsyncLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 业务类型
     */
    private String type;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 任务载荷
     */
    private String payload;
    /**
     * 已重试次数
     */
    private Integer retryCount;
    /**
     * 任务状态
     */
    private Integer status;
    /**
     * 失败原因
     */
    private String failCause;
    /**
     * 任务载荷的SHA1值
     */
    private String sha1;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 版本号
     */
    @Version
    private Integer version;

}

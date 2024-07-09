package com.hyf.async.controller.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class AsyncLogVo {

    private Long id;
    private String type;
    private String name;
    private String payload;
    private Integer retryCount;
    private Integer status;
    private String failCause;
    private String remark;
    private Date createTime;
    private Date updateTime;

}

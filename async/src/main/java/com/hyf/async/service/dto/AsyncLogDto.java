package com.hyf.async.service.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 */
@Data
public class AsyncLogDto {

    private Long id;
    private String type;
    private String name;
    private String payload;
    private Integer retryCount;
    private Integer status;
    private String failCause;
    private String sha1;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private Integer version;

}

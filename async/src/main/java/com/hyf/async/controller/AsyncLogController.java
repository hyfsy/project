package com.hyf.async.controller;

import com.hyf.async.constant.AsyncLogConstants;
import com.hyf.async.controller.vo.AsyncLogVo;
import com.hyf.async.converter.AsyncLogConverter;
import com.hyf.async.entity.AsyncLog;
import com.hyf.async.service.IAsyncLogService;
import com.hyf.async.service.dto.AsyncLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping(AsyncLogConstants.API_ASYNC_LOG)
public class AsyncLogController {

    @Autowired
    private IAsyncLogService asyncLogService;

    @RequestMapping("list")
    public List<AsyncLogVo> list() {
        List<AsyncLog> asyncLogList = asyncLogService.list();
        List<AsyncLogDto> asyncLogDtos = AsyncLogConverter.INSTANCE.convertDoList(asyncLogList);
        List<AsyncLogVo> asyncLogVos = AsyncLogConverter.INSTANCE.convertDtoList(asyncLogDtos);
        return asyncLogVos;
    }
}

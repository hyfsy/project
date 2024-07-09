package com.hyf.async.converter;

import com.hyf.async.controller.vo.AsyncLogVo;
import com.hyf.async.entity.AsyncLog;
import com.hyf.async.service.dto.AsyncLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface AsyncLogConverter {

    AsyncLogConverter INSTANCE = Mappers.getMapper(AsyncLogConverter.class);

    AsyncLogDto convertDo(AsyncLog log);

    List<AsyncLogDto> convertDoList(List<AsyncLog> log);

    AsyncLogVo convertDto(AsyncLogDto log);

    List<AsyncLogVo> convertDtoList(List<AsyncLogDto> log);
}

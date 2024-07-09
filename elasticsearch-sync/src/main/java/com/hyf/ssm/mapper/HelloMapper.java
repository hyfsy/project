package com.hyf.ssm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.hyf.ssm.pojo.Hello;

/**
 * @author baB_hyf
 * @date 2020/05/17
 */
@Mapper
public interface HelloMapper
{

    @Insert("insert into hello (id, name) values (#{id}, #{name})")
    boolean add(Hello hello);

    @Delete("delete from hello where id = #{id}")
    boolean delete(@Param("id") Integer id);

    @Update("update hello set name = #{name} where id = #{id}")
    boolean update(Hello hello);

    @Select("select id, name from hello where id = #{id}")
    Hello find(@Param("id") Integer id);

    @Select("select id, name from hello")
    List<Hello> list();
}

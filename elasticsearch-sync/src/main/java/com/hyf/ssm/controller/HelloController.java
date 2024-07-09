package com.hyf.ssm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyf.ssm.mapper.HelloMapper;
import com.hyf.ssm.pojo.Hello;

/**
 * @author baB_hyf
 * @date 2020/05/17
 */
@RestController
public class HelloController
{

    @Autowired
    private HelloMapper helloMapper;

    @RequestMapping("add")
    public boolean find(@RequestParam Integer id, @RequestParam String name) {
        return helloMapper.add(new Hello(id, name));
    }

    @RequestMapping("delete/{id}")
    public boolean delete(@PathVariable Integer id) {
        return helloMapper.delete(id);
    }

    @RequestMapping("update")
    public boolean update(@RequestParam Integer id, @RequestParam String name) {
        return helloMapper.update(new Hello(id, name));
    }

    @RequestMapping("find/{id}")
    public Hello find(@PathVariable Integer id) {
        return helloMapper.find(id);
    }

    @RequestMapping("list")
    public List<Hello> list() {
        return helloMapper.list();
    }
}

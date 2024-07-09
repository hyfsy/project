package com.hyf.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyf.ssm.mapper.HelloMapper;
import com.hyf.ssm.pojo.Hello;
import com.hyf.ssm.service.IHelloService;

/**
 * @author baB_hyf
 * @date 2020/05/17
 */
@Service
public class HelloServiceImpl implements IHelloService
{

    @Autowired
    private HelloMapper helloMapper;

    @Override
    public Hello hello(Integer id) {
        return helloMapper.find(id);
    }

    @Override
    public boolean add(Hello hello) {
        return false;
    }
}

package com.hyf.ssm.service;

import com.hyf.ssm.pojo.Hello;

/**
 * @author baB_hyf
 * @date 2020/05/17
 */
public interface IHelloService {
    Hello hello(Integer id);
    
    boolean add(Hello hello);
}

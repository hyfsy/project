package com.hyf.test;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author baB_hyf
 * @date 2022/03/01
 */
@SpringBootTest(classes = CacheApplicationTests.class)
public class CacheApplicationTests
{

    @Autowired
    private List<String> slist;
    @Autowired
    private ApplicationContext context;

    @Bean
    public String s1() {
        return "s1";
    }

    @Bean
    public String s2() {
        return "s2";
    }

    @Test
    public void testA() {
        context.getBean(String.class);
        System.out.println();
    }
}

package com.hyf.cache.test.test.origin;

import org.springframework.cache.annotation.Cacheable;

/**
 * @author baB_hyf
 * @date 2022/01/13
 */
public class CacheService implements ICacheService {

    @Cacheable(value = "test1")
    @Override
    public String testCacheable1(Integer id) {
        return null;
    }

    @Cacheable(value = "test2", key = "#id")
    @Override
    public String testCacheable2(Integer id) {
        System.out.println("test");
        return "test";
    }

    @Override
    public String testCacheable3(Integer id) {
        return testCacheable2(id);
    }

    @Override
    @Cacheable(value = "test3", key = "#person.id")
    public String testCacheable4(Person person) {
        return testCacheable2(person.getId());
    }

    @Cacheable("test5")
    @Override
    public String testCacheable5(Person person) {
        return testCacheable2(person.getId());
    }

    @Cacheable("test6")
    @Override
    public String testCacheable6(Integer id, Person person) {
        return "ttt";
    }
}

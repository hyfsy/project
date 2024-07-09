package com.hyf.cache.test.test.origin;

/**
 * @author baB_hyf
 * @date 2022/01/20
 */
public interface ICacheService {

    public String testCacheable1(Integer id);

    public String testCacheable2(Integer id);

    public String testCacheable3(Integer id);

    public String testCacheable4(Person person);

    public String testCacheable5(Person person);

    public String testCacheable6(Integer id, Person person);
}

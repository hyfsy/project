package com.hyf.cache.test.test.origin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author baB_hyf
 * @date 2022/01/20
 */
@RestController
// @EnableCaching
// @EnableLoadTimeWeaving
// @EnableCaching(mode = AdviceMode.ASPECTJ)
public class TTT
{

    @Autowired
    private ICacheService cacheService;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TTT.class);
        ICacheService cacheService = ctx.getBean(ICacheService.class);

        // cacheService.testCacheable1(1);
        //
        // cacheService.testCacheable2(111);
        // cacheService.testCacheable2(111);
        //
        // cacheService.testCacheable3(111);
        // cacheService.testCacheable3(111);
        //
        // cacheService.testCacheable4(new Person(222, "张三"));
        // cacheService.testCacheable4(new Person(111, "张三"));
        //
        // cacheService.testCacheable5(new Person(111, "张三"));
        // cacheService.testCacheable5(new Person(111, "张三"));

        cacheService.testCacheable6(111, new Person(111, "张三"));

    }

    @RequestMapping("/ttt")
    public String ttt() {

        cacheService.testCacheable2(111);
        cacheService.testCacheable2(111);


        cacheService.testCacheable3(111);
        cacheService.testCacheable3(111);

        return "success";
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

    @Bean
    public ICacheService cacheService() {
        return new CacheService();
    }
}

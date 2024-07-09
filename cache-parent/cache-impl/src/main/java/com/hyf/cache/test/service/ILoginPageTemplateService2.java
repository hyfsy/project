package com.hyf.cache.test.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.hyf.cache.annotation.EpCacheEvict;
import com.hyf.cache.annotation.EpCacheable;
import com.hyf.cache.test.entity.LoginPageTemplate;
import com.hyf.cache.test.entity.PageData;

/**
 * 模板模板服务上层接口
 * 
 * @author baB_hyf
 * @date 2021/03/08
 */
// @CacheKey(cacheNames = "logindesign", cacheClass = LoginPageTemplate.class)
public interface ILoginPageTemplateService2
{

    @EpCacheEvict(cacheNames = "logindesign", key = "#loginPageTemplate.rowGuid", cacheClass = LoginPageTemplate.class)
    boolean insertLoginPageTemplate(LoginPageTemplate loginPageTemplate);

    @EpCacheEvict(cacheNames = "logindesign", key = "#loginPageTemplate.rowGuid", cacheClass = LoginPageTemplate.class)
    boolean updateLoginPageTemplate(LoginPageTemplate loginPageTemplate);

    @EpCacheEvict(cacheNames = "logindesign", key = "#rowGuid", cacheClass = LoginPageTemplate.class)
    boolean deleteLoginPageTemplate(String rowGuid);

    boolean insertOrUpdateLoginPageTemplate(LoginPageTemplate loginPageTemplate);

    //
    @EpCacheable(cacheNames = "logindesign", key = "#rowGuid", ttl = 1000, unit = TimeUnit.SECONDS, forceConsistency = true, cacheClass = LoginPageTemplate.class)
    LoginPageTemplate findLoginPageTemplate(String rowGuid, String txt);

    @EpCacheable(cacheNames = "logindesign", cacheClass = LoginPageTemplate.class)
    LoginPageTemplate findLoginPageTemplateByTemplateName(String templateName);

    @EpCacheable(cacheNames = "logindesign", cacheClass = LoginPageTemplate.class)
    List<LoginPageTemplate> listLoginPageTemplate();

    @EpCacheable(cacheNames = "logindesign",
            // key = "#templateGuidList",
            cacheClass = LoginPageTemplate.class)
    List<LoginPageTemplate> listLoginPageTemplateByGuids(List<String> templateGuidList);

    @EpCacheable(cacheNames = "logindesign", key = "#pageGuid + '_' + #enable", cacheClass = LoginPageTemplate.class)
    List<LoginPageTemplate> findLoginPageTemplateByPageGuidOrEnable(String pageGuid, boolean enable);

    @EpCacheEvict(cacheNames = "logindesign", key = "#pageGuid + '_' + #enable", cacheClass = LoginPageTemplate.class)
    void updateLoginPageTemplateByPageGuid(String pageGuid, boolean enable, String updateField);

    @EpCacheable(cacheNames = "logindesign", cacheClass = LoginPageTemplate.class)
    PageData<LoginPageTemplate> paginatorAllLoginPageTemplate(String templateName, int first, int pageSize, String sortField, String sortOrder);

}

package com.hyf.cache.test.service;

import java.util.List;

import com.hyf.cache.annotation.CacheKey;
import com.hyf.cache.annotation.EpCacheable;
import com.hyf.cache.bus.annotation.ReuseModifiable;
import com.hyf.cache.bus.annotation.ReuseCacheable;
import com.hyf.cache.test.entity.LoginPageTemplate;
import com.hyf.cache.test.entity.PageData;
import com.hyf.cache.enums.ModifyOperation;

/**
 * 模板模板服务上层接口
 * 
 * @author baB_hyf
 * @date 2021/03/08
 */
public interface ILoginPageTemplateService
{

    @ReuseModifiable(cacheNames = "logindesign", op = ModifyOperation.INSERT,
            mapperIdx = "#loginPageTemplate.rowGuid",
            cacheClass = LoginPageTemplate.class)
    boolean insertLoginPageTemplate(LoginPageTemplate loginPageTemplate);

    @ReuseModifiable(cacheNames = "logindesign", op = ModifyOperation.UPDATE,
            mapperIdx = "#loginPageTemplate.rowGuid",
            cacheClass = LoginPageTemplate.class)
    boolean updateLoginPageTemplate(LoginPageTemplate loginPageTemplate);

    @ReuseModifiable(cacheNames = "logindesign", op = ModifyOperation.DELETE,
            mapperIdx = "#rowGuid",
            cacheClass = LoginPageTemplate.class)
    boolean deleteLoginPageTemplate(String rowGuid);

    boolean insertOrUpdateLoginPageTemplate(LoginPageTemplate loginPageTemplate);

    @ReuseCacheable(cacheNames = "logindesign",
            mapperIdx = "#rowGuid",
            cacheClass = LoginPageTemplate.class)
    LoginPageTemplate findLoginPageTemplate(String rowGuid);

    @ReuseCacheable(cacheNames = "logindesign",
            mapperIdx = "#templateName",
            cacheClass = LoginPageTemplate.class)
    LoginPageTemplate findLoginPageTemplateByTemplateName(String templateName);

    @ReuseCacheable(cacheNames = "logindesign",
            mapperIdx = "",
            cacheClass = LoginPageTemplate.class)
    List<LoginPageTemplate> listLoginPageTemplate();

    @ReuseCacheable(cacheNames = "logindesign",
            mapperIdx = "#templateGuidList:templateGuid",
            cacheClass = LoginPageTemplate.class)
    List<LoginPageTemplate> listLoginPageTemplateByGuids(List<String> templateGuidList);

    @ReuseCacheable(cacheNames = "logindesign",
            mapperIdx = {"#pageGuid", "#enable"},
            cacheClass = LoginPageTemplate.class)
    List<LoginPageTemplate> findLoginPageTemplateByPageGuidOrEnable(String pageGuid, boolean enable);

    @ReuseModifiable(cacheNames = "logindesign", op = ModifyOperation.UPDATE,
            mapperIdx = {"#pageGuid", "#enable"},
            cacheClass = LoginPageTemplate.class)
    void updateLoginPageTemplateByPageGuid(String pageGuid, boolean enable, String updateField);

    @CacheKey(cacheNames = "logindesign", cacheClass = LoginPageTemplate.class)
    @EpCacheable
    PageData<LoginPageTemplate> paginatorAllLoginPageTemplate(String templateName, int first, int pageSize,
                                                              String sortField, String sortOrder);
}

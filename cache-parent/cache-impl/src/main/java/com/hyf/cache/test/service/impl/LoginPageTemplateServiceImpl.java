package com.hyf.cache.test.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.hyf.cache.test.entity.LoginPageTemplate;
import com.hyf.cache.test.entity.PageData;
import com.hyf.cache.test.service.ILoginPageTemplateService;

/**
 * @author baB_hyf
 * @date 2022/02/10
 */
@Component
public class LoginPageTemplateServiceImpl implements ILoginPageTemplateService
{

    @Override
    public boolean insertLoginPageTemplate(LoginPageTemplate loginPageTemplate) {
        return false;
    }

    @Override
    public boolean updateLoginPageTemplate(LoginPageTemplate loginPageTemplate) {
        return false;
    }

    @Override
    public boolean insertOrUpdateLoginPageTemplate(LoginPageTemplate loginPageTemplate) {
        return false;
    }

    @Override
    public boolean deleteLoginPageTemplate(String rowGuid) {
        return false;
    }

    @Override
    public LoginPageTemplate findLoginPageTemplate(String rowGuid) {
        System.out.println("invoke");
        return getLoginPageTemplateList(1).get(0);
    }

    @Override
    public LoginPageTemplate findLoginPageTemplateByTemplateName(String templateName) {
        return getLoginPageTemplateList(1).get(0);
    }

    @Override
    public List<LoginPageTemplate> listLoginPageTemplate() {
        return null;
    }

    @Override
    public List<LoginPageTemplate> listLoginPageTemplateByGuids(List<String> templateGuidList) {
        return getLoginPageTemplateList(templateGuidList.size());
    }

    @Override
    public List<LoginPageTemplate> findLoginPageTemplateByPageGuidOrEnable(String pageGuid, boolean enable) {
        int pageSize = 10;
        return getLoginPageTemplateList(pageSize);
    }

    @Override
    public PageData<LoginPageTemplate> paginatorAllLoginPageTemplate(String templateName, int first, int pageSize, String sortField, String sortOrder) {
        return new PageData<>(pageSize, getLoginPageTemplateList(pageSize));
    }

    @Override
    public void updateLoginPageTemplateByPageGuid(String pageGuid, boolean enable, String updateField) {

    }

    private List<LoginPageTemplate> getLoginPageTemplateList(int size) {
        List<LoginPageTemplate> templateList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            templateList.add(new LoginPageTemplate(i + "", UUID.randomUUID().toString()));
        }
        return templateList;
    }
}

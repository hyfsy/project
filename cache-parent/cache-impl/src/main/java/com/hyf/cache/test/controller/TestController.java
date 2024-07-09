package com.hyf.cache.test.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyf.cache.test.entity.LoginPageTemplate;
import com.hyf.cache.test.service.ILoginPageTemplateService;
import com.hyf.cache.test.service.ILoginPageTemplateService2;

/**
 * http://localhost:8082/test/2/d?p=222
 * 
 * @author baB_hyf
 * @date 2022/02/10
 */
@RestController
@RequestMapping("test")
public class TestController
{

    @Resource
    private ILoginPageTemplateService loginPageTemplateService;

    @Resource
    private ILoginPageTemplateService2 loginPageTemplateService2;

    @RequestMapping("")
    public LoginPageTemplate test() {
        return new LoginPageTemplate("1", "张三");
    }

    @RequestMapping("1")
    public LoginPageTemplate test1(@RequestParam(value = "p", defaultValue = "111") String p) {
        return loginPageTemplateService.findLoginPageTemplate(p);
    }

    @RequestMapping("1/d")
    public String test1_d(@RequestParam(value = "p", defaultValue = "111") String p) {
        loginPageTemplateService.updateLoginPageTemplate(new LoginPageTemplate("2", "1"));
        return "success";
    }

    @RequestMapping("2")
    public LoginPageTemplate test2(@RequestParam(value = "p", defaultValue = "111") String p) {
        LoginPageTemplate loginPageTemplate = loginPageTemplateService2.findLoginPageTemplate(p, "txt");
        return loginPageTemplate;
    }

    @RequestMapping("2/l")
    public String test3(@RequestParam(value = "p", defaultValue = "111") String p) {
        List<LoginPageTemplate> list = loginPageTemplateService2.listLoginPageTemplateByGuids(Arrays.asList(new String[] {"2", "3" }));
        return list.toString();
    }

    // findLoginPageTemplateByPageGuidOrEnable
    @RequestMapping("2/list")
    public String test4(@RequestParam(value = "p", defaultValue = "111") String p) {
        List<LoginPageTemplate> list = loginPageTemplateService2.findLoginPageTemplateByPageGuidOrEnable("2", true);
        return list.toString();
    }

    @RequestMapping("2/update")
    public String test2_update(@RequestParam(value = "p", defaultValue = "111") String p) {
        LoginPageTemplate loginPageTemplate = new LoginPageTemplate("0", "新的值");
        loginPageTemplateService2.updateLoginPageTemplate(loginPageTemplate);
        return "success";
    }

    @RequestMapping("2/delete")
    public String test2_delete(@RequestParam(value = "p", defaultValue = "111") String p) {
        loginPageTemplateService2.deleteLoginPageTemplate(p);
        return "success";
    }

    @RequestMapping("2/d")
    public String test2_d(@RequestParam(value = "p", defaultValue = "111") String p) {
        LoginPageTemplate loginPageTemplate = new LoginPageTemplate("2", "1");
        String id = "1";
        List<String> stringList = Arrays.asList(id);

        // loginPageTemplateService2.insertLoginPageTemplate(loginPageTemplate);
        loginPageTemplateService2.findLoginPageTemplate("111", "xx");

        // loginPageTemplateService2.insertLoginPageTemplate(loginPageTemplate);
        loginPageTemplateService2.updateLoginPageTemplate(loginPageTemplate);
        // loginPageTemplateService2.deleteLoginPageTemplate(id);
        // loginPageTemplateService2.findLoginPageTemplate(id);
        // loginPageTemplateService2.findLoginPageTemplateByTemplateName("loginPageTemplate");
        // loginPageTemplateService2.listLoginPageTemplate();
        // loginPageTemplateService2.listLoginPageTemplateByGuids(stringList);
        // loginPageTemplateService2.findLoginPageTemplateByPageGuidOrEnable("pageGuid",
        // true);
        // loginPageTemplateService2.updateLoginPageTemplateByPageGuid("pageGuid", true,
        // "updateField");
        // loginPageTemplateService2.paginatorAllLoginPageTemplate("pageGuid", 0, 0,
        // null, null);
        return "success";
    }
}

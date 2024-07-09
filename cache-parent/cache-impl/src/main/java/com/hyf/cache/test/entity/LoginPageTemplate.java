package com.hyf.cache.test.entity;

/**
 * @author baB_hyf
 * @date 2022/01/18
 */
@Entity(table = "login_page", id = "rowguid")
public class LoginPageTemplate extends BaseEntity
{

    String rowGuid;
    String name;

    public LoginPageTemplate() {
    }

    public String getRowGuid() {
        return rowGuid;
    }

    public void setRowGuid(String rowGuid) {
        this.rowGuid = rowGuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoginPageTemplate(String rowGuid, String name) {
        super();
        this.rowGuid = rowGuid;
        this.name = name;
    }

}

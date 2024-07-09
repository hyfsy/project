package com.hyf.cache.impl.key;

import org.springframework.beans.factory.annotation.Value;

import com.hyf.cache.impl.constants.CacheKeyConstants;
import com.hyf.cache.impl.utils.ApplicationContextUtils;

/**
 * @author baB_hyf
 * @date 2022/02/08
 */
public class CacheKeyUtils
{

    // @Autowired(required = false)
    // private CacheVersionSupplier cacheVersionSupplier;
    // @Autowired(required = false)
    // private CacheApplicationSupplier cacheApplicationSupplier;
    // @Autowired(required = false)
    // private CacheTenantSupplier cacheTenantSupplier;

    public static final String DEFAULT_APPLICATION_NAME = "";
    public static final String DEFAULT_TENANT_NAME = "";

    @Value("${spring.application.name:" + DEFAULT_APPLICATION_NAME + "}")
    private String applicationName = DEFAULT_APPLICATION_NAME;
    @Value("${spring.application.tenant:" + DEFAULT_TENANT_NAME + "}")
    private String tenantName = DEFAULT_TENANT_NAME;

    public static String getVersion() {
        return CacheKeyConstants.VERSION;
    }

    public static String getApplication() {
        CacheKeyUtils utils = ApplicationContextUtils.getBeanIfExist(CacheKeyUtils.class);
        if (utils == null) {
            return DEFAULT_APPLICATION_NAME;
        }

        return utils.applicationName;
    }

    public static String getTenant() {
        CacheKeyUtils utils = ApplicationContextUtils.getBeanIfExist(CacheKeyUtils.class);
        if (utils == null) {
            return DEFAULT_TENANT_NAME;
        }

        return utils.tenantName;
    }

}

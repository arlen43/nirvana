/**
 * 项目名: product-service
 * 文件名：BusinessContext.java 
 * 版本信息： V1.0
 * 日期：2017年3月31日 
 * Copyright: Corporation 2017 版权所有
 *
 */
package com.arlen.common.context;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

/** 
 * 项目名称：product-service <br>
 * 类名称：BusinessContext <br>
 * 类描述：业务上下文，线程安全，可以放置一些全局的参数，比如登录用户信息，不管再biz层还是在service层，不用参数传来传去<br>
 * 支持子线程等。
 * 创建人：arlen <br>
 * 创建时间：2015年7月25日<br>
 * @version 1.0
 * @author arlen
 */
public class BusinessContext {

    protected BusinessContext() {}

    private final static InheritableThreadLocal<Map<String, Object>> BIZ_MAP = new InheritableThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    /**
     * 设置属性
     * @param key
     * @param value
     */
    public static void setObjectProperty(String key, Object value) {
        if (!StringUtils.isEmpty(key)) {
            BIZ_MAP.get().put(key.toUpperCase(), value);
        }
    }

    /**
     * 设置只读属性，如果值已存在，则保留原有值，返回context中的值
     * @param key
     * @param value
     * @return 设置的值
     */
    public static Object setReadOnlyProperty(String key, Object value) {
        Object oldValue = null;
        if (!StringUtils.isEmpty(key) && (oldValue = BIZ_MAP.get().get(key.toUpperCase())) == null) {
            BIZ_MAP.get().put(key.toUpperCase(), value);
            return value;
        }
        return oldValue;
    }
    
    /**
     * 获取变量
     * @param key
     * @return 获取的变量值
     */
    public static Object getObjectProperty(String key) {
        if (!StringUtils.isEmpty(key)) {
            return BIZ_MAP.get().get(key.toUpperCase());
        }
        return null;
    }
}

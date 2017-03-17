/**
 * 项目名: greenpass-api
 * 文件名：ParamJson.java 
 * 版本信息： V1.0
 * 日期：2016年7月25日 
 * Copyright: Corporation 2016 版权所有
 *
 */
package com.arlen.common.verify.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 项目名称：greenpass-api <br>
 * 类名称：ParamJson <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2016年7月25日 上午11:42:57 <br>
 * @version 1.0
 * @author arlen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamJson {
	Class<?> clazz();
	boolean array() default true;
}

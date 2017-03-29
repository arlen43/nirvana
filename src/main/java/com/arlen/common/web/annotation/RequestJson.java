/**
 * 项目名: nirvana
 * 文件名：RequestJson.java 
 * 版本信息： V1.0
 * 日期：2017年3月6日 
 */
package com.arlen.common.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 项目名称：nirvana <br>
 * 类名称：RequestJson <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月6日 下午3:43:53 <br>
 * @version 1.0
 * @author arlen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestJson {

	abstract String path() default "";
	abstract Class<?>[] types() default {Object.class};

}

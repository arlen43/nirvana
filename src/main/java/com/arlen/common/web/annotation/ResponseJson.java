/**
 * 项目名: nirvana
 * 文件名：ResponseJson.java 
 * 版本信息： V1.0
 * 日期：2017年3月13日 
 *
 */
package com.arlen.common.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 项目名称：nirvana <br>
 * 类名称：ResponseJson <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月13日 下午5:15:50 <br>
 * @version 1.0
 * @author arlen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResponseJson {
	boolean base64() default true;
}

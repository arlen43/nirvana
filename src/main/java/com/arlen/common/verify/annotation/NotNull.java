/** 
 * 文件名：NotNull.java <br>
 * 版本信息： V1.0<br>
 * 日期：2015年10月20日 <br>
 * Copyright: Corporation 2015 <br>
 * 版权所有 江苏宏坤供应链有限公司<br>
 */
package com.arlen.common.verify.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 项目名称：greenpass <br>
 * 类名称：NotNull <br>
 * 类描述：参数校验注解，暂时只做非空校验，后期加其他的校验规则 <br>
 * Copyright: Copyright (c) 2015 by 江苏宏坤供应链有限公司<br> 
 * 创建人：arlen<br>
 * 创建时间：2015年10月20日 上午11:46:50 <br>
 * 修改人：arlen<br>
 * 修改时间：2015年10月20日 上午11:46:50 <br>
 * 修改备注：<br>
 * @version 1.0<br>
 * @author arlen<br>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
	String value() default "";
}

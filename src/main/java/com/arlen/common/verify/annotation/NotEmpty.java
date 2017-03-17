/** 
 * 文件名：NotEmpty.java <br>
 * 版本信息： V1.0<br>
 * 日期：2015年11月11日 <br>
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
 * 类名称：NotEmpty <br>
 * 类描述：只对String类型生效 <br>
 * Copyright: Copyright (c) 2015 by 江苏宏坤供应链有限公司<br> 
 * 创建人：arlen<br>
 * 创建时间：2015年11月11日 下午10:38:29 <br>
 * 修改人：arlen<br>
 * 修改时间：2015年11月11日 下午10:38:29 <br>
 * 修改备注：<br>
 * @version 1.0<br>
 * @author arlen<br>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
	String value() default "";
}

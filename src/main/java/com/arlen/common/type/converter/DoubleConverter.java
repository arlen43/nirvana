/**
 * 项目名: greenpass-api
 * 文件名：DoubuleCaster.java 
 * 版本信息： V1.0
 * 日期：2016年7月25日 
 * Copyright: Corporation 2016 版权所有
 *
 */
package com.arlen.common.type.converter;

import com.arlen.common.type.exception.TypeConvertException;

/**
 * 项目名称：greenpass-api <br>
 * 类名称：DoubuleCaster <br>
 * 类描述：<br>
 * Copyright: Copyright (c) 2016 by 江苏宏坤供应链管理有限公司<br>
 * Company: 江苏宏坤供应链管理有限公司<br>
 * 创建人：arlen <br>
 * 创建时间：2016年7月25日 下午4:23:39 <br>
 * 修改人：arlen<br>
 * 修改时间：2016年7月25日 下午4:23:39 <br>
 * 修改备注：<br>
 * 
 * @version 1.0
 * @author arlen
 */
public class DoubleConverter extends TypeConverter<Double> {

	@Override
	protected Double doConvert(String value) {
		try {
			return Double.valueOf(value);
		} catch (NumberFormatException e) {
			throw new TypeConvertException(Double.class, e);
		}
	}

}

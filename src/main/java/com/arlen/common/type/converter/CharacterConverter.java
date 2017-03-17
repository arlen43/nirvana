/**
 * 项目名: greenpass-api
 * 文件名：CharacterCaster.java 
 * 版本信息： V1.0
 * 日期：2016年7月25日 
 * Copyright: Corporation 2016 版权所有
 *
 */
package com.arlen.common.type.converter;

/** 
 * 项目名称：greenpass-api <br>
 * 类名称：CharacterCaster <br>
 * 类描述：<br>
 * Copyright: Copyright (c) 2016 by 江苏宏坤供应链管理有限公司<br>
 * Company: 江苏宏坤供应链管理有限公司<br>
 * 创建人：arlen <br>
 * 创建时间：2016年7月25日 下午4:23:39 <br>
 * 修改人：arlen<br>
 * 修改时间：2016年7月25日 下午4:23:39 <br>
 * 修改备注：<br>
 * @version 1.0
 * @author arlen
 */
public class CharacterConverter extends TypeConverter<Character> {

	/**
	 * 只把第一个字符转换
	 * @param value
	 */
	@Override
	protected Character doConvert(String value) {
		if (value.length() > 1) {
			logger.warn("Convert string value '{}' to Character, value's length great than 1", value);
		}
		return Character.valueOf(value.charAt(0));
	}

}

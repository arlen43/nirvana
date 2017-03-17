/** 
 * 文件名：TypeUtils.java <br>
 * 版本信息： V1.0<br>
 * 日期：2015年10月22日 <br>
 * Copyright: Corporation 2015 <br>
 * 版权所有 江苏宏坤供应链有限公司<br>
 */
package com.arlen.common.type;

import java.math.BigDecimal;
import java.util.Date;

import com.arlen.common.type.converter.BigDecimalConverter;
import com.arlen.common.type.converter.BooleanConverter;
import com.arlen.common.type.converter.ByteConverter;
import com.arlen.common.type.converter.CharacterConverter;
import com.arlen.common.type.converter.DateConverter;
import com.arlen.common.type.converter.DoubleConverter;
import com.arlen.common.type.converter.FloatConverter;
import com.arlen.common.type.converter.IntegerConverter;
import com.arlen.common.type.converter.LongConverter;
import com.arlen.common.type.converter.StringConverter;
import com.arlen.common.type.converter.TypeConverter;

/** 
 * 项目名称：greenpass <br>
 * 类名称：TypeUtils <br>
 * 类描述： <br>
 * Copyright: Copyright (c) 2015 by 江苏宏坤供应链有限公司<br> 
 * 创建人：arlen<br>
 * 创建时间：2015年10月22日 下午3:33:31 <br>
 * 修改人：arlen<br>
 * 修改时间：2015年10月22日 下午3:33:31 <br>
 * 修改备注：<br>
 * @version 1.0<br>
 * @author arlen<br>
 */
public class TypeUtil {

	private TypeUtil() {}
	private final static Class<?>[] baseClazzArr = new Class[] { Boolean.class, Byte.class, Character.class, Integer.class, Long.class, Double.class, Float.class, String.class, Date.class, BigDecimal.class };
	private final static String[] baseTypeArr = new String[] { "boolean", "byte", "char", "int", "long", "double", "float" };
	private final static TypeConverter<?> [] converterArr = new TypeConverter[] {new BooleanConverter(), new ByteConverter(), new CharacterConverter(), new IntegerConverter(), new LongConverter(), new DoubleConverter(), new FloatConverter(), new StringConverter(), new DateConverter(), new BigDecimalConverter()};
	
	public static boolean isInstanceOfBaseType(Class<?> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("Input class is null");
		}
		for (Class<?> baseClazz : baseClazzArr) {
			if (clazz.equals(baseClazz)) {
				return true;
			}
		}
		for (String baseType : baseTypeArr) {
			if (baseType.equals(clazz.toString())) {
				return true;
			}
		}
		return false;
	}

	public static Object getObjectValue(String value, Class<?> clazz) throws Exception {
		if (value == null || value.equals("") || clazz == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < baseClazzArr.length; i++) {
			Class<?> baseClazz = baseClazzArr[i];
			if (clazz.equals(baseClazz)) {
				return converterArr[i].convert(value);
			}
		}
		for (int i = 0; i < baseTypeArr.length; i++) {
			String baseType = baseTypeArr[i];
			if (baseType.equals(clazz.toString())) {
				return converterArr[i].convert(value);
			}
		}
		return null;
	}

}

/**
 * 项目名: oss-service
 * 文件名：VerifyUtils.java 
 * 版本信息： V1.0
 * 日期：2016年4月19日 
 * Copyright: Corporation 2016 版权所有
 *
 */
package com.arlen.common.verify;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.arlen.common.type.TypeUtil;
import com.arlen.common.verify.annotation.Length;
import com.arlen.common.verify.annotation.NotEmpty;
import com.arlen.common.verify.annotation.NotNull;


/** 
 * 项目名称：oss-service <br>
 * 类名称：VerifyUtils <br>
 * 类描述：参数校验，基于注解，兼容之前的NotNull、NotEmpty，后期会去掉；加入新注解Length，后期会加入Regex注解，用正则校验<br>
 * Copyright: Copyright (c) 2016 by 江苏宏坤供应链管理有限公司<br>
 * Company: 江苏宏坤供应链管理有限公司<br>
 * 创建人：arlen <br>
 * 创建时间：2016年4月19日 下午3:41:42 <br>
 * 修改人：arlen<br>
 * 修改时间：2016年4月19日 下午3:41:42 <br>
 * 修改备注：<br>
 * @version 1.0
 * @author arlen
 */
public class VerifyUtils {

	private VerifyUtils() {}

	private final static Logger logger = LoggerFactory.getLogger(VerifyUtils.class);
	
	private static String getDisplayFiledName(String fieldName, String fieldNameCn) {
		return (fieldNameCn == null || "".equals(fieldNameCn))? fieldName: fieldName+"("+fieldNameCn+")";
	}
	
	/**
	 * 校验参数是否正常，如果遇到一个null则返回校验结果 <br>
	 * 
	 * @param data
	 * @return 校验结果
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @since CodingExample　Ver1.0
	 */
	public static <T> VerifyResult validate(T data) {
		
		if (data == null) {
			return new VerifyResult("参数为空", "", false);
		}
		
		// 默认校验成功
		VerifyResult result = new VerifyResult();
		
		List<Field> fieldList = new ArrayList<Field>();
		Class<?> clazz = data.getClass(), superClazz = clazz;
		do {
			fieldList.addAll(Arrays.asList(superClazz.getDeclaredFields()));
		} while (null != (superClazz = superClazz.getSuperclass()));

		for (Field field : fieldList) {
			field.setAccessible(true);
			try {
				Object value = field.get(data);
				// 获取Length注解属性
				Length lengthAno = field.getAnnotation(Length.class);
				String fieldNameCn = null;
				String defaultValue = null;
				String options = null;
				int length = 0;
				boolean must = false;
				boolean cut = false;
				if (null != lengthAno) {
					fieldNameCn = lengthAno.name();
					defaultValue = lengthAno.dflt();
					must = lengthAno.must();
					cut = lengthAno.cut();
					length = lengthAno.value();
					options = lengthAno.options();
				}
				String displayFiledName = getDisplayFiledName(field.getName(), fieldNameCn);
				
				// 兼容之前的NotNull、NotEmpty
				NotNull notNull = field.getAnnotation(NotNull.class);
				NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
				
				// TODO: 待修改为表驱动法，校验太多了
				
				// 如果值是null
				if (null == value) {
					// 不可以为null，直接返回
					if (null != notNull || null != notEmpty || (isEmpty(defaultValue) && must)) {
						return new VerifyResult(displayFiledName + "不能为空", field.getName(), false);
					} 
					// 如果值可为空，设置默认值，直接下一步
					else {
						// 设置默认值
						if (!isEmpty(defaultValue)) {
							value = TypeUtil.getObjectValue(defaultValue, field.getType());
							field.set(data, value);
						}
						continue;
					}
				}
				
				// 如果值不是null
				// 如果是一般类型
				if (TypeUtil.isInstanceOfBaseType(field.getType())) {
					String valueStr = value.toString();
					// 设置默认值
					if ("".equals(valueStr) && !isEmpty(defaultValue)) {
						valueStr = defaultValue;
						value = TypeUtil.getObjectValue(valueStr, field.getType());
						field.set(data, value);
					}
					
					// 处理空值；如果必填
					if (null != notEmpty || must) {
						if ("".equals(valueStr)) {
							return new VerifyResult(displayFiledName + "不能为空", field.getName(), false);
						}
					} else {
						if ("".equals(valueStr)) {
							continue;
						}
					}
					// 校验长度
					if (length > 0) {
						if (valueStr.length() > length) {
							if (cut) {
								// 截取字符串
								value = TypeUtil.getObjectValue(valueStr.substring(0, length), field.getType());
								field.set(data, value);
							}
							return new VerifyResult(displayFiledName + "过长，限定"+length+"字符，目前为"+valueStr.length()+"字符。参数值为["+valueStr+"]", field.getName(), false);
						} else {
							if (!isEmpty(options) && !contain(options, valueStr)) {
								return new VerifyResult(displayFiledName + "限定只能为["+options+"]，而目前参数值为["+valueStr+"]", field.getName(), false);
							}
						}
					}
				}
				// 其他非一般类型
				else {
					if (value instanceof Collection) {
						Collection<?> collection = (Collection<?>) value;
						if (null != notEmpty && CollectionUtils.isEmpty(collection)) {
							return new VerifyResult(displayFiledName + "不能为空", field.getName(), false);
						}
						Iterator<?> it = collection.iterator();
						while (it.hasNext()) {
							Object child = it.next();
							if (null == child)
								continue;
							if (!TypeUtil.isInstanceOfBaseType(child.getClass())) {
								result = validate(child);
								if (!result.isSuccess()) {
									return result;
								}
							}
						}
					} else {
						result = validate(value);
						if (!result.isSuccess()) {
							return result;
						}
					}
				}
			} catch (Exception e) {
				logger.error("参数校验异常", e);
			} finally {
				field.setAccessible(false);
			}
		}
		result.setSuccess(true);
		return result;
	}

	private static boolean isEmpty(Object value) {
		return value == null || "".equals(value);
	}
	
	private static boolean contain(String options, String valueStr) {
		if (isEmpty(options)) {
			return false;
		}
		String[] optionArr = options.split(",");
		for (String string : optionArr) {
			if (string.trim().equals(valueStr)) {
				return true;
			}
		}
		return false;
	}
	
	public static VerifyResult validate(Object... datas) {
		if (datas == null || datas.length == 0) {
			return new VerifyResult("参数为空", "", false);
		}
		for (Object data : datas) {
			VerifyResult result = validate(data);
			if (!result.isSuccess) {
				return result;
			}
		}
		return new VerifyResult("", "", true);
	}
	
	public static void main(String[] args) {
		// String ss = "{\"Indx\":\"126\",\"OrderSerialNo\":\"ORD15111100000003\",\"OrderNo\":\"FBA33J9FXY20151111002\",\"ActualGrossWeight\":\"\",\"ActualVol\":\"\",\"Tax\":\"\",\"IncPrice\":\"\",\"IncCurrency\":\"142\",\"IncCurrencyCN\":\"人民币\"}";
		// OrderLogisticsGoodsReceivedDto log = JsonUtil.toBean(ss, OrderLogisticsGoodsReceivedDto.class);
		// ParamVerifyResult result = new ParamVerifyResult();
		// result.setFieldName("sss");
		// //result.setMessage("mess");
		//
		VerifyResult result1 = new VerifyResult();
		result1.message = "message";
		result1.fieldName = "ds";
		//result1.count = 4555;
		
		VerifyResult result = VerifyUtils.validate(result1);
		System.out.println(result);
		System.out.println(result1);

		// List<ParamVerifyResult> list = new ArrayList<>();
		// list.add(result1);
		// result.setList(list);
		// System.out.println(JSON.toJSONString(validate(log)));
	}

	public static class VerifyResult {
		@NotNull("校验信息")
		@Length(value=20, name="message")
		private String message;
		@Length(value=20, name="fieldName", options="ss,dd")
		private String fieldName;
		private boolean isSuccess;

		public VerifyResult() {}

		public VerifyResult(String message, String fieldName, boolean isSuccess) {
			this.message = message;
			this.fieldName = fieldName;
			this.isSuccess = isSuccess;
		}

		public boolean isSuccess() {
			return isSuccess;
		}

		public void setSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		public String toString() {
			return "VerifyResult [message=" + message + ", fieldName=" + fieldName + ", isSuccess=" + isSuccess + "]";
		}
		
	}

}

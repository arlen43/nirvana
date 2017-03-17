package com.arlen.common.paramtype;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ReflectionUtils;

public class Type2Class {

	/**
	 * 根据type获取最终的泛型类型，因为是示例性代码，不够完善<br>
	 * TypeVariable只支持一种泛型，eg: class Test &lt;T&gt;<br>
	 * WildCardType只支持上边界<br>
	 * ParameterizedType只支持一种类型参数，eg: List&ltString&gt<br>
	 */
	public static Class<?> getArgumentOfType(Type type) {
		
		if (type instanceof TypeVariable) {
			Type bounds = ((TypeVariable<?>)type).getBounds()[0];
			return getArgumentOfType(bounds);
		} else if (type instanceof WildcardType) {
			Type uBoundArr[] = ((WildcardType)type).getUpperBounds();
			if (uBoundArr.length > 0) {
				return getArgumentOfType(uBoundArr[0]);
			}
			return Object.class;
		} else if (type instanceof ParameterizedType) {
			Type argu = ((ParameterizedType)type).getActualTypeArguments()[0];
			return getArgumentOfType(argu);
		} else if (type instanceof GenericArrayType) {
			Type comp = ((GenericArrayType)type).getGenericComponentType();
			return getArgumentOfType(comp);
		} else {
			// (type instanceof Class)
			return (Class<?>)type;
		}
	}
	
	/**
	 * 根据Type获取其对应的Class<br>
	 * Type到了ParameterizedType之后，就可以直接取出其原生类了
	 * 
	 */
	public static Class<?> getRawTypeOfType(Type type) {
		if (type instanceof TypeVariable) {
			// 已到最里层
			Type bounds = ((TypeVariable<?>)type).getBounds()[0];
			return (Class<?>)bounds;
		} else if (type instanceof WildcardType) {
			// 已到最里层
			Type uBoundArr[] = ((WildcardType)type).getUpperBounds();
			if (uBoundArr.length > 0) {
				return (Class<?>)uBoundArr[0];
			}
			return Object.class;
		} else if (type instanceof ParameterizedType) {
			// 获取原生类型
			Type argu = ((ParameterizedType)type).getRawType();
			return (Class<?>)argu;
		} else if (type instanceof GenericArrayType) {
			// 递归，直到找到ParameterizedType
			Type comp = ((GenericArrayType)type).getGenericComponentType();
			return getRawTypeOfType(comp);
		} else {
			// (type instanceof Class)
			return (Class<?>)type;
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private List<? super Number>[] numArr = new ArrayList[1];
	
	public static void main(String[] args) {
		Field numArr = ReflectionUtils.findField(Type2Class.class, "numArr");
		Type numArrType = numArr.getGenericType();
		// 输出：class java.lang.Object
		System.out.println(Type2Class.getArgumentOfType(numArrType));
		// 输出：interface java.util.List
		System.out.println(Type2Class.getRawTypeOfType(numArrType));
	}
}

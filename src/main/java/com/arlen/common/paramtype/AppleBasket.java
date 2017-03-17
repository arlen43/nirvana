package com.arlen.common.paramtype;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class AppleBasket extends Basket<Apple> {

	// 1. 获取子类的泛型类型
	public void getSubClassActualType() {
		Type superType = AppleBasket.class.getGenericSuperclass();
		if (ParameterizedType.class.isAssignableFrom(superType.getClass())) {
			ParameterizedType superParamType = (ParameterizedType)superType;
			System.out.println("Actual subclass argument: " + superParamType.getActualTypeArguments()[0]);
		}
	}
}

package com.arlen.common.paramtype;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ReflectionUtils;

public class Student {

	private Basket<Apple> appleBasket;
	
	public Basket<Apple> getAppleBasket() {
		return appleBasket;
	}

	public void setAppleBasket(Basket<Apple> appleBasket) {
		this.appleBasket = appleBasket;
	}
	
	// 2. 获取属性的泛型类型
	public void getFieldActualType() {
		Field field = ReflectionUtils.findField(Student.class, "appleBasket");
		Type fieldType = field.getGenericType();
		if (ParameterizedType.class.isAssignableFrom(fieldType.getClass())) {
			ParameterizedType fieldParamType = (ParameterizedType)fieldType;
			System.out.println("Actual type arguments: "+fieldParamType.getActualTypeArguments()[0]);
		}
	}
	
	// 3. 获取方法的返回值泛型类型
	public void getMethodReturnActualType() {
		Method method = ReflectionUtils.findMethod(Student.class, "getAppleBasket");
		Type returnType = method.getGenericReturnType();
		if (ParameterizedType.class.isAssignableFrom(returnType.getClass())) {
			ParameterizedType returnParamType = (ParameterizedType)returnType;
			System.out.println("Actual return arguments: "+returnParamType.getActualTypeArguments()[0]);
		}
	}
	
	// 4. 获取方法的参数泛型类型
	public void getMethodParamActualType() {
		Method method = ReflectionUtils.findMethod(Student.class, "setAppleBasket", Basket.class);
		Type[] paramTypes = method.getGenericParameterTypes();
		method.getGenericExceptionTypes();
		for (Type paramType : paramTypes) {
			if (ParameterizedType.class.isAssignableFrom(paramType.getClass())) {
				ParameterizedType paramParamType = (ParameterizedType)paramType;
				System.out.println("Actual param arguments: " + paramParamType.getActualTypeArguments()[0]);
			}
		}
	}
	
	// 5. 获取局部变量的泛型类型——行不通
	@SuppressWarnings("unused")
	public void getVariableActualType() {
		Basket<Apple> appleBasket = new Basket<Apple>();
		// 想要获取该局部变量 appleBasket 的泛型类型是没有任何办法的，原因看class文件的截图
	}
	
	// 5. 获取局部变量的泛型类型——行得通
	public void getVariableActualType1() {
		// Basket<Apple> appleBasket = new Basket<Apple>();
		AppleBasket appleBasket = new AppleBasket();
		Type type = appleBasket.getClass().getGenericSuperclass();
		if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
			System.out.println("Actual variable arguments: " + ((ParameterizedType)type).getActualTypeArguments()[0]);
		}
	}
	
	private static String[] testComp = new String[2];
	@SuppressWarnings("unchecked")
	private static List<Student>[] stuListArr = new ArrayList[2];
	
	public static void main(String[] args) {
		Student.class.getTypeParameters();
		System.out.println(Student.class.getTypeParameters());
		System.out.println(testComp.getClass().getComponentType());
		System.out.println(stuListArr.getClass().getComponentType());
		System.out.println(stuListArr.getClass().getTypeParameters());
		System.out.println();
		new AppleBasket().getSubClassActualType();
		new Student().getFieldActualType();
		new Student().getMethodReturnActualType();
		new Student().getMethodParamActualType();
		new Student().getVariableActualType1();
	}
}

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

@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class TypeTest<T> {

	
	private List objList = new ArrayList<>();
	private List<String> strList = new ArrayList<>();
	private List<T> paraList = new ArrayList<>();
	private List<? extends Number> numList = new ArrayList<>();
	private List<T[]> arrList = new ArrayList<>();
	private List<? super Number>[] numArr = new ArrayList[1];
	
	/**
	 * 测试Type子接口间的互相转换及含义
	 */
	public void testTypeSonInterface() {
		Field objList = ReflectionUtils.findField(TypeTest.class, "objList");
		// 返回值为Class类型，即List
		Type objType = objList.getGenericType();
		
		Field strList = ReflectionUtils.findField(TypeTest.class, "strList");
		// 返回值为ParameterizedType类型，即 List<String>
		Type strType = strList.getGenericType();
		// 返回值为Class类型，即List
		Type strRawTypes = ((ParameterizedType)strType).getRawType();
		// 返回值为Type数组，数组元素为Class类型，即String
		Type[] strArguArr = ((ParameterizedType)strType).getActualTypeArguments();

		Field paraList = ReflectionUtils.findField(TypeTest.class, "paraList");
		// 返回值为ParameterizedType类型，即 List<T>
		Type paraType = paraList.getGenericType();
		// 返回值为Class类型，即List
		Type paraRawTypes = ((ParameterizedType)paraType).getRawType();
		// 返回值为Type数组，数组元素为TypeVariable类型，即T
		Type[] paraArguArr = ((ParameterizedType)paraType).getActualTypeArguments();
		// 返回值为Type数组，数组元素为Class类型，即Object（因泛型擦除，T即Object）
		Type[] paraVarArr = ((TypeVariable)paraArguArr[0]).getBounds();
		
		Field numList = ReflectionUtils.findField(TypeTest.class, "numList");
		// 返回值为ParameterizedType类型，即List<? extends Number>
		Type numType = numList.getGenericType();
		// 返回值为Class类型，即List
		Type numRawTypes = ((ParameterizedType)numType).getRawType();
		// 返回值为Type数组，数组元素为WildCardType类型，即 ? extends Number
		Type[] numArguArr = ((ParameterizedType)numType).getActualTypeArguments();
		// 返回值为Type数组，数组元素为Class类型，即Number（获取通配类型的上边界）
		Type[] numUBoundArr = ((WildcardType)numArguArr[0]).getUpperBounds();
		// 返回值为Type数组，数组元素为Class类型，为null，因为下边界不可知
		Type[] numLBoundArr = ((WildcardType)numArguArr[0]).getLowerBounds();
		
		Field arrList = ReflectionUtils.findField(TypeTest.class, "arrList");
		// 返回值为ParameterizedType类型，即List<T[]>
		Type arrType = arrList.getGenericType();
		// 返回值为Class类型，即List
		Type arrRawTypes = ((ParameterizedType)arrType).getRawType();
		// 返回值为Type数组，数组元素为GenericArrayType类型，即T[]
		Type[] arrArguArr = ((ParameterizedType) arrType).getActualTypeArguments();
		// 返回值为TypeVariable类型，即T
		Type arrComType = ((GenericArrayType)arrArguArr[0]).getGenericComponentType();
		// 返回值为Type数组，数组元素为Class类型，即Object（因泛型擦除，T即Object）
		Type[] arrComBound = ((TypeVariable)arrComType).getBounds();
		
		Field numArr = ReflectionUtils.findField(TypeTest.class, "numArr");
		// 返回值为GenericArrayType类型，即List<? super Number>[]
		Type numArrType = numArr.getGenericType();
		// 返回值为ParameterizedType类型，即List<? super Number>
		Type numArrComType = ((GenericArrayType) numArrType).getGenericComponentType();
		// 返回值为Class类型，即List
		Type numArrRawTypes = ((ParameterizedType)numArrComType).getRawType();
		// 返回值为WildCardType类型，即<? super Number>
		Type[] numArrArguTypes = ((ParameterizedType)numArrComType).getActualTypeArguments();
		// 返回值为Type数组，数组元素为Class类型，即Object（也即?）
		Type[] numArrUBoundArr = ((WildcardType)numArrArguTypes[0]).getUpperBounds();
		// 返回值为Type数组，数组元素为Class类型，即Number
		Type[] numArrLBoundArr = ((WildcardType)numArrArguTypes[0]).getLowerBounds();
	}
	
	/**
	 * 测试Type子类的两个方法
	 */
	public void testTypeSonClass() {
		// 1. getComponentType
		List<? extends Number>[] nums = new ArrayList[2];
		// 返回值为Class类型，即ArrayList[]
		Class<?> numsClass = nums.getClass();
		// 返回值为Class类型，即ArrayList
		Class<?> numsComType = nums.getClass().getComponentType();
		
		// 返回值为null，因TypeTest没有数组类型，也即getComponentType是针对变量才能使用的
		Class<?> clazz = TypeTest.class.getComponentType();
		
		// 2. getTypeParameters
		// 局部变量
		TypeTest<Student> test = new TypeTest<>();
		// 返回值为TypeVarialble数组，数组元素为TypeVarialble类型，即T
		TypeVariable[] tv = test.getClass().getTypeParameters();
		// 返回值为Type数组，数组元素为Class类型，即Object（因泛型擦除，T即Object）
		// 因为test.getClass，返回的是TypeTest的声明，局部定义的不会去拿，想拿也拿不到，并且
		Type[] boud = tv[0].getBounds();
		
		// 类属性
		TypeVariable[] tv1 = strList.getClass().getTypeParameters();
		// 返回值为Type数组，数组元素为Class类型，即Object（因泛型擦除，T即Object）
		// 因为strList.getClass，返回的是TypeTest的声明，属性定义的不会拿
		Type[] boud1 = tv1[0].getBounds();
		
		// 类
		// 返回值为TypeVarialble数组，数组元素为TypeVarialble类型，即T
		TypeVariable[] tv2 = TypeTest.class.getTypeParameters();
		// 返回值为Type数组，数组元素为Class类型，即Object（因泛型擦除，T即Object）
		Type[] boud2 = tv2[0].getBounds();
	}
	
	public static void main(String[] args) {
		new TypeTest<Student>().testTypeSonInterface();;
		new TypeTest<Student>().testTypeSonClass();
	}

}

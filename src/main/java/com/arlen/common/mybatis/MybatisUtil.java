package com.arlen.common.mybatis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.arlen.common.reflect.ReflectUtil;

public class MybatisUtil {

	private final static Logger logger = LoggerFactory.getLogger(MybatisUtil.class);
	
	public static Object transDomainToQuery(Object object) {
		
		Class<?> clazzDomain = object.getClass();
		try {
			// Example类
			Class<?> clazzQuery = Class.forName(clazzDomain.getName() + "Query");
			Object query = clazzQuery.newInstance();
			
			// Criteria类
			Method criteriaMethod = ReflectionUtils.findMethod(clazzQuery, "createCriteria");
			Object criteria = criteriaMethod.invoke(query);
			Class<?> genertedCriteriaType = criteria.getClass().getSuperclass();
			
			List<Field> fieldList = Arrays.asList(clazzDomain.getDeclaredFields());
			for (Field field : fieldList) {
				// Domain类的get方法，取值
				Method getMethod = ReflectionUtils.findMethod(clazzDomain, ReflectUtil.getGetterByFieldName(field.getName()));
				Object value = getMethod.invoke(object);
				if (value == null || StringUtils.isEmpty(value)) {
					continue;
				}
				
				// Criteria类的add*EqualTo方法，拼接where
				Class<?> paramType = field.getType();
				Method queryMethod = ReflectionUtils.findMethod(genertedCriteriaType, ReflectUtil.getMethodByFieldName(field.getName(), "and", "EqualTo"), paramType);
				criteria = queryMethod.invoke(criteria, value);
			}
			return query;
			
		} catch (Exception e) {
			logger.error("Translate domain "+clazzDomain.getSimpleName()+" to query object failed. ", e);
		}
		return null;
	}
	
}

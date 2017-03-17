/**
 * 项目名: nirvana
 * 文件名：CriteriaGenerator.java 
 * 版本信息： V1.0
 * 日期：2017年2月24日 
 */
package com.arlen.common.mybatis.criteria;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.arlen.common.mybatis.criteria.CriteriaFilter.CriteriaRule;
import com.arlen.common.mybatis.exception.CriteriaGeneratException;
import com.arlen.common.reflect.ReflectUtil;
import com.arlen.common.type.TypeUtil;
import com.arlen.common.verify.VerifyUtils;
import com.arlen.common.verify.VerifyUtils.VerifyResult;

/** 
 * 项目名称：nirvana <br>
 * 类名称：CriteriaGenerator <br>
 * 类描述：根据前台参数生成criteria<br>
 * 创建人：arlen <br>
 * 创建时间：2017年2月24日 下午3:19:31 <br>
 * @version 1.0
 * @author arlen
 */
public class CriteriaGenerator {
	private final static Logger logger = LoggerFactory.getLogger(CriteriaGenerator.class);

	public final static String AND = "AND";
	public final static String OR = "OR";
	
	private final static String OBJECT = "OBJECT";
	private final static String NULL = "NULL";
	private final static String LIST = "LIST";
	private final static String STRING = "String";
	
	private final static Integer EXAMPLE_SUFFIX_LENGTH = 5;
	
	private static final Map<String, String[]> OPER_MAP;
	static {
		// ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc', 'nu', 'nn']
		// ['equal','not equal', 'less', 'less or equal','greater','greater or equal', 'begins with',
		// 'does not begin with','is in','is not in','ends with','does not end with','contains','does not contain', ,'is null','is not null'] 
		OPER_MAP = new ConcurrentHashMap<String, String[]>();
		// 等于
		OPER_MAP.put("eq", new String[]{"and{0}EqualTo", OBJECT});
		// 不等于
		OPER_MAP.put("ne", new String[]{"and{0}NotEqualTo", OBJECT});
		// 小于
		OPER_MAP.put("lt", new String[]{"and{0}LessThan", OBJECT});
		// 小于等于
		OPER_MAP.put("le", new String[]{"and{0}LessThanOrEqualTo", OBJECT});
		// 大于
		OPER_MAP.put("gt", new String[]{"and{0}GreaterThan", OBJECT});
		// 大于等于
		OPER_MAP.put("ge", new String[]{"and{0}GreaterThanOrEqualTo", OBJECT});
		// 开始于
		OPER_MAP.put("bw", new String[]{"and{0}Like", "{0}%", STRING});
		// 不开始于
		OPER_MAP.put("bn", new String[]{"and{0}NotLike", "{0}%", STRING});
		// 属于（集合）
		OPER_MAP.put("in", new String[]{"and{0}In", LIST});
		// 不属于（集合）
		OPER_MAP.put("ni", new String[]{"and{0}NotIn", LIST});
		// 结束于
		OPER_MAP.put("ew", new String[]{"and{0}Like", "%{0}", STRING});
		// 不结束于
		OPER_MAP.put("en", new String[]{"and{0}NotLike", "%{0}", STRING});
		// 包含（子串）
		OPER_MAP.put("cn", new String[]{"and{0}Like", "%{0}%", STRING});
		// 不包含（子串）
		OPER_MAP.put("nc", new String[]{"and{0}NotLike", "%{0}%", STRING});
		// 存在
		OPER_MAP.put("nn", new String[]{"and{0}IsNull", NULL});
		// 不存在
		OPER_MAP.put("nu", new String[]{"and{0}IsNotNull", NULL});
	}
	
	private static <T> T genInvalid(Class<T> clazzQuery) {
		try {
			T query = clazzQuery.newInstance();
			// Or方法
			Method orMethod = ReflectionUtils.findMethod(clazzQuery, "or");
			if (orMethod == null) {
				throw new CriteriaGeneratException("Generate criteria faild, the or method dose not exist cause invalid class :" + clazzQuery.getName());
			}
			
			Object criteria = orMethod.invoke(query);
			Class<?> genertedCriteriaType = criteria.getClass().getSuperclass();
			
			// addCriterion方法
			Method criterionMethod = ReflectionUtils.findMethod(genertedCriteriaType, "addCriterion", String.class);
			if (criterionMethod == null) {
				throw new CriteriaGeneratException("Generate criteria faild, the addCriterion method dose not exist cause invalid class : '" + clazzQuery.getName());
			}
			// 添加条件 1<>1
			criterionMethod.invoke(criteria, "1 <> 1");
			return query;
		} catch (Exception e) {
			logger.error("Generate invalid criteria faild, fatal error occurs.", e);
			throw new CriteriaGeneratException("Generate invalid criteria faild, fatal error occurs.", e);
		}
	}
	
	public static <T> T generate(CriteriaQuery<T> criteriaQuery) {
		if (criteriaQuery == null || criteriaQuery.getQueryType() == null) {
			logger.error("Generate criteria faild, input pram is empty");
			throw new IllegalArgumentException("Generate criteria faild, input pram is empty. \n -------- Please check your method declared, and ensure your method has arguments types");
		}
		Class<T> clazzQuery = criteriaQuery.getQueryType();
		
		T query = null;
		try {
			query = clazzQuery.newInstance();
			// 查询条件
			if (criteriaQuery.isHasSearch()) {
				generateWhere(clazzQuery, query, criteriaQuery);
			}
			// 分页 
			if (criteriaQuery.isPageFlag()) {
				generatePage(clazzQuery, query, criteriaQuery);
			}
			// 排序
			if (criteriaQuery.hasSortClause()) {
				generateSort(clazzQuery, query, criteriaQuery);
			}
			return query;
		} catch (Exception e) {
			logger.error("Generate criteria faild, some bad thing occurs.", e);
			return genInvalid(clazzQuery);
		}
	}

	private static <T> void generateWhere(Class<T> clazzQuery, T query, CriteriaQuery<T> criteriaQuery) {
		CriteriaFilter filter = criteriaQuery.getCriteriaFilter();
		// 参数统一校验，包含oper、field
		VerifyResult verifyResult = VerifyUtils.validate(filter);
		if (!verifyResult.isSuccess()) {
			throw new CriteriaGeneratException("Generate criteria faild, input pram is invalid. " + verifyResult.getMessage());
		}
		
		String groupOper = filter.getGroupOper();
		List<CriteriaRule> ruleList = filter.getRuleList();
		
		// Or方法
		Method orMethod = ReflectionUtils.findMethod(clazzQuery, "or");
		if (orMethod == null) {
			throw new CriteriaGeneratException("Generate criteria faild, the or method dose not exist cause invalid class :" + clazzQuery.getName());
		}
		
		// criteria
		if (AND.equalsIgnoreCase(groupOper)) {
			groupAnd(clazzQuery, query, orMethod, ruleList);
		} else if (OR.equalsIgnoreCase(groupOper)) {
			groupOr(clazzQuery, query, orMethod, ruleList);
		}
		
	}

	private static <T> void generatePage(Class<T> clazzQuery, T query, CriteriaQuery<T> criteriaQuery) {
		try {
			Method setPageFlagMethod = ReflectionUtils.findMethod(clazzQuery, "setPageFlag", boolean.class);
			Method setOffsetMethod = ReflectionUtils.findMethod(clazzQuery, "setOffset", int.class);
			Method setLimitMethod = ReflectionUtils.findMethod(clazzQuery, "setLimit", int.class);
			if (setPageFlagMethod == null || setOffsetMethod == null || setLimitMethod == null) {
				throw new CriteriaGeneratException("Methods of paging dose not exist. Please check is class '"+clazzQuery.getSimpleName()+"' valid.");
			}
			setPageFlagMethod.invoke(query, true);
			setOffsetMethod.invoke(query, criteriaQuery.getOffset());
			setLimitMethod.invoke(query, criteriaQuery.getLimit());
		} catch (Exception e) {
			throw new CriteriaGeneratException(e);
		}
	}

	private static <T> void generateSort(Class<T> clazzQuery, T query, CriteriaQuery<T> criteriaQuery) {
		try {
			Method setSortClauseMethod = ReflectionUtils.findMethod(clazzQuery, "setOrderByClause", String.class);
			if (setSortClauseMethod == null) {
				throw new CriteriaGeneratException("Method setOrderByClause dose not exist. Please check is class '"+clazzQuery.getSimpleName()+"' valid.");
			}
			setSortClauseMethod.invoke(query, criteriaQuery.getSortClause());
		} catch (Exception e) {
			throw new CriteriaGeneratException(e);
		}
	}

	/**
	 * 多个条件用and拼接
	 */
	private static <T> void groupAnd(Class<T> clazzQuery, T query, Method orMethod, List<CriteriaRule> ruleList) {
		try {
			Object criteria = orMethod.invoke(query);
			for (CriteriaRule rule : ruleList) {
				
				// 获取Domain类的field
				Field field = getField(clazzQuery, rule.getField());
				
				// 根据操作，获取SQL值
				String[] formatMethodAndValue = OPER_MAP.get(rule.getOperation());
				Object value = getSqlValue(rule, field.getType(), formatMethodAndValue[1]);
				
				// 根据操作，获取Criteria方法
				Method queryMethod = getQueryMethod(field, criteria, formatMethodAndValue, value);
				
				// 调用Criteria方法
				criteria = queryMethod.invoke(criteria, value);
			}
		} catch (Exception e) {
			throw new CriteriaGeneratException(e);
		}
	}
	
	/**
	 * 多个条件用OR拼接
	 */
	private static <T> void groupOr(Class<T> clazzQuery, T query, Method orMethod, List<CriteriaRule> ruleList) {
		try {
			for (CriteriaRule rule : ruleList) {
				Object criteria = orMethod.invoke(query);
				
				// 获取Domain类的field
				Field field = getField(clazzQuery, rule.getField());
				
				// 根据操作，获取SQL值
				String[] methodAndFormatValue = OPER_MAP.get(rule.getOperation());
				Object value = getSqlValue(rule, field.getType(), methodAndFormatValue[1]);
				
				// 执行Criteria方法
				Method queryMethod = getQueryMethod(field, criteria, methodAndFormatValue, value);
				
				// 调用Criteria方法
				queryMethod.invoke(criteria, value);
			}
		} catch (Exception e) {
			throw new CriteriaGeneratException(e);
		}
	}
	
	private static Field getField(Class<?> clazzQuery, String filedName) {
		try {
			// Domain类
			String clazzQueryName = clazzQuery.getName();
			String clazzDomainName = clazzQueryName.substring(0, clazzQueryName.length()-EXAMPLE_SUFFIX_LENGTH);
			Class<?> clazzDomain = Class.forName(clazzDomainName);
			Field field = ReflectionUtils.findField(clazzDomain, filedName);
			if (field == null) {
				throw new CriteriaGeneratException("Field "+filedName+" is not in domain "+clazzDomainName+".");
			}
			return field;
		} catch (Exception e) {
			throw new CriteriaGeneratException(e);
		}
	}

	private static Method getQueryMethod(Field field, Object criteria, String[] formatMethodAndValue, Object value) {
		try {
			String queryMethodName = MessageFormat.format(formatMethodAndValue[0], ReflectUtil.getSuffixOfMethodByFieldName(field.getName()));
			Class<?> genertedCriteriaType = criteria.getClass().getSuperclass();
			
			// 如果oper_map有限制类型，且字段与限制类型不符
			String fieldSimpleType = field.getType().getSimpleName();
			if (formatMethodAndValue.length > 2 && !fieldSimpleType.equalsIgnoreCase(STRING)) {
				throw new CriteriaGeneratException("Method and*Like just support String type, but field is "+fieldSimpleType+" type.");
			}
			// Criteria类的addFieldOper方法
			Method queryMethod;
			if (value == null) {
				queryMethod = ReflectionUtils.findMethod(genertedCriteriaType, queryMethodName);
			} else if (value instanceof List) {
				queryMethod = ReflectionUtils.findMethod(genertedCriteriaType, queryMethodName, List.class);
			} else {
				queryMethod = ReflectionUtils.findMethod(genertedCriteriaType, queryMethodName, field.getType());
			}
			if (queryMethod == null) {
				throw new CriteriaGeneratException("Method "+queryMethodName+" dose not exist. Please check is field '"+field.getName()+"' valid.");
			}
			return queryMethod;
		} catch (Exception e) {
			throw new CriteriaGeneratException(e);
		}
	}
	
	private static Object getSqlValue(CriteriaRule rule, Class<?> fieldType, String formatValue) {
		try {
			String valueStr = rule.getValue();
			String operate = rule.getOperation();
			
			Object value = null;
			// 如果值无需改变
			if (OBJECT.equals(formatValue)) {
				if (StringUtils.isEmpty(valueStr)) {
					throw new CriteriaGeneratException("Operate '"+operate+"' dose not support value '"+valueStr+"'.");
				}
				// 转换为合法值
				value = TypeUtil.getObjectValue(valueStr, fieldType);
				if (value == null) {
					throw new CriteriaGeneratException("Value '"+valueStr+"' cant convert to '"+fieldType+"'.");
				}
			}
			// 如果是IN或NOT IN，值为list
			else if (LIST.equals(formatValue)) {
				if (StringUtils.isEmpty(valueStr)) {
					throw new CriteriaGeneratException("Operate '"+operate+"' dose not support value '"+valueStr+"'.");
				}
				List<Object> valueList = new ArrayList<Object>();
				List<String> strList = Arrays.asList(valueStr.split(","));
				for (String string : strList) {
					Object tmp = TypeUtil.getObjectValue(string, fieldType);
					if (tmp == null) {
						throw new CriteriaGeneratException("Value '"+string+"' in '"+valueStr+"' cant convert to '"+fieldType+"'.");
					}
					valueList.add(tmp);
				}
				value = valueList;
			}
			// 如果是NULL或NOT NULL，值为null
			else if (NULL.equals(formatValue)) {
				value = null;
			}
			// 剩下的LIKE或NOT LIKE的
			else {
				if (StringUtils.isEmpty(valueStr)) {
					throw new CriteriaGeneratException("Operate '"+operate+"' dose not support value '"+valueStr+"'.");
				}
				// 最终还是拼接为string，无需转换
				value = TypeUtil.getObjectValue(valueStr, fieldType);
				if (value == null) {
					throw new CriteriaGeneratException("Value '"+valueStr+"' cant convert to '"+fieldType+"'.");
				}
				value = MessageFormat.format(formatValue, value);
			}
			
			// 如果抛异常，则参数非法
			return value;
		} catch (Exception e) {
			throw new CriteriaGeneratException(e);
		}
		
	}
	
}

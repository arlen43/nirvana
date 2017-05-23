/**
 * 项目名: nirvana
 * 文件名：RequestJsonArgumentResolver.java 
 * 版本信息： V1.0
 * 日期：2017年3月6日 
 *
 */
package com.arlen.common.web.argument;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.arlen.common.web.ServletUtil;
import com.arlen.common.web.annotation.RequestJson;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 项目名称：nirvana <br>
 * 类名称：RequestJsonArgumentResolver <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月6日 下午3:04:13 <br>
 * 
 * @version 1.0
 * @author arlen
 */
public class RequestJsonArgumentResolver implements HandlerMethodArgumentResolver {
	private static final Logger logger = LoggerFactory.getLogger(RequestJsonArgumentResolver.class);
	private ObjectMapper objectMapper;
	private static final String PATH_DELIMITER = "/";

	public RequestJsonArgumentResolver() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		this.objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		this.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestJson.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		try {
			RequestJson jsonAnn = (RequestJson) parameter.getParameterAnnotation(RequestJson.class);
			String path = jsonAnn.path();
			String allParam = ServletUtil.getHttpRequestParamUrlDecode((HttpServletRequest)webRequest.getNativeRequest());
			if (StringUtils.isEmpty(allParam)) {
				return null;
			}
			
			JsonNode node = this.objectMapper.readTree(allParam);
			
			// 未指定路径
			if (StringUtils.isEmpty(path)) {
				logger.debug("Resolve argument {}, index {}", parameter.getParameterType().toString(), Integer.valueOf(parameter.getParameterIndex()));
				// 按变量名找到路径，按路径值转换
				try {
					path = parameter.getParameterName();
					if (node.has(path)) {
						return this.objectMapper.readValue(node.path(path).toString(), getReferenceType(parameter, jsonAnn));
					}
				} catch (Exception e) {
					logger.warn("Just a warn: Resolve path argument failed. Type "+parameter.getParameterType().toString()+", index "+Integer.valueOf(parameter.getParameterIndex()), e);
				}
				// 如果失败，则全部参数转换
				try {
					return this.objectMapper.readValue(allParam, getReferenceType(parameter, jsonAnn));
				} catch (Exception e) {
					logger.error("Resolve argument failed. Type "+parameter.getParameterType().toString()+", index "+Integer.valueOf(parameter.getParameterIndex()), e);
					throw e;
				}
			}

			// 如果指定了路径，遍历到路劲最底层
			String[] pathArr = path.split(PATH_DELIMITER);
			for (String p : pathArr) {
				node = node.path(p);
			}
			if (node == null) {
				logger.error("Resolve argument failed. Path {} dose not exist.", this.objectMapper.writeValueAsString(pathArr));
				return null;
			}
			return this.objectMapper.readValue(node.toString(), getReferenceType(parameter, jsonAnn));
		} catch (Exception e) {
			logger.error("Resolve argument "+parameter.getParameterName()+" failed!", e);
			throw e;
		}
	}

//	private String getParameterName(MethodParameter parameter) {
//		String parameterName = null;
//		for (int i = 0; i < 3; i++) {
//			try {
//				parameterName = parameter.getParameterName();
//			} catch (Exception ex) {
//				logger.error("[JsonMapperArgumentResolver][getParameterName]", ex);
//			}
//		}
//		return parameterName;
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JavaType getReferenceType(MethodParameter parameter, RequestJson annt) {
		Class<?>[] typeArr = annt.types();
		if ((typeArr.length == 1) && (typeArr[0].equals(Object.class))) {
			return this.objectMapper.getTypeFactory().constructType(parameter.getParameterType());
		}

		if (Collection.class.isAssignableFrom(parameter.getParameterType())) {
			return this.objectMapper.getTypeFactory()
					.constructCollectionType((Class<? extends Collection>) parameter.getParameterType(), typeArr[0]);
		}

		if (Map.class.isAssignableFrom(parameter.getParameterType())) {
			if (typeArr.length >= 2) {
				return this.objectMapper.getTypeFactory()
						.constructMapType((Class<? extends Map>) parameter.getParameterType(), typeArr[0], typeArr[1]);
			}

			return this.objectMapper.getTypeFactory()
					.constructMapType((Class<? extends Map>) parameter.getParameterType(), typeArr[0], Object.class);
		}

		StringBuilder buffer = new StringBuilder();
		buffer.append("Unsuppored Reference To JavaType : ").append(parameter.getParameterType().getName()).append("<");

		int i = 0;
		for (Class<?> type : typeArr) {
			if (i++ > 0)
				buffer.append(",");
			buffer.append(type.getSimpleName());
		}
		buffer.append(">");
		throw new UnsupportedOperationException(buffer.toString());
	}

}

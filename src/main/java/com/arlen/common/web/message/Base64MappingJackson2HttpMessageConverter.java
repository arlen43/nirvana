/**
 * 项目名: nirvana
 * 文件名：Base64MappingJackson2HttpMessageConverter.java 
 * 版本信息： V1.0
 * 日期：2017年3月13日 
 */
package com.arlen.common.web.message;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

/** 
 * 项目名称：nirvana <br>
 * 类名称：Base64MappingJackson2HttpMessageConverter <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月13日 下午5:28:21 <br>
 * @version 1.0
 * @author arlen
 */
public class Base64MappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

	private static final Logger logger = LoggerFactory.getLogger(Base64MappingJackson2HttpMessageConverter.class);
	
	public Base64MappingJackson2HttpMessageConverter() {
		ObjectMapper mapper = getObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		Set<String> fliterSet = new HashSet<String>();
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("filter",
				SimpleBeanPropertyFilter.serializeAllExcept(fliterSet));

		mapper.setFilterProvider(filterProvider);
	}
	
	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		try {
			byte[] bytes = getObjectMapper().writeValueAsBytes(object);
			FileCopyUtils.copy(Base64.encodeBase64(bytes), outputMessage.getBody());
		} catch (JsonProcessingException ex) {
			logger.error("Could not write content: " + ex.getMessage(), ex);
			throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
		}
	}

}

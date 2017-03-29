/**
 * 项目名: nirvana
 * 文件名：Base64HandlerMehodReturnValueHandler.java 
 * 版本信息： V1.0
 * 日期：2017年3月13日 
 * Copyright: Corporation 2017 版权所有
 *
 */
package com.arlen.common.web.returnvalue;

import java.util.Arrays;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.arlen.common.web.annotation.ResponseJson;
import com.arlen.common.web.message.Base64MappingJackson2HttpMessageConverter;

/** 
 * 项目名称：nirvana <br>
 * 类名称：Base64HandlerMehodReturnValueHandler <br>
 * 类描述：返回值base64编码<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月13日 下午5:14:22 <br>
 * @version 1.0
 * @author arlen
 */
public class Base64HandlerMehodReturnValueHandler extends RequestResponseBodyMethodProcessor {

	public Base64HandlerMehodReturnValueHandler() {
		super(Arrays.asList(new HttpMessageConverter<?>[] {new Base64MappingJackson2HttpMessageConverter()}));
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.hasParameterAnnotation(ResponseJson.class) 
				|| returnType.getMethod().isAnnotationPresent(ResponseJson.class);
	}

//	@Override
//	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
//			NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException, IOException {
//		mavContainer.setRequestHandled(true);
//		writeWithMessageConverters(returnValue, returnType, webRequest);
//	}

//	private void writeWithMessageConverters(Object returnValue, MethodParameter returnType,
//			NativeWebRequest webRequest) {
//		ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
//		
//	}
	
}

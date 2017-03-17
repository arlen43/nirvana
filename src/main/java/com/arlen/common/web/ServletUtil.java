/**
 * 项目名: nirvana
 * 文件名：ServletUtil.java 
 * 版本信息： V1.0
 * 日期：2017年3月6日 
 */
package com.arlen.common.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 
 * 项目名称：nirvana <br>
 * 类名称：ServletUtil <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月6日 上午11:44:21 <br>
 * @version 1.0
 * @author arlen
 */
public class ServletUtil {

	public final static String ENCODING = "UTF-8";

	/**
	 * 将json字符串转换为Map
	 * @param jsonParam
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException 
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 */
	public static Map<String, String> convertJson2FieldMap(String jsonParam) throws JsonProcessingException, IOException {
		if (StringUtils.isEmpty(jsonParam)) {
			return new HashMap<String, String>(0);
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(jsonParam);
		Iterator<String> fieldIt = jsonNode.fieldNames();
		while (fieldIt.hasNext()) {
			String key = fieldIt.next();
			String value = jsonNode.get(key).toString();
			// 去掉双引号
			if (value != null && value.length() > 2 && value.startsWith("\"")) {
				paramMap.put(key, value.substring(1, value.length() - 1));
			} else {
				paramMap.put(key, value);
			}
		}
		return paramMap;
	}
	
	/**
	 * 获取http请求参数，UTF-8编码，base64解码，转为json map
	 * @param request
	 * @return json map
	 * @author arlen
	 * @throws IOException 
	 */
	public static Map<String, String> getHttpJsonParamMapBase64Decode(HttpServletRequest request, boolean base64) throws IOException {
		return getHttpJsonParamMap(request, ENCODING, base64);
	}
	
	/**
	 * 获取http请求参数，指定URL编码方式，指定是否base64解码，转为json map。
	 * @param request
	 * @param encoding 指定URL编码方式，utf-8
	 * @param base64 是否base64解码
	 * @return json map
	 * @author arlen
	 * @throws IOException 
	 */
	public static Map<String, String> getHttpJsonParamMap(HttpServletRequest request, String encoding, boolean base64) throws IOException {
		String rawParam = ServletUtil.getHttpRequestParamUrlDecode(request, encoding);
		if (StringUtils.isEmpty(rawParam)) {
			return new HashMap<String, String>(0);
		}
		
		String jsonParam = rawParam;
		if (base64 && Base64.isBase64(rawParam)) {
			jsonParam = new String(Base64.decodeBase64(rawParam), ENCODING);
		}
		return convertJson2FieldMap(jsonParam);
	}
	
	/**
	 * 获取 url utf-8编码，指定是否Base64解码的query String
	 * @param request
	 * @param base64 是否base64解码
	 * @return
	 * @throws IOException 
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 */
	public static String getHttpQueryStringBase64Decode(HttpServletRequest request, boolean base64) throws IOException {
		return getHttpQueryString(request, ENCODING, base64);
	}
	
	/**
	 * 获取http请求参数，指定URL编码方式，指定是否base64解码
	 * @param request
	 * @param encoding 指定URL编码方式，utf-8
	 * @param base64 是否base64解码
	 * @return string or null
	 * @throws IOException 
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 */
	public static String getHttpQueryString(HttpServletRequest request, String encoding, boolean base64) throws IOException {
		String rawParam = ServletUtil.getHttpQueryStringUrlDecode(request, encoding);
		if (StringUtils.isEmpty(rawParam)) {
			return null;
		}
		
		if (base64 && Base64.isBase64(rawParam)) {
			return new String(Base64.decodeBase64(rawParam), encoding);
		}
		return rawParam;
	}
	
	/**
	 * 获取经过UrlDecoder解码后的http query string，默认UTF-8编码
	 * @param request 
	 * @return 返回解码请求串，如果参数为空，则返回null
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 * @throws IOException
	 */
	public static String getHttpQueryStringUrlDecode(HttpServletRequest request) throws IOException {
    	return getHttpQueryStringUrlDecode(request, ENCODING);
	}

	/**
	 * 获取经过UrlDecoder解码后的http query string
	 * @param request 
	 * @param encoding 指定编码
	 * @return 返回解码请求串，如果参数为空，则返回null
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 * @throws IOException
	 */
	public static String getHttpQueryStringUrlDecode(HttpServletRequest request, String encoding) throws IOException {
		if (request == null || StringUtils.isEmpty(encoding)) {
			return null;
		}
		String rawStr = request.getQueryString();
		if (StringUtils.isEmpty(rawStr)) {
    		return null;
    	}
    	return URLDecoder.decode(rawStr, encoding);
	}
	
	/**
	 * 获取Http请求InputStream中的字节数组，utf-8编码，可指定是否base64解码
	 * @param request
	 * @param base64
	 * @return
	 * @throws IOException 
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 */
	public static byte[] getHttpInputStreamBytesBase64Decode(HttpServletRequest request, boolean base64) throws IOException {
		return getHttpInputStreamBytes(request, ENCODING, base64);
	}
	
	/**
	 * 获取Http请求InputStream中的字节数组，可指定编码，可指定是否base64解码
	 * @param request
	 * @param encoding
	 * @param base64
	 * @return
	 * @throws IOException 
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 */
	public static byte[] getHttpInputStreamBytes(HttpServletRequest request, String encoding, boolean base64) throws IOException {
		byte[] rawBytes = ServletUtil.getHttpInputStreamBytesEncoding(request, encoding);
		if (rawBytes == null || rawBytes.length <= 0) {
			return new byte[0];
		}
		
		if (base64 && Base64.isBase64(rawBytes)) {
			return Base64.decodeBase64(rawBytes);
		}
		return rawBytes;
	}
	
	/**
	 * 获取请求InputStream字节数组，以UTF-8方式编码获取
	 * @param request
	 * @return
	 * @throws IOException 
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 */
	public static byte[] getHttpInputStreamBytesEncoding(HttpServletRequest request) throws IOException {
		return getHttpInputStreamBytesEncoding(request, ENCODING);
	}
	
	/**
	 * 获取请求InputStream字节数组，以指定方式编码获取
	 * @param request
	 * @param encoding
	 * @return
	 * @throws IOException 
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 */
	public static byte[] getHttpInputStreamBytesEncoding(HttpServletRequest request, String encoding) throws IOException {
		if (request == null || encoding == null) {
			return new byte[0];
		}
		StringBuilder buffer = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString().getBytes(encoding);
	}
	
	/**
	 * 获取http请求的参数，默认utf-8编码
	 * @param request
	 * @return 参数str
	 * @throws IOException 
	 * @author arlen
	 */
	public static String getHttpRequestParamUrlDecode(HttpServletRequest request) throws IOException {
		return getHttpRequestParamUrlDecode(request, ENCODING);
	}
	
	/**
     * 获取HttpServletRequest参数体
     * 
     * @param webRequest
     * @return
     * @throws IOException
     */
    public static String getHttpRequestParamUrlDecode(HttpServletRequest request, String encoding) throws IOException {
    	if (request == null || StringUtils.isEmpty(encoding)) {
    		return null;
    	}
        String method = request.getMethod();
        if (method.equals("GET") || method.equals("DELETE") || method.equals("OPTIONS")) {
        	return getHttpQueryStringUrlDecode(request, encoding);
        }
        return new String(getHttpInputStreamBytesEncoding(request, encoding), encoding);
    }
    
}

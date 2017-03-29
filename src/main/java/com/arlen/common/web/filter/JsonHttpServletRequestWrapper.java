/**
 * 项目名: nirvana
 * 文件名：JsonHttpServletRequestWrapper.java 
 * 版本信息： V1.0
 * 日期：2017年3月6日 
 *
 */
package com.arlen.common.web.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.arlen.common.web.ServletUtil;

/** 
 * 项目名称：nirvana <br>
 * 类名称：JsonHttpServletRequestWrapper <br>
 * 类描述：将http请求中的JSON参数转为controller的参数<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月6日 上午10:20:17 <br>
 * @version 1.0
 * @author arlen
 */
public class JsonHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private Map<String, String> paramMap;
	private boolean base64 = false;
	private byte[] bytes = new byte[0];
	
	public JsonHttpServletRequestWrapper(HttpServletRequest request, boolean base64) {
		super(request);
		this.base64 = base64;
		this.init(request, base64);
	}
	
	private void init(HttpServletRequest request, boolean base64) {
		try {
			String jsonParam;
			String method = request.getMethod();
			if (method.equals("GET") || method.equals("DELETE") || method.equals("OPTIONS")) {
				jsonParam = ServletUtil.getHttpQueryStringBase64Decode(request, this.base64);
				this.bytes = new byte[0];
			} else {
				this.bytes = ServletUtil.getHttpInputStreamBytesBase64Decode(this.getHttpServletRequest(), this.base64);
				jsonParam = new String(this.bytes, ServletUtil.ENCODING);
			}
			
			this.paramMap = ServletUtil.convertJson2FieldMap(jsonParam);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private HttpServletRequest getHttpServletRequest() {
		return (HttpServletRequest) super.getRequest();
	}

	@Override
	public String getQueryString() {
		try {
			return ServletUtil.getHttpQueryStringBase64Decode((HttpServletRequest)this.getHttpServletRequest(), this.base64);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BufferedReader getReader() throws IOException {
		InputStreamReader inputStream = new InputStreamReader(new ByteArrayInputStream(this.bytes), ServletUtil.ENCODING);
		return new BufferedReader(inputStream);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		ServletInputStream sin = new ServletInputStream() {
			private int index = 0;
			@Override
			public int read() throws IOException {
				if (JsonHttpServletRequestWrapper.this.bytes.length > this.index) {
					return JsonHttpServletRequestWrapper.this.bytes[this.index++];
				}
				return -1;
			}
		};
		return sin;
	}

	@Override
	public String getParameter(String name) {
		return this.paramMap.get(name);
	}

	@Override
	public Map<String, String> getParameterMap() {
		return this.paramMap;
	}

	@Override
	public Enumeration<?> getParameterNames() {
		final Iterator<String> it = this.paramMap.keySet().iterator();
		return new Enumeration<String>() {
			@Override
			public boolean hasMoreElements() {
				return it.hasNext();
			}
			@Override
			public String nextElement() {
				return it.next();
			}
		};
	}

	@Override
	public String[] getParameterValues(String name) {
		String value = getParameter(name);
	    return value == null? null : new String[]{value};
	}
	
}

/**
 * 项目名: nirvana
 * 文件名：JsonParamFilter.java 
 * 版本信息： V1.0
 * 日期：2017年3月4日 
 *
 */
package com.arlen.common.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

/** 
 * 项目名称：nirvana <br>
 * 类名称：JsonParamFilter <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月4日 下午11:39:50 <br>
 * @version 1.0
 * @author arlen
 */
public class JsonParamCrosFilter implements Filter {

	private final static Logger logger = LoggerFactory.getLogger(JsonParamCrosFilter.class);
	private static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
	private PathMatcher matcher = new AntPathMatcher();
	private List<String> crosDomainList = new ArrayList<>();
	private List<String> excludeList = new ArrayList<>();
	private boolean base64 = false;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String excludesStr = filterConfig.getInitParameter("excludes");
		if (!StringUtils.isEmpty(excludesStr)) {
			this.excludeList.clear();
			this.excludeList.addAll(Arrays.asList(StringUtils.tokenizeToStringArray(excludesStr, CONFIG_LOCATION_DELIMITERS)));
		}
		
		String base64Str = filterConfig.getInitParameter("base64");
		if (!StringUtils.isEmpty(base64Str)) {
			if (base64Str.equalsIgnoreCase("true")) {
				this.base64 = true;
			}
		}
		
		String crosDomainsStr = filterConfig.getInitParameter("crosDomains");
		if (!StringUtils.isEmpty(crosDomainsStr)) {
			this.crosDomainList.clear();
			this.crosDomainList.addAll(Arrays.asList(StringUtils.tokenizeToStringArray(crosDomainsStr, CONFIG_LOCATION_DELIMITERS)));
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		setCrosHttpResponse(request, response);
		if (isInExclude(request)) {
			chain.doFilter(request, response);
		} else {
			JsonHttpServletRequestWrapper DecodingRequest = new JsonHttpServletRequestWrapper(request, base64);
	        chain.doFilter(DecodingRequest, response);
		}
	}
	
	private void setCrosHttpResponse(HttpServletRequest request, HttpServletResponse response) {
		// 简单请求
		String orgin = request.getHeader("Origin");
		int index = -1;
		// 设置多个域名
		if (( index = this.crosDomainList.indexOf(orgin)) > 0) {
			response.setHeader("Access-Control-Allow-Origin", this.crosDomainList.get(index));
		} else {
			response.setHeader("Access-Control-Allow-Origin", "http://kyd.greenpass360.com");
		}
		
		// 复杂请求
		response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Content-Type");
		response.setHeader("Access-Control-Allow-Credentials","true");
		// 请求头为application/json
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
	}

	@Override
	public void destroy() {
	}

	private boolean isInExclude(HttpServletRequest request) {
		String path = StringUtils.isEmpty(request.getServletPath())? request.getRequestURI(): request.getServletPath();
		if (this.excludeList.contains(path)) {
			logger.info("JsonCros filter: path {} is in exclude urls.", path);
			return true;
		}
		for (String excludePatten : this.excludeList) {
			if (this.matcher.match(excludePatten, path)) {
				logger.info("JsonCros filter: path {} is in exclude urls.", path);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		
	}

}

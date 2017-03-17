package com.arlen.common.web;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

	
	/**
     * 获取HttpServletRequest参数体
     * 
     * @param webRequest
     * @return
     * @throws IOException
     */
    public String getJsonParameter(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        if (method.equals("GET") || method.equals("DELETE")) {
            return request.getQueryString();
        }
        StringBuilder buffer = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }
}

/**
 * 项目名: nirvana
 * 文件名：LogbackInitializer.java 
 * 版本信息： V1.0
 * 日期：2017年3月17日 
 */
package com.arlen.common.logback;

import java.io.FileNotFoundException;
import java.net.URL;

import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/** 
 * 项目名称：nirvana <br>
 * 类名称：LogbackInitializer <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年3月17日 下午3:23:59 <br>
 * @version 1.0
 * @author arlen
 */
public class LogbackInitializer {

	public static void init(String logbackConfigLocation) throws FileNotFoundException, JoranException {
        String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(logbackConfigLocation);
        if (StringUtils.isEmpty(resolvedLocation) || !resolvedLocation.toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("Log config file is not valid.");
        }
 
        URL logbackConfUrl = ResourceUtils.getURL(resolvedLocation);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory(); 
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator(); 
        configurator.setContext(loggerContext); 
        configurator.doConfigure(logbackConfUrl);
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
    }
}

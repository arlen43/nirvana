/**
 * 项目名: oss-web
 * 文件名：TransParentImplTest.java 
 * 版本信息： V1.0
 * 日期：2016年5月27日 
 * Copyright: Corporation 2016 版权所有
 *
 */
package com.arlen.test.transaction;

import javax.annotation.Resource;

import org.junit.Test;

import com.arlen.test.BaseTest;

/** 
 * 项目名称：oss-web <br>
 * 类名称：TransParentImplTest <br>
 * 类描述：<br>
 * Copyright: Copyright (c) 2016 by 江苏宏坤供应链管理有限公司<br>
 * Company: 江苏宏坤供应链管理有限公司<br>
 * 创建人：arlen <br>
 * 创建时间：2016年5月27日 下午5:06:30 <br>
 * 修改人：arlen<br>
 * 修改时间：2016年5月27日 下午5:06:30 <br>
 * 修改备注：<br>
 * @version 1.0
 * @author arlen
 */
public class TransParentImplTest extends BaseTest {

	@Resource
	private ITransParent parent;
	
	@Test
	public void updateOrder() {
		parent.updateOrder();
	}

	public static void main(String[] args) {
		System.out.println("12345".substring(0, 4) + "9");
	}
}

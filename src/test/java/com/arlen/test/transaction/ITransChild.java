package com.arlen.test.transaction;

import com.arlen.common.sequence.BaseSequence;

/**
 * 项目名称：nirvana <br>
 * 类名称：ITransChild <br>
 * 类描述：<br>
 * 创建人：arlen <br>
 * 创建时间：2017年5月23日 下午4:11:17 <br>
 * @version 1.0
 * @author arlen
 */
public interface ITransChild {

	/**
	 * updateOrderStatus(这里用一句话描述这个方法的作用) 
	 * @param orderInfo 
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 * @author arlen
	 */
	void updateOrderStatus(BaseSequence sequence);

}

/**
 * 项目名: oss-service
 * 文件名：TransParentImpl.java 
 * 版本信息： V1.0
 * 日期：2016年5月27日 
 * Copyright: Corporation 2016 版权所有
 *
 */
package com.arlen.test.transaction.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arlen.common.sequence.BaseSequence;
import com.arlen.common.sequence.dao.IBaseSequenceDao;
import com.arlen.test.transaction.ITransChild;
import com.arlen.test.transaction.ITransParent;

/** 
 * 项目名称：oss-service <br>
 * 类名称：TransParentImpl <br>
 * 类描述：<br>
 * Copyright: Copyright (c) 2016 by 江苏宏坤供应链管理有限公司<br>
 * Company: 江苏宏坤供应链管理有限公司<br>
 * 创建人：arlen <br>
 * 创建时间：2016年5月27日 下午4:30:57 <br>
 * 修改人：arlen<br>
 * 修改时间：2016年5月27日 下午4:30:57 <br>
 * 修改备注：<br>
 * @version 1.0
 * @author arlen
 */
@Service
@Transactional
public class TransParentImpl implements ITransParent {

	@Resource
	private IBaseSequenceDao sequenceDao;
	
	@Resource
	private ITransChild child;
	
	@Resource
	private Map<String, ITransChild> childMap;
	
	@Override
	public void updateOrder() {
		Byte seqKey = 2;
		int startId = 200002;
		BaseSequence sequence = sequenceDao.selectByPrimaryKey(seqKey);
		System.err.println("主事务进入时，startId："+sequence.getStartId());
		
		// 主事务更新
		sequence.setStartId(startId);
		this.updateOrderStatus(sequence);
		System.err.println("主事务修改后，startId："+sequence.getStartId());
		
		// 子事务更新
		try {
			sequence.setStartId(seqKey + 100);
			child.updateOrderStatus(sequence);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sequence = sequenceDao.selectByPrimaryKey(seqKey);
		System.err.println("主事务退出时，startId：" + sequence.getStartId());
	}
	
	private void updateOrderStatus(BaseSequence sequenceUpd) {
		int row = sequenceDao.updateByPrimaryKeySelective(sequenceUpd);
		System.out.println(row);
	}
}

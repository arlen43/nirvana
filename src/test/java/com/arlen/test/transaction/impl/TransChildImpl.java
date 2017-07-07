package com.arlen.test.transaction.impl;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.arlen.common.sequence.BaseSequence;
import com.arlen.common.sequence.dao.IBaseSequenceDao;
import com.arlen.test.transaction.ITransChild;

@Service
@Transactional
public class TransChildImpl implements ITransChild {

	@Resource
	private IBaseSequenceDao sequenceDao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class, noRollbackFor = Exception.class)
	public void updateOrderStatus(BaseSequence seq) {
		BaseSequence sequence = sequenceDao.selectByPrimaryKey(seq.getSeqKey());
		System.err.println("子事务进入时，startId："+sequence.getStartId());
		
//		throw new BusinessException("哈哈哈哈");
		
		BaseSequence sequenceUpd = new BaseSequence();
		sequenceUpd.setSeqKey(seq.getSeqKey());
		sequenceUpd.setStartId(10000222);
		int row = sequenceDao.updateByPrimaryKeySelective(sequenceUpd);
		System.err.println("子事务修改后，orderStatus："+sequenceUpd.getStartId());
		System.out.println(row);
	}
	
	public void sss() {
		SqlSessionTemplate template = null;
//		template.begin();
		template.rollback();
	}
	
}

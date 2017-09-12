package com.arlen.common.sequence;

import com.arlen.common.sequence.dao.IBaseSequenceDao;
import com.arlen.common.sequence.exception.SequenceGenerateException;
import com.arlen.common.spring.SpringContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by arlen on 2017/9/12.
 */
@Service
public class BaseSequenceServiceImpl implements IBaseSequenceService {

    @Resource
    private IBaseSequenceDao dao;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BaseSequence getNewBaseSequence(byte seqKey) {
        BaseSequence baseSequence = checkAndReload(seqKey);
        return baseSequence;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int getNextId(BaseSequence baseSequence) {
        if (baseSequence.getNextId() == baseSequence.getStartId()) {
            baseSequence.setStartId(baseSequence.getStartId() + baseSequence.getStepBy());
            // 每次到了步长都得去数据库再捞一把，不然可能应用修改了数据库中的值。
            this.checkAndReload(baseSequence.getSeqKey());
        }
        int nextId = baseSequence.getNextId();
        // 序列号加1
        baseSequence.setNextId(nextId + 1);
        return nextId;
    }

    // 顶层方法加事务，确保多机并发情况下，查询和修改在同一个事务里边，并发不会产生问题。
    private BaseSequence checkAndReload(byte seqKey) {
        BaseSequence sequence = getDao().selectByPrimaryKey(seqKey);
        int rowNum = 0;
        if (sequence == null) {
            sequence = new BaseSequence();
            sequence.setSeqKey(seqKey);
            sequence.setNextId(BaseSequence.START);
            sequence.setStartId(BaseSequence.START + BaseSequence.STEP);
            sequence.setStepBy(BaseSequence.STEP);
            rowNum = dao.insert(sequence);
        } else {
            sequence.setNextId(sequence.getStartId());
            //this.stepBy = sequence.getStepBy();
            sequence.setStartId(sequence.getStartId() + sequence.getStepBy());
            rowNum = dao.updateByPrimaryKeySelective(sequence);
        }
        if (rowNum <= 0) {
            throw new RuntimeException("@@@ Fatal error: Generate sequence failed. Affected rows of DB less than 1.");
        }
        return sequence;
    }

    private IBaseSequenceDao getDao() {
        if (dao == null) {
            ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
            if (applicationContext == null) {
                throw new SequenceGenerateException("Init sequence faild, not in web environment. Get WebApplication failed");
            }
            dao = applicationContext.getBean(IBaseSequenceDao.class);
            if (dao == null) {
                throw new SequenceGenerateException("Init sequence faild, IBaseSequenceDao dose not init");
            }
        }
        return dao;
    }
}

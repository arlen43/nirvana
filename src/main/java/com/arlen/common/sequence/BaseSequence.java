package com.arlen.common.sequence;

import org.springframework.context.ApplicationContext;

import com.arlen.common.sequence.dao.IBaseSequenceDao;
import com.arlen.common.sequence.exception.SequenceGenerateException;
import com.arlen.common.spring.SpringContextUtil;

public class BaseSequence {
	private final static int START = 1000000;
    private final static int STEP = 10;
    /**
     * 业务类型，0：product；1：line；2：user
     */
    private Byte seqKey;
 
    /**
     * 起始id
     */
    private Integer startId;

    /**
     * 步长
     */
    private Integer stepBy;
 
    /**
     * 下个id
     */
    private int nextId;
 
    private IBaseSequenceDao dao;
 
    public BaseSequence() {
    }
 
    public BaseSequence(byte seqKey) {
        this.seqKey = seqKey;
        checkAndReload();
    }
 
    public int getNextId() {
        if (this.nextId == this.startId) {
        	this.startId += this.stepBy;
        	getDao().updateByPrimaryKeySelective(this);
        }
        return nextId++;
    }
 
    private void checkAndReload() {
        BaseSequence sequence = getDao().selectByPrimaryKey(this.seqKey);
        if (sequence == null) {
            this.nextId = START;
            this.startId = START + STEP;
            this.stepBy = STEP;
            getDao().insert(this);
        } else {
            this.nextId = sequence.getStartId();
            this.stepBy = sequence.getStepBy();
            this.startId = sequence.getStartId() + this.stepBy;
            getDao().updateByPrimaryKeySelective(this);
        }
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
 
    public Byte getSeqKey() {
        return seqKey;
    }
 
    public void setSeqKey(Byte seqKey) {
        this.seqKey = seqKey;
    }

    public Integer getStepBy() {
        return stepBy;
    }
 
    public void setStepBy(Integer stepBy) {
        this.stepBy = stepBy;
    }
    public Integer getStartId() {
        return startId;
    }

    public void setStartId(Integer startId) {
        this.startId = startId;
    }
}
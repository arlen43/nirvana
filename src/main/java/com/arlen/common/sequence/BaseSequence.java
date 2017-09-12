package com.arlen.common.sequence;

import org.springframework.context.ApplicationContext;

import com.arlen.common.sequence.dao.IBaseSequenceDao;
import com.arlen.common.sequence.exception.SequenceGenerateException;
import com.arlen.common.spring.SpringContextUtil;

public class BaseSequence {
	public final static int START = 1000000;
    public final static int STEP = 10;
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

    public BaseSequence() {
    }

    public Byte getSeqKey() {
        return seqKey;
    }

    public void setSeqKey(Byte seqKey) {
        this.seqKey = seqKey;
    }

    public Integer getStartId() {
        return startId;
    }

    public void setStartId(Integer startId) {
        this.startId = startId;
    }

    public Integer getStepBy() {
        return stepBy;
    }

    public void setStepBy(Integer stepBy) {
        this.stepBy = stepBy;
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }
}
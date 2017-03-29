package com.arlen.common.sequence.dao;

import com.arlen.common.sequence.BaseSequence;

public interface IBaseSequenceDao {

    int insert(BaseSequence record);

    BaseSequence selectByPrimaryKey(Byte seqKey);

    int updateByPrimaryKeySelective(BaseSequence record);
}